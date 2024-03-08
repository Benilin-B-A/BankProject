package com.bank.db.queries;

public class EmployeeTableQuery {
	
	public static String getAdminAccess = "select ADMIN_ACCESS from Employee where ID = ?";

	public static String getBranchId = "select BRANCH_ID from Employee where ID = ?";

	public static String addEmployee = "insert into Employee(ID,BRANCH_ID,ADMIN_ACCESS) values(?,?,?)";
	
	public static String getEmployeeProfile = "select NAME,DOB, GENDER, ADDRESS, EMAIL, PHONE, STATUS, BRANCH_ID, ADMIN_ACCESS "
			+ "from Employee inner join User on User.ID = Employee.ID where Employee.ID = ?";

	public static String validateEmployeePresence = "select exists(select * from Employee where ID = ?)";
}
