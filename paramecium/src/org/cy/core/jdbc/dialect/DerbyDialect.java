package org.cy.core.jdbc.dialect;

import java.sql.Connection;

/**
 * 功 能 描 述:<br>
 * Derby数据库方言实现
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-3-30下午03:10:47
 * <br>项 目 信 息:paramecium:org.cy.core.jdbc.dialect.DerbyDialect.java
 */
public final class DerbyDialect extends BaseDialect implements Dialect {

	public DerbyDialect(Connection connection) {
		super(connection);
	}

	public String getSql(final String sql,Page page){
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			String queryLastSql=") temp_results) final_results WHERE row_number > "+page.getFirst()+" AND row_number <= "+page.getFirst()+page.getPageSize();
			int groupby=sql.toUpperCase().indexOf("GROUP BY");
			int orderby=sql.toUpperCase().indexOf("ORDER BY");
			if(orderby>groupby){
				groupby=sql.length();
			}
			String temp1 = "";
			if(orderby>0){
				temp1 = sql.substring(orderby, groupby);
			}
			String queryFristSql="SELECT * FROM (SELECT ROW_NUMBER() OVER ("+temp1+") row_number,temp_results.* FROM(";
			String lastSql=queryFristSql.concat(sql.concat(queryLastSql));
			return lastSql;
		}else{
			return sql;
		}
	}
	
	
}
