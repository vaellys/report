package com.reps.tj.bean;


public class TjSchool implements java.io.Serializable {
	private static final long serialVersionUID = 5686990918199176575L;
	
	private String id;
	private String name;
	private String district;
	private String orgType;
	private String schoolBb;
	private String townType;
	private String classesCount;
	private String teacherManCount;
	private String teacherWomanCount;
	private String studentBoyCount;
	private String studentGirlCount;
	private String teacherStudentRatio;
	
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
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getSchoolBb() {
		return schoolBb;
	}
	public void setSchoolBb(String schoolBb) {
		this.schoolBb = schoolBb;
	}
	public String getTownType() {
		return townType;
	}
	public void setTownType(String townType) {
		this.townType = townType;
	}
	public String getClassesCount() {
		return classesCount;
	}
	public void setClassesCount(String classesCount) {
		this.classesCount = classesCount;
	}
	public String getTeacherManCount() {
		return teacherManCount;
	}
	public void setTeacherManCount(String teacherManCount) {
		this.teacherManCount = teacherManCount;
	}
	public String getTeacherWomanCount() {
		return teacherWomanCount;
	}
	public void setTeacherWomanCount(String teacherWomanCount) {
		this.teacherWomanCount = teacherWomanCount;
	}
	public String getStudentBoyCount() {
		return studentBoyCount;
	}
	public void setStudentBoyCount(String studentBoyCount) {
		this.studentBoyCount = studentBoyCount;
	}
	public String getStudentGirlCount() {
		return studentGirlCount;
	}
	public void setStudentGirlCount(String studentGirlCount) {
		this.studentGirlCount = studentGirlCount;
	}
	public String getTeacherStudentRatio() {
		return teacherStudentRatio;
	}
	public void setTeacherStudentRatio(String teacherStudentRatio) {
		this.teacherStudentRatio = teacherStudentRatio;
	}
	
}
