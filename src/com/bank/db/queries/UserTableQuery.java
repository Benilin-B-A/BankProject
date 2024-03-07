package com.bank.db.queries;

public class UserTableQuery {
	
	public static String getPassword = "select PASSWORD from User where ID = ?";
	
	public static String validateUser = "select exists(select * from User where ID = ?)";

	public static String getAttempts = "select FAILED_ATTEMPTS from Login_Attempts where USER_ID = ?";

	public static String setAttempts = "update Login_Attempts set FAILED_ATTEMPTS = ? where USER_ID = ?";

	public static String getStatus = "select STATUS from User where ID = ?";

	public static String setStatus = "update User set STATUS = ? where ID = ?";

	public static String getUserType = "select USER_TYPE from User where ID = ?";
	
	public static String changePassword = "update User set PASSWORD = ? where ID = ?";

	public static String getLastInsertId = "select last_insert_ID()";

	public static String addUser = "insert into User(NAME,DOB,PHONE,EMAIL,GENDER,USER_TYPE,ADDRESS,PASSWORD) "
			+ "values(?,?,?,?,?,?,?,?)";
}