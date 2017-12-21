package com.reps.dc.server;

public class StatData {
	private String agent;
	private String timeName;
	private Long value;

	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getTimeName() {
		return this.timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	public Long getValue() {
		return this.value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
}
