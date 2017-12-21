package com.reps.tj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 统计指标命名空间 对应数据库表名 reps_tj_namespace
 * 
 * @author qianguobing
 * @date 2017年7月27日 上午10:41:07
 */
@Entity
@Table(name = "reps_tj_namespace")
public class TjNamespace implements Serializable {
	private static final long serialVersionUID = 4283726820082328509L;

	public TjNamespace() {

	}

	/** 命名空间名称 */
	@Id
	@Column(name = "namespace", nullable = false, length = 200)
	private String namespace;

	/** 描述 */
	@Column(name = "description", length = 100)
	private String description;

	/** 显示标题 */
	@Column(name = "displaytitle", length = 50)
	private String displayTitle;

	/** 显示顺序 */
	@Column(name = "displayorder")
	private Integer displayOrder;

	/** 命名空间包指标主题 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "tjNamespace", targetEntity = TjZbztmcdyb.class)

	private List<TjZbztmcdyb> topics = new ArrayList<TjZbztmcdyb>();

	public List<TjZbztmcdyb> getTopics() {
		return topics;
	}

	public void setTopics(List<TjZbztmcdyb> topics) {
		this.topics = topics;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	
}
