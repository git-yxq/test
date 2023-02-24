package com.supergo.feign.client;

import com.supergo.http.HttpResult;
import com.supergo.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//指定远程调用哪个微服务
@FeignClient("supergo-manager")
//配置远程调用接口
public interface ItemClient {

    //配置远程调用哪个接口(支持springMVC注解)
    @PostMapping("/item/query/{page}/{size}")
     HttpResult findPage(@RequestBody Item item, @PathVariable("page") int page, @PathVariable("size") int size);

}
