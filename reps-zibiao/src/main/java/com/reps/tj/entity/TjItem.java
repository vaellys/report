package com.reps.tj.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;

/**
 * 统计指标项
 */
@Entity
@Table(name = "reps_tj_sys_item")
public class TjItem extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5465179964305284679L;

	/** 所属分类ID */
	@Column(name = "category_id", nullable = false, length = 32, insertable=false, updatable=false)
	private String categoryId;
	
	/** 所属分类 */
	@JsonIgnore
    @ManyToOne(cascade = {})
    @JoinColumn(name = "category_id")
    private TjItemCategory tjItemCategory;

	/** 指标名称 */
	@Column(name = "name", nullable = false, length = 30)
	private String name;

	/** 中文名称 */
	@Column(name = "chinese_name", nullable = false, length = 30)
	private String chineseName;

	/** 字段类型：	varchar - 字符型（单字节）
	 *				nvarchar - 字符型（双字节）
	 *				byte - 超短整型
	 *				short - 短整型
	 *				int - 整型
	 *				long - 长整型
	 *				numeric - 数字型
	 *				datetime - 时间型
	 */
	@Column(name = "field_type", length = 10)
	private String fieldType;

	/** 字段长度 */
	@Column(name = "field_length", length = 5)
	private String fieldLength;

	/** 默认表达式 */
	@Column(name = "default_expression", length = 500)
	private String defaultExpression;

	/** 表达式类型：分为“SQL”，“运算”，“变量”，“常量” */
	@Column(name = "expression_type", length = 10)
	private String expressionType;
	
	/** 显示顺序 */
	@Column(name = "show_order")
	private Integer showOrder;
	
	/** 是否字典项*/
	@Column(name = "is_dictionary")
	private Short isDictionary;
	
	/** 引用字典*/
	@Column(name = "refer_dictionary", length = 50)
	private String referDictionary;

	public Short getIsDictionary() {
		return isDictionary;
	}

	public void setIsDictionary(Short isDictionary) {
		this.isDictionary = isDictionary;
	}

	public String getReferDictionary() {
		return referDictionary;
	}

	public void setReferDictionary(String referDictionary) {
		this.referDictionary = referDictionary;
	}

	public TjItemCategory getTjItemCategory() {
		return tjItemCategory;
	}

	public void setTjItemCategory(TjItemCategory tjItemCategory) {
		this.tjItemCategory = tjItemCategory;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(String fieldLength) {
		this.fieldLength = fieldLength;
	}

	public String getDefaultExpression() {
		return defaultExpression;
	}

	public void setDefaultExpression(String defaultExpression) {
		this.defaultExpression = defaultExpression;
	}

	public String getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(String expressionType) {
		this.expressionType = expressionType;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	
}
