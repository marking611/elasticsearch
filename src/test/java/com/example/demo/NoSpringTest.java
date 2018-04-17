package com.example.demo;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by makai on 2018/4/17.
 */
public class NoSpringTest {
    @Test
    public void test01() throws ParseException {
        String str = "fskjfkldjsfs发布于2017年3月10日fjskdljf";
        str = str.substring(str.indexOf("发布于"),str.lastIndexOf("日")+1);
        str = str.replace("发布于","");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        System.out.println(simpleDateFormat.parse(str));
    }
}
