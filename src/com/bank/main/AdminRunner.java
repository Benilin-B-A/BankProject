package com.bank.main;

import java.util.List;
import java.util.Map;

import com.bank.custom.exceptions.BankingException;
import com.bank.custom.exceptions.InvalidInputException;
import com.bank.enums.Status;
import com.bank.pojo.Account;
import com.bank.pojo.Branch;
import com.bank.pojo.Customer;
import com.bank.pojo.Employee;
import com.bank.pojo.Transaction;
import com.bank.pojo.User;
import com.bank.services.AdminServices;

public class AdminRunner {

	public static void run(AdminServices user) {

		boolean continueService = true;

		while (continueService) {

			int userChoice = MainUtil.getInt("1) View Balance\n" + "2) Withdraw Money\n" + "3) Deposit Money\n"
					+ "4) Transfer Money\n" + "5) Change Password\n" + "6) View Statements\n" + "7) View Account\n"
					+ "8) Add Employee\n" + "9) Add Customer\n" + "10) Add Account\n" + "11) Add Branch\n"
					+ "12) Activate/Deactivate User\n" + "13) Activate/Deactivate Account\n" + "14) Close Account\n"
					+ "15) View Customer Details\n" + "16) View Employee Details\n" + "17) Logout\n"
					+ "Enter choice : ", 17);

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
					Transaction trans = MainUtil.getTransactionObj();
					user.transfer(trans, accNum);
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
				break;

			case 7:
				int accChoice = MainUtil.getInt(
						"1) View account by account number\n" + "2) View account by customer Id\nEnter choice : ", 2);
				switch (accChoice) {

				case 1:
					long accNum = MainUtil.getLong("Enter Account number : ");
					Account acc;
					try {
						acc = user.getAccount(accNum);
						System.out.println(acc);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
					break;

				case 2:
					long customerId = MainUtil.getLong("Enter customer Id : ");
					Map<Long, Account> map;
					try {
						map = user.getAccounts(customerId);
						System.out.println(map);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
					break;
				}

				break;

			case 8:
				try {
					Employee emp = getEmployee();
					long id = user.addEmployee(emp);
					System.out.println("New Employee ID : " + id);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				} catch (InvalidInputException excep) {
					System.out.println(excep.getMessage());
				}

				break;

			case 9:
				try {
					Customer cus = MainUtil.getCustomer();
					Account acc = MainUtil.getAccount();
					long branchId = MainUtil.getLong("Enter BranchID of the account : ");
					long id = user.addCustomer(cus, acc, branchId);
					System.out.println(id);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}

				break;

			case 10:

				try {
					Account account = MainUtil.getAccount();
					long customerId = MainUtil.getLong("Enter customer Id : ");
					account.setUId(customerId);
					long branchId = MainUtil.getLong("Enter BranchID of the Account : ");
					account.setBranchId(branchId);
					user.addAccount(account, branchId);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 11:

				Branch branch = getBranch();
				try {
					user.addBranch(branch);
				} catch (BankingException | InvalidInputException exception) {
					System.out.println(exception.getMessage());
				}

				break;

			case 12:

				int stateChoice = MainUtil.getInt("1) Activate user\n2) Deactivate user\nEnter choice", 2);

				long customerId = MainUtil.getLong("Enter customer Id : ");

				if (stateChoice == 1) {
					try {
						user.setStatus(customerId, Status.ACTIVE);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
				} else if (stateChoice == 2) {
					try {
						user.setStatus(customerId, Status.INACTIVE);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
				}

				break;

			case 13:

				int statusChoice = MainUtil.getInt("1) Activate account\n2) Deactivate account\nEnter choice : ", 2);

				long accNum = MainUtil.getLong("Enter account number : ");

				if (statusChoice == 1) {
					try {
						user.setAccountStatus(accNum, Status.ACTIVE);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
				} else if (statusChoice == 2) {
					try {
						user.setAccountStatus(accNum, Status.INACTIVE);
					} catch (BankingException exception) {
						System.out.println(exception.getMessage());
					}
				}

				break;

			case 14:

				long accNumber = MainUtil.getLong("Enter account number : ");

				try {
					user.closeAcc(accNumber);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 15:

				long cusId = MainUtil.getLong("Enter customer Id : ");
				try {
					Customer cus = user.getCustomerDetails(cusId);
					System.out.println(cus);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 16:

				long empId = MainUtil.getLong("Enter Employee Id : ");
				try {
					Employee emp = user.getEmployeeDetails(empId);
					System.out.println(emp);
				} catch (BankingException exception) {
					exception.printStackTrace();
					System.out.println(exception.getMessage());
				}
				break;

			case 17:

				continueService = false;
			}

		}
	}

	public static Employee getEmployee() {
		User user = new Employee();
		MainUtil.getUserDetails(user);
		long branchID = MainUtil.getLong("Enter Branch Id : ");
		((Employee) user).setBranchID(branchID);
		int admin = MainUtil.getInt("Does the Employee have admin privileges (1-Yes) (2-No): ", 2);
		boolean isAdmin = false;
		if (admin == 1) {
			isAdmin = true;
		}
		((Employee) user).setAdmin(isAdmin);
		return (Employee) user;
	}
	
	public static Branch getBranch() {
		Branch branch = new Branch();
		String iFSC = MainUtil.getString("Enter IFSC code : ");
		branch.setIFSC(iFSC);
		String name = MainUtil.getString("Enter branch name : ");
		branch.setBranchName(name);
		String address = MainUtil.getString("Enter address : ");
		branch.setAddress(address);
		return branch;
	}
}
