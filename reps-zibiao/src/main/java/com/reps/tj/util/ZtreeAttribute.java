package com.reps.tj.util;

/**
 * ztree数据属性定义 
 * @author qianguobing
 * @date 2017年7月28日 下午2:27:46
 *
 */
public class ZtreeAttribute {
	
	private String id;
	
	private String name;
	
	private String parentId;
	
	private String type; // 1.命名空间 2.主题 3.指标

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
