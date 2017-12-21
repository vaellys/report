package com.reps.tj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 统计指标主题名称定义表 对应数据库表名 reps_tj_zbztmcdyb
 * 
 * @author qianguobing
 * @date 2017年7月27日 上午10:41:07
 */
@Entity
@Table(name = "reps_tj_zbztmcdyb")
public class TjZbztmcdyb implements Serializable {
	private static final long serialVersionUID = 4283726820082328509L;

	public TjZbztmcdyb() {

	}

	@Id
	@GeneratedValue(generator = "sys-uuid")
	@GenericGenerator(name = "sys-uuid", strategy = "uuid")
	@Column(name = "zbztid", length = 32)
	private String zbztId;

	/** 指标主题名称 */
	@Column(name = "zbztmc", nullable = false, length = 200)
	private String zbztmc;

	/** 指标主题说明 */
	@Column(name = "zbztsm", length = 200)
	private String zbztsm;

	/** 权限类型 */
	@Column(name = "qxlx")
	private Short qxlx;

	/** 所属单位 */
	@Column(name = "ssdw", length = 200)
	private String ssdw;

	/** 指标主题包含指标 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "tjZbztmcdyb", targetEntity = TjZdyzbdyb.class)

	private List<TjZdyzbdyb> indicators = new ArrayList<TjZdyzbdyb>();

	/** 所属命名空间 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "namespace")
	private TjNamespace tjNamespace;

	public String getZbztId() {
		return zbztId;
	}

	public void setZbztId(String zbztId) {
		this.zbztId = zbztId;
	}

	public TjNamespace getTjNamespace() {
		return tjNamespace;
	}

	public void setTjNamespace(TjNamespace tjNamespace) {
		this.tjNamespace = tjNamespace;
	}

	public List<TjZdyzbdyb> getIndicators() {
		return indicators;
	}

	public void setIndicators(List<TjZdyzbdyb> indicators) {
		this.indicators = indicators;
	}

	public String getZbztmc() {
		return zbztmc;
	}

	public void setZbztmc(String zbztmc) {
		this.zbztmc = zbztmc;
	}

	public String getZbztsm() {
		return zbztsm;
	}

	public void setZbztsm(String zbztsm) {
		this.zbztsm = zbztsm;
	}

	public Short getQxlx() {
		return qxlx;
	}

	public void setQxlx(Short qxlx) {
		this.qxlx = qxlx;
	}

	public String getSsdw() {
		return ssdw;
	}

	public void setSsdw(String ssdw) {
		this.ssdw = ssdw;
	}
	
}
