package com.wisdom.core.security;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
/**
 * 功 能 描 述:<br>
 * 在线用户缓存
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2010-11-1下午02:56:41
 * <br>项 目 信 息:wisdom.3.0:com.wisdom.core.security.OnlineUserCache.java
 */
public class OnlineUserCache {
	
	private static ConcurrentMap<String, UserDetails> onlineUsers = new ConcurrentHashMap<String, UserDetails>();
	private static Logger logger = LoggerFactory.getLogger(OnlineUserCache.class);
	
	public static void put(UserDetails user){
		if(user!=null&&user.getUsername()!=null){
			onlineUsers.put(user.getUsername(), user);
			logger.info("{}已经登录!",user.getUsername());
		}
	}
	
	public static Collection<UserDetails> getOnlineUsers(){
		return onlineUsers.values();
	}
	
	public static void remove(UserDetails user){
		if(user!=null&&user.getUsername()!=null){
			onlineUsers.remove(user.getUsername());
			logger.info("{}退出登录!",user.getUsername());
		}
	}

	public static void remove(String username){
		if(username!=null){
			onlineUsers.remove(username);
			logger.info("{}退出登录!",username);
		}
	}
	
}
