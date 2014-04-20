package com.portal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.portal.exception.DaoException;
import com.portal.po.Po;

@SuppressWarnings("all")
public class PoGenUtil {
	private static Logger logger = Logger.getLogger(PoGenUtil.class);
	private static final int TYPE_CLSNAME = 1;
	private static final int TYPE_ATTRNAME = 2;
	private String genPath = null;

	private Map<String, Map<String, String>> dbConnsMap = null;
	private Map<String, Map<String, Object>> packagesMap = null;

	private String queryString = "select * from {0}";

	private String comment = "/*\n* Copyright (c) 2013 Portal, Inc. All  Rights Reserved.\n* This software is published under the terms of the Portal Software\n* License version 1.0, a copy of which has been included with this\n* distribution in the LICENSE.txt file.\n*\n* CreateDate : {0,date,yyyy-MM-dd HH:mm:ss}\n* CreateBy   : {1}\n* Comment    : generate by com.portal.po.POGen\n*/\n";

	private String headTemp = "package {0};\n\n{1,choice,0#|1#import java.util.Date;\n}{2,choice,0#|1#import java.sql.Blob;\n}{3,choice,0#|1#import java.sql.Clob;\n}import com.portal.po.Po;\n";

	private String clsTemp = "@SuppressWarnings(\"serial\")\npublic class {0}Po extends Po'{'\n";

	private String attrDecl = "\tprivate {1} {0};\n";

	private String setTemp = "\tpublic void set{0}({1} {2})'{'\n\t\tthis.{2}={2};\n\t}\n";

	private String getTemp = "\tpublic {1} get{0}()'{'\n\t\treturn this.{2};\n\t}\n";

	public void loadConf() {
		loadConf("/POGenConf.xml");
	}

	public void loadConf(String path) {
		try {
			SAXReader reader = new SAXReader();
			InputStream in = PoGenUtil.class.getResourceAsStream(path);
			if (in == null) {
				in = new FileInputStream(path);
			}
			Document doc = reader.read(in);
			List<Node> dbConnsNodeList = doc.selectNodes("/PO_CONFIG/DB_CONNECTIONS/DB_CONNECTION");
			Node poPathNode = doc.selectSingleNode("/PO_CONFIG/PO_PATH");
			List<Node> packagesNodeList = doc.selectNodes("/PO_CONFIG/PACKAGE");
			this.dbConnsMap = new Hashtable();
			Element connEl = null;
			String type;
			String key;
			for (Node node : dbConnsNodeList) {
				connEl = (Element) node;
				if (connEl.attributeValue("VALID").equalsIgnoreCase("TRUE")) {
					type = connEl.attributeValue("TYPE");
					List<Node> plist = node.selectNodes("PROPERTY");
					Map propertyMap = new Hashtable();
					Element pEl = null;
					for (Node pnode : plist) {
						pEl = (Element) pnode;
						key = pEl.attributeValue("NAME");
						String value = pEl.getText();
						System.out.println(key + "=" + value);
						propertyMap.put(key, value);
					}
					this.dbConnsMap.put(type, propertyMap);
				}
			}
			this.packagesMap = new Hashtable();
			Element packageEl = null;
			for (Node node : packagesNodeList) {
				packageEl = (Element) node;
				if (packageEl.attributeValue("IS_GENERATOR").equalsIgnoreCase(
						"TRUE")) {
					String packageName = packageEl.attributeValue("NAME");
					List<Node> tabList = packageEl.selectNodes("TABLE");
					Element tabEl = null;
					Map tabMap = new Hashtable();
					for (Node tabNode : tabList) {
						tabEl = (Element) tabNode;
						String tabName = tabEl.attributeValue("NAME");
						System.out.println("Table Name is " + tabName);
						if ((tabName != null) && (!tabName.equals(""))) {
							tabMap.put(tabName, "");
						}
					}
					this.packagesMap.put(packageName, tabMap);
				}
			}
			this.genPath = ((Element) poPathNode).attributeValue("PATH");
			System.out.println("genPath==================" + this.genPath);
		} catch (Exception e) {
			System.out.println("配置读取错误，退出执行!" + e.getMessage() + ","
					+ e.getCause());
			e.printStackTrace();
			System.exit(0);
		}
	}

	private String tabName2PoName(int type, String tname) {
		byte ascii = -32;
		tname = tname.toLowerCase();
		StringBuilder sbd = new StringBuilder();
		for (int i = 0; i < tname.length(); i++) {
			if ((tname.charAt(i) == '_') && (i + 1 < tname.length())) {
				i++;
				if ((tname.charAt(i) >= 'a') && (tname.charAt(i) <= 'z'))
					sbd.append((char) (tname.charAt(i) + ascii));
				else
					sbd.append(tname.charAt(i));
			} else {
				sbd.append(tname.charAt(i));
			}
		}
		if ((type == 1) && (sbd.charAt(0) >= 'a') && (sbd.charAt(0) <= 'z')) {
			sbd.setCharAt(0, (char) (sbd.charAt(0) + ascii));
		}
		return sbd.toString();
	}

	private void writePOFile(String tabName, StringBuilder sbd, String pkgName) {
		try {
			String file = this.genPath + '/' + pkgName.replace('.', '/') + '/';
			File dir = new File(file);
			if (!dir.exists())
				dir.mkdirs();
			file = file + tabName2PoName(1, tabName) + "Po.java";

			System.out.println("Write Class File : " + file);
			FileWriter fw = new FileWriter(file);
			fw.write(sbd.toString());
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void genMethod(StringBuilder sbd, String attrName, Class clsType) {
		try {
			String attrName1 = tabName2PoName(1, attrName);
			String attrName2 = tabName2PoName(2, attrName);
			sbd.append(MessageFormat.format(this.setTemp, new Object[] {
					attrName1, clsType.getSimpleName(), attrName2 }));
			sbd.append("\n");
			sbd.append(MessageFormat.format(this.getTemp, new Object[] {
					attrName1, clsType.getSimpleName(), attrName2 }));
			sbd.append("\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void genPOFile(String tabName, HashMap<String, Class> infos,
			String pkgName) {
		try {
			int impDate = infos.containsValue(Date.class) ? 1 : 0;
			int impBlob = infos.containsValue(Blob.class) ? 1 : 0;
			int impClob = infos.containsValue(Clob.class) ? 1 : 0;

			StringBuilder sbd = new StringBuilder();
			sbd.append(MessageFormat.format(this.comment, new Object[] {
					new Date(), System.getenv("USERNAME") }));
			sbd.append("\n");
			sbd.append(MessageFormat.format(this.headTemp, new Object[] {
					pkgName, Integer.valueOf(impDate),
					Integer.valueOf(impBlob), Integer.valueOf(impClob) }));
			sbd.append("\n");
			sbd.append(MessageFormat.format(this.clsTemp,
					new Object[] { tabName2PoName(1, tabName) }));
			sbd.append("\n");

			Iterator ite = infos.keySet().iterator();
			String key = null;

			while (ite.hasNext()) {
				key = (String) ite.next();
				sbd.append(MessageFormat.format(this.attrDecl, new Object[] {
						tabName2PoName(2, key),
						((Class) infos.get(key)).getSimpleName() }));
			}
			sbd.append("\n");

			ite = infos.keySet().iterator();
			while (ite.hasNext()) {
				key = (String) ite.next();
				genMethod(sbd, key, (Class) infos.get(key));
			}

			sbd.append("}");

			writePOFile(tabName, sbd, pkgName);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void gen() throws Exception {
		Connection conn = null;
		try {
			if (!this.dbConnsMap.isEmpty())
				for (Map.Entry entry : this.dbConnsMap.entrySet()) {
					String type = (String) entry.getKey();
					if ((type.equalsIgnoreCase("oracle"))|| (type.equalsIgnoreCase("db2")) || (type.equalsIgnoreCase("mysql"))) {
						Map dbConnMap = (Map) entry.getValue();
						Class.forName((String) dbConnMap.get("DB_DRIVER"));
						conn = DriverManager.getConnection((String) dbConnMap.get("DB_URL"), (String) dbConnMap.get("DB_USER"), (String) dbConnMap.get("DB_PASSWORD"));
						Statement ste = conn.createStatement();
						if (!this.packagesMap.isEmpty()) {
							for (Map.Entry ety : this.packagesMap.entrySet()) {
								String pkgName = (String) ety.getKey();
								Map<String,Map.Entry> tabMap = (Map) ety.getValue();
								for (Map.Entry en : tabMap.entrySet()) {
									System.out.println("------Gen tab : "+ (String) en.getKey() + "------");
									System.out.println(MessageFormat.format(this.queryString, new Object[] { en.getKey() }));
									ResultSet rs = ste.executeQuery(MessageFormat.format(this.queryString,new Object[] { en.getKey() }));
									HashMap infos = this.getResultSetMetaData((String) dbConnMap.get("DB_DRIVER"),rs);
									genPOFile((String) en.getKey(), infos,pkgName);
									rs.close();
								}
							}
						}
						ste.close();
					} else {
						throw new Exception(type
								+ "类型的数据库，PO暂不支持!");
					}
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	
	public static HashMap<String, Class> getResultSetMetaData(
			String jdbcDriver, ResultSet rs) throws Exception {
		HashMap maps = new HashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		String colName = null;
		Class cls = null;
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			colName = rsmd.getColumnName(i);
			try {
				cls = getJavaType(rsmd.getColumnName(i), rsmd.getColumnType(i),
						rsmd.getPrecision(i), rsmd.getScale(i));
				maps.put(colName, cls);
				System.out.println(rsmd.getColumnName(i) + "\t"
						+ rsmd.getColumnTypeName(i) + "\t"
						+ rsmd.getPrecision(i) + "\t" + rsmd.getScale(i));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return maps;
	}

	public static Class getJavaType(String colName, int colType,
			int colPrecision, int colScale) throws Exception {
		Class cls = null;

		switch (colType) {
		case 2003:
			break;
		case -5:
			cls = Long.class;
			break;
		case -2:
			break;
		case -7:
			break;
		case 2004:
			cls = Blob.class;
			break;
		case 16:
			cls = Boolean.class;
			break;
		case 1:
			cls = String.class;
			break;
		case 2005:
			cls = Clob.class;
			break;
		case 70:
			break;
		case 91:
			cls = Date.class;
			break;
		case 3:
			cls = getClassByPrecisionAndScale(colPrecision, colScale);
			break;
		case 2001:
			break;
		case 8:
			cls = Double.class;
			break;
		case 6:
			cls = Float.class;
			break;
		case 4:
			cls = Integer.class;
			break;
		case 2000:
			break;
		case -4:
			break;
		case -1:
			break;
		case 0:
			break;
		case 2:
			cls = getClassByPrecisionAndScale(colPrecision, colScale);
			break;
		case 1111:
			break;
		case 7:
			cls = Float.class;
			break;
		case 2006:
			break;
		case 5:
			cls = Integer.class;
			break;
		case 2002:
			break;
		case 92:
			break;
		case 93:
			cls = Date.class;
			break;
		case -6:
			cls = Integer.class;
			break;
		case -3:
			break;
		case 12:
			cls = String.class;
		}

		if (cls != null) {
			return cls;
		}
		throw new Exception("Not supported column type! colName=" + colName
				+ " type=" + colType + " precision=" + colPrecision + " Scale="
				+ colScale);
	}

	private static Class getClassByPrecisionAndScale(int colPrecision,
			int colScale) {
		Class cls = null;
		if ((colScale == 0) && (colPrecision >= 0)) {
			if (colPrecision <= 9) {
				return Integer.class;
			}
			if ((colPrecision > 9) && (colPrecision <= 19)) {
				return Long.class;
			}
		}
		if ((colScale > 0) && (colPrecision > 0)) {
			if ((colPrecision - colScale < 8) && (colScale < 4)) {
				return Float.class;
			}
			return Double.class;
		}

		if ((colPrecision == 0) && (colScale == -127)) {
			return BigDecimal.class;
		}
		return cls;
	}
	
	public static <T extends Po> List<T> getPOFromResultSet(ResultSet rs, T bean) {
		POMeta binfo = POMetaRepository.getInstance().getMeta(bean);
		LinkedList rets = new LinkedList();
		try {
			Object val = null;
			while (rs.next()) {
				Po po = (Po) binfo.getCls().newInstance();
				for (int i = 0; i < binfo.getColSize(); i++) {
					val = getValue(rs, binfo.getColName(i), binfo.getColType(i));
					binfo.setColVal(po, i, val);
				}
				rets.addLast(po);
			}
			return rets;
		} catch (Exception e) {
			throw new DaoException("Convert ResultSet to list<po> error!", e);
		}
	}
	
	public static SqlAndParams genSelectSqlByPO(Po po) {
	    try {
	      POMeta meta = POMetaRepository.getInstance().getMeta(po);
	      StringBuilder sbd = new StringBuilder();
	      sbd.append("SELECT * FROM " + meta.getTabName() + " WHERE 1=1 ");

	      LinkedList params = new LinkedList();
	      for (int i = 0; i < meta.getColSize(); i++) {
	        Object obj = meta.getColVal(po, i);
	        if (obj != null) {
	          sbd.append(" AND " + meta.getColName(i) + "=? ");
	          params.addLast(obj);
	        }
	      }
	      return new SqlAndParams(sbd.toString(), params); } 
	    catch (Exception e) {
	    	throw new DaoException("gen select sql error!", e);
	    }
	  }
	
	public static void setParam(PreparedStatement ps, int idx, Object obj) {
		try {
			if ((obj instanceof Date)) {
				ps.setTimestamp(idx, new Timestamp(((Date) obj).getTime()));
				return;
			}
			if ((obj instanceof Blob)) {
				ps.setBlob(idx, (Blob) obj);
				return;
			}
			if ((obj instanceof Clob)) {
				ps.setClob(idx, (Clob) obj);
				return;
			}
			if ((obj instanceof BigDecimal)) {
				ps.setBigDecimal(idx, (BigDecimal) obj);
				return;
			}
			if ((obj instanceof BigInteger)) {
				ps.setBigDecimal(idx, new BigDecimal((BigInteger) obj));
				return;
			}
			if ((obj instanceof Boolean)) {
				ps.setInt(idx, ((Boolean) obj).booleanValue() ? 0 : 1);
				return;
			}
			ps.setObject(idx, obj);
		} catch (SQLException e) {
			logger.error("ps set params error!", e);
			throw new DaoException("ps set params error!", e);
		}
	}

	public static Object getValue(ResultSet rs, String colName, Class targetType) {
		try {
			if (String.class.equals(targetType))
				return rs.getString(colName);
			if (Integer.class.equals(targetType))
				return Integer.valueOf(rs.getInt(colName));
			if (Long.class.equals(targetType))
				return Long.valueOf(rs.getLong(colName));
			if (Blob.class.equals(targetType)) {
				return null;
			}
			if (Clob.class.equals(targetType)) {
				return null;
			}
			if (Date.class.equals(targetType)) {
				Timestamp t = rs.getTimestamp(colName);
				return t != null ? new Date(t.getTime()) : null;
			}
			if (Float.class.equals(targetType))
				return Float.valueOf(rs.getFloat(colName));
			if (Double.class.equals(targetType))
				return Double.valueOf(rs.getDouble(colName));
			if (BigInteger.class.equals(targetType)) {
				BigDecimal b = rs.getBigDecimal(colName);
				return b != null ? b.toBigInteger() : null;
			}
			if (BigDecimal.class.equals(targetType))
				return rs.getBigDecimal(colName);
			if (Boolean.class.equals(targetType)) {
				int bint = rs.getInt(colName);

				return Boolean.valueOf(bint == 0);
			}
			throw new DaoException("not supported type:" + targetType);
		} catch (SQLException e) {
			logger.error("get " + colName + " error", e);
			throw new DaoException("get " + colName + " error", e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("### 使用系统默认路径的PO配置文件<POGenConf.xml>###");
			PoGenUtil gen = new PoGenUtil();
			gen.loadConf();
			gen.gen();
			System.out.println("###########################\n\n");
		} else {
			for (int i = 0; i < args.length; i++) {
				System.out.println("### config file : " + args[i] + " ###");
				PoGenUtil gen = new PoGenUtil();
				gen.loadConf(args[i]);
				gen.gen();
				System.out.println("###########################\n\n");
			}
		}
	}
	
	
	public static class POMetaRepository {
		private static final POMetaRepository inst = new POMetaRepository();
		private HashMap<Class, POMeta> maps = new HashMap();

		public static POMetaRepository getInstance() {
			return inst;
		}

		public <P extends Po> POMeta getMeta(P po) {
			if (this.maps.containsKey(po.getClass())) {
				return (POMeta) this.maps.get(po.getClass());
			}
			POMeta meta = new POMeta(po);
			this.maps.put(po.getClass(), meta);
			return meta;
		}
	}
	
	public static class SqlAndParams {
		private String sql;
		private LinkedList<?> params;

		public String getSql() {
			return this.sql;
		}

		public void setSql(String sql) {
			this.sql = sql;
		}

		public LinkedList<?> getParams() {
			return this.params;
		}

		public void setParams(LinkedList<?> params) {
			this.params = params;
		}

		public SqlAndParams(String sql, LinkedList<?> params) {
			this.sql = sql;
			this.params = params;
		}
	}

	public static class POMeta{
		private Class cls;
		private String tabName;
		private LinkedList<String> mthName = new LinkedList();
		private LinkedList<String> colName = new LinkedList();
		private LinkedList<Class> colType = new LinkedList();
		
		public POMeta(Po obj) {
			this.cls = obj.getClass();
			this.tabName = parseTabName(obj.getClass());

			Method[] methods = this.cls.getMethods();
			for (int i = 0; i < methods.length; i++)
				if (methods[i].getName().startsWith("set")) {
					this.mthName.addLast(methods[i].getName().substring(3));
					this.colName.addLast(parseColName(methods[i]));
					this.colType.addLast(methods[i].getParameterTypes()[0]);
				}
		}

		public String parseColName(Method meth) {
			String name = meth.getName().substring(3);

			StringBuilder sbuf = new StringBuilder();

			for (int i = 0; i < name.length(); i++) {
				char ch = name.charAt(i);
				if ((ch <= 'Z') && (ch >= 'A') && (i != 0))
					sbuf.append('_');
				sbuf.append(ch);
			}
			return sbuf.toString();
		}

		public String parseTabName(Class cls) {
			String name = cls.getName().substring(
					cls.getName().lastIndexOf(".") + 1);
			String tabName = name.substring(0, name.length() - 2);

			StringBuilder sbuf = new StringBuilder();

			for (int i = 0; i < tabName.length(); i++) {
				char ch = tabName.charAt(i);
				if ((ch <= 'Z') && (ch >= 'A') && (i != 0))
					sbuf.append('_');
				sbuf.append(ch);
			}
			return sbuf.toString();
		}
		
		public Object getColVal(Po po, int idx) throws Exception {
			Method mth = this.cls.getMethod("get"
					+ (String) this.mthName.get(idx), new Class[0]);
			return mth.invoke(po, new Object[0]);
		}

		public void setColVal(Po po, int idx, Object val)
				throws Exception {
			Method mth = this.cls.getMethod("set"
					+ (String) this.mthName.get(idx),
					new Class[] { (Class) this.colType.get(idx) });
			mth.invoke(po, new Object[] { val });
		}
		
		public String getColName(int idx) {
			return (String) this.colName.get(idx);
		}

		public Class getColType(int idx) {
			return (Class) this.colType.get(idx);
		}

		public String getMethodName(int idx) {
			StringBuilder sbd = new StringBuilder((String) this.mthName
					.get(idx));
			sbd.setCharAt(0, (char) (sbd.charAt(0) + ' '));
			return sbd.toString();
		}

		public int getColSize() {
			return this.colName.size();
		}

		public Class getCls() {
			return this.cls;
		}

		public String getTabName() {
			return tabName;
		}

		public LinkedList<String> getMthName() {
			return mthName;
		}

		public LinkedList<String> getColName() {
			return colName;
		}

		public LinkedList<Class> getColType() {
			return colType;
		}
	}
	
}
