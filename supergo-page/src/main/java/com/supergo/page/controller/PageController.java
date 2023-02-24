package com.supergo.page.controller;

import com.supergo.http.HttpResult;
import com.supergo.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("/page/build")
    @ResponseBody
    public HttpResult buildStaticPage() {
        try {
            return pageService.statcPageBuild();
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error(500, e.getMessage());
        }
    }
    @GetMapping("/page/goods/{goodsId}")
    @ResponseBody
    public HttpResult buildGoodsPage(@PathVariable long goodsId) {
        try {
            HttpResult httpResult = pageService.buildGoodsPage(goodsId);
            return httpResult;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResult.error(500, e.getMessage());
        }
    }
    @GetMapping("/goods/stock/{goodsId}")
    @ResponseBody
    public HttpResult getGoodsStock(@PathVariable long goodsId) {
        Map<Object, Object> itemsStock = pageService.getItemsStock(goodsId);
        return HttpResult.ok(itemsStock);
    }
}
