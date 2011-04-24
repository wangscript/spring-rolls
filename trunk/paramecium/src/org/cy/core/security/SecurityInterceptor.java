package org.cy.core.security;

import java.lang.reflect.Method;

import org.cy.core.aop.cglib.MethodProxy;
/**
 * 功能描述(Description):<br><b>
 * 安全拦截器实现
 * </b><br>作 者(Author): <i><b>曹阳(Cao.Yang)</b></i>
 * <br>建立日期(Create Date): <b>2011-4-23下午08:52:57</b>
 * <br>项目名称(Project Name): <b>paramecium</b>
 * <br>包及类名(Package Class): <b>org.cy.core.security.SecurityInterceptor.java</b>
 */
public class SecurityInterceptor extends BaseSecurityInterceptor {
	
	public Object nextIntercept(Object service, Method method, Object[] parameters, MethodProxy proxy) throws Throwable{
		return proxy.invokeSuper(service, parameters);
	}
	
}
