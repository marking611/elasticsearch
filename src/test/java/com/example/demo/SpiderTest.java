package com.example.demo;

import com.example.demo.service.InfoQSpider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by makai on 2018/3/20.
 */
public class SpiderTest extends BaseTest {
    @Autowired
    private InfoQSpider infoQSpider;

    @Test
    public void InfoQ() {
        int pageNum = 0;
        for (int i = 20; i >= 0; i--) {
            pageNum = i;
            infoQSpider.spider(12 * pageNum);
        }

    }
}
