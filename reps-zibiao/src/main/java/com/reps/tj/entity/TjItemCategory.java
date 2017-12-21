package com.reps.tj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.reps.core.orm.IdEntity;

/**
 * 统计指标项分类
 */
@Entity
@Table(name = "reps_tj_sys_item_category")
public class TjItemCategory extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4283726820082328509L;

	public TjItemCategory(){
		
	}
	
	public TjItemCategory(String id){
		 super.setId(id);
	}
	
	/** 上级分类ID */
	@Column(name = "parent_id", nullable = false, length = 32)
	private String parentId;

	/** 分类名称 */
	@Column(name = "name", nullable = false, length = 30)
	private String name;

	/** 备注 */
	@Column(name = "remark", length = 200)
	private String remark;
	
	/** 显示顺序 */
	@Column(name = "show_order")
	private Integer showOrder;
	

	/** 包含的指标 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY,
			mappedBy = "tjItemCategory", targetEntity = TjItem.class)
	
	private List<TjItem> items = new ArrayList<TjItem>();
	
	@Transient
	private String isItem;
	
	public List<TjItem> getItems() {
		return items;
	}

	public void setItems(List<TjItem> items) {
		this.items = items;
	}

	public String getIsItem() {
		return isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	
}
