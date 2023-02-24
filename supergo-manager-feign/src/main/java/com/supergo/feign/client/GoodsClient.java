package com.supergo.feign.client;

import com.supergo.http.HttpResult;
import com.supergo.pojo.Goods;
import com.supergo.pojo.Goodsdesc;
import com.supergo.pojo.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//指定远程调用哪个微服务
@FeignClient("supergo-manager")
//配置远程调用接口
public interface GoodsClient {

    @GetMapping("/goods/{goodsId}")
    public Goods getGoodsById(@PathVariable("goodsId") long goodsId);
    @GetMapping("/goods/desc/{goodsId}")
    public Goodsdesc getGoodsDescById(@PathVariable("goodsId") long goodsId);
    @GetMapping("/goods/item/{goodsId}")
    public List<Item> getItemList(@PathVariable("goodsId") long goodsId);

}
