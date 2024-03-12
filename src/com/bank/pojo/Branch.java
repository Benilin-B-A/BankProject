package com.bank.pojo;

public class Branch {
	
	private long id;
	private String branchName;
	private String address;
	private String iFSC;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIFSC() {
		return iFSC;
	}

	public void setIFSC(String iFSC) {
		this.iFSC = iFSC;
	}
}
