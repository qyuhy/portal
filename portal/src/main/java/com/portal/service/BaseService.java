package com.portal.service;

import java.util.List;
import java.util.Map;

import com.portal.po.PagerPo;
import com.portal.po.Po;

public interface BaseService<T extends Po> {
	
	public int add(T po);
	
	public int add(List<T> pos);
	
	public int delete(T po);
	
	public int delete(T po,String [] ids);
	
	public int update(T po,T where);
	
	public List<T> findAll();
	
	public List<T> find(T po);
	
	public List<T> find(String sql,Object [] args);
	
	public List<Map<String,Object>> select(String sqlId,Object params);
	
	public PagerPo<T> getScrollData(T po,PagerPo<T> page);
	
	public PagerPo<T> getScrollData(String sqlId,PagerPo<T> page);
	
	public PagerPo<Map<String,Object>> getScrollDataForMap(String sqlId,PagerPo<Map<String,Object>> page);
	
}
