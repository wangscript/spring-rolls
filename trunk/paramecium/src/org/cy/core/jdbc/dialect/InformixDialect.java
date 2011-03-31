package org.cy.core.jdbc.dialect;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * 功 能 描 述:<br>
 * Informix数据库方言实现
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-3-30下午03:16:27
 * <br>项 目 信 息:paramecium:org.cy.core.jdbc.dialect.InformixDialect.java
 */
public final class InformixDialect extends BaseDialect implements Dialect {

	public InformixDialect(Connection connection) {
		super(connection);
	}

	public String getSql(final String sql,Page page){
		String querySql=sql;
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			querySql="SELECT SKIP "+page.getFirst()+" FIRST "+page.getFirst()+page.getPageSize()+" "+querySql.substring(7, querySql.length());
		}
		return querySql;
	}
	
	public Long getAutoGeneratedValue() throws SQLException {
		throw new SQLException("该数据库没有相关方法!请手动调用getAutoGeneratedValue(String sql)方法.推荐语句：SELECT dbinfo('sqlca.sqlerrd1') FROM <TABLE>");
	}

	public Long getAutoGeneratedValue(final String sql) throws SQLException {
		if(sql==null){
			return getAutoGeneratedValue();
		}
		return (Long) queryUniqueColumnValue(sql);
	}
	
	
}
