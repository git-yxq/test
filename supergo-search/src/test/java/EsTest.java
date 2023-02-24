import com.netflix.discovery.converters.Auto;
import com.supergo.search.SearchApplication;
import com.supergo.search.dao.UserRepository;
import com.supergo.search.entity.GoodsEntity;
import com.supergo.search.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

import java.util.Optional;

@SpringBootTest(classes = SearchApplication.class)
//原生方法测试
public class EsTest {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void fun1(){
        IndexOperations ops = template.indexOps(GoodsEntity.class);
        ops.create();//创建索引
       // ops.createMapping(UserEntity.class);//创建映射
    }

    @Test
    public void addDocument() {
        //创建对象
        UserEntity userEntity = new UserEntity();
        userEntity.setId(4);
        userEntity.setName("赵六");
        userEntity.setAddress("北京延庆");
        userEntity.setSex("男");
        //保存对象 -> 保存到ES中
        userRepository.save(userEntity);
    }

    @Test

    //查询
    public void findDoc() {
        //springData的单体查询,结果会封装到Optional对象
        Optional<UserEntity> result = userRepository.findById( 1l);
        //取出封装的查询结果
        UserEntity userEntity = result.get();

        System.out.println(userEntity);
    }

    @Test
    public void updateDocument() {
        Optional<UserEntity> result = userRepository.findById( 1L);
        UserEntity userEntity = result.get();
        userEntity.setName("李四");
        //记录存在就是修改, 不存在就是添加
        userRepository.save(userEntity);
    }

    @Test
    public void deleteDocument() {
        userRepository.deleteById(1L);
    }



    @Test
    //排序查询
    public void findSort() {
        System.out.println(userRepository.findAll(Sort.by("id").ascending()));
        System.out.println(userRepository.findAll(Sort.by("id").descending()));
    }
    @Test
    //分页查询
    public void findPage() {
        //page:当前页数
        //size:每页显示条数
        //参数3: 排序规则对象
        Page<UserEntity> page = userRepository.findAll(PageRequest.of(1, 2, Sort.by("id").ascending()));

        System.out.println("总记录数:"+page.getTotalElements());
        System.out.println("总页数:"+page.getTotalPages());
        System.out.println("当前页数据"+page.getContent());
    }
}
