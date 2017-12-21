package com.reps.tj.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public enum Meta {
	
	DATABASE_TYPE_LIST("数据库类型", "databaseTypeList"), OUTPUT_FIELD_DEFINED("输出字段定义", "outputFieldDefined");

	private String code;
	private String text;

	private Meta(String text, String code) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public static final List<String> META_CODES = new ArrayList<>();
	  
    static {  
        for (Meta m : EnumSet.allOf(Meta.class)) {  
        	META_CODES.add(m.code);
        }  
    }
	
}