package org.freeman.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import myUtils.MyJson;

import java.util.concurrent.*;


//单例模式
public class ThreadPool {

    //获取线程池信息
    static String data = MyJson.get("ThreadPool.json");
    static JSONObject jsonObject = JSON.parseObject(data);
    static JSONObject threadPool = jsonObject.getJSONObject("ThreadPool");


    //构造唯一实例
    private static volatile ThreadPoolExecutor instance;

    //双重检查锁定
    public static ThreadPoolExecutor getThreadPool(){
        // 第一次检查，如果实例不存在，则进入同步代码块
        if(instance == null){
            // 同步代码块，确保同一时间只有一个线程可以进入
            synchronized (ThreadPool.class){
                // 第二次检查，确保在等待锁的时候没有其他线程创建实例
                if(instance == null){
                    // 初始化核心参数
                    ThreadPool.corePoolSize = (int) threadPool.get("corePoolSize");
                    ThreadPool.maxPoolSize = (int) threadPool.get("maxPoolSize");
                    ThreadPool.keepAliveTime = (int) threadPool.get("keepAliveTime");
                    ThreadPool.keepAliveTimeUnit = TimeUnit.valueOf(threadPool.getString("keepAliveTimeUnit"));

                    // 初始化线程池相关配置
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler rejectedHandler = new ThreadPoolExecutor.DiscardPolicy();

                    instance = new ThreadPoolExecutor(
                            corePoolSize,maxPoolSize,keepAliveTime,keepAliveTimeUnit,
                            workQueue,threadFactory,rejectedHandler);
                }
            }
        }
        return instance;
    }

    private static int corePoolSize;//核心线程数
    private static int maxPoolSize;//最大线程数
    private static long keepAliveTime;//线程最长闲置时间
    private static TimeUnit keepAliveTimeUnit;//指定 keepAliveTime 参数的时间单位
    private static BlockingQueue<Runnable> workQueue;//任务队列。通过线程池的 execute() 方法提交的 Runnable 对象将存储在该参数中。
    private static ThreadFactory threadFactory;//线程工厂
    private static RejectedExecutionHandler rejectedHandler;//阻塞队列，当队列阻塞时的方法
}
