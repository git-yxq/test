package com.supergo.managerweb.controller;

import com.supergo.feign.client.ItemClient;
import com.supergo.http.HttpResult;
import com.supergo.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
//允许跨域注解,加上该注解,当前Controller中的资源,均支持跨域访问
//origins属性: 配置允许哪些域能够访问
//@CrossOrigin
public class ItemController {

    //引入feign接口
    @Autowired
    private ItemClient itemClient;

    @PostMapping("/query/{page}/{size}")
    public HttpResult findPage(@RequestBody(required = false) Item item, @PathVariable int page, @PathVariable int size) {
       //远程调用ManagerService项目中分页查询
        HttpResult result = itemClient.findPage(item, page, size);
        return result;
    }

    @GetMapping("/query/{page}/{size}")
    //分页查询所有
    public HttpResult findPage2( @PathVariable int page, @PathVariable int size) {
        //远程调用ManagerService项目中分页查询
        HttpResult result = itemClient.findPage(new Item(), page, size);
        return result;
    }

}
