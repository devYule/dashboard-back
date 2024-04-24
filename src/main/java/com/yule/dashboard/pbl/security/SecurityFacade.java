package com.yule.dashboard.pbl.security;

import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityFacade {

    private SecurityUserDetails getLoginUser() {
        try {
            return (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            log.info("error", e);
            throw new ClientException(ExceptionCause.RETRY_SIGN_IN);
        }
    }

    public Long getId() {
        return getLoginUser().getPrincipal().getId();
    }
//    public Auth getLoginUserAuth(){
//        return Auth.getAuth(getLoginUser().getPrincipal().getAuth());
//    }

}
