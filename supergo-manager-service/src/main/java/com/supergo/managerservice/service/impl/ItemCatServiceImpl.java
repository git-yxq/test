package com.supergo.managerservice.service.impl;

import com.supergo.http.HttpResult;
import com.supergo.managerservice.service.ItemCatService;
import com.supergo.managerservice.service.base.impl.BaseServiceImpl;
import com.supergo.mapper.ItemcatMapper;
import com.supergo.pojo.Itemcat;
import com.supergo.user.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ItemCatServiceImpl extends BaseServiceImpl<Itemcat> implements ItemCatService {
    @Autowired
    private ItemcatMapper itemcatMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public HttpResult categorysList() {
        //判断缓存中是否有数据
        String json = redisTemplate.opsForValue().get("item-catagorys");
        if (StringUtils.isNotBlank(json)) {
            List<Itemcat> list = JsonUtils.jsonToList(json, Itemcat.class);
            return HttpResult.ok(list);
        }
        //缓存中没有查询数据库
        List<Itemcat> list = getCategroyList(0);
        //把数据添加到缓存
        redisTemplate.opsForValue().set("item-catagorys", JsonUtils.objectToJson(list));
        return HttpResult.ok(list);
    }

    private List<Itemcat> getCategroyList(long parentId) {
        Example example = new Example(Itemcat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        List<Itemcat> list = itemcatMapper.selectByExample(example);
        if (list==null || list.size() ==0) {
            return null;
        }
        list.forEach(itemcat -> itemcat.setChildren(getCategroyList(itemcat.getId())));
        return list;
    }
}
