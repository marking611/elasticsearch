package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by makai on 2018/3/21.
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 查询列表
     *
     * @param pageNum  页码默认从0开始
     * @param pageSize
     * @param keyword
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(required = false, defaultValue = "0") int pageNum,
                       @RequestParam(required = false, defaultValue = "10") int pageSize,
                       String keyword, String channel) {
        Page<Article> page = articleService.list(pageNum, pageSize, keyword, channel);
        return JSONObject.fromObject(page).toString();
    }
}