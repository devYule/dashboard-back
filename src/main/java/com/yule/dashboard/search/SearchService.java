package com.yule.dashboard.search;

import com.yule.dashboard.bookmark.BookmarkRepository;
import com.yule.dashboard.entities.BookMark;
import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.mypage.HistoryRepository;
import com.yule.dashboard.mypage.SiteRepository;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.mythreadpool.ThreadPoolProvider;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.search.drivers.DriverServiceProvider;
import com.yule.dashboard.search.drivers.model.NaverSiteInfo;
import com.yule.dashboard.search.drivers.model.SiteInfo;
import com.yule.dashboard.search.model.req.SearchDto;
import com.yule.dashboard.search.model.resp.SiteRespData;
import com.yule.dashboard.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
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
    private final ExecutorService threadPool;

    public SearchService(HistoryRepository historyRepository, DriverServiceProvider driverServiceProvider, SiteRepository siteRepository,
                         UserRepository userRepository, BookmarkRepository bookmarkRepository,
                         SecurityFacade facade, ThreadPoolProvider threadPoolProvider) {
        this.historyRepository = historyRepository;
        this.driverServiceProvider = driverServiceProvider;
        this.siteRepository = siteRepository;
        this.userRepository = userRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.facade = facade;
        this.threadPool = threadPoolProvider.getThreadPool();
    }

    // logic
    public List<SiteRespData> search(String query) {
        List<Site> findSites = siteRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED);
        if (findSites.isEmpty()) throw new ClientException(ExceptionCause.SITE_IS_EMPTY);
        // count 기준 desc -> site 식별자번호를 list 에 추가. -> 0번 idx 부터 count 순위로 내림차순 정렬.
        List<Integer> orderedUserSiteRanking = findSites.stream()
                .sorted((s1, s2) -> (int) (s2.getRank().getCount() - s1.getRank().getCount()))
                .map(s -> s.getSite().getValue()).toList();

        // 멀티스레드이므로 순서보장이 안됨 -> orderedUserSiteRanking 과 같은 key를 가짐이 보장되는 ( site num ) hashMap 을 만들어,
        // 마지막 리턴 전에 sort 수행.
        Map<Integer, List<SearchDto>> unorderedResult = new HashMap<>();
        threadAction(query, findSites, unorderedResult);

        // add history
        Users findUser = userRepository.findById(facade.getId());
        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.SEARCH),
                HistoryType.SEARCH, query);


        // 북마크 여부 찾기
        Map<String, SearchDto> totalUrls = new HashMap<>();
        unorderedResult.values().forEach(searchDtoList -> searchDtoList.forEach(dto -> totalUrls.put(dto.getLink(), dto)));

        List<BookMark> bookmarkedUrls = bookmarkRepository.findByUrlIn(new ArrayList<>(totalUrls.keySet()));
        bookmarkedUrls.forEach(k -> {
            if (k.getState().equals(BaseState.DEACTIVATED)) return;
            totalUrls.get(k.getUrl()).setBookmarkId(k.getId());
        });


        return orderedUserSiteRanking.stream().

                map(siteIdentity ->

                {
                    List<SearchDto> searchDtos = unorderedResult.get(siteIdentity);
                    return new SiteRespData(siteIdentity, searchDtos);
                }).

                toList();
    }

    private void threadAction(String query, List<Site> findSites,
                              Map<Integer, List<SearchDto>> unorderedResult) {

        Map<Site, Future<List<? extends SiteInfo>>> futures = new HashMap<>();
        for (Site site : findSites) {
            Future<List<? extends SiteInfo>> future = doCrawling(site, query);
            futures.put(site, future);
        }


        for (Site findSite : findSites) {
            try {
                Future<List<? extends SiteInfo>> future = futures.get(findSite);
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


    private Future<List<? extends SiteInfo>> doCrawling(Site userSite, String query) {
        return threadPool.submit(() ->
                switch (userSite.getSite().getValue()) {
                    case 0 -> driverServiceProvider.naver(query);
                    case 1 -> driverServiceProvider.google(query);
                    default -> new ArrayList<>();
                });
    }
}
