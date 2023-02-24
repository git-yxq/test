import com.supergo.search.SearchApplication;
import com.supergo.search.dao.UserRepository;
import com.supergo.search.entity.UserEntity;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

@SpringBootTest(classes = SearchApplication.class)
//使用ElasticsearchRestTemplate进行自定义查询
public class EsTest4 {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void queryStringQuery() {
        //创建查询构建器
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                // 配置查询条件
                // queryStringQuery查询 -> 在name字段查询"张三"关键词
                .withQuery(QueryBuilders.queryStringQuery("张三").defaultField("name"))
                //返回构建结果
                .build();
        //执行查询.并获得结果
        SearchHits<UserEntity> hits = template.search(query, UserEntity.class);
        //便利并打印结果
        hits.forEach(System.out::println);
    }

    @Test
    public void multiMatchQuery() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //使用multiMatchQuery(多字段匹配)
                .withQuery(QueryBuilders.multiMatchQuery("北京", "name", "address"))
                //指定排序条件
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC))
                //指定分页条件
                .withPageable(PageRequest.of(0,2))
                .build();
        //查询并返回
        SearchHits<UserEntity> list = template.search(searchQuery, UserEntity.class);
        //便利打印
        list.forEach(System.out::println);
    }

    @Test
    public void testAggregation() {
        //创建聚合查询条件
        //根据sex进行分组
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("sex_count").field("sex");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("北京", "name", "address"))
                //设置分页信息
                .withPageable(PageRequest.of(0, 2))
                //添加聚合条件
                .addAggregation(aggregationBuilder)
                //设置高亮显示
                .withHighlightFields(new HighlightBuilder.Field("address").preTags("<em>").postTags("</em>"))
                .build();

        //查询并获得结果
        SearchHits<UserEntity> searchHits = template.search(searchQuery, UserEntity.class);
        //打印查询结果
        searchHits.forEach(hit ->{
            System.out.println("高亮字段:"+hit.getHighlightField("address").get(0));
            System.out.println(hit);

        });
        //获得所有聚合结果
        Aggregations aggregations = searchHits.getAggregations();
        //从所有聚合结果中取得指定聚合结果
        ParsedStringTerms terms = aggregations.get("sex_count");
        //打印结果
        terms.getBuckets().forEach(e->{
            System.out.println(e.getKeyAsString()); //键
            System.out.println(e.getDocCount()); //值
        });
    }

    @Test
    public void testBoolQeuryAndFilter() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //.withQuery(QueryBuilders.queryStringQuery("张三").defaultField("name"))
                //.withFilter(QueryBuilders.termQuery("sex","男"))
                .withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.queryStringQuery("张三").defaultField("name"))
                        //添加filter条件
                        .filter(QueryBuilders.termQuery("sex", "女"))
                )
                .build();
        SearchHits<UserEntity> hits = template.search(searchQuery, UserEntity.class);

        hits.forEach(hit-> System.out.println(hit));

    }
}
