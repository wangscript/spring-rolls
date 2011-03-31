package org.cy.core.jdbc.dialect;

import java.sql.Connection;
/**
 * 功 能 描 述:<br>
 * MYSQL数据库方言实现
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-3-30下午03:17:41
 * <br>项 目 信 息:paramecium:org.cy.core.jdbc.dialect.MySqlDialect.java
 */
public final class MySqlDialect extends BaseDialect implements Dialect {

	public MySqlDialect(Connection connection) {
		super(connection);
	}

	public String getSql(final String sql,Page page){
		String querySql=sql;
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			querySql=querySql.concat(" LIMIT "+page.getFirst()+","+page.getPageSize());
		}
		return querySql;
	}
	
}
