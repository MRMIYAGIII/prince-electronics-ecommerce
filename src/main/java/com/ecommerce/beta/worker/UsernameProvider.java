package com.ecommerce.beta.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecommerce.beta.entity.UserInfo;
import com.ecommerce.beta.service.UserInfoService;

@Service
public class UsernameProvider {

    @Autowired
    UserInfoService userInfoService;

    public UserInfo get() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userInfoService.findByUsername(username);
    }
}