package com.supergo.adminui;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//启动Actuator的Server端
@EnableAdminServer
@SpringBootApplication
public class AdminUIApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminUIApplication.class,args);
    }
}
