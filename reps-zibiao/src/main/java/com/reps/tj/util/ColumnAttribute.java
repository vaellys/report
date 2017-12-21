package com.reps.tj.util;

/**
 * @Title ColumnAttribute
 * @Description 实体表中的字段属性定义
 * @Author Karlova
 * @Date 2014-6-18
 */
public class ColumnAttribute {
	
	private String name;
	
	private String columnName;
	
	private String columnType;
	
	private int length;
	
	private String notNull = "false";
	
	private String unique = "false";

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getNotNull() {
		return notNull;
	}

	public void setNotNull(String notNull) {
		this.notNull = notNull;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}
	

}
