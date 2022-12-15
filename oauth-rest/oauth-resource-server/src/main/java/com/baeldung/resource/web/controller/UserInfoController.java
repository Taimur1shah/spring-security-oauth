package com.baeldung.resource.web.controller;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    @GetMapping("/user/info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt principal) {

        Map<String, String> map = new Hashtable<String, String>();
        map.put("user_name", principal.getClaimAsString("preferred_username"));

        return Collections.unmodifiableMap(map);
    }
}