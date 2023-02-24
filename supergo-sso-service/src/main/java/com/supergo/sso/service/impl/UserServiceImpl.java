package com.supergo.sso.service.impl;

import com.supergo.http.HttpResult;
import com.supergo.managerservice.service.base.impl.BaseServiceImpl;
import com.supergo.pojo.User;
import com.supergo.sso.service.UserService;
import com.supergo.user.utils.JwtUtils;
import com.supergo.user.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Autowired
    private JwtUtils jwtUtil;
    @Override
    //登录
    //UserInfo => 传入用户提交的用户名密码
    public HttpResult doLogin(UserInfo userInfo) {

        //构建查询条件
        User query = new User();
        query.setUsername(userInfo.getUsername());


        List<User> list = findByWhere(query); //根据用户名查询用户

        //用户名不存在
        if (list == null || list.size() == 0) {
            return HttpResult.error(400, "用户名不存在");
        }
        User user = list.get(0); //取出用户
        //比对密码是否正确
        if (!BCrypt.checkpw(userInfo.getPassword(), user.getPassword())) {
            return HttpResult.error(400, "用户名或密码错误");
        }
        //用户登录成功，签发jwt token
        String token = jwtUtil.createToken(user.getId() + "", user.getUsername(), "user");
        return HttpResult.ok(token);
    }
}
