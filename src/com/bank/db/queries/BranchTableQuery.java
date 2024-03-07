package com.bank.db.queries;

public class BranchTableQuery {

	public static String addBranch = "insert into Branch(NAME,IFSC_CODE,ADDRESS) values(?,?,?)";
	
	public static String validateIFSC = "select exists(select * from Branch where IFSC_CODE = ?)";

	public static String validateBranch = "select exists(select * from Branch where ID = ?)";

}
