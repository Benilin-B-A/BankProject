package com.bank.db.queries;

public class AccountsTableQuery {

	public static String getBalance = "select BALANCE from Accounts where ACC_NUMBER = ?";

	public static String getPrimaryAccount = "select ACC_NUMBER from Accounts where CUSTOMER_ID = ? and IS_PRIMARY = 1";

	public static String validateAccount = "select exists(select * from Accounts where ACC_NUMBER = ?)";

	public static String getCustomerId = "select CUSTOMER_ID from Accounts where ACC_NUMBER = ?";

	public static String validateAccIFSC = "select exists(select Accounts.ACC_NUMBER,Branch.IFSC_CODE from Accounts inner "
			+ "join Branch on Branch.ID = Accounts.BRANCH_ID where ACC_NUMBER = ?  and IFSC_CODE = ?)";

	public static String validateCustomer = "select exists (select * from Accounts where CUSTOMER_ID = ?)";

	public static String changePrimaryAcc = "update Accounts set IS_PRIMARY = case when ACC_NUMBER = ? then"
			+ " 1 when ACC_NUMBER = ? then 0 else IS_PRIMARY end;";

	public static String getAccountDetails = "select * from Accounts where ACC_NUMBER = ?";

	public static String getCustomerAccounts = "select * from Accounts where CUSTOMER_ID = ? ";

	public static String addAccount = "insert into Accounts(CUSTOMER_ID,ACCOUNT_TYPE,BRANCH_ID,IS_PRIMARY) values (?,?,?,?)";

	public static String getAccountStatus = "select ACC_STATUS from Accounts where ACC_NUMBER = ?";

	public static String setAccountStatus = "update Accounts set ACC_STATUS = ? where ACC_NUMBER = ?";

	public static String moveToClosed = "insert into Closed_Accounts(CUSTOMER_ID,ACC_NUMBER,ACCOUNT_TYPE,BRANCH_ID,OPENED_ON ) "
			+ "select CUSTOMER_ID,ACC_NUMBER,ACCOUNT_TYPE,BRANCH_ID,OPENED_ON  from Accounts where ACC_NUMBER = ?";
	
	public static String closeAccount = "delete from Accounts where ACC_NUMBER = ?";

}
