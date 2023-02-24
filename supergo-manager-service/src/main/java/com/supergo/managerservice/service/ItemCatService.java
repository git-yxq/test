package com.supergo.managerservice.service;

import com.supergo.http.HttpResult;
import com.supergo.managerservice.service.base.BaseService;
import com.supergo.pojo.Itemcat;

public interface ItemCatService extends BaseService<Itemcat> {
    HttpResult categorysList();
}
