package com.bank.pojo;

public class Customer extends User {
	private long aadharNum;

	private String panNum;
	
	private int noOfAcc;

	public long getAadharNum() {
		return aadharNum;
	}

	public void setAadharNum(long aadharNum) {
		this.aadharNum = aadharNum;
	}

	public String getPanNum() {
		return panNum;
	}

	public void setPanNum(String panNum) {
		this.panNum = panNum;
	}
	


	public String toString() {
		return ("Name : " + this.name + "\nDOB : " + this.dOB + "\nGender : " + this.gender + "\nAddress : "
				+ this.eMail + "\nPhone : " + this.phone + "\nStatus : " + this.status + "\nAadhar Num : "
				+ this.aadharNum + "\nPan Num : " + this.panNum + "\nNoOfAcc : " + this.noOfAcc);
	}

	public void setNoOfAcc(int noOfAcc) {
		this.noOfAcc = noOfAcc;
	}
	
	public int getNoOfAcc() {
		return this.noOfAcc;
	}
}
