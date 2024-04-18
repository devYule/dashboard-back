package com.yule.dashboard.user;

import com.yule.dashboard.user.model.data.req.LoginSuccessData;
import com.yule.dashboard.user.model.data.req.SignupInfoData;
import com.yule.dashboard.user.model.data.req.SignupMailCheckData;
import com.yule.dashboard.user.model.data.req.SignupMailInfoData;
import com.yule.dashboard.user.model.data.res.CheckIdData;
import com.yule.dashboard.user.model.data.res.CheckPwData;
import com.yule.dashboard.user.model.data.res.RedisKeyData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/id")
    public RedisKeyData checkId(CheckIdData loginId) {
        return userService.checkId(loginId);
    }

    @PostMapping("/pw")
    public LoginSuccessData checkPw(CheckPwData data) {
        return userService.checkPw(data);
    }

    @PostMapping("/info")
    public RedisKeyData signupInfo(SignupInfoData data) {
        return userService.signupInfo(data);
    }

    @PostMapping("/mail")
    public RedisKeyData mailInfo(SignupMailInfoData data) {
        return userService.mailInfo(data);
    }

    @GetMapping("/mail")
    public LoginSuccessData mailCheck(SignupMailCheckData data) {
        return userService.mailCheck(data);
    }


}
