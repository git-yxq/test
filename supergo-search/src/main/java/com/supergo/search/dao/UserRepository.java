package com.supergo.search.dao;

import com.supergo.pojo.User;
import com.supergo.search.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.awt.print.Book;
import java.util.List;


public interface UserRepository extends ElasticsearchRepository<UserEntity, Long> {


    //根据address属性查询
    List<UserEntity> findByAddress(String address);

    List<UserEntity> findByAddressNot(String address);

    //分页查询
    SearchPage<UserEntity> findByAddress(String address,Pageable pageable);

    //排序
    List<UserEntity> findByAddress(String address,Sort sort);

    //高亮
    @Highlight(fields = {
            @HighlightField(name = "address"), //指定高亮字段
    },
            parameters = @HighlightParameters(preTags = "<font color='red' >",postTags = "</font>"))//修改高亮标签
    List<SearchHit<UserEntity>> findByAddressStartingWith(String address);


    //@Query查询
    @Query(" {\n" +
            "    \"multi_match\" : {\n" +
            "      \"query\":    \"?0\", \n" +
            "      \"fields\":  \"address\"  \n" +
            "    }\n" +
            "  }")
    List<UserEntity> abc(String address);


    @Query(" {\n" +
            "    \"multi_match\" : {\n" +
            "      \"query\":    \"?0\", \n" +
            "      \"fields\":  \"address\"  \n" +
            "    }\n" +
            "  }")
    Page<UserEntity> abc(String address, Pageable pageable);
}
