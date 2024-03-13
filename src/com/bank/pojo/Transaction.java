package com.bank.pojo;

import com.bank.util.TimeUtil;

public class Transaction {

	private long accountNumber;
	private long transactionAccountNumber;
	private String iFSC;
	private long amount;
	private String description;
	private long customerId;

	public long getAccNumber() {
		return accountNumber;
	}

	public void setAccNumber(long accNumber) {
		this.accountNumber = accNumber;
	}

	public long getTransAccNum() {
		return transactionAccountNumber;
	}

	public void setTransAccNum(long transAccNum) {
		this.transactionAccountNumber = transAccNum;
	}

	public String getiFSC() {
		return iFSC;
	}

	public void setiFSC(String iFSC) {
		this.iFSC = iFSC;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descriptionString) {
		this.description = descriptionString;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long userId) {
		this.customerId = userId;
	}

	public String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private long transactionId;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	private long openingBal;

	public long getOpeningBal() {
		return openingBal;
	}

	public void setOpeningBal(long openingBal) {
		this.openingBal = openingBal;
	}

	private long closingBal;

	public long getClosingBal() {
		return closingBal;
	}

	public void setClosingBal(long openingBal) {
		this.closingBal = openingBal;
	}

	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long timestamp) {
		this.time = timestamp;
	}

//	TRANSACTION_ID,AMOUNT,TYPE,TIME,OPENING_BAL,CLOSING_BAL,DESCRIPTION
	public String toString() {
		return ("TransactionId : " + this.transactionId + " | Amount : " + this.amount + " | Type : " + this.type
				+ " | Time : " + TimeUtil.getDateTime(this.time) + " | Opening Balance : " + this.openingBal
				+ " | Closing Balance : " + this.closingBal + " | Description : " + this.description);
	}
}
