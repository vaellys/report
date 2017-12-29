package com.reps.tj.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	
	STATISTICS_ITEM_CATEGORY("引用统计项目分类", "statisticsItemCategory"),
	
	DETAILS_INDICATOR("关联明细指标", "detailsIndicator");

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

	/**
	 * 指标元数据session存储key
	 */
	public static final List<String> META_CODES = new ArrayList<>();
	
	/**
	 * 复制指标元数据session存储key
	 */
	public static final List<String> COPY_META_CODES = new ArrayList<>();
	
	/**
	 * 元数据key->value
	 */
	public static final Map<String, String> META_MAPS = new LinkedHashMap<>();
	
	/**
	 * 复制元数据 存储 key 连接符
	 */
	public static final String META_JOINER = "_";

	static {
		for (Meta m : EnumSet.allOf(Meta.class)) {
			META_CODES.add(m.code);
			COPY_META_CODES.add(META_JOINER + m.code);
			META_MAPS.put(m.getCode(), m.getText());
		}
	}

}