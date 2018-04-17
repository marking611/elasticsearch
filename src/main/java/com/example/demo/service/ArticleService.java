package com.example.demo.service;

import com.example.demo.dao.ArticleDao;
import com.example.demo.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by makai on 2018/3/21.
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleDao articleDao;

    public Page<Article> list(int pageNum, int pageSize, String keyword, String channel) {
        pageNum = pageNum >= 1 ? pageNum-1 : 0;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Article> page = articleDao.findByTitleContainingAndChannelContainingOrderByPublishTimeDesc(keyword, channel, pageable);
        return page;
    }
}
