package com.bank.pojo;

import com.bank.enums.UserLevel;
import com.bank.enums.Status;

public abstract class User {

	protected long iD;
	protected String name;
	protected String dOB;
	protected long phone;
	protected String eMail;
	protected String gender;
	protected Status status;
	protected String address;
	protected UserLevel userLevel;
	
	
	public String toString() {
		return ("Name : " + this.name + "\nDOB : " + this.dOB + "\nGender : " + this.gender + "\nAddress : "
				+ this.eMail + "\nPhone : " + this.phone + "\nStatus : " + this.status + "\nAddress : ");
	}
	
	public void setLevel(UserLevel level) {
		userLevel = level;
	}
	
	public UserLevel getLevel() {
		return this.userLevel;
	}
	
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
