package com.portal.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.portal.exception.DaoException;
import com.portal.po.PagerPo;
import com.portal.po.Po;
import com.portal.service.BaseService;
import com.portal.util.PoGenUtil;
import com.portal.util.ReflectUtil;
import com.portal.util.PoGenUtil.POMeta;
import com.portal.util.PoGenUtil.POMetaRepository;
import com.portal.util.PoGenUtil.SqlAndParams;


public class  BaseServiceImpl<T extends Po> implements BaseService<T> {

	private static Logger logger = Logger.getLogger(BaseServiceImpl.class);
	
	protected static POMetaRepository metas = POMetaRepository.getInstance();
	
	private Class<T> clazz = this.getParameterizedType();
	
	@SuppressWarnings("unchecked")
	private  Class<T> getParameterizedType(){
		return ReflectUtil.getSuperClassParameterizedType(this);
	}
	
	@Autowired
	private SqlSessionTemplate sst;
	
	public int add(T po) {
		PreparedStatement ps = null;
		try {
			POMeta meta = metas.getMeta(po);
			StringBuilder sbd = new StringBuilder();
			sbd.append("INSERT INTO " + meta.getTabName() + "(");

			LinkedList<Object> params = new LinkedList<Object>();
			Object obj = null;
			for (int i = 0; i < meta.getColSize(); i++) {
				obj = meta.getColVal(po, i);
				if (obj != null) {
					sbd.append(meta.getColName(i) + ",");
					params.addLast(obj);
				}
			}
			sbd.deleteCharAt(sbd.length() - 1);
			sbd.append(") VALUES(");

			for (int i = 0; i < params.size(); i++) {
				sbd.append("?,");
			}
			sbd.deleteCharAt(sbd.length() - 1);
			sbd.append(")");
			logger.info("==>" + sbd.toString() + ";  Params:" + params);

			Connection conn = sst.getConnection();
			ps = conn.prepareStatement(sbd.toString());
			for (int i = 0; i < params.size(); i++) {
				PoGenUtil.setParam(ps, i + 1, params.get(i));
			}
			int i = ps.executeUpdate();
			return i;
		} catch (Exception e) {
			throw new DaoException("insert error!", e);
		}
	}

	@SuppressWarnings("unchecked")
	public int add(List<T> pos){
		int i = 0;
		try {
			if(pos!=null && pos.size()>0){
				for (Po t : pos)
					i+=add((T)t);
			}
			return i;
		} catch (DaoException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException("insert error", e);
		}
	}
	
	public int delete(T po) {
		try {
			POMeta meta = metas.getMeta(po);
			StringBuilder sbd = new StringBuilder();
			sbd.append("DELETE FROM " + meta.getTabName() + " WHERE 1=1 ");

			LinkedList<Object> params = new LinkedList<Object>();
			for (int i = 0; i < meta.getColSize(); i++) {
				Object obj = meta.getColVal(po, i);
				if (obj != null) {
					sbd.append("AND " + meta.getColName(i) + "=? ");
					params.addLast(obj);
				}
			}
			return delete(sbd.toString(), params);
		} catch (Exception e) {
			throw new DaoException("delete error!", e);
		}
	}

	public int delete(String sql, List<?> params) {
		logger.info("==>" + sql + ";  Params:" + params);

		PreparedStatement ps = null;
		try {
			Connection conn = sst.getConnection();
			ps = conn.prepareStatement(sql);
			if ((params != null) && (params.size() > 0)) {
				for (int i = 0; i < params.size(); i++) {
					PoGenUtil.setParam(ps, i + 1, params.get(i));
				}
			}
			int i = ps.executeUpdate();
			return i;
		} catch (Exception e) {
			throw new DaoException("delete error!", e);
		}
	}

	
	public int update(T po, T where) {
		try {
			POMeta meta = metas.getMeta(where);

			StringBuilder sbd = new StringBuilder();
			sbd.append("UPDATE " + meta.getTabName() + " SET ");

			LinkedList<Object> params = new LinkedList<Object>();
			Object obj = null;
			for (int i = 0; i < meta.getColSize(); i++) {
				obj = meta.getColVal(po, i);
				if (obj != null) {
					sbd.append(meta.getColName(i) + "=?,");
					params.addLast(obj);
				}
			}
			sbd.deleteCharAt(sbd.length() - 1);

			sbd.append(" WHERE 1=1 ");
			for (int i = 0; i < meta.getColSize(); i++) {
				obj = meta.getColVal(where, i);
				if (obj != null) {
					sbd.append(" AND " + meta.getColName(i) + "=?");
					params.addLast(obj);
				}
			}

			return update(sbd.toString(), params);
		} catch (Exception e) {
			throw new DaoException("update error", e);
		}
	}
	
	public int update(String sql, List<?> params) {
		logger.info("==>" + sql + ";  Params:" + params);

		PreparedStatement ps = null;
		try {
			Connection conn = sst.getConnection();
			ps = conn.prepareStatement(sql);
			if ((params != null) && (params.size() > 0)) {
				for (int i = 0; i < params.size(); i++) {
					PoGenUtil.setParam(ps, i + 1, params.get(i));
				}
			}
			int i = ps.executeUpdate();
			return i;
		} catch (Exception e) {
			throw new DaoException("update error!", e);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Po po = (Po)Class.forName(clazz.getName()).newInstance();
			SqlAndParams sap = PoGenUtil.genSelectSqlByPO(po);

			logger.info("==>" + sap.getSql() + ";  Params:" + sap.getParams());
			Connection conn = sst.getConnection();
			ps = conn.prepareStatement(sap.getSql());
			for (int i = 0; i < sap.getParams().size(); i++) {
				PoGenUtil.setParam(ps, i + 1, sap.getParams().get(i));
			}
			rs = ps.executeQuery();

			List<Po> localList = PoGenUtil.getPOFromResultSet(rs, po);
			return (List<T>) localList;
		} catch (DaoException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException("select error!", e);
		}
	}
	
	public List<T> find(T po){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SqlAndParams sap = PoGenUtil.genSelectSqlByPO(po);

			logger.info("==>" + sap.getSql() + ";  Params:" + sap.getParams());
			Connection conn = sst.getConnection();
			ps = conn.prepareStatement(sap.getSql());
			for (int i = 0; i < sap.getParams().size(); i++) {
				PoGenUtil.setParam(ps, i + 1, sap.getParams().get(i));
			}
			rs = ps.executeQuery();

			List<T> localList = PoGenUtil.getPOFromResultSet(rs, po);
			return localList;
		} catch (DaoException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException("select error!", e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<T> find(String sql,Object [] args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			Po po = (Po)Class.forName(clazz.getName()).newInstance();
			logger.info("==>" + sql + ";  Params:" + args);
			Connection conn = sst.getConnection();
			ps = conn.prepareStatement(sql);
			if(args!=null && args.length>0){
				for (int i = 0; i < args.length; i++) {
					PoGenUtil.setParam(ps, i + 1, args[i]);
				}
			}
			rs = ps.executeQuery();
			List<Po> localList = PoGenUtil.getPOFromResultSet(rs, po);
			return localList ==null ? null :(List<T>)localList;
		} catch (DaoException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException("select error!", e);
		}
	}
	
	public int delete(T po, String[] ids) {
		return 0;
	}
	
	
	public List<Map<String, Object>> select(String sqlId, Object params) {
		return null;
	}

	
	public PagerPo<T> getScrollData(T po, PagerPo<T> page) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SqlAndParams sap = PoGenUtil.genSelectSqlByPO(po);

			//1.计算总记录数
			String countSql = this.getCountSql(sap.getSql());
			Connection conn = sst.getConnection();
			ps = conn.prepareStatement(countSql);
			for (int i = 0; i < sap.getParams().size(); i++) {
				PoGenUtil.setParam(ps, i + 1, sap.getParams().get(i));
			}
			rs = ps.executeQuery();
			logger.info("==>" + countSql + ";  Params:" + sap.getParams());
			if (rs.next()) { 
				int totalRecord = rs.getInt(1);  
				page.setTotalRows(totalRecord);  
			}
			
			//2.查询列表
			StringBuffer listSql = new StringBuffer(sap.getSql());
			Map<String,String> order = page.getOrder();
			if(order!=null && order.size()>0){
				listSql.append(" order by ");
				Set<String> keys = order.keySet();
				int i = 0;
				for(String key : keys){
					if(i != keys.size()-1){
						listSql.append(key+" "+order.get(key)+", ");
					}else{
						listSql.append(key+" "+order.get(key)+" ");
					}
					i++;
				}
			}
			listSql.append(" limit ").append(page.getStart()).append(",").append(page.getPageSize()); 
			ps = conn.prepareStatement(listSql.toString());
			for (int i = 0; i < sap.getParams().size(); i++) {
				PoGenUtil.setParam(ps, i + 1, sap.getParams().get(i));
			}
			rs = ps.executeQuery();
			logger.info("==>" + sap.getSql() + ";  Params:" + sap.getParams());
			logger.info("==>" + listSql + ";  Params:" + sap.getParams());
			List<T> localList = PoGenUtil.getPOFromResultSet(rs, po);
			
			page.setEntities(localList);
			return page;
		} catch (DaoException e) {
			throw e;
		} catch (Exception e) {
			throw new DaoException("select error!", e);
		}
	}
	
	
	
	public PagerPo<T> getScrollData(String sqlId, PagerPo<T> page) {
		MappedStatement mappedStatement = this.sst.getConfiguration().getMappedStatement(clazz.getName()+"."+sqlId);
		this.setTotalRows(mappedStatement, page);
		this.setEntitiesForBean(mappedStatement, page);
		return page;
	}

	public PagerPo<Map<String, Object>> getScrollDataForMap(String sqlId,PagerPo<Map<String,Object>> page) {
		MappedStatement mappedStatement = this.sst.getConfiguration().getMappedStatement(clazz.getName()+"."+sqlId);
		this.setTotalRows(mappedStatement, page);
		this.setEntitiesForMap(mappedStatement, page);
		return page;
	}

	private void setEntitiesForMap(MappedStatement mappedStatement,PagerPo<Map<String, Object>> page){
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		String sql = boundSql.getSql();
		String pageSql = this.getPageSql(page, sql);
		
		PreparedStatement pstmt = null;  
		ResultSet rs = null;
		
		try {  
			pstmt = sst.getConnection().prepareStatement(pageSql);  
			List<Object> params = setParameters(pstmt,mappedStatement,boundSql,boundSql.getParameterObject());
			
			rs = pstmt.executeQuery();  
			
			logger.info("==>" + sql + ";  Params:" + params);
			logger.info("==>" + pageSql + ";  Params:" + params);
			
			List<Map<String,Object>> entities = new LinkedList<Map<String,Object>>();
			
			while(rs.next()){
				ResultSetMetaData rsda = rs.getMetaData();
				Map<String,Object> map = new HashMap<String,Object>();
				for(int i=1; i<rsda.getColumnCount(); i++){
					map.put(rsda.getColumnName(i).toUpperCase(),rs.getObject(rsda.getColumnName(i)));
				}
				entities.add(map);
			}
			page.setEntities(entities);
		} catch (SQLException e) {  
			e.printStackTrace(); 
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void setEntitiesForBean(MappedStatement mappedStatement,PagerPo<T> page){
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		String sql = boundSql.getSql();
		String pageSql = this.getPageSql(page, sql);
		
		PreparedStatement pstmt = null;  
		ResultSet rs = null;
		
		try {  
			pstmt = sst.getConnection().prepareStatement(pageSql);  
			List<Object> params = setParameters(pstmt,mappedStatement,boundSql,boundSql.getParameterObject());
			
			rs = pstmt.executeQuery();  
			
			logger.info("==>" + sql + ";  Params:" + params);
			logger.info("==>" + pageSql + ";  Params:" + params);
			
			Po po = (Po)Class.forName(clazz.getName()).newInstance();
			List<T> entities = (List<T>) PoGenUtil.getPOFromResultSet(rs, po);
			page.setEntities(entities);
		} catch (SQLException e) {  
			e.printStackTrace(); 
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void setTotalRows(MappedStatement mappedStatement,PagerPo<?> page){
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		String sql = boundSql.getSql();
		String countSql = this.getCountSql(sql);
		PreparedStatement pstmt = null;  
		ResultSet rs = null;
		try {  
			pstmt = sst.getConnection().prepareStatement(countSql);  
			List<Object> params = setParameters(pstmt,mappedStatement,boundSql,boundSql.getParameterObject());
			
			rs = pstmt.executeQuery();  
			
			logger.info("==>" + countSql + ";  Params:" + params);
			
			if (rs.next()) { 
				int totalRecord = rs.getInt(1);  
				page.setTotalRows(totalRecord);  
			}
		} catch (SQLException e) {  
			e.printStackTrace(); 
		}
	}
	

	private String getPageSql(PagerPo<?> page,String sql){
		StringBuffer sqlBuffer = new StringBuffer(sql);
		Map<String,String> order = page.getOrder();
		if(order!=null && order.size()>0){
			sqlBuffer.append(" order by ");
			Set<String> keys = order.keySet();
			int i = 0;
			for(String key : keys){
				if(i != keys.size()-1){
					sqlBuffer.append(key+" "+order.get(key)+", ");
				}else{
					sqlBuffer.append(key+" "+order.get(key)+" ");
				}
				i++;
			}
			
		}
		sqlBuffer.append(" limit ").append(page.getStart()).append(",").append(page.getPageSize()); 
		return sqlBuffer.toString();
	}

	private String getCountSql(String sql) {
		return "SELECT COUNT(*) FROM ("+sql+") TEMP_123";
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Object> setParameters(PreparedStatement pstmt,
			MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		List<Object> params = new LinkedList<Object>();
		ErrorContext.instance().activity("setting parameters" ).object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if  (parameterMappings !=  null ) {    
			Configuration configuration = mappedStatement.getConfiguration(); 
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();    
			MetaObject metaObject = parameterObject == null  ?  null : configuration.newMetaObject(parameterObject); 
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}

					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException(
								"There was no TypeHandler found for parameter "
										+ propertyName + " of statement "
										+ mappedStatement.getId());
					}
					typeHandler.setParameter(pstmt, i + 1, value,parameterMapping.getJdbcType());
					params.add(value);
				}
			}
		}
		return params;
	}
}
