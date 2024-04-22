package com.yule.dashboard.search;

import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.mypage.HistoryRepository;
import com.yule.dashboard.mypage.SiteRepository;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.mythreadpool.ThreadPoolProvider;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.search.drivers.DriverServiceProvider;
import com.yule.dashboard.search.drivers.model.NaverSiteInfo;
import com.yule.dashboard.search.drivers.model.SiteInfo;
import com.yule.dashboard.search.model.req.SearchData;
import com.yule.dashboard.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service

public class SearchService {
    private final HistoryRepository historyRepository;
    private final SiteRepository siteRepository;
    private final UserSiteRankRepository userSiteRankRepository;
    private final UserRepository userRepository;
    private final DriverServiceProvider driverServiceProvider;
    private final SecurityFacade facade;
    private final ExecutorService threadPool;

    public SearchService(HistoryRepository historyRepository, DriverServiceProvider driverServiceProvider, SiteRepository siteRepository,
                         UserRepository userRepository,
                         UserSiteRankRepository userSiteRankRepository, SecurityFacade facade, ThreadPoolProvider threadPoolProvider) {
        this.historyRepository = historyRepository;
        this.driverServiceProvider = driverServiceProvider;
        this.siteRepository = siteRepository;
        this.userRepository = userRepository;
        this.userSiteRankRepository = userSiteRankRepository;
        this.facade = facade;
        this.threadPool = threadPoolProvider.getThreadPool();
    }

    // logic
    public List<List<SearchData>> search(String query) {
        List<Site> findSites = siteRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED);
        List<Integer> orderedUserSiteRanking = userSiteRankRepository.findSiteNumByUserId(facade.getId());

        Map<Integer, List<SearchData>> unorderedResult = new HashMap<>();
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
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new ServerException();
            }
        }

        List<Integer> sortedKeys = new ArrayList<>(unorderedResult.keySet());
        sortedKeys.sort(Comparator.naturalOrder());


        return sortedKeys.stream().map(unorderedResult::get).toList();
    }

    private Future<Boolean> threadAction(String query, Site findSite, List<Integer> orderedUserSiteRanking, Map<Integer, List<SearchData>> unorderedResult) {
        return threadPool.submit(() -> {
            List<? extends SiteInfo> siteInfos = doCrawling(findSite, query);
            int mapKey = orderedUserSiteRanking.indexOf(findSite.getSite().getValue());
            unorderedResult.put(mapKey, siteInfos.stream().map(s -> SearchData.builder()
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
