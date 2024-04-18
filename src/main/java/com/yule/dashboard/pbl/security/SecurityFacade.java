package com.yule.dashboard.pbl.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityFacade {

    private SecurityUserDetails getLoginUser() {
        return (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public Long getId() {
        return getLoginUser().getPrincipal().getId();
    }
//    public Auth getLoginUserAuth(){
//        return Auth.getAuth(getLoginUser().getPrincipal().getAuth());
//    }

}
