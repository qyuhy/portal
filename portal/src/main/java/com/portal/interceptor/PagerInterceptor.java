package com.portal.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import com.portal.exception.DaoException;
import com.portal.po.PagerPo;
import com.portal.util.ReflectUtil;

@Intercepts( { @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class PagerInterceptor implements Interceptor {

	private String databaseType;//数据库类型，不同的数据库有不同的分页方法
	
	public Object intercept(Invocation invocation) throws Throwable {
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		StatementHandler delegate = (StatementHandler)ReflectUtil.getFieldValue(handler, "delegate");
		BoundSql boundSql = delegate.getBoundSql();  
		Object obj = boundSql.getParameterObject();  
		if (obj instanceof PagerPo<?>) {  
			PagerPo<?> page = (PagerPo<?>) obj;
			MappedStatement mappedStatement = (MappedStatement)ReflectUtil.getFieldValue(delegate, "mappedStatement"); 
			Connection connection = (Connection)invocation.getArgs()[0]; 
			String sql = boundSql.getSql();  
			this.setTotalRecord(page,mappedStatement, connection); 

			String pageSql = this.getPageSql(page, sql); 
			ReflectUtil.setFieldValue(boundSql, "sql", pageSql); 
		}
		return invocation.proceed();
	}



	public Object plugin(Object target) {
		return Plugin.wrap(target, this);  
	}

	public void setProperties(Properties properties) {
		this.databaseType = properties.getProperty("database");  
	}
	
	private String getPageSql(PagerPo<?> page, String sql) {
		StringBuffer sqlBuffer = new StringBuffer(sql); 
		if ("mysql".equalsIgnoreCase(databaseType)) {  
			return getMysqlPageSql(page, sqlBuffer);  
		}else if ("oracle".equalsIgnoreCase(databaseType)){  
			 return getOraclePageSql(page, sqlBuffer);
		}
		throw new DaoException("不支持的分页的数据库==>"+databaseType);
	}
	
	private String getOraclePageSql(PagerPo<?> page, StringBuffer sqlBuffer) {
		StringBuffer pageSql = new StringBuffer();
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
		pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");  
        pageSql.append(sqlBuffer);  
        pageSql.append(") as tmp_tb where ROWNUM<=");  
        pageSql.append(page.getStart()+page.getPageSize());  
        pageSql.append(") where row_id>");  
        pageSql.append(page.getStart());
		return pageSql.toString();  
	}



	private String getMysqlPageSql(PagerPo<?> page, StringBuffer sqlBuffer) {
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



	private void setTotalRecord(PagerPo<?> page,MappedStatement mappedStatement, Connection connection) {
		BoundSql boundSql = mappedStatement.getBoundSql(page); 
		String sql = boundSql.getSql();
		String countSql = this.getCountSql(sql);
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings(); 
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
		PreparedStatement pstmt = null;  
		ResultSet rs = null;
		try {  
			pstmt = connection.prepareStatement(countSql);  
			parameterHandler.setParameters(pstmt);  
			rs = pstmt.executeQuery();  
			if (rs.next()) { 
				 int totalRecord = rs.getInt(1);  
				 page.setTotalRows(totalRecord);  
			}
		} catch (SQLException e) {  
			e.printStackTrace(); 
		}
		
	}

	private String getCountSql(String sql) {
		int index = sql.indexOf("from");
		return "select count(*) " + sql.substring(index);
	}
	

}
