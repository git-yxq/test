package com.supergo.managerservice.interceptor;

import com.supergo.user.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
        //jwt Token 通常是通过请求头来发送的  => Authorization头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) { //没获得该头 -> 没有token
            throw new AuthException("没有登录"); //抛出异常
        }
        // Authorization:Bearer Token内容
        if (!authHeader.startsWith("Bearer ")) { //检查Authorization是否以Bearer 开头
            throw new AuthException("权限不足");
        }

        //截取字符串,去掉"Bearer "部分
        final String token = authHeader.substring(7); // The part after "Bearer "

        //调用工具类解析token -> 抛出异常
        Claims claims = jwtUtil.parseToken(token);

        if (claims != null) {
            //提取解析结果,存入request.交给后面的Controller使用
            if ("admin".equals(claims.get("roles"))) {//如果是管理员
                request.setAttribute("admin_claims", claims);
            }
            if ("user".equals(claims.get("roles"))) {//如果是用户
                request.setAttribute("user_claims", claims);
            }
        }
        return true;
    }
}