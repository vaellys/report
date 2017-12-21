package com.reps.tj.util;

import java.util.ArrayList;
import java.util.List;

import com.reps.core.util.DBTableUtil;

/**
 * @Title FormTable
 * @Description 动态创建实体表定义
 * @Author Karlova
 * @Date 2014-6-18
 */
public class FormTable {

	private String name;
	
	private String tableName;
	
	private List<ColumnAttribute> columnAttrList;
	
	public void addColunms(List<ColumnAttribute> newColumnAttrList){
		if (newColumnAttrList == null){
			return;
		}
		
		if (this.columnAttrList == null){
			this.columnAttrList = new ArrayList<ColumnAttribute>();
		}
		
		for (ColumnAttribute c : newColumnAttrList){
			c.setName(DBTableUtil.getPojoPropertyName(c.getColumnName()));
			c.setColumnName(c.getColumnName());
			c.setColumnType(c.getColumnType());
			c.setLength(c.getLength());
			c.setNotNull(c.getNotNull());
			c.setUnique(c.getUnique());
			
			this.columnAttrList.add(c);
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
		this.name = DBTableUtil.getPojoPropertyName(tableName);
	}

	public List<ColumnAttribute> getColumnAttrList() {
		return columnAttrList;
	}

	public void setColumnAttrList(List<ColumnAttribute> columnAttrList) {
		this.columnAttrList = columnAttrList;
		
		if (this.columnAttrList!=null && this.columnAttrList.size()>0){
			for(ColumnAttribute col : this.columnAttrList){
				col.setName(DBTableUtil.getPojoPropertyName(col.getColumnName()));
			}
		}
	}

	/*
	 * 将数据库字段命名转为POJO属性的命名规范，全部小写，遇下划线下一个字母大写
	 * 如　login_name 变为 loginName
	 */
//	private String getPojoPropertyName(String nature) {
//		String s = nature.toLowerCase();
//		char[] cs = s.toCharArray();
//		String pojo = "";
//		for (int i = 0; i < cs.length; i++) {
//			String ch = String.valueOf(cs[i]);
//			if (ch.equalsIgnoreCase("_")) {
//				pojo += String.valueOf(cs[i+1]).toUpperCase();
//				i++;				
//			}else{
//				pojo += ch;
//			}
//		}
//		return pojo;
//	}
	

}
