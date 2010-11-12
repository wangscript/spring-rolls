package com.wisdom.core.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wisdom.core.security.OnlineUserCache;
import com.wisdom.core.security.domain.Resource;
import com.wisdom.core.security.domain.User;

/**
 * 实现SpringSecurity的UserDetailsService接口,获取用户Detail信息.
 * 
 * @author wisdom
 */
public class UserDetailServiceImpl implements UserDetailsService {

	private UserService userService;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		User user = userService.getUserByLoginName(userName);
		if (user == null){
			throw new UsernameNotFoundException(userName + " 用户名不存在，登录失败！");
		}
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		GrantedAuthority ag=null;
		Collection<Resource> ress=userService.getResourcesByUserName(userName);
		for(Resource res:ress){
			ag=new GrantedAuthorityImpl(res.getName());
			authorities.add(ag);
		}
		//UserDetails userDetails = new UserDetailsImpl(user, authorities);
		//用以上方法会使Session并发控制失效，但可以不操作数据库的情况下获得非常全面的用户信息。
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user.isEnabled(),user.isAccountExpired(),user.isCredentialsExpired(),user.isAccountLocked(),authorities);
		OnlineUserCache.put(user);
		return userDetails;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
