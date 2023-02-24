package com.supergo.sso.controller;

import com.supergo.http.HttpResult;
import com.supergo.sso.service.UserService;
import com.supergo.user.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/dologin")
    public HttpResult doLogin(@RequestBody UserInfo userInfo) {
        HttpResult httpResult = userService.doLogin(userInfo);
        return httpResult;
    }
}