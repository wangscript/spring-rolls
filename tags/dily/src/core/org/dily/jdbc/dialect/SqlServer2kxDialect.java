package org.dily.jdbc.dialect;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * 功 能 描 述:<br>
 * sqlserver2005、2008级以上版本数据库方言实现
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-3-30下午03:25:28
 * <br>项 目 信 息:dily:org.dily.jdbc.dialect.SqlServer200kxDialect.java
 */
public final class SqlServer2kxDialect extends BaseDialect implements Dialect {

	/**
	 * 如果复杂语句中滥用orderby的话，会有问题，请根据业务优化好sql语句，避免无谓的orderby
	 */
	public String getSql(final String sql,Page page){
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			String queryLastSql=") temp_results) final_results WHERE row_number > "+page.getFirst();
			int groupby=sql.toUpperCase().indexOf("GROUP BY");
			int orderby=sql.toUpperCase().indexOf("ORDER BY");
			if(orderby>groupby){
				groupby=sql.length();
			}
			String temp1 = "";
			if(orderby>0){
				temp1 = sql.substring(orderby, groupby);
			}
			String queryFristSql="SELECT TOP "+page.getPageSize()+" * FROM (SELECT ROW_NUMBER() OVER ("+temp1+") row_number,temp_results.* FROM(";
			String filterOrderBy=sql.replaceAll(temp1, "");//过滤orderby
			String lastSql=queryFristSql.concat(filterOrderBy.concat(queryLastSql));
			return lastSql;
		}else{
			return sql;
		}
	}
	
	public Number getAutoGeneratedKey(final Connection connection) throws SQLException {
		return (Number) queryUniqueColumnValue(connection,"SELECT SCOPE_IDENTITY()");
	}

	public Number getAutoGeneratedKey(final Connection connection,final String sql) throws SQLException {
		if(sql==null){
			return getAutoGeneratedKey(connection);
		}
		return (Number) queryUniqueColumnValue(connection,sql);
	}
	
}
