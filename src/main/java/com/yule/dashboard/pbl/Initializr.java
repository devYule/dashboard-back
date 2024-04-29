package com.yule.dashboard.pbl;

import com.yule.dashboard.bookmark.BookmarkRepository;
import com.yule.dashboard.entities.*;
import com.yule.dashboard.entities.enums.*;
import com.yule.dashboard.mypage.repositories.jparepo.SiteJpaRepository;
import com.yule.dashboard.search.repositories.UserSiteRankJpaRepository;
import com.yule.dashboard.user.repositories.jparepo.UserJpaRepository;
import com.yule.dashboard.widget.repositories.jparepo.WidgetJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("local")
@Component
@RequiredArgsConstructor
@Slf4j
public class Initializr {

    private final PasswordEncoder encoder;
    private final UserJpaRepository userJpaRepository;
    private final WidgetJpaRepository widgetRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SiteJpaRepository siteJpaRepository;
    private final UserSiteRankJpaRepository userSiteRankJpaRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        log.info("initData!!!");
        String encoded = encoder.encode("123123");
        Users user = Users.builder()
                .loginId("testt")
                .pw(encoded)
                .mail("testMail@mail.com")
                .nick("testt")
                .build();
        Site naver = new Site(SiteType.NAVER, UserSiteRank.builder()
                .user(user)
                .count(0)
                .build());
        Site google = new Site(SiteType.GOOGLE, UserSiteRank.builder()
                .user(user)
                .count(0)
                .build());
        naver.setUser(user);
        google.setUser(user);

        userJpaRepository.save(user);
        userJpaRepository.flush();
        siteJpaRepository.save(naver);
        siteJpaRepository.save(google);
        siteJpaRepository.flush();


        for (int i = 0; i < 10; i++) {
            int randomWidth = (int) (Math.random() * 2) + 1;
            int randomHeight = (int) (Math.random() * 2) + 1;
            String[] strs = {"ase", "as", "h455", "하ㅐ댇", "다마ㅐ재", "elkf0203", "ㅁㄴㄹ잘32ㅐㅏ2", "믿oaawegkㅁㅈ0gk", "3ㅎ2oegaweㅎ"};

            StringBuilder title = new StringBuilder();
            int randNum = (int) (Math.random() * strs.length);
            for (int j = 0; j < randNum; j++) {
                title.append(strs[j]);
            }
            StringBuilder url = new StringBuilder();
            randNum = (int) (Math.random() * strs.length);
            for (int j = 0; j < randNum; j++) {
                url.append(strs[j]);
            }
            StringBuilder memo = new StringBuilder();
            randNum = (int) (Math.random() * strs.length);
            for (int j = 0; j < randNum; j++) {
                memo.append(strs[j]);
            }

            Bookmark bookMark = new Bookmark(title.toString(), "www." + url + ".com", memo.toString().repeat(1) + i);
            Widget widget = new Widget(
                    WidgetSize.getByValue(randomWidth),
                    WidgetSize.getByValue(randomHeight),
                    "www." + url + ".com",
                    WidgetType.BOOKMARK,
                    bookMark);
            widget.setUser(user);
            bookmarkRepository.save(bookMark);
            widgetRepository.save(widget);
        }
        widgetRepository.flush();

        List<Widget> byUserIdAndStateOffsetPageLimit = widgetRepository.findByUserIdAndStateOffsetPageLimitDesc(user.getId(), BaseState.ACTIVATED, 1);
        for (Widget widget : byUserIdAndStateOffsetPageLimit) {
            System.out.println("widget.getId() = " + widget.getId());

        }

    }


}
