package com.reps.tj.entity;

public class DetailsIndicator {
	
	private String id;
	
	private String detailIndicId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDetailIndicId() {
		return detailIndicId;
	}

	public void setDetailIndicId(String detailIndicId) {
		this.detailIndicId = detailIndicId;
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
		DetailsIndicator other = (DetailsIndicator) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DetailsIndicator [id=" + id + ", detailIndicId=" + detailIndicId + "]";
	}
	
}
