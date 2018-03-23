package com.example.demo;

import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * Created by makai on 2018/3/21.
 */
public class ArticleTest extends BaseTest {
    @Autowired
    private ArticleService articleService;

    @Test
    public void listTest(){
        Page<Article> page = articleService.list(4,10,"java","");
        System.out.println(JSONObject.fromObject(page).toString());
    }
}
