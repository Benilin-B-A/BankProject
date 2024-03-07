package com.bank.main;

import java.util.List;

import com.bank.custom.exceptions.BankingException;
import com.bank.custom.exceptions.InvalidInputException;
import com.bank.custom.exceptions.PinNotSetException;
import com.bank.pojo.Account;
import com.bank.pojo.Transaction;
import com.bank.services.CustomerServices;

public class CustomerRunner {

	public static void run(CustomerServices user) {

		boolean continueService = true;

		while (continueService) {

			int userChoice = MainUtil.getInt("1) View Balance\n" + "2) Withdraw Money\n" + "3) Deposit Money\n"
					+ "4) Transfer Money\n" + "5) Change Password\n" + "6) Change Pin\n" + "7) Get Statement\n"
					+ "8) Switch Account\n" + "9) View Account Details\n" + "10) Logout\n" + "Enter choice : ", 10);
			switch (userChoice) {

			case 1:
				try {
					long bal = user.getBalance();
					System.out.println(bal);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 2:
				try {
					long amount = MainUtil.getLong("Enter amount to withdraw : ");
					String pin = MainUtil.getString("Enter PIN : ");
					user.withdraw(amount, pin);
					System.out.println("Wihtdrawl successful");
				} catch (PinNotSetException exc) {
					String pin = MainUtil.getString("Enter PIN : ");
					try {
						user.setPin(pin);
					} catch (BankingException | InvalidInputException e) {
						System.out.println(e.getMessage());
					}
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
					exception.printStackTrace();
				} catch (InvalidInputException excep) {
					System.out.println(excep.getMessage());
				}
				break;

			case 3:
				try {
					long amount = MainUtil.getLong("Enter amount to deposit : ");
					user.deposit(amount);
					System.out.println("Deposit successful");
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 4:
				try {
					Transaction trans = MainUtil.getTransactionObj();
					String pin = MainUtil.getString("Enter pin : ");
					user.transfer(trans, pin);
				} catch (PinNotSetException exc) {
					String pin = MainUtil.getString("Enter PIN : ");
					try {
						user.setPin(pin);
					} catch (BankingException | InvalidInputException e) {
						System.out.println(e.getMessage());
					}
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				} catch (InvalidInputException excep) {
					System.out.println(excep.getMessage());
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
				try {
					String pin = MainUtil.getString("Enter existing Pin : ");
					String newPin = MainUtil.getString("Enter new Pin : ");
					user.changePin(pin, newPin);
				} catch (BankingException | InvalidInputException exception) {
					System.out.println(exception.getMessage());
				}

				break;

			case 7:
				try {
					boolean continueStatementView = true;
					List<Transaction> list = user.getAccountStatement();
					while (continueStatementView) {
						MainUtil.printList(list);
						MainUtil.filter(list);
						int pageChoice = MainUtil.getInt("Pages(5) : Enter page to view or enter 6 to exit : ", 6);
						if (pageChoice == 6) {
							continueStatementView = false;
						} else {
							list = user.getAccountStatement(pageChoice);
						}
					}
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}

				break;

			case 8:
				try {
					long accNum = MainUtil.getLong("Enter account number : ");
					user.switchAccount(accNum);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 9:

				try {
					Account acc = user.getAccount();
					System.out.println(acc);
				} catch (BankingException exception) {
					System.out.println(exception.getMessage());
				}
				break;

			case 10:

				continueService = false;
			}
		}
	}
}
