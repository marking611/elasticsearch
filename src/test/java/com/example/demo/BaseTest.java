package com.example.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by makai on 2018/3/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class BaseTest {
    @Before
    public void before(){
        System.out.println("-----------------开始测试---------------------");
    }

    @After
    public void after(){
        System.out.println("-----------------结束测试---------------------");
        System.exit(0);
    }
}
