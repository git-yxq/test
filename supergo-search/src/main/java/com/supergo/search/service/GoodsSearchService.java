package com.supergo.search.service;

import com.supergo.http.HttpResult;
import com.supergo.search.entity.SearchResult;

import java.util.Map;

public interface GoodsSearchService {
    SearchResult search(String keyword, Map<String, String> filters, int page, int size);
    HttpResult importAllGoods();
}
