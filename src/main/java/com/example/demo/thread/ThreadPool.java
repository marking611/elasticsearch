package com.example.demo.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by makai on 2018/3/29.
 */
public class ThreadPool {
    private static ThreadPool instance;
    private ExecutorService pool = Executors.newCachedThreadPool();

    public static synchronized ThreadPool getInstance() {
        if (instance == null) instance = new ThreadPool();
        return instance;
    }

    public  ExecutorService getPool() {
        return pool;
    }
}
