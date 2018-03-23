package com.example.demo.dao;

import com.example.demo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by makai on 2018/3/20.
 */
public interface ArticleDao extends JpaRepository<Article,Integer> {
    int countByTitleAndUrl(String title,String url);

    Page<Article> findByTitleContainingAndChannelContainingOrderByCreateTimeDesc(String title,String channel, Pageable pageable);
}
