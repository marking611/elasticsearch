package com.example.demo;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by makai on 2018/3/15.
 */
public class BookTest extends BaseTest{

    @Autowired
    private BookDao bookDao;

    @Test
    public void save(){
        Book book = new Book();
        book.setName("Java并发编程");
        book.setDescription("时间久了,总是要学点多线程的东西的");
        book.setPrice(50);
        bookDao.saveAndFlush(book);
    }
}
