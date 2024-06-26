package com.yule.dashboard.search;

import com.yule.dashboard.bookmark.BookmarkRepository;
import com.yule.dashboard.entities.Bookmark;
import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.mypage.HistoryRepository;
import com.yule.dashboard.mypage.SiteRepository;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.aop.Retry;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.search.drivers.DriverServiceProvider;
import com.yule.dashboard.search.drivers.model.NaverSiteInfo;
import com.yule.dashboard.search.drivers.model.SiteInfo;
import com.yule.dashboard.search.model.SearchType;
import com.yule.dashboard.search.model.req.SearchDto;
import com.yule.dashboard.search.model.resp.SiteRespData;
import com.yule.dashboard.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class SearchService {
    private final HistoryRepository historyRepository;
    private final SiteRepository siteRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final DriverServiceProvider driverServiceProvider;
    private final SecurityFacade facade;

    public SearchService(HistoryRepository historyRepository, DriverServiceProvider driverServiceProvider, SiteRepository siteRepository,
                         UserRepository userRepository, BookmarkRepository bookmarkRepository,
                         SecurityFacade facade) {
        this.historyRepository = historyRepository;
        this.driverServiceProvider = driverServiceProvider;
        this.siteRepository = siteRepository;
        this.userRepository = userRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.facade = facade;
    }

    // logic
    public List<SiteRespData> search(String query) {
        List<Site> findSites = siteRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED);
        if (findSites.isEmpty()) throw new ClientException(ExceptionCause.SITE_IS_EMPTY);
        // count 기준 desc -> site 식별자번호를 list 에 추가. -> 0번 idx 부터 count 빈도 순위로 내림차순 정렬.
        List<Integer> orderedUserSiteRanking = findSites.stream()
                .sorted((s1, s2) -> (int) (s2.getRank().getCount() - s1.getRank().getCount()))
                .map(s -> s.getSite().getValue()).toList();

        // 멀티스레드이므로 순서보장이 안됨 -> orderedUserSiteRanking 과 같은 key를 가짐이 보장되는 ( site num ) hashMap 을 만들어,
        // 마지막 리턴 전에 sort 수행.
        Map<Integer, List<SearchDto>> unorderedResult = new HashMap<>();

        Users findUser = userRepository.findById(facade.getId());

        // 웹 크롤링 위임
        threadAction(query, findSites, unorderedResult, findUser.getSearchType());

        // add history
        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.SEARCH),
                HistoryType.SEARCH, query);

        // 북마크 여부 찾기
        Map<String, List<SearchDto>> totalUrls = new HashMap<>();
        unorderedResult.values().forEach(searchDtoList -> searchDtoList.forEach(dto -> {
            List<SearchDto> value = totalUrls.computeIfAbsent(dto.getLink(), k -> new ArrayList<>());
            value.add(dto);
        }));

        // 동일한 url 로 북마크 등록된 목록 찾기
        List<Bookmark> bookmarkedUrls = bookmarkRepository.findByUserAndUrlInAndState(
                findUser,
                new ArrayList<>(totalUrls.keySet()),
                BaseState.ACTIVATED
        );

        // 북마크 등록된 목록과 동일한 url 을 가진 검색 결과들에는 북마크 id 를 설정 ( 북마크 됨 표시 )
        bookmarkedUrls.forEach(k -> totalUrls.get(k.getUrl()).forEach(obj -> obj.setBookmarkId(k.getId())));

        // 사이드 선호도 순서대로 세팅하여 반환
        return orderedUserSiteRanking
                .stream()
                .map(siteIdentity -> {
                    List<SearchDto> searchDtos = unorderedResult.get(siteIdentity);
                    return new SiteRespData(siteIdentity, searchDtos);
                })
                .toList();
    }

    @Retry(3)
    protected void threadAction(String query, List<Site> findSites,
                              Map<Integer, List<SearchDto>> unorderedResult,
                              SearchType type) {

        Map<Site, Future<List<SiteInfo>>> futures = new HashMap<>();
        for (Site site : findSites) {
            Future<List<SiteInfo>> future = doCrawling(site, query, type);
            futures.put(site, future);
        }

        for (Site findSite : findSites) {
            try {
                Future<List<SiteInfo>> future = futures.get(findSite);
                List<? extends SiteInfo> siteInfos = future.get();
                int mapKey = findSite.getSite().getValue();
                unorderedResult.put(mapKey, siteInfos.stream().map(s -> SearchDto.builder()
                        .title(s.getTitle())
                        .link(s.getLink())
                        .content(s.getContent())
                        .category(s.getCategory())
                        .iconPath(s.getIconPath())
                        .subTitle(s instanceof NaverSiteInfo ? ((NaverSiteInfo) s).getSubTitle() : null)
                        .build()).toList());
            } catch (InterruptedException | ExecutionException e) {
                throw new ServerException();
            }
        }
    }


    private Future<List<SiteInfo>> doCrawling(Site userSite, String query, SearchType type) {

        if (userSite.getSite().getValue() == 0) {
            return driverServiceProvider.naver(query, type);
        }
        if (userSite.getSite().getValue() == 1) {
            return driverServiceProvider.google(query, type);
        }
        throw new ServerException();

    }

    @Transactional
    public BaseResponse changeSearchType(SearchType type) {
        Users findUser = userRepository.findById(facade.getId());
        if (findUser.getSearchType().equals(type)) return new BaseResponse((long) type.getValue());
        findUser.setSearchType(type);

        return new BaseResponse((long) findUser.getSearchType().getValue());
    }

    public BaseResponse getSearchType() {
        return new BaseResponse((long) userRepository.findById(facade.getId()).getSearchType().getValue());
    }

}
