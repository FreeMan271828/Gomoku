package org.freeman;

import org.freeman.control.GomokuApp;
import org.freeman.service.ThreadPool;

import java.util.concurrent.ExecutorService;


public class Main {

    public static void main(String[] args) {
        ExecutorService executor =  ThreadPool.getThreadPool();
        executor.execute(new GomokuApp());
    }
}