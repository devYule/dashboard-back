package com.yule.dashboard.user;

import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.UserSiteRank;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.SearchbarStyle;
import com.yule.dashboard.entities.enums.SiteType;
import com.yule.dashboard.mypage.SiteRepository;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.pbl.security.SecurityPrincipal;
import com.yule.dashboard.pbl.security.SecurityProvider;
import com.yule.dashboard.pbl.utils.MailAuthenticationUtils;
import com.yule.dashboard.redis.entities.RedisBaseUserInfoEntity;
import com.yule.dashboard.search.UserSiteRankRepository;
import com.yule.dashboard.user.model.data.req.LoginSuccessData;
import com.yule.dashboard.user.model.data.req.SignupInfoData;
import com.yule.dashboard.user.model.data.req.SignupMailCheckData;
import com.yule.dashboard.user.model.data.req.SignupMailInfoData;
import com.yule.dashboard.user.model.data.res.AccessTokenData;
import com.yule.dashboard.user.model.data.res.CheckIdData;
import com.yule.dashboard.user.model.data.res.CheckPwData;
import com.yule.dashboard.user.model.data.res.RedisKeyData;
import com.yule.dashboard.user.model.dto.WidgetVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProvider provider;
    private final MailAuthenticationUtils mailAuthenticationUtils;
    private final SecurityFacade facade;

    // 탈퇴: history 에 저장 ( em 으로 json 화 ) , Users 테이블에는 데이터 삭제

    public RedisKeyData checkId(CheckIdData loginId) {
        if(Pattern.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*", loginId.loginId())) throw new ClientException(ExceptionCause.ID_LANGUAGE_ERROR);
        Users user = userRepository.findByLoginId(loginId.loginId());
        if (user == null) throw new ClientException(ExceptionCause.ID_NOT_EXISTS);
        RedisBaseUserInfoEntity cacheUser = RedisBaseUserInfoEntity.builder()
                .pw(user.getPw())
                .mail(user.getMail())
                .nick(user.getNick())
                .loginId(user.getLoginId())
                .pic(user.getPic())
                .searchbar(user.getSearchbar())
                .pk(user.getId())
                .build();
        return new RedisKeyData(userRepository.save(cacheUser));
    }

    @Transactional
    public LoginSuccessData checkPw(CheckPwData data) {
        RedisBaseUserInfoEntity findInfo = userRepository.findByKey(data.key());
        if (!passwordEncoder.matches(data.pw(), findInfo.getPw())) {
            throw new ClientException(ExceptionCause.PW_NOT_MATCHES);
        }
        String at = genAndSaveToken(findInfo.getPk());

        // get widgets
//        List<WidgetVo> widgetVos = userRepository.getAllWidgets(findInfo.getPk(), BaseState.ACTIVATED, 1)
//                .stream()
//                .map(w -> WidgetVo.builder()
//                        .id(w.getId())
//                        .order(w.getOrder())
//                        .width(w.getWidth().getValue())
//                        .height(w.getHeight().getValue())
//                        .url(w.getUrl().getUrl())
//                        .isShown(w.getIsShown().getValue())
//                        .build())
//                .toList();

        return new LoginSuccessData(at);
    }


    public RedisKeyData signupInfo(SignupInfoData data) {

        List<Users> findUsers = userRepository.checkSignupInfo(data.loginId(), data.nick());

        if (findUsers.size() == 2) throw new ClientException(ExceptionCause.ID_AND_NICK_IS_ALREADY_EXISTS);

        try {

            if (findUsers.get(0).getLoginId().equals(data.loginId())) {
                throw new ClientException(ExceptionCause.ID_IS_ALREADY_EXISTS);
            }
            throw new ClientException(ExceptionCause.NICK_IS_ALREADY_EXISTS);

        } catch (IndexOutOfBoundsException ignore) {

            return new RedisKeyData(userRepository.save(RedisBaseUserInfoEntity.builder()
                    .loginId(data.loginId())
                    .nick(data.nick())
                    .pw(passwordEncoder.encode(data.pw()))
                    .build()));

        }
    }

    public RedisKeyData mailInfo(SignupMailInfoData data) {
        if (userRepository.existsByMail(data.mail())) throw new ClientException(ExceptionCause.MAIL_IS_ALREADY_EXISTS);
        String savedMailKey = userRepository.saveMailCode(mailAuthenticationUtils.sendAuthMail(data.mail()));
        RedisBaseUserInfoEntity userInfo = userRepository.findByKey(data.key());
        userInfo.setValidMailKey(savedMailKey);
        userInfo.setMail(data.mail());
        userRepository.save(userInfo);

        return new RedisKeyData(data.key());
    }

    public LoginSuccessData mailCheck(SignupMailCheckData data) {
        RedisBaseUserInfoEntity cacheUser = userRepository.findByKey(data.key());
        if (!userRepository.checkMailCode(cacheUser.getValidMailKey(), data.code())) {
            throw new ClientException(ExceptionCause.AUTH_CODE_IS_NOT_MATCHES);
        }

        Users saveUser = userRepository.save(Users.builder()
                .loginId(cacheUser.getLoginId())
                .pw(cacheUser.getPw())
                .nick(cacheUser.getNick())
                .mail(cacheUser.getMail())
                .searchbar(SearchbarStyle.LINE)
                .pic("default")
                .build());

        userRepository.deleteCache(data.key());
        return new LoginSuccessData(genAndSaveToken(saveUser.getId()));
    }

    public AccessTokenData refreshToken(HttpServletRequest request) {
        String rt = userRepository.refreshToken(provider.getTokenFromHeader(request));
        if (rt == null) {
            throw new ClientException(ExceptionCause.TOKEN_IS_EXPIRED);
        }
        // get user pk from rt
        return new AccessTokenData(genAndSaveToken(facade.getId(rt)));
    }

    /* --- Extracted Methods -- */


    private String genAndSaveToken(Long id) {
        // create tokens
        String at = provider.genAT(SecurityPrincipal.builder()
                .id(id)
                .build());
        String rt = provider.genRT(SecurityPrincipal.builder()
                .id(id)
                .build());

        // save in redis - key: at, value: rt
        userRepository.saveToken(at, rt);
        return at;
    }


    @Transactional
    public void plusRank(int siteIdentity) {
        Site findSite = siteRepository.findByUserIdAndStateAndSite(facade.getId(), BaseState.ACTIVATED, SiteType.getByValue(siteIdentity));
        UserSiteRank rank = findSite.getRank();
        rank.setCount(rank.getCount() + 1);
    }
}
