package com.supergo.search.controller;

import com.supergo.http.HttpResult;
import com.supergo.search.entity.SearchResult;
import com.supergo.search.service.GoodsSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class GoodsSearchController {
    @Autowired
    private GoodsSearchService goodsSearchService;

    @GetMapping("/goods/importall")
    //访问该方法会同步数据
    public HttpResult importAll() {
        //同步数据
        goodsSearchService.importAllGoods();
        return HttpResult.ok();
    }

    @GetMapping("/search")
    public SearchResult search(@RequestParam(required = true) String keyword,
                               @RequestParam(name = "ev", required = false) String filter,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "50") int size) {
        System.out.println(keyword);
        System.out.println(page);
        System.out.println(size);
        Map<String, String> filterMap = null;
        // ev=aaa-bbb#ccc-ddd
        try {
            if (StringUtils.isNotBlank(filter)) {
                //切割键值对
                String[] filters = filter.split("\\#");
                //分离出键-值,并封装到map中
                filterMap = Stream.of(filters).collect(Collectors.toMap(e -> e.split("-")[0], e -> e.split("-")[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(filter);
        System.out.println(filterMap);
        SearchResult searchResult = goodsSearchService.search(keyword, filterMap, page, size);
        return searchResult;
    }
}
