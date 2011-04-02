package org.cy.core.log.handler;

import org.cy.core.log.LogHandler;
/**
 * 功 能 描 述:<br>
 * 日志打印在控制台
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-4-2下午02:48:31
 * <br>项 目 信 息:paramecium:org.cy.core.log.handler.ConsoleLogHandler.java
 */
public class ConsoleLogHandler implements LogHandler{

	public void log(String message) {
		System.out.println(message);
	}

}
