package com.reps.tj.entity;

public class DatabaseType {
	
	private String id;
	
	private String dbType;
	
	private String field;
	
	public DatabaseType() {
	}

	public DatabaseType(String dbType, String field) {
		super();
		this.dbType = dbType;
		this.field = field;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DatabaseType [id=" + id + ", dbType=" + dbType + ", field=" + field + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbType == null) ? 0 : dbType.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
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
		DatabaseType other = (DatabaseType) obj;
		if (dbType == null) {
			if (other.dbType != null)
				return false;
		} else if (!dbType.equals(other.dbType))
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
