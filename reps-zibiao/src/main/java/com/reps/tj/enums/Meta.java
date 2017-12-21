package com.reps.tj.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * 元数据类型
 * 
 * @author qianguobing
 * @date 2017年12月21日 下午4:02:52
 */
public enum Meta {

	DATABASE_TYPE_LIST("数据库类型", "databaseTypeList"),

	PARAM_DEFINED_LIST("参数定义", "paramDefinedList"), 
	
	OUTPUT_FIELD_DEFINED("输出字段定义", "outputFieldDefined"),
	
	STATISTICS_ITEM_CATEGORY("引用统计项目分类", "statisticsItemCategory");

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