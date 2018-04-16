package com.example.demo.task;

import com.example.demo.service.InfoQSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by makai on 2018/3/26.
 */
@Component
@EnableScheduling
public class SpiderTask {
    @Autowired
    private InfoQSpider infoQSpider;

    @Scheduled(cron = "0 0 18 * * *")
    public void infoQSpider() {
        infoQSpider.spider();
    }
}
