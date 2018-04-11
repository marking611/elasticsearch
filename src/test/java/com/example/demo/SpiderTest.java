package com.example.demo;

import com.example.demo.service.InfoQSpider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * Created by makai on 2018/3/20.
 */
public class SpiderTest extends BaseTest {
    @Autowired
    private InfoQSpider infoQSpider;

    @Test
    public void InfoQ() throws InterruptedException {
        int pageNum = 0;
        for (int i = 2; i >= 0; i--) {
            pageNum = i;
            infoQSpider.spider(12 * pageNum);
        }
        //junit测试，主线程结束之后，子线程也会结束（main方法不会），故让主线程sleep一会儿，保证子线程可以执行完毕
        TimeUnit.SECONDS.sleep(600l);
    }

}
