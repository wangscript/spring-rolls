package org.cy.core.jdbc.dialect;

import java.sql.Connection;
/**
 * 功 能 描 述:<br>
 * ORACLE数据库方言实现
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-3-30下午03:18:38
 * <br>项 目 信 息:paramecium:org.cy.core.jdbc.dialect.OracleDialect.java
 */
public final class OracleDialect extends BaseDialect implements Dialect {

	public OracleDialect(Connection connection) {
		super(connection);
	}

	public String getSql(final String sql,Page page){
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			String querySqlFirst="SELECT * FROM ( SELECT tempt.*,ROWNUM rn FROM ( ";
			String querySqlLast=" ) tempt WHERE ROWNUM<="+page.getFirst()+page.getPageSize()+" ) WHERE rn>"+page.getFirst();
			String lastSql=querySqlFirst.concat(sql.concat(querySqlLast));
			return lastSql;
		}else{
			return sql;
		}
	}
	
}
