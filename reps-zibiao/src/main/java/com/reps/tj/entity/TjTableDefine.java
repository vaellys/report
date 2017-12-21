package com.reps.tj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import com.reps.core.orm.IdEntity;

/**
 * 统计表结构定义
 * @author Karlova
 */
@Entity
@Table(name = "reps_tj_sys_table_define")
public class TjTableDefine extends IdEntity implements Serializable {
	private static final long serialVersionUID = -6359325989221039391L;

	public TjTableDefine(){
		
	}
	
	public TjTableDefine(String id){
		 super.setId(id);
	}
	
	/** 物理表名 */
	@Column(name = "name", nullable = false, length = 30)
	private String name;

	/** 中文名称 */
	@Column(name = "chinese_name", nullable = false, length = 30)
	private String chineseName;

	/** 是否基础表 */
	@Column(name = "is_basic")
	private Short isBasic;

	/** 类型 */
	/* 定义:
	  清单报表 - 最简单的一种陈列方式，主要用于列举数据，如销售清单、客户清单、商品清单等；
	  固定/套打报表 - 主要用于制式报表的打印，报表的整体格式、每个数据的打印位置都有严格要求，打印时只需将数据打印到指定的位置，常见的有：财务发票打印、发货清单打印、提货单打印等；
	  分栏报表 - 通过分栏可充分利用报表绘制区域，该类型的报表也可细分为横向分栏和纵向分栏两种结构；
	  分组报表 - 这类报表对数据进行分类显示，便于实现数据的汇总，可分为单条件分组和嵌套分组；
	  交叉报表 - 按照行、列两个维度分类汇总数据的一种报表结构，行、列均支持单条件分组和嵌套分组；
	  并排报表 - 将报表按照纵向分为多个不同的布局的区域，每个区域可设置单独的数据源；
	  主从报表 - 主要用于显示一对多结构的数据；
	  图表报表 - 将数据以图表的方式呈现，可更好的分析数据之间的关系，数据的发展趋势；
	  交互式报表 - 主要满足用户按需分析报表数据的需求，通常包含向下钻取、贯穿钻取、数据过滤、数据排序等方式；
	*/
	@Column(name = "category", nullable = false, length = 10)
	private String category;

	/** 统计优先级 */
	@Column
	private Integer priority;
	
	/** 统计方法，V：纵向等价，H-横向非等价 */
	@Column(nullable = false, length = 10)
	private String method;

	/** 是否启用 */
	@Column
	private Short enabled;
	
	/** 包含的指标项 */
	@OneToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "tjTableDefine", targetEntity = TjTableItem.class)
	@OrderBy(clause="statOrder asc")
	private List<TjTableItem> items;
	
	@Transient
	private String tjTableDefineTypeId;
	
	/** 字段名，中文名称 */
	@Transient
	private List<Map<String, String>> itemChineseNameList;
	
	/**
	 * 读取字段名及其中午名称的对照表
	 * @param includeItems 需要的字段名
	 * @param excludeItems 不需要的字段名
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getItemChineseNameList(String[] includeItems, String[] excludeItems) {
		itemChineseNameList = new ArrayList<Map<String, String>>();
		if (items != null && !items.isEmpty()){
			boolean needed;
			List<String> includeList = includeItems==null || includeItems.length<1 ? new ArrayList<String>() : Arrays.asList(includeItems);
			List<String> excludeList = excludeItems==null || excludeItems.length<1 ? new ArrayList<String>() : Arrays.asList(excludeItems);
			for (TjTableItem item : items){
				needed = true;
				if (includeList.size() > 0){
					needed = includeList.contains(item.getItemName()) ? true : false;
				}
				if (excludeList.size() > 0){
					needed = excludeList.contains(item.getItemName()) ? false : true;
				}
				if (needed){
					Map<String, String> map = new HashMap<String, String>();
					map.put(item.getItemName(), item.getItemChineseName());
					itemChineseNameList.add(map);
				}
			}
		}
		
		return itemChineseNameList;
	}

	public String getTjTableDefineTypeId() {
		return tjTableDefineTypeId;
	}

	public void setTjTableDefineTypeId(String tjTableDefineTypeId) {
		this.tjTableDefineTypeId = tjTableDefineTypeId;
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

	public Short getIsBasic() {
		return isBasic;
	}

	public void setIsBasic(Short isBasic) {
		this.isBasic = isBasic;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Short getEnabled() {
		return enabled;
	}

	public void setEnabled(Short enabled) {
		this.enabled = enabled;
	}

	public List<TjTableItem> getItems() {
		return items;
	}

	public void setItems(List<TjTableItem> items) {
		this.items = items;
	}


}
