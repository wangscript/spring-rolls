package com.wisdom.core.dao.db2;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.Assert;

import com.wisdom.core.dao.BaseJdbcTemplate;
import com.wisdom.core.utils.Page;
/**
 * 功能描述：
 * 基于db2的dao实现
 * <br>代码作者：<b>CaoYang</b>
 * <br>创建日期：<b>2009-10-29</b>
 * <br>创建时间：<b>下午04:08:42</b>
 * <br>文件结构：<b>spring:com.wisdom.core.dao.db2/Db2JdbcDao.java</b>
 */
@SuppressWarnings("unchecked")
public final class Db2JdbcDao extends BaseJdbcTemplate{

	public Db2JdbcDao(DataSource dataSource) {
		super(dataSource);
	}
	
	public Db2JdbcDao(DataSource dataSource,int mappingStrategy) {
		super(dataSource,mappingStrategy);
	}
	
	
	/**
	 * 未实现
	 * oracle用分页方法,page对象中返回结果为bean集合
	 * @param sql语句，不用写分页函数、排序，只需要写获取数据的内容，如：select * from tb_table where id=? and user_name like :?
	 * @param arrayParameters参数集合
	 * @param page分页对象
	 * @return
	 */
	public Page findPageListBean(final String sql,Class clazz,Page page,Object... arrayParameters){
		Assert.notNull(page,"分页信息不能为空");
		Assert.hasText(sql,"sql语句不正确!");
		long count=0;
		if (page.isAutoCount()) {
			count=findLongByArray(getCountSql(sql), arrayParameters);
			page.setTotalCount((int)count);
		}
		List list=null;
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			String queryLastSql=")AS rs1) AS rs2 WHERE rs2.rs1.rn BETWEEN ? AND ?";
			int groupby=sql.toUpperCase().indexOf("GROUP BY");
			int orderby=sql.toUpperCase().indexOf("ORDER BY");
			if(orderby>groupby){
				groupby=sql.length();
			}
			String temp1 = sql.substring(sql.toUpperCase().indexOf("ORDER BY"), groupby);
			String queryFristSql="SELECT * FROM (SELECT rs1.*,ROWNUMBER() OVER("+temp1+") AS rs1.rn FROM(";
			String lastSql=queryFristSql.concat(sql.concat(queryLastSql));
			if(arrayParameters!=null){
				Object[] obs=new Object[2];
				obs[0]=page.getFirst();
				obs[1]=page.getFirst()+page.getPageSize();
				Object[] newprmts=ArrayUtils.addAll(arrayParameters, obs);
				list= findListBeanByArray(lastSql, clazz, newprmts);
			}else{
				list= findListBeanByArray(lastSql, clazz, page.getFirst(),page.getFirst()+page.getPageSize());
			}
		}else{
			list= findListBeanByArray(sql, clazz, arrayParameters);
		}
		page.setResult(list);
		return page;
	}

	/**
	 * 未实现
	 * oracle用分页方法,page对象中返回结果为map集合
	 * @param sql语句，不用写分页函数、排序，只需要写获取数据的内容，如：select * from tb_table where id=? and user_name like :?
	 * @param arrayParameters参数集合
	 * @param page分页对象
	 * @return
	 */
	public Page findPageListMap(final String sql,Page page,Object... arrayParameters){
		Assert.notNull(page,"分页信息不能为空");
		Assert.hasText(sql,"sql语句不正确!");
		long count=0;
		if (page.isAutoCount()) {
			count=findLongByArray(getCountSql(sql), arrayParameters);
			page.setTotalCount((int)count);
		}
		List list=null;
		if (page.isFirstSetted()&&page.isPageSizeSetted()) {
			String queryLastSql=")AS rs1) AS rs2 WHERE rs2.rs1.rn BETWEEN ? AND ?";
			int groupby=sql.toUpperCase().indexOf("GROUP BY");
			int orderby=sql.toUpperCase().indexOf("ORDER BY");
			if(orderby>groupby){
				groupby=sql.length();
			}
			String temp1 = sql.substring(sql.toUpperCase().indexOf("ORDER BY"), groupby);
			String queryFristSql="SELECT * FROM (SELECT rs1.*,ROWNUMBER() OVER("+temp1+") AS rs1.rn FROM(";
			String lastSql=queryFristSql.concat(sql.concat(queryLastSql));
			if(arrayParameters!=null){
				Object[] obs=new Object[2];
				obs[0]=page.getFirst();
				obs[1]=page.getFirst()+page.getPageSize();
				Object[] newprmts=ArrayUtils.addAll(arrayParameters, obs);
				list= findListMapByArray(lastSql, newprmts);
			}else{
				list= findListMapByArray(lastSql,page.getFirst(),page.getFirst()+page.getPageSize());
			}
		}else{
			list= findListMapByArray(sql,arrayParameters);
		}
		page.setResult(list);
		return page;
	}
	
	private static String getCountSql(String sql){
		Assert.hasText(sql,"sql语句不正确!");
		sql=sql.toUpperCase();
		String[] froms=sql.split(" FROM ");
		String tempSql="";
		for(int i=0;i<froms.length;i++){
			if(i!=froms.length-1){
				tempSql=tempSql.concat(froms[i]+" FROM ");
			}else{
				tempSql=tempSql.concat(froms[i]);
			}
			int left=tempSql.split("\\(").length;
			int right=tempSql.split("\\)").length;
			if(left==right){
				break;
			}
		}
		tempSql=" FROM "+sql.substring(tempSql.length(),sql.length());
		String withOutOrderBy=tempSql;
		if(tempSql.indexOf(" ORDER BY ")>=0){
			withOutOrderBy=tempSql.substring(0,tempSql.indexOf(" ORDER BY"));
		}
		return "SELECT COUNT(*) ".concat(withOutOrderBy);
	}
	
}
