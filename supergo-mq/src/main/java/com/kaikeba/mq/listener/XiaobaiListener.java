package com.kaikeba.mq.listener;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//交给spring容器管理
@Component
//指定该类是消息监听(消费)类,参数指定订阅哪个消息队列
@RabbitListener(queues = "xiaobai")
public class XiaobaiListener {

    //由该方法接收消息
    @RabbitHandler
    public void showMessage(String message){
        System.err.println("xiaobai消息队列发来消息:"+message);
        //提取手机号
        //提取短信内容
        //发送消息
    }

}
