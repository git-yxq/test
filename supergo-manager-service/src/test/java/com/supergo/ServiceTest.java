package com.supergo;

import com.supergo.managerservice.ManagerServiceApplication;
import com.supergo.managerservice.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ManagerServiceApplication.class)
public class ServiceTest {
    @Autowired
    private ItemService itemService;

    @Test
    public void fun1(){
        System.out.println(itemService.findAll());
    }

}
