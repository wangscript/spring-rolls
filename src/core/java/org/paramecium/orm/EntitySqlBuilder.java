package org.paramecium.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.paramecium.cache.Cache;
import org.paramecium.cache.CacheManager;
import org.paramecium.commons.BeanUtils;
import org.paramecium.orm.annotation.Column;
import org.paramecium.orm.annotation.Entity;
import org.paramecium.orm.annotation.NotUpdate;
import org.paramecium.orm.annotation.PrimaryKey;
import org.paramecium.orm.annotation.ReferenceColumn;
import org.paramecium.orm.annotation.VirtualColumn;
import org.paramecium.orm.annotation.PrimaryKey.AUTO_GENERATE_MODE;
/**
 * 功能描述(Description):<br><b>
 * 构建实体所对应的Sql语句
 * </b><br>作 者(Author): <i><b>曹阳(Cao.Yang)</b></i>
 * <br>建立日期(Create Date): <b>2011-10-13下午10:19:11</b>
 * <br>项目名称(Project Name): <b>paramecium</b>
 * <br>包及类名(Package Class): <b>org.paramecium.orm.EntitySqlBuilder.java</b>
 */
public class EntitySqlBuilder {
	
	@SuppressWarnings("unchecked")
	private final static Cache<String,String> sqlCache = CacheManager.getDefaultCache("CACHE_SQL_BUILDER");
	
	public static String getInsertSql(Object bean){
		String key = bean.getClass().getName()+":insert";
		String sql = sqlCache.get(key);
		if(sql!=null){
			return sql;
		}
		Entity entity = bean.getClass().getAnnotation(Entity.class);
		StringBuffer sb = new StringBuffer();
		if(entity!=null){
			String tableName = entity.tableName();
			Collection<String> columns = new ArrayList<String>();
			Collection<String> propertys = new ArrayList<String>();
			Class<?> clazz = bean.getClass();
			String mark = ":";
			boolean isAuto = true;
			for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fields = superClass.getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
					try {
						Column column = field.getAnnotation(Column.class);
						PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
						if(primaryKey!=null&&primaryKey.autoGenerateMode()==AUTO_GENERATE_MODE.MANUAL){
							if(column!=null&&!column.value().isEmpty()){
								columns.add(column.value());
								propertys.add(mark.concat(field.getName()));
							}else{
								columns.add(BeanUtils.getDbFieldName(field.getName()));
								propertys.add(mark.concat(field.getName()));
							}
							isAuto = false;
						}else if(primaryKey!=null&&primaryKey.autoGenerateMode()==AUTO_GENERATE_MODE.NATIVE_SEQUENCE){
							if(column!=null&&!column.value().isEmpty()){
								columns.add(column.value());
								propertys.add(primaryKey.sequenceName());
							}else{
								columns.add(BeanUtils.getDbFieldName(field.getName()));
								propertys.add(primaryKey.sequenceName());
							}
							isAuto = false;
						}else if(primaryKey==null){
							if(column!=null&&!column.value().isEmpty()){
								columns.add(column.value());
								propertys.add(mark.concat(field.getName()));
							}else if(column!=null&&column.value().isEmpty()){
								columns.add(BeanUtils.getDbFieldName(field.getName()));
								propertys.add(mark.concat(field.getName()));
							}
						}
					} catch (Exception e) {
					}
				}
			}
			if(isAuto){//判断是否为自增
				sb.append("A");
			}else{
				sb.append("M");
			}
			sb.append("INSERT INTO ").append(tableName).append("(");
			for(String column : columns){
				sb.append(column).append(",");
			}
			sb.delete(sb.length()-1, sb.length()).append(")");
			sb.append(" VALUES(");
			for(String propertyc : propertys){
				sb.append(propertyc).append(",");
			}
			sb.delete(sb.length()-1, sb.length()).append(")");
			sqlCache.put(key, sb.toString());
		}
		return sb.toString();
	}
	
	public static String getUpdateSql(Object bean){
		String key = bean.getClass().getName()+":update";
		String sql = sqlCache.get(key);
		if(sql!=null){
			return sql;
		}
		Entity entity = bean.getClass().getAnnotation(Entity.class);
		StringBuffer sb = new StringBuffer();
		if(entity!=null){
			String tableName = entity.tableName();
			Collection<String> sets = new ArrayList<String>();
			Collection<String> wheres = new ArrayList<String>();
			Class<?> clazz = bean.getClass();
			for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fields = superClass.getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
					try {
						NotUpdate notUpdate = field.getAnnotation(NotUpdate.class);
						if(notUpdate!=null){
							continue;
						}
						Column column = field.getAnnotation(Column.class);
						PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
						if(primaryKey!=null){
							if(column!=null&&!column.value().isEmpty()){
								wheres.add(column.value()+"=:"+field.getName()+" AND ");
							}else{
								wheres.add(BeanUtils.getDbFieldName(field.getName())+"=:"+field.getName()+" AND ");
							}
						}else if(primaryKey==null){
							if(column!=null&&!column.value().isEmpty()){
								sets.add(column.value()+"=:"+field.getName()+",");
							}else if(column!=null&&column.value().isEmpty()){
								sets.add(BeanUtils.getDbFieldName(field.getName())+"=:"+field.getName()+",");
							}
						}
					} catch (Exception e) {
					}
				}
			}
			sb.append("UPDATE ").append(tableName).append(" SET ");
			for(String set : sets){
				sb.append(set);
			}
			sb.delete(sb.length()-1, sb.length());
			if(!wheres.isEmpty()){
				sb.append(" WHERE ");
				for(String where : wheres){
					sb.append(where);
				}
				sb.delete(sb.length()-5, sb.length());
			}else{
				return null;
			}
			sqlCache.put(key, sb.toString());
		}
		return sb.toString();
	}
	
	public static String getUpdateSqlNotNull(Object bean){
		Entity entity = bean.getClass().getAnnotation(Entity.class);
		StringBuffer sb = new StringBuffer();
		if(entity!=null){
			String tableName = entity.tableName();
			Collection<String> sets = new ArrayList<String>();
			Collection<String> wheres = new ArrayList<String>();
			Class<?> clazz = bean.getClass();
			for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fields = superClass.getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
					try {
						if(field.get(bean)==null){
							continue;
						}
						NotUpdate notUpdate = field.getAnnotation(NotUpdate.class);
						if(notUpdate!=null){
							continue;
						}
						Column column = field.getAnnotation(Column.class);
						PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
						if(primaryKey!=null){
							if(column!=null&&!column.value().isEmpty()){
								wheres.add(column.value()+"=:"+field.getName()+" AND ");
							}else{
								wheres.add(BeanUtils.getDbFieldName(field.getName())+"=:"+field.getName()+" AND ");
							}
						}else if(primaryKey==null){
							if(column!=null&&!column.value().isEmpty()){
								sets.add(column.value()+"=:"+field.getName()+",");
							}else if(column!=null&&column.value().isEmpty()){
								sets.add(BeanUtils.getDbFieldName(field.getName())+"=:"+field.getName()+",");
							}
						}
					} catch (Exception e) {
					}
				}
			}
			sb.append("UPDATE ").append(tableName).append(" SET ");
			for(String set : sets){
				sb.append(set);
			}
			sb.delete(sb.length()-1, sb.length());
			if(!wheres.isEmpty()){
				sb.append(" WHERE ");
				for(String where : wheres){
					sb.append(where);
				}
				sb.delete(sb.length()-5, sb.length());
			}else{
				return null;
			}
		}
		return sb.toString();
	}
	
	public static String getDeleteSql(Class<?> clazz){
		String key = clazz.getName()+":delete";
		String sql = sqlCache.get(key);
		if(sql!=null){
			return sql;
		}
		Entity entity = clazz.getAnnotation(Entity.class);
		StringBuffer sb = new StringBuffer();
		if(entity!=null){
			String tableName = entity.tableName();
			Collection<String> wheres = new ArrayList<String>();
			root:for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fields = superClass.getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
					try {
						Column column = field.getAnnotation(Column.class);
						PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
						if(primaryKey!=null){
							if(column!=null&&!column.value().isEmpty()){
								wheres.add(column.value()+"=?");
							}else{
								wheres.add(BeanUtils.getDbFieldName(field.getName())+"=?");
							}
							break root;
						}
					} catch (Exception e) {
					}
				}
			}
			sb.append("DELETE FROM ").append(tableName);
			if(!wheres.isEmpty()){
				sb.append(" WHERE ");
				for(String where : wheres){
					sb.append(where);
				}
			}else{
				return null;
			}
			sqlCache.put(key, sb.toString());
		}
		return sb.toString();
	}
	
	public static String getSelectSqlByPk(Class<?> clazz){
		String key = clazz.getName()+":select";
		String sql = sqlCache.get(key);
		if(sql!=null){
			return sql;
		}
		Entity entity = clazz.getAnnotation(Entity.class);
		StringBuffer sb = new StringBuffer();
		if(entity!=null){
			String tableName = entity.tableName();
			Collection<String> columns = new ArrayList<String>();
			Collection<String> wheres = new ArrayList<String>();
			for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fields = superClass.getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
					try {
						Column column = field.getAnnotation(Column.class);
						ReferenceColumn referenceColumn = field.getAnnotation(ReferenceColumn.class);
						PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
						if(primaryKey!=null&&wheres.isEmpty()){
							if(column!=null&&!column.value().isEmpty()){
								wheres.add("base."+column.value()+"=?");
							}else{
								wheres.add("base."+BeanUtils.getDbFieldName(field.getName())+"=?");
							}
						}
						if(column!=null&&!column.value().isEmpty()){
							columns.add("base."+column.value()+" "+field.getName());
						}else if(column!=null&&column.value().isEmpty()){
							columns.add("base."+BeanUtils.getDbFieldName(field.getName()));
						}else if(referenceColumn!=null&&referenceColumn.subSelectSql()!=null&&!referenceColumn.subSelectSql().isEmpty()){
							columns.add(referenceColumn.subSelectSql());
						}
					} catch (Exception e) {
					}
				}
			}
			sb.append("SELECT ");
			if(!columns.isEmpty()){
				for(String column : columns){
					sb.append(column).append(",");
				}
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(" FROM ").append(tableName+" base");
			if(!wheres.isEmpty()){
				sb.append(" WHERE ");
				for(String where : wheres){
					sb.append(where);
				}
			}else{
				return null;
			}
			sqlCache.put(key, sb.toString());
		}
		return sb.toString();
	}
	
	public static String getDynamicWhereSql(Object dynamicWhereBean){
		StringBuffer sb = new StringBuffer();
		Entity entity = dynamicWhereBean.getClass().getAnnotation(Entity.class);
		if(entity!=null){
			Collection<String> wheres = new ArrayList<String>();
			Class<?> clazz = dynamicWhereBean.getClass();
			for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fields = superClass.getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
					try {
						if(field.get(dynamicWhereBean)==null||field.get(dynamicWhereBean).toString().isEmpty()){
							continue;
						}
						VirtualColumn virtualColumn = field.getAnnotation(VirtualColumn.class);
						Column column = field.getAnnotation(Column.class);
						PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
						if(primaryKey!=null){
							if(column!=null&&!column.value().isEmpty()){
								wheres.add(column.logical()+column.value()+column.comparison()+":"+field.getName());
							}else{
								wheres.add(VirtualColumn.DYNAMIC_WHERE_LOGIC.AND+BeanUtils.getDbFieldName(field.getName())+"=:"+field.getName());
							}
						}else if(virtualColumn!=null){
							wheres.add(virtualColumn.logical()+":"+field.getName()+virtualColumn.comparison()+virtualColumn.comparisonColumn());
						}else if(column!=null&&column.isDynamicWhere()){
							if(!column.value().isEmpty()){
								wheres.add(column.logical()+column.value()+column.comparison()+":"+field.getName());
							}else{
								wheres.add(column.logical()+BeanUtils.getDbFieldName(field.getName())+column.comparison()+":"+field.getName());
							}
						}
					} catch (Exception e) {
					}
				}
			}
			for(String where : wheres){
				sb.append(where);
			}
			if(entity != null && entity.where().isEmpty()){
				sb.append(" AND ".concat(entity.where()));
			}
			sb.delete(0, 4);
		}
		return sb.toString();
	}

	public static String getPkName(Class<?> clazz){
		String pkName = sqlCache.get(clazz.getName().concat("#PK_NAME#"));
		if(pkName != null){
			return pkName;
		}
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				try {
					PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
					if(primaryKey!=null){
						sqlCache.put(clazz.getName().concat("#PK_NAME#"), field.getName());
						return getPkName(clazz);
					}
				} catch (Exception e) {
				}
			}
		}
		throw new RuntimeException(clazz.getName()+"没有设置主键注解@PrimaryKey");
	}
	
	public static Class<?> getPkType(Class<?> clazz){
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				try {
					PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
					if(primaryKey!=null){
						return field.getType();
					}
				} catch (Exception e) {
				}
			}
		}
		throw new RuntimeException(clazz.getName()+"没有设置主键注解@PrimaryKey");
	}
	
}
