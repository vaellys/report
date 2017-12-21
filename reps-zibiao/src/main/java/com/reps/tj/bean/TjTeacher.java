package com.reps.tj.bean;


public class TjTeacher implements java.io.Serializable {
	private static final long serialVersionUID = 5686990918199176575L;
	
	private String id;
	private String name;
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
	
}
