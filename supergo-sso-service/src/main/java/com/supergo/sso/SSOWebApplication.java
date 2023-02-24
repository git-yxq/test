package com.supergo.sso;

import com.supergo.user.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

//开启Feign远程调用
@EnableEurekaClient
@SpringBootApplication
@MapperScan(value="com.supergo.mapper")
@ComponentScan(value={"com.supergo.user.utils","com.supergo.sso"})
public class SSOWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSOWebApplication.class);
    }

}
