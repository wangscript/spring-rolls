package com.wisdom.core.utils;

import java.util.List;

/**
 * 封装分页和排序查询的结果,并继承QueryParameter的所有查询请求参数.
 * 
 */
public class Page extends QueryParameter {

	@SuppressWarnings("unchecked")
	private List result = null;

	private int totalCount = -1;

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page(int pageSize, boolean autoCount) {
		this.pageSize = pageSize;
		this.autoCount = autoCount;
	}

	public Page(int pageSize, boolean autoCount,String orderBy,String order) {
		this.pageSize = pageSize;
		this.autoCount = autoCount;
	}

	/**
	 * 页内的数据列表.
	 */
	@SuppressWarnings("unchecked")
	public List getResult() {
		if(result != null && !result.isEmpty()){
			return result;
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void setResult(List result) {
		this.result = result;
	}

	/**
	 * 总记录数.
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 计算总页数.
	 */
	public int getTotalPages() {
		if (totalCount == -1)
			return -1;

		int count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 返回下页的页号,序号从1开始.
	 */
	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	/**
	 * 是否还有上一页. 
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 返回上页的页号,序号从1开始.
	 */
	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}
}
