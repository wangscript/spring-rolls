package org.paramecium.mvc;

import javax.servlet.jsp.PageContext;

/**
 * 功 能 描 述:<br>
 * Servlet常量
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-11-16下午03:04:55
 * <br>项 目 信 息:paramecium:org.paramecium.mvc.ServletConstant.java
 */
public class ServletConstant {

	public static final String ERROR_MESSAGE = "paramecium_error_message";
	public static final String $ERROR_MESSAGE = "?paramecium_error_message=";
	public static final String ERROR_MESSAGE$ = "&paramecium_error_message=";
	public static final String SUCCESS_MESSAGE = "paramecium_success_message";
	public static final String $SUCCESS_MESSAGE = "?paramecium_success_message=";
	public static final String SUCCESS_MESSAGE$ = "&paramecium_success_message=";
	/**
	 * 获得JSP的Exception所对应的键值
	 */
	public static final String PAGE_CONTEXT_EXCEPTION = PageContext.EXCEPTION;
	
}
