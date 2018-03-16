package com.example.demo.task;

import com.example.demo.service.ESBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by makai on 2018/3/16.
 */
@Service
@EnableScheduling
public class ESBookTask {
    @Autowired
    private ESBookService esBookService;

    //每天12点定时执行
    @Scheduled(cron = "0 12 0 ? * *")
    public void esSave(){
        esBookService.save();
    }
}
