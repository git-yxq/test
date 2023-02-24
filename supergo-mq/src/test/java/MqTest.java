import com.kaikeba.mq.MqApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MqApplication.class)
public class MqTest {
    @Autowired
    //注入消息模板对象,操作消息队列
    private RabbitTemplate rabbitTemplate;
    @Test
    //直接(direct)模式 -> 直接将消息发送给指定消息队列
    public void fun1(){
        //发送消息
        //参数1: 指定队列名称
        //参数2: 指定消息对象(String,Map)
        rabbitTemplate.convertAndSend("kkb","hello RabbitMQ!");
    }

    @Test
    //分列(fanout)模式 -> 将消息发送给分列模式交换器, 交换器会将消息发给所有绑定的队列
    public void fun2(){
        //发送消息
        //参数1: 指定exchange名称
        //参数2: 用不到,填写""即可
        //参数3: 消息内容
        rabbitTemplate.convertAndSend("fanoutdemo","","hello fanoutdemo!");
    }

    @Test
    //主题(topic)模式 -> 将消息发送给主题模式交换器, 交换器会根据路由键将消息发给所有符合规则的队列
    public void fun3(){
        //发送消息
        //参数1: 指定exchange名称
        //参数2: 指定路由键
        //参数3: 消息内容

        //三个队列都能收到消息
        //rabbitTemplate.convertAndSend("topicdemo","kkb.lyd.xiaobai","hello topicdemo!");
        //只让kkb与xiaobai接收到消息
//        rabbitTemplate.convertAndSend("topicdemo","kkb.xiaobai","hello topicdemo!");
        //只让lyd与xiaobai接收到消息
//        rabbitTemplate.convertAndSend("topicdemo","lyd.xiaobai","hello topicdemo!");
        //只让lyd与kkb接收到消息
        rabbitTemplate.convertAndSend("topicdemo","kkb.lyd","hello topicdemo!");
    }


    @Test
    public void revice(){
        //死循环保持消息监听器始终执行
        while(true);
    }

}
