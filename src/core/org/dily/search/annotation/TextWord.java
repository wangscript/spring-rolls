package org.dily.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 功能描述(Description):<br><b>
 * 文本字段，建立分词和索引，但不做存储
 * </b><br>作 者(Author): <i><b>曹阳(Cao.Yang)</b></i>
 * <br>建立日期(Create Date): <b>2011-8-30下午07:23:30</b>
 * <br>项目名称(Project Name): <b>dily</b>
 * <br>包及类名(Package Class): <b>org.dily.search.annotation.TextWord.java</b>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TextWord {
	
	/**
	 * 是否过滤HTML标签
	 * @return
	 */
	public boolean isFilterHtmlTags() default false;
	
}
