package com.reps.tj.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.reps.tj.entity.TjTableDefine;
import com.reps.tj.entity.TjTableItem;

/**
 * @Title Jdbc
 * @Description 通过Jdbc，读取数据库表单数据（临时类，调试时使用）。
 * @Author Karlova
 * @Date 2014-5-20
 */
public class Jdbc {
	private static String toDBType = "MySQL";
	private static String toHost = "localhost";
	private static String toPort = "3306";
	private static String toDB = "reps_test";
	private static String toUser = "root";
	private static String toPwd = "root";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<TjTableDefine> getTable() {
		
		String url = "jdbc:"+toDBType+"://"+toHost+":"+toPort+"/"+toDB;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<TjTableDefine> ls = null;
		
		ResultSetHandler rsh = new BeanListHandler(TjTableDefine.class);
		try{
			conn = DriverManager.getConnection(url , toUser , toPwd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select " +
					"id," +
					"name," +
					"chinese_name as chineseName," +
					"is_fixed as isFixed," +
					"priority" +
					" from reps_tj_sys_table_define order by priority");
			
			ls = (ArrayList)rsh.handle(rs);
			
		}catch(SQLException se){
			System.out.println("数据库连接失败！" + se.getMessage());
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				if (stmt != null){
					stmt.close();
				}
				if (conn != null){
					conn.close();
				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
		}
		return ls;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<TjTableItem> getTableItems(String tableId) {
		
		String url = "jdbc:"+toDBType+"://"+toHost+":"+toPort+"/"+toDB;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<TjTableItem> ls = null;
		
		ResultSetHandler rsh = new BeanListHandler(TjTableItem.class);
		try{
			conn = DriverManager.getConnection(url , toUser , toPwd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select " +
					"item_name as itemName," +
					"item_chinese_name as itemChineseName," +
					"field_type as fieldType," +
					"field_length as fieldLength," +
					"expression," +
					"expression_type as expressionType," +
					"dependency," +
					"value_fetch as valueFetch" +
					" from reps_tj_sys_table_item where table_id='" + tableId + "'" +
					" order by stat_order asc");
			
			
			ls = (ArrayList)rsh.handle(rs);
			
		}catch(SQLException se){
			System.out.println("数据库连接失败！" + se.getMessage());
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				if (stmt != null){
					stmt.close();
				}
				if (conn != null){
					conn.close();
				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
		}
		return ls;
	}
	
	public static Map<String, TjTableItem> listToMap(List<TjTableItem> items){
		Map<String, TjTableItem> map = new HashMap<String, TjTableItem>();
		for(TjTableItem item : items){
			map.put(item.getItemName(), item);
		}
		
		return map;
	}
	
//	@SuppressWarnings({ "rawtypes" })
	public static List<Map<String, String>> executeQuery(String sql) {
		
		String url = "jdbc:"+toDBType+"://"+toHost+":"+toPort+"/"+toDB;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
//		ResultSetHandler rsh = new BeanListHandler(TjTableItem.class);
		try{
			conn = DriverManager.getConnection(url , toUser , toPwd);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			
			ResultSetMetaData meta = rs.getMetaData();
//			for(int i=1; i<=meta.getColumnCount(); i++){
//				System.out.println("列名：" + meta.getColumnName(i));
//			}
			
			while(rs.next()){
				Map<String, String> map = new HashMap<String, String>();
				for(int i=1; i<=meta.getColumnCount(); i++){
					map.put(meta.getColumnLabel(i), rs.getString(i));
//					System.out.println("列名：" + meta.getColumnLabel(i) + ", 值：" + rs.getString(i));
				}
				list.add(map);
			}
			
		}catch(SQLException se){
			System.out.println("数据库连接失败！" + se.getMessage());
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				if (stmt != null){
					stmt.close();
				}
				if (conn != null){
					conn.close();
				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
		}
		return list;
	}
	
	public static void execute(String sql) {
		String url = "jdbc:"+toDBType+"://"+toHost+":"+toPort+"/"+toDB;
		
		Connection conn = null;
		Statement stmt = null;
		
		try{
			conn = DriverManager.getConnection(url , toUser , toPwd);
			stmt = conn.createStatement();
			stmt.execute(sql);
		}catch(SQLException se){
			System.out.println("数据库连接失败！" + se.getMessage());
		}finally{
			try{
				if (stmt != null){
					stmt.close();
				}
				if (conn != null){
					conn.close();
				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
		}
	}
	
}
