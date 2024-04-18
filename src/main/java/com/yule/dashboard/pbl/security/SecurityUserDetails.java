package com.yule.dashboard.pbl.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUserDetails implements UserDetails {

    private SecurityPrincipal principal;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return principal == null ? null :
//                principal.getRolles()
//                        .stream()
//                        .map(r => new SimpleGrantedAuthority("ROLE_" + r))
//                        .collect(Collectors.toList());

        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
