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
 * 统计自定义指标定义表 对应数据库表名 reps_tj_zdyzbdyb
 * 
 * @author qianguobing
 * @date 2017年7月27日 上午11:06:04
 */
@Entity
@Table(name = "reps_tj_zdyzbdyb")
public class TjZdyzbdyb extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5465179964305284679L;

	/** 指标主题ID */
	@Column(name = "zbztid", nullable = false, length = 100, insertable = false, updatable = false)
	private String zbztId;

	/** 所属分类 */
	@JsonIgnore
	@ManyToOne(cascade = {})
	@JoinColumn(name = "zbztid")
	private TjZbztmcdyb tjZbztmcdyb;

	/** 指标名称 */
	@Column(name = "zbmc", length = 200)
	private String zbmc;

	/** 指标算法 */
	@Column(name = "zbsf")
	private String zbsf;

	/** 指标元数据 */
	@Column(name = "zbmeta", length = 4000)
	private String zbmeta;

	/** 上级指标父ID */
	@Column(name = "fid", length = 50)
	private String fId;

	/** 指标说明 */
	@Column(name = "zbsm", length = 4000)
	private String zbsm;

	/** 显示顺序 */
	@Column(name = "xsxh")
	private Integer xsxh;

	/** 指标算法MSSQL */
	@Column(name = "zbsf_mssql", length = 4000)
	private String zbsfMssql;

	/** Mysql专用算法 */
	@Column(name = "zbsf_mysql", length = 4000)
	private String zbsfMysql;

	/** Oracle专用算法 */
	@Column(name = "zbsf_oracle", length = 4000)
	private String zbsfOracle;
	
	/** Mongodb专用算法 */
	@Column(name = "zbsf_mongodb", length = 4000)
	private String zbsfMongodb;

	public TjZbztmcdyb getTjZbztmcdyb() {
		return tjZbztmcdyb;
	}

	public void setTjZbztmcdyb(TjZbztmcdyb tjZbztmcdyb) {
		this.tjZbztmcdyb = tjZbztmcdyb;
	}

	public String getZbztId() {
		return zbztId;
	}

	public void setZbztId(String zbztId) {
		this.zbztId = zbztId;
	}

	public String getfId() {
		return fId;
	}

	public void setfId(String fId) {
		this.fId = fId;
	}

	public String getZbmc() {
		return zbmc;
	}

	public void setZbmc(String zbmc) {
		this.zbmc = zbmc;
	}

	public String getZbsf() {
		return zbsf;
	}

	public void setZbsf(String zbsf) {
		this.zbsf = zbsf;
	}

	public String getZbmeta() {
		return zbmeta;
	}

	public void setZbmeta(String zbmeta) {
		this.zbmeta = zbmeta;
	}

	public String getZbsm() {
		return zbsm;
	}

	public void setZbsm(String zbsm) {
		this.zbsm = zbsm;
	}

	public Integer getXsxh() {
		return xsxh;
	}

	public void setXsxh(Integer xsxh) {
		this.xsxh = xsxh;
	}

	public String getZbsfMssql() {
		return zbsfMssql;
	}

	public void setZbsfMssql(String zbsfMssql) {
		this.zbsfMssql = zbsfMssql;
	}

	public String getZbsfMysql() {
		return zbsfMysql;
	}

	public void setZbsfMysql(String zbsfMysql) {
		this.zbsfMysql = zbsfMysql;
	}

	public String getZbsfOracle() {
		return zbsfOracle;
	}

	public void setZbsfOracle(String zbsfOracle) {
		this.zbsfOracle = zbsfOracle;
	}

	public String getZbsfMongodb() {
		return zbsfMongodb;
	}

	public void setZbsfMongodb(String zbsfMongodb) {
		this.zbsfMongodb = zbsfMongodb;
	}
	
}
