package com.reps.tj.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reps.core.orm.IdEntity;

/**
 * 统计表指标项
 * @author Karlova
 */
@Entity
@Table(name = "reps_tj_sys_table_item")
public class TjTableItem extends IdEntity implements Serializable {
	private static final long serialVersionUID = 7084305658289802548L;

	/** 关联统计表结构定义 */
	@JsonIgnore
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "table_id", nullable = false)
	private TjTableDefine tjTableDefine;

	/** 指标名称 */
	@Column(name = "item_name", nullable = false, length = 30)
	private String itemName;

	/** 中文名称 */
	@Column(name = "item_chinese_name", nullable = false, length = 30)
	private String itemChineseName;

	/** 字段类型 */
	@Column(name = "field_type", length = 10)
	private String fieldType;

	/** 字段长度 */
	@Column(name = "field_length", length = 5)
	private String fieldLength;

	/** 统计表达式 */
	@Column(name = "expression", length = 500)
	private String expression;

	/** 表达式类型 */
	@Column(name = "expression_type", length = 10)
	private String expressionType;

	/** 条件依赖 */
	@Column(name = "dependency", length = 200)
	private String dependency;
	
	/** 指标取值项，当有多个输出项时，要指定本指标项取值所对应的字段 */
	@Column(name = "valued_column", length = 80)
	private String valuedColumn;
	
	/** 数据唯一批次约束项 */
	@Column(name = "batch_key")
	private Short batchKey;
	
	/** 是否是临时项 */
	@Column(name = "is_temporary", nullable = false)
	private Short isTemporary;//1表示 是，0表示 不是。

	/** 统计顺序 */
	@Column(name = "stat_order")
	private Short statOrder;
	
	/** 字典项 */
	@Transient
	private Short isDictionary;//1表示 是，0表示 不是。
	
	@Transient
	private String referDictionary;
	
	@Transient
	private String itemValue;

	
	public TjTableDefine getTjTableDefine() {
		return tjTableDefine;
	}

	public void setTjTableDefine(TjTableDefine tjTableDefine) {
		this.tjTableDefine = tjTableDefine;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemChineseName() {
		return itemChineseName;
	}

	public void setItemChineseName(String itemChineseName) {
		this.itemChineseName = itemChineseName;
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

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpressionType() {
		return expressionType;
	}

	public void setExpressionType(String expressionType) {
		this.expressionType = expressionType;
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	public String getValuedColumn() {
		return valuedColumn;
	}

	public void setValuedColumn(String valuedColumn) {
		this.valuedColumn = valuedColumn;
	}

	public Short getBatchKey() {
		return batchKey;
	}

	public void setBatchKey(Short batchKey) {
		this.batchKey = batchKey;
	}

	public Short getIsTemporary() {
		return isTemporary;
	}

	public void setIsTemporary(Short isTemporary) {
		this.isTemporary = isTemporary;
	}

	public Short getStatOrder() {
		return statOrder;
	}

	public void setStatOrder(Short statOrder) {
		this.statOrder = statOrder;
	}

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

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

}
