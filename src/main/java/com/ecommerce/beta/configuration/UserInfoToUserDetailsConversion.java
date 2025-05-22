package com.ecommerce.beta.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.beta.entity.UserInfo;

public class UserInfoToUserDetailsConversion implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private boolean enabled;
    
    public UserInfoToUserDetailsConversion(UserInfo userInfo) {
	    username = userInfo.getUsername();
	    password = userInfo.getPassword();
	    authorities = Arrays.stream(userInfo.getRole().getRoleName().split(","))
	            .map(SimpleGrantedAuthority::new)
	            .collect(Collectors.toList());
	    enabled = userInfo.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
