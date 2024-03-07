package com.bank.pojo;

public class Account {
	
	private long uId;
	public long getUId() {
		return uId;
	}
	public void setUId(long uId) {
		this.uId = uId;
	}
	private long accNum;
	private long balance;
	private String type;
	private String status;
	private long branchId;
	private java.sql.Timestamp openedOn;

	public boolean isPrimary() {
		return isPrimary;
	}
	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	private boolean isPrimary;
	
	public long getAccNum() {
		return accNum;
	}
	public void setAccNum(long accNum) {
		this.accNum = accNum;
	}
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
	public java.sql.Timestamp getOpenedOn() {
		return openedOn;
	}
	public void setOpenedOn(java.sql.Timestamp timestamp) {
		this.openedOn = timestamp;
	}
	
	public String toString(){
		return ("Customer ID : " + this.uId + " | Account Number : " + this.accNum + " | Balance : " + this.balance + " | Branch ID : " 
	+ this.branchId + " | Opened On : " + this.openedOn + " | Status : " + this.status + " | Type : " + this.type);
	}
}
