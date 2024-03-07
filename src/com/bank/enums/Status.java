package com.bank.enums;

public enum Status {
	ACTIVE("Active"),
	INACTIVE("Inactive"),
	BLOCKED("Blocked");
	
private String state;
	
	Status(String state){
		this.state = state;
	}
	
	public String getState() {
		return this.state;
	}
}
