package com.supergo.search.repository;

import com.supergo.search.entity.GoodsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsRepository extends ElasticsearchRepository<GoodsEntity, Long> {
}
