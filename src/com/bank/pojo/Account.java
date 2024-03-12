package com.bank.pojo;

import com.bank.enums.AccountType;
import com.bank.enums.Status;
import com.bank.util.TimeUtil;

public class Account {

	private long uId;
	private long accNum;
	private long balance;
	private AccountType type;
	private Status status;
	private long branchId;
	private long openedOn;
	private boolean primary;

	public long getUId() {
		return uId;
	}

	public void setUId(long uId) {
		this.uId = uId;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

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

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getOpenedOn() {
		return openedOn;
	}

	public void setOpenedOn(long timestamp) {
		this.openedOn = timestamp;
	}

	public String toString() {
		return ("Customer ID : " + this.uId + " | Account Number : " + this.accNum + " | Balance : " + this.balance
				+ " | Branch ID : " + this.branchId + " | Opened On : " + TimeUtil.getDateTime(this.openedOn)
				+ " | Status : " + this.status + " | Type : " + this.type);
	}
}
