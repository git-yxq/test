package com.supergo.managerservice.exception;

import com.supergo.http.HttpResult;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.message.AuthException;

@ControllerAdvice
public class GlobalExceptionResolver {

    /**
     * token认证异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public HttpResult authExceptionResolver(AuthException e) {
        return HttpResult.error(400, e.getMessage());
    }

    /**
     * jwt的token过期
     * @param e
     * @return
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public HttpResult authExceptionResolver(ExpiredJwtException e) {
        return HttpResult.error(401, "token已经过期");
    }
    /**
     * 系统异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResult exceptionResolver(Exception e) {
        return HttpResult.error(500, e.getMessage());
    }
}
