package com.supergo.managerservice.service;

import com.supergo.managerservice.service.base.BaseService;
import com.supergo.pojo.Item;

import java.util.List;
import java.util.Map;

public interface ItemService extends BaseService<Item> {
    public List<Item> skuList(Long goodsId);
    public int getItemStock(long itemId);
}
