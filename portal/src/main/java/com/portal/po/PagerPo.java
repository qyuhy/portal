package com.portal.po;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagerPo<T> {

	private static final String RETURN_TYPE_MAP = "MAP";
	private static final String RETURN_TYPE_PO = "PO";
	private static final String RETURN_TYPE = "$RETURN_TYPE$";
	
	/** 从第几条记录开始 **/
	private int start;
	
	/** 数据列表 **/
	private List<T> entities;

	/** 总记录数 **/
	private long totalRows;

	/** 每页大小 **/
	private int pageSize = 2;

	/** 总页数 **/
	@SuppressWarnings("unused")
	private long pageCount;
	
	/** 其他的参数我们把它分装成一个Map对象 **/
	private Map<String, Object> params = new HashMap<String, Object>();
	
	/** 排序的字段 **/
	private Map<String,String> order = new HashMap<String, String>();

	public List<T> getEntities() {
		return entities;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getPageCount() {
		return totalRows % pageSize == 0 ? totalRows / pageSize : totalRows
				/ pageSize + 1;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public Map<String, String> getOrder() {
		return order;
	}

	public void setOrder(Map<String, String> order) {
		this.order = order;
	}
}
