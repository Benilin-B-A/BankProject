package com.bank.pojo;

public class User {

	protected long iD;
	protected String name;
	protected String dOB;
	protected long phone;
	protected String eMail;
	protected String gender;
	protected String userType;
	protected String status;
	protected String address;

	public long getID() {
		return iD;
	}
	public void setID(long iD) {
		this.iD = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getdOB() {
		return dOB;
	}
	public void setdOB(String dOB) {
		this.dOB = dOB;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phoneString) {
		this.phone = phoneString;
	}
	public String getMail() {
		return eMail;
	}
	public void setMail(String eMailString) {
		this.eMail = eMailString;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
