package org.cy.core.jdbc.dialect;

import java.sql.SQLException;
import java.util.Map;
/**
 * 功 能 描 述:<br>
 * 数据库方言接口
 * <br>代 码 作 者:曹阳(CaoYang)
 * <br>开 发 日 期:2011-3-30下午02:13:10
 * <br>项 目 信 息:paramecium:org.cy.core.jdbc.dialect.Dialect.java
 */
public interface Dialect {
	
	/**
	 * 获得刚刚产生的自增主键,用于insert产生自增值的获取
	 * @return
	 * @throws SQLException
	 */
	public Number getAutoGeneratedKey() throws SQLException ;
	
	/**
	 * 通过sql获得刚刚产生的自增主键,用于insert产生自增值的获取，对oracle等不支持自增列的数据库提供支持,无用可空实现
	 * @param sql语句
	 * @return
	 * @throws SQLException
	 */
	public Number getAutoGeneratedKey(final String sql) throws SQLException ;
	
	/**
	 * 通用用分页方法,page对象中返回结果为bean集合
	 * @param sql语句，不用写分页函数、排序，只需要写获取数据的内容，如：select * from tb_table where id=? and user_name like ?
	 * @param clazz返回对象类型
	 * @param arrayParameters参数集合
	 * @param page分页对象
	 * @return
	 */
	public Page queryPageBeanByArray(final String sql,Class<?> clazz,Page page,Object... arrayParameters) throws SQLException ;

	/**
	 * 通用用分页方法,page对象中返回结果为bean集合
	 * @param sql语句，不用写分页函数、排序，只需要写获取数据的内容，如：select * from tb_table where id=:userId and user_name =:userName
	 * @param clazz返回对象类型
	 * @param page分页对象
	 * @param beanParameters参数bean
	 * @return
	 */
	public Page queryPageBeansByBean(final String sql,Class<?> clazz,Page page,Object beanParameters) throws SQLException ;
	
	/**
	 * 通用用分页方法,page对象中返回结果为bean集合
	 * @param sql语句，不用写分页函数、排序，只需要写获取数据的内容，如：select * from tb_table where id=:user_id and user_name =:user_name
	 * @param clazz返回对象类型
	 * @param page分页对象
	 * @param mapParameters参数map
	 * @return
	 */
	public Page queryPageBeansByMap(final String sql,Class<?> clazz,Page page,Map<String,Object> mapParameters) throws SQLException ;

	/**
	 * 通用用分页方法,page对象中返回结果为map集合
	 * @param sql语句，不用写分页函数、排序，只需要写获取数据的内容，如：select * from tb_table where id=? and user_name like ?
	 * @param arrayParameters参数集合
	 * @param page分页对象
	 * @return
	 */
	public Page queryPageMapsByArray(final String sql,Page page,Object... arrayParameters) throws SQLException ;
	
	/**
	 * 通用用分页方法,page对象中返回结果为map集合
	 * @param sql语句，不用写分页函数、排序，只需要写获取数据的内容，如：select * from tb_table where id=:user_id and user_name like :user_name
	 * @param page分页对象
	 * @param mapParameters参数map
	 * @return
	 */
	public Page queryPageMapsByMap(final String sql,Page page,Map<String,Object> mapParameters) throws SQLException ;
}
