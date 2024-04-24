package com.yule.dashboard.search;

import com.yule.dashboard.bookmark.BookmarkRepository;
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
    private final UserSiteRankRepository userSiteRankRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final DriverServiceProvider driverServiceProvider;
    private final SecurityFacade facade;
    private final ExecutorService threadPool;

    public SearchService(HistoryRepository historyRepository, DriverServiceProvider driverServiceProvider, SiteRepository siteRepository,
                         UserRepository userRepository, BookmarkRepository bookmarkRepository,
                         UserSiteRankRepository userSiteRankRepository, SecurityFacade facade, ThreadPoolProvider threadPoolProvider) {
        this.historyRepository = historyRepository;
        this.driverServiceProvider = driverServiceProvider;
        this.siteRepository = siteRepository;
        this.userRepository = userRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userSiteRankRepository = userSiteRankRepository;
        this.facade = facade;
        this.threadPool = threadPoolProvider.getThreadPool();
    }

    // logic
    public List<List<SearchDto>> search(String query) {
        List<Site> findSites = siteRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED);
        if(findSites.isEmpty()) throw new ClientException(ExceptionCause.SITE_IS_EMPTY);
        List<Integer> orderedUserSiteRanking = userSiteRankRepository.findSiteNumByUserId(facade.getId());

        Map<Integer, List<SearchDto>> unorderedResult = new HashMap<>();
        List<Future<Boolean>> allDoneChecker = new ArrayList<>();
        for (Site findSite : findSites) {
            allDoneChecker.add(threadAction(query, findSite, orderedUserSiteRanking, unorderedResult));
        }

        // add history
        Users findUser = userRepository.findById(facade.getId());
        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.SEARCH),
                HistoryType.SEARCH, query);

        for (Future<Boolean> future : allDoneChecker) {
            try {
                Boolean b = future.get();
                log.info("future.get(): {}", b);
            } catch (InterruptedException | ExecutionException e) {
                throw new ServerException();
            }
        }

        List<Integer> sortedKeys = new ArrayList<>(unorderedResult.keySet());
        sortedKeys.sort(Comparator.naturalOrder());

        // 북마크 여부 찾기
        Map<String, SearchDto> totalUrls = new TreeMap<>();
        List<List<SearchDto>> result =
                sortedKeys.stream().map(k -> unorderedResult.get(k).stream().peek(obj -> totalUrls.put(obj.getLink(),
                        obj)).toList()).toList();

        List<String> bookmarkedUrls = bookmarkRepository.findUrlByUrlPathIn(new ArrayList<>(totalUrls.keySet()));
        bookmarkedUrls.forEach(k -> totalUrls.get(k).setIsBookmarked(1));

        return result;
    }

    private Future<Boolean> threadAction(String query, Site findSite, List<Integer> orderedUserSiteRanking, Map<Integer, List<SearchDto>> unorderedResult) {
        return threadPool.submit(() -> {
            List<? extends SiteInfo> siteInfos = doCrawling(findSite, query);
            int mapKey = orderedUserSiteRanking.indexOf(findSite.getSite().getValue());
            unorderedResult.put(mapKey, siteInfos.stream().map(s -> SearchDto.builder()
                    .title(s.getTitle())
                    .link(s.getLink())
                    .content(s.getContent())
                    .category(s.getCategory())
                    .iconPath(s.getIconPath())
                    .subTitle(s instanceof NaverSiteInfo ? ((NaverSiteInfo) s).getSubTitle() : null)
                    .build()).toList());
            return true;
        });
    }

    private List<? extends SiteInfo> doCrawling(Site userSite, String query) {
        return switch (userSite.getSite().getValue()) {
            case 0 -> driverServiceProvider.naver(query);
            case 1 -> driverServiceProvider.google(query);
            default -> new ArrayList<>();
        };
    }
}
