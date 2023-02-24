package com.supergo.feign.client;

import com.supergo.http.HttpResult;
import com.supergo.pojo.Itemcat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//指定远程调用哪个微服务
@FeignClient("supergo-manager")
//配置远程调用接口
public interface ItemCatClient {

    @GetMapping("/itemcat/category/list")
    HttpResult getItemCatgoryList();
    @RequestMapping("/itemcat/category/{categoryId}")
    public Itemcat getItemCatById(@PathVariable("categoryId") long categoryId);
}
