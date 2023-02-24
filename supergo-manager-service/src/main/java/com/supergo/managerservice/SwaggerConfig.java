package com.supergo.managerservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//声明该类是配置类
@Configuration
//启用swagger
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    //配置swagger的方法
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) //配置文档类型
                .apiInfo(apiInfo()) //配置文档信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.supergo.controller")) //指定接口包
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() //生成文档信息配置对象
    {
        return new ApiInfoBuilder().title("Supergo Api Doc")
                .description("网上商城")
                .contact(new Contact("lyd", null, "75687343@qq.com"))
                .version("1.0.0")
                .build();
    }



}