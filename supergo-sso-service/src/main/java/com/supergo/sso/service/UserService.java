package com.supergo.sso.service;

import com.supergo.http.HttpResult;
import com.supergo.user.utils.UserInfo;

public interface UserService {
    public HttpResult doLogin(UserInfo userInfo);
}
