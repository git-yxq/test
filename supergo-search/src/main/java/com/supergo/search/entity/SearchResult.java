package com.supergo.search.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SearchResult implements Serializable {
    // 查询结果(商品)列表
    private List<GoodsEntity> goodsList;
    // 分组信息
    private List<?> aggs;

    public List<GoodsEntity> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsEntity> goodsList) {
        this.goodsList = goodsList;
    }

    public List<?> getAggs() {
        return aggs;
    }

    public void setAggs(List<?> aggs) {
        this.aggs = aggs;
    }
}
