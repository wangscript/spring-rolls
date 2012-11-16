package org.paramecium.commons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.paramecium.log.Log;
import org.paramecium.log.LoggerFactory;

public abstract class ThreadUtils {
	private final static Log logger = LoggerFactory.getLogger();
	private static int count = 0;
	private final static ExecutorService execute = Executors.newFixedThreadPool(8);

	public static void add(Runnable runnable,String name) {
		if(count<8){
			execute.execute(runnable);
			logger.info("["+name+"]线程被放入线程池!");
			count++;
			return;
		}
		logger.warn("ExecutorService线程池超过峰值8，不能再放入线程!");
	}
	
	public static void add(Runnable runnable) {
		add(runnable,runnable.getClass().getName());
	}
	
	public static void shutdonw() {
		if(execute.isTerminated()){
			logger.info("所有线程已经执行完毕!");
		}else{
			logger.warn("线程池中含有未执行完毕的线程!");
		}
		if(!execute.isShutdown()){
			execute.shutdownNow();
			logger.info("线程池被强制停止!");
		}
	}


}
