import com.supergo.search.SearchApplication;
import com.supergo.search.dao.UserRepository;
import com.supergo.search.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.List;

@SpringBootTest(classes = SearchApplication.class)
//@Query查询
public class EsTest3 {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void fun1(){

        List<UserEntity> list = userRepository.abc("朝阳");

        System.out.println(list);

    }

    @Test
    public void fun2(){

        Page<UserEntity> page = userRepository.abc("北京", PageRequest.of(1, 2,Sort.by("id")));

        System.out.println("总记录数:"+page.getTotalElements());
        System.out.println("总页数:"+page.getTotalPages());
        System.out.println("当前页数据"+page.getContent());
    }

}
