package com.yule.dashboard.pbl;

import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.Widget;
import com.yule.dashboard.entities.embeddable.UrlPath;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.TrueOrFalse;
import com.yule.dashboard.entities.enums.WidgetSize;
import com.yule.dashboard.entities.enums.WidgetType;
import com.yule.dashboard.user.repositories.jparepo.UserJpaRepository;
import com.yule.dashboard.widget.WidgetRepository;
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
        userJpaRepository.save(user);
        userJpaRepository.flush();
        for (int i = 0; i < 10; i++) {
            int randomWidth = (int) (Math.random() * 2) + 1;
            int randomHeight = (int) (Math.random() * 2) + 1;
            Widget widget = new Widget(
                    i + 1,
                    WidgetSize.getByValue(randomWidth),
                    WidgetSize.getByValue(randomHeight),
                    new UrlPath("https://www.naver.com"),
                    TrueOrFalse.TRUE,
                    WidgetType.BOOKMARK);
            widget.setUser(user);
            widgetRepository.save(widget);
        }
        widgetRepository.flush();

        List<Widget> byUserIdAndStateOffsetPageLimit = widgetRepository.findByUserIdAndStateOffsetPageLimit(user.getId(), BaseState.ACTIVATED, 1);
        for (Widget widget : byUserIdAndStateOffsetPageLimit) {
            System.out.println("widget.getId() = " + widget.getId());

        }

    }


}
