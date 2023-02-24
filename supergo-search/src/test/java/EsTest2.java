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
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = SearchApplication.class)
//方法命名查询
public class EsTest2 {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void fun1(){

        List<UserEntity> list = userRepository.findByAddress("昌平");

        System.out.println(list);

    }

    @Test
    public void fun2(){

        List<UserEntity> list = userRepository.findByAddressNot("昌平");

        System.out.println(list);

    }

    @Test
    public void fun3(){

        SearchPage<UserEntity> page = userRepository.findByAddress("北京", PageRequest.of(0, 2,Sort.by("id").descending()));


        System.out.println("总记录数:"+page.getTotalElements());
        System.out.println("总页数:"+page.getTotalPages());
        System.out.println("当前页数据"+page.getContent());
    }
    @Test
    public void fun4(){

        List<UserEntity> list = userRepository.findByAddress("北京",Sort.by("id").descending());

        System.out.println(list);

    }
    @Test
    public void fun5(){

        List<SearchHit<UserEntity>> list = userRepository.findByAddressStartingWith("北京");
        System.out.println(list.size());
        list.forEach(hit -> {
            List<String> name = hit.getHighlightField("address");//获得指定属性的高亮字段
            System.out.println("高亮字段:"+name.get(0));
            System.out.println("实体对象:"+hit.getContent());
        });

    }

}
