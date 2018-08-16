package com.ztesoft.mdod.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池
 * @author lyg
 *
 */
public class ThreadPoolExecutorUtil {
	
	private ThreadPoolExecutor pool = null;    
    private int corePoolSize = 10;
    private int maximumPoolSize = 10;
    private int keepAliveTime = 30;
    private int workQueueNum = 10;
    
    public static ThreadPoolExecutorUtil getNewInstace(){
    	return new ThreadPoolExecutorUtil();
    }
    
    public static ThreadPoolExecutorUtil getNewInstace(int corePoolSize,int maximumPoolSize,int keepAliveTime,int workQueueNum){
    	return new ThreadPoolExecutorUtil(corePoolSize,maximumPoolSize,keepAliveTime,workQueueNum);
    }
    
    private ThreadPoolExecutorUtil(){
    	//初始化线程池
    	this.init();
    }
    
    private ThreadPoolExecutorUtil(int corePoolSize,int maximumPoolSize,int keepAliveTime,int workQueueNum){
    	this.corePoolSize = corePoolSize;
    	this.maximumPoolSize = maximumPoolSize;
    	this.keepAliveTime = keepAliveTime;
    	this.workQueueNum = workQueueNum;
    	//初始化线程池
    	this.init();
    }
    
    /**  
     * 线程池初始化方法  
     * corePoolSize 核心线程池大小  
     * maximumPoolSize 最大线程池大小
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间+单位TimeUnit  
     * TimeUnit keepAliveTime时间单位
     * workQueue 阻塞队列
     * threadFactory 新建线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,任务会交给RejectedExecutionHandler来处理  
     */    
    public void init() {    
        this.pool = new ThreadPoolExecutor(
        		this.corePoolSize,
        		this.maximumPoolSize,
        		this.keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1),
                new ThreadFactoryImpl(),    
                new RejectedExecutionHandlerImpl());
        //线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭
        this.pool.allowCoreThreadTimeOut(true);
    }    
    
        
    public void destory() {    
        if(this.pool != null) {    
        	this.pool.shutdownNow();    
        }    
    }    
        
        
    public ExecutorService getThreadPoolExecutor() {
        return this.pool; 
    }    
        
    private class ThreadFactoryImpl implements ThreadFactory {    
    
        private AtomicInteger count = new AtomicInteger(0);
        
        public Thread newThread(Runnable r) {    
            Thread t = new Thread(r);    
            String threadName = ThreadPoolExecutorUtil.class.getSimpleName() + count.addAndGet(1);  
            t.setName(threadName);    
            return t;    
        }    
    }    
        
        
    private class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
                //改造由blockingqueue的offer改成put阻塞方法  
                executor.getQueue().put(r);
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }
		}
    } 
}
