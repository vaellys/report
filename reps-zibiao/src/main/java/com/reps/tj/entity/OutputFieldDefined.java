package com.reps.tj.entity;

public class OutputFieldDefined {
	
	private String id;
	
	private String resultFieldName;
	
	private String displayName;
	
	private String fieldType;
	
	private Integer showOrder;
	
	private Integer showWidth;
	
	/** 对齐方式 */
	private String  alignment;
	
	/** 可否隐藏 */
	private String isHide;
	
	private String showFormat;

	public String getResultFieldName() {
		return resultFieldName;
	}

	public void setResultFieldName(String resultFieldName) {
		this.resultFieldName = resultFieldName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Integer getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}

	public Integer getShowWidth() {
		return showWidth;
	}

	public void setShowWidth(Integer showWidth) {
		this.showWidth = showWidth;
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public String getIsHide() {
		return isHide;
	}

	public void setIsHide(String isHide) {
		this.isHide = isHide;
	}

	public String getShowFormat() {
		return showFormat;
	}

	public void setShowFormat(String showFormat) {
		this.showFormat = showFormat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OutputFieldDefined other = (OutputFieldDefined) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OutputFieldDefined [id=" + id + ", resultFieldName=" + resultFieldName + ", displayName=" + displayName + ", fieldType=" + fieldType + ", showOrder=" + showOrder + ", showWidth="
				+ showWidth + ", alignment=" + alignment + ", isHide=" + isHide + ", showFormat=" + showFormat + "]";
	}

	
}
