package org.cy.core.jdbc.dialect;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * 功 能 描 述:<br>
 * SQLITE数据库方言实现
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-3-30下午03:22:32
 * <br>项 目 信 息:paramecium:org.cy.core.jdbc.dialect.SqliteDialect.java
 */
public final class SqliteDialect extends BaseDialect implements Dialect {

	public String getSql(final String sql,Page page){
		String querySql=sql;
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			querySql=querySql.concat(" LIMIT "+page.getPageSize()+" OFFSET "+page.getFirst());
		}
		return querySql;
	}
	
	public Number getAutoGeneratedKey(final Connection connection) throws SQLException {
		return (Number) queryUniqueColumnValue(connection,"select LAST_INSERT_ROWID()");
	}

	public Number getAutoGeneratedKey(final Connection connection,final String sql) throws SQLException {
		if(sql==null){
			return getAutoGeneratedKey(connection);
		}
		return (Number) queryUniqueColumnValue(connection,sql);
	}
	
}
