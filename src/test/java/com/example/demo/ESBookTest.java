package com.example.demo;

import com.example.demo.service.ESBookService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by makai on 2018/3/16.
 */
public class ESBookTest extends BaseTest {
    @Autowired
    private ESBookService esBookService;

    @Test
    public void 重建索引(){
        esBookService.resetIndex();
    }

    @Test
    public void 批量保存索引(){
        esBookService.save();
    }

    @Test
    public void 删除索引(){
        esBookService.deleteIndex();
    }
}
