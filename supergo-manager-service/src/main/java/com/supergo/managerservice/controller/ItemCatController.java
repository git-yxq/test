package com.supergo.managerservice.controller;

import com.netflix.discovery.converters.Auto;
import com.supergo.http.HttpResult;
import com.supergo.managerservice.service.ItemCatService;
import com.supergo.pojo.Itemcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("itemcat")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;
    @RequestMapping("/category/list")
    public HttpResult getItemCategory() {
        HttpResult httpResult = itemCatService.categorysList();
        return httpResult;
    }
    @RequestMapping("/category/{categoryId}")
    public Itemcat getItemCatById(@PathVariable long categoryId) {
        Itemcat itemcat = itemCatService.findOne(categoryId);
        return itemcat;
    }
}
