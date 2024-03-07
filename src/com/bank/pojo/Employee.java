package com.bank.pojo;

public class Employee extends User{
	
	private long branchId;
	private boolean isAdmin;
	
	public long getBranchID() {
		return branchId;
	}
	public void setBranchID(long branchID) {
		this.branchId = branchID;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public String toString() {
		return ("Name : " + this.name + "\nDOB : " + this.dOB + "\nGender : " + this.gender + "\nAddress : "
				+ this.eMail + "\nPhone : " + this.phone + "\nStatus : " + this.status + "\nBranch ID : "
				+ this.branchId + "\nAdmin : " + this.isAdmin);
	}
}
