package com.supergo.page.service;

import com.alibaba.fastjson.JSON;
import com.supergo.feign.client.GoodsClient;
import com.supergo.feign.client.ItemCatClient;
import com.supergo.http.HttpResult;
import com.supergo.page.config.GoodsLock;
import com.supergo.pojo.Goods;
import com.supergo.pojo.Goodsdesc;
import com.supergo.pojo.Item;
import com.supergo.pojo.Itemcat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PageService {
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private GoodsClient goodsFeign;
    @Autowired
    private ItemCatClient itemCatFeign;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GoodsLock goodsLock;

    public HttpResult statcPageBuild() throws IOException {
        FileWriter fileWriter = new FileWriter("d:/temp/hello.html");
        Context context = new Context();
        context.setVariable("hello", "hello spring and thymeleaf!");
        templateEngine.process("hello", context, fileWriter);
        return HttpResult.ok();
    }

    public HttpResult buildGoodsPage(long goodsId) throws IOException {
        FileWriter fileWriter = new FileWriter("d:/temp/goods/"+goodsId+".html");
        Context context = getGoodsData(goodsId);
        templateEngine.process("item", context, fileWriter);
        return HttpResult.ok();
    }
    public Context getGoodsData(Long goodsId){
        Context context = new Context();
        // Goods、
        Goods goods = goodsFeign.getGoodsById(goodsId);
        //查询商品的分类  3个分类
        Itemcat itemCat1 = itemCatFeign.getItemCatById(goods.getCategory1Id());
        Itemcat itemCat2 = itemCatFeign.getItemCatById(goods.getCategory1Id());
        Itemcat itemCat3 = itemCatFeign.getItemCatById(goods.getCategory1Id());
        // GoodsDesc、
        //GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        Goodsdesc goodsDesc = goodsFeign.getGoodsDescById(goodsId);
        //取图片列表
        String jsonImages = goodsDesc.getItemImages();
        if (StringUtils.isNotBlank(jsonImages)) {
            try {
                List<Map> imagesList = JSON.parseArray(jsonImages, Map.class);
                context.setVariable("itemImageList", imagesList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //取属性信息
        String jsonCustomAttributeItems = goodsDesc.getCustomAttributeItems();
        if (StringUtils.isNotBlank(jsonCustomAttributeItems)) {
            try {
                List<Map> customAttributeList = JSON.parseArray(jsonCustomAttributeItems, Map.class);
                context.setVariable("customAttributeList", customAttributeList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //提取规格数据
        String jsonSpecificationItems = goodsDesc.getSpecificationItems();
        if (StringUtils.isNotBlank(jsonSpecificationItems)) {
            try {
                List<Map> specificationItems = JSON.parseArray(jsonSpecificationItems, Map.class);
                context.setVariable("specificationList", specificationItems);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // List<Item>
        List<Item> itemList = goodsFeign.getItemList(goodsId);
        context.setVariable("goods", goods);
        context.setVariable("goodsDesc", goodsDesc);
        context.setVariable("itemCat1", itemCat1);
        context.setVariable("itemCat2", itemCat2);
        context.setVariable("itemCat3", itemCat3);
        context.setVariable("itemList", itemList);
        return context;
    }

    /**
     * 查询sku库存
     * @param goodsId
     * @return
     */
    public Map<Object, Object> getItemsStock(long goodsId) {
        //防止缓存穿透,非法id直接返回
        if (goodsId <= 0 ) {
            Map result = new HashMap();
            result.put("0","0");
            return result;
        }
        //查询数据库之前先查询缓存
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("goods-stock:" + goodsId);
        if (entries != null && !entries.isEmpty()) {
            return entries;
        }
        Map<Object, Object> result = new HashMap<>();
        //获得锁
        ReentrantLock lock = goodsLock.getLock(goodsId);
        //加锁，同一个商品只需要有一个线程查询就可以了防止缓存击穿
        if (lock.tryLock()) {
            //查询数据库
            List<Item> itemList = goodsFeign.getItemList(goodsId);
            //防止缓存穿透,把空值添加到缓存
            if (itemList == null || itemList.isEmpty()) {
                //添加到缓存
                redisTemplate.opsForHash().put("goods-stock:" + goodsId,
                        "0", "0");
                redisTemplate.expire("goods-stock:" + goodsId, 5, TimeUnit.MINUTES);
                result.put("0","0");
                //返回结果
                return result;
            }
            //取查询结果
            Map<Object, Object> map = new HashMap<>();
            itemList.forEach(item -> {
                map.put(item.getId(), item.getNum());
                //添加到缓存
                redisTemplate.opsForHash().put("goods-stock:" + goodsId,
                        item.getId().toString(), item.getNum().toString());
            });
            //提高缓存的利用率设置缓存的过期时间
            redisTemplate.expire("goods-stock:" + goodsId, 1, TimeUnit.DAYS);
            result = map;
            //释放锁
            lock.unlock();
            goodsLock.removeLock(goodsId);
        } else {
            //加锁失败，等待100毫秒
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //递归查询，应该从缓存中可以取到数据了
            result = getItemsStock(goodsId);
        }
        return result;
    }

}
