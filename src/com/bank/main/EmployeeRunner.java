package com.bank.main;

import java.util.List;

import com.bank.custom.exceptions.BankingException;
import com.bank.custom.exceptions.InvalidInputException;
import com.bank.enums.Status;
import com.bank.pojo.Account;
import com.bank.pojo.Customer;
import com.bank.pojo.Employee;
import com.bank.pojo.Transaction;
import com.bank.services.EmployeeServices;

public class EmployeeRunner {

	public static void run(EmployeeServices user) {

		boolean continueService = true;

		while (continueService) {

			int userChoice = MainUtil.getInt("1) View Balance\n" + "2) Withdraw Money\n" + "3) Deposit Money\n"
					+ "4) Transfer Money\n" + "5) Change Password\n" + "6) View Statements\n" + "7) Get Account Details\n"
					+ "8) Add Customer\n" + "9) Add Account\n" + "10) Activate/Deactivate account\n"
					+ "11) Close Account\n" + "12) View Customer Details\n" + "13) View Employee Details\n" + "14) Logout\n"
					+ "Enter choice : ", 14);

			switch (userChoice) {

			case 1:
				try {
					long accNum = MainUtil.getLong("Enter account number : ");
					long bal = user.getBalance(accNum);
					System.out.println(bal);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 2:
				try {
					long accNum = MainUtil.getLong("Enter account number : ");
					long amount = MainUtil.getLong("Enter amount : ");
					user.withdraw(accNum, amount);
					System.out.println("Withdrawl successful");
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 3:

				try {
					long accNum = MainUtil.getLong("Enter account number : ");
					long amount = MainUtil.getLong("Enter amount : ");
					user.deposit(accNum, amount);
					System.out.println("Deposit successful");
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 4:

				try {
					long accNum = MainUtil.getLong("Enter sender's account number : ");
					int transferType = MainUtil.getInt("Is the money transfer within Bank ?  (1-Yes) (2-No): ", 2);
					boolean withinBank = false;
					if(transferType == 1) {
						withinBank = true;
					}
					Transaction trans = MainUtil.getTransactionObj(withinBank);
					user.transfer(trans, accNum, withinBank);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 5:
				try {
					String oldPass = MainUtil.getString("Enter Current Password : ");
					String newPass = MainUtil.getString("Enter new Password : ");
					user.changePassword(oldPass, newPass);
					System.out.println("Password changed successfully");
					break;
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 6:

				int statementChoice = MainUtil.getInt("1) Fetch statements using Account Number"
						+ "\n3) Fetch statements using Transaction ID" + "\nEnter Choice : ", 3);
				List<Transaction> list = null;
				switch (statementChoice) {
				case 1:
					try {
						long accNum = MainUtil.getLong("Enter account number: ");
						boolean continueStatementView = true;
						list = user.getAccountStatement(accNum);
						while (continueStatementView) {
							MainUtil.printList(list);
							MainUtil.filter(list);
							int pageChoice = MainUtil.getInt("Pages(5) : Enter page to view or enter 6 to exit : ", 6);
							if (pageChoice == 6) {
								continueStatementView = false;
							} else {
								list = user.getAccountStatement(accNum, pageChoice);
							}
						}
					} catch (BankingException exception) {
						exception.printStackTrace();
						System.out.println(exception.getMessage());
					}

					break;

				case 2:
					try {
						long transId = MainUtil.getLong("Enter TransactionId : ");
						list = user.getTransStatement(transId);
						MainUtil.printList(list);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
					break;
				}

			case 7:

				long accNum = MainUtil.getLong("Enter Account number : ");
				Account acc;
				try {
					acc = user.getAccount(accNum);
					System.out.println(acc);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 8:

				try {
					Customer cus = MainUtil.getCustomer();
					Account account = MainUtil.getAccount();
					long id = user.addCustomer(cus, account);
					System.out.println(id);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				} catch (InvalidInputException exception) {
					System.out.println(exception.getMessage());
				}

				break;

			case 9:

				try {
					long customerId = MainUtil.getLong("Enter Customer ID : ");
					Account account = MainUtil.getAccount();
					account.setUId(customerId);
					user.addAccount(account);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}

				break;

			case 10:

				int statusChoice = MainUtil.getInt("1) Activate account\n2) Deactivate account\nEnter choice : ", 2);

				long accoNum = MainUtil.getLong("Enter account number : ");

				if (statusChoice == 1) {
					try {
						user.setAccountStatus(accoNum, Status.ACTIVE);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
				} else if (statusChoice == 2) {
					try {
						user.setAccountStatus(accoNum, Status.INACTIVE);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
				}

				break;
			case 11:

				long accNumber = MainUtil.getLong("Enter account number : ");

				try {
					user.closeAcc(accNumber);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 12:

				long cusId = MainUtil.getLong("Enter customer Id : ");
				try {
					Customer cus = user.getCustomerDetails(cusId);
					System.out.println(cus);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 13:

				long empId = MainUtil.getLong("Enter customer Id : ");
				try {
					Employee emp = user.getEmployeeDetails(empId);
					System.out.println(emp);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 14:

				continueService = false;
			}

		}
	}
}
