package com.supergo.portal.controller;

import com.supergo.feign.client.ItemCatClient;
import com.supergo.http.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorys")
public class ItemCatController {
    @Autowired
    private ItemCatClient itemCatFeign;

    @GetMapping("/categorys")
    public HttpResult getCategorys() {
        return itemCatFeign.getItemCatgoryList();
    }
}
