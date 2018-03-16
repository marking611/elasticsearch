package com.example.demo.dao;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by makai on 2018/3/15.
 */
public interface BookDao extends JpaRepository<Book,Integer> {
}
