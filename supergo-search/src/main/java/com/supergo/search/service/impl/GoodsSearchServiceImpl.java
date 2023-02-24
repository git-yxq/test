package com.supergo.search.service.impl;

import com.supergo.http.HttpResult;
import com.supergo.search.entity.GoodsEntity;
import com.supergo.search.entity.SearchResult;
import com.supergo.search.mapper.GoodsMapper;
import com.supergo.search.repository.GoodsRepository;
import com.supergo.search.service.GoodsSearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsSearchServiceImpl implements GoodsSearchService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    //搜索商品业务方法
    /**
     * keyword: 搜索关键字
     * filters: 过滤搜索结果
     * page: 当前页数
     * size: 每页条数
     */
    public SearchResult search(String keyword, Map<String, String> filters, int page, int size) {
        //创建聚合查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                //设置分页信息
                .withPageable(PageRequest.of(page, size))
                //添加聚合条件
                .addAggregation(AggregationBuilders.terms("category_aggs").field("cname3"))
                .addAggregation(AggregationBuilders.terms("brand_aggs").field("brand_name"))
                //设置高亮显示
                .withHighlightFields(new HighlightBuilder.Field("goods_name").preTags("<em>").postTags("</em>"));
        //查询条件
        System.out.println("queryStringQuery:"+keyword);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.multiMatchQuery(keyword, "goods_name"));
        //判断是否有过滤条件
        if (filters != null && !filters.isEmpty()) {
            filters.keySet().forEach(key->{
                boolQuery.filter(QueryBuilders.termQuery(key, filters.get(key)));
            });
        }
        //设置查询条件
        queryBuilder.withQuery(boolQuery);
        //创建查询对象
        NativeSearchQuery searchQuery = queryBuilder.build();

        //执行查询
        SearchHits<GoodsEntity> searchHits = elasticsearchTemplate.search(searchQuery, GoodsEntity.class);

        //后的查询到的商品列表
        List<SearchHit<GoodsEntity>> searchHitList = searchHits.getSearchHits();

        List<GoodsEntity> list = new ArrayList<>();
        //便利取出高亮属性并封装
        for (int i = 0;i<searchHitList.size();i++){
            SearchHit<GoodsEntity> goodsEntitySearchHit = searchHitList.get(i);
            GoodsEntity goods = goodsEntitySearchHit.getContent();
            //取出高亮字段并放回实体对应属性中
            goods.setGoods_name(goodsEntitySearchHit.getHighlightField("goods_name").get(0));
            list.add(goods);
        }

        //取查询结果
        SearchResult searchResult = new SearchResult();
        //封装查询结果
        searchResult.setGoodsList(list);

        Aggregations aggregations = searchHits.getAggregations();

        //取分类聚合结果
        Terms termsCat = (Terms) aggregations.get("category_aggs");
        List<String> catAggsList = termsCat.getBuckets().stream().map(e -> e.getKeyAsString()).collect(Collectors.toList());
        Map catAggsMap = new HashMap();
        catAggsMap.put("name","分类");
        catAggsMap.put("field", "cname3");
        catAggsMap.put("content", catAggsList);

        //取品牌聚合结果
        Terms termsBrand = (Terms) aggregations.get("brand_aggs");
        List<String> brandAggsList = termsBrand.getBuckets().stream().map(e -> e.getKeyAsString()).collect(Collectors.toList());
        Map brandAggsMap = new HashMap();
        brandAggsMap.put("name","品牌");
        brandAggsMap.put("field", "brand_name");
        brandAggsMap.put("content", brandAggsList);
        List aggsList = new ArrayList();
        aggsList.add(brandAggsMap);
        aggsList.add(catAggsMap);

//        封装分组结果
       searchResult.setAggs(aggsList);

        return searchResult;
    }
    @Autowired
    private ElasticsearchRestTemplate template;

    @Override
    //将数据库中的商品数据同步到ES中(现实开发不可取)
    public HttpResult importAllGoods() {
        //从DB查询商品列表
        List<GoodsEntity> goodsList = goodsMapper.getGoodsList();
        //将查询出的数据存入ES
        goodsList.forEach(goods->goodsRepository.save(goods));

        return HttpResult.ok();
    }


}
