package com.supergo.zuul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class,args);
    }


    @Component
    @Primary
    //配置swagger
    class SwaggerResourceConfig implements SwaggerResourcesProvider {
        @Autowired
        RouteLocator routeLocator;

        @Override
        public List<SwaggerResource> get() {
            //Swagger文档json来源
            List<SwaggerResource> resources = new ArrayList<>();
            //获得网关路由路径
            List<Route> routes = routeLocator.getRoutes(); //
            //便利每个微服务路由路径
            for (Route route:routes) {
                //添加swagger 文档json获取的路径
                resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs")));
                System.err.println("~~~~~~~~~~~替换前~~~~~~~~~~~~~~~~"+route.getFullPath());
                System.err.println("~~~~~~~~~~~替换后~~~~~~~~~~~~~~~~"+route.getFullPath().replace("**", "v2/api-docs"));
            }
            return resources;
        }
        private SwaggerResource swaggerResource(String name, String location) {
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setName(name);
            swaggerResource.setLocation(location);
            swaggerResource.setSwaggerVersion("2.0");
            return swaggerResource;
        }

    }
}
