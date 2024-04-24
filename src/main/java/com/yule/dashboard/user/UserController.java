package com.yule.dashboard.user;

import com.yule.dashboard.user.model.data.req.LoginSuccessData;
import com.yule.dashboard.user.model.data.req.SignupInfoData;
import com.yule.dashboard.user.model.data.req.SignupMailCheckData;
import com.yule.dashboard.user.model.data.req.SignupMailInfoData;
import com.yule.dashboard.user.model.data.res.AccessTokenData;
import com.yule.dashboard.user.model.data.res.CheckIdData;
import com.yule.dashboard.user.model.data.res.CheckPwData;
import com.yule.dashboard.user.model.data.res.RedisKeyData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/id")
    public RedisKeyData checkId(@Validated @RequestBody CheckIdData loginId) {
        return userService.checkId(loginId);
    }

    @PostMapping("/pw")
    public LoginSuccessData checkPw(@Validated @RequestBody CheckPwData data) {
        return userService.checkPw(data);
    }

    @PostMapping("/info")
    public RedisKeyData signupInfo(@Validated @RequestBody SignupInfoData data) {
        return userService.signupInfo(data);
    }

    @PostMapping("/mail")
    public RedisKeyData mailInfo(@Validated @RequestBody SignupMailInfoData data) {
        return userService.mailInfo(data);
    }

    @PostMapping("/mail/check")
    public LoginSuccessData mailCheck(@Validated @RequestBody SignupMailCheckData data) {
        return userService.mailCheck(data);
    }

    @GetMapping("/rt")
    public AccessTokenData refreshToken(HttpServletRequest request) {
        return userService.refreshToken(request);
    }
}
