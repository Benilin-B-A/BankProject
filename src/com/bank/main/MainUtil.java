package com.bank.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bank.custom.exceptions.InvalidInputException;
import com.bank.pojo.Account;
import com.bank.pojo.Customer;
import com.bank.pojo.Transaction;
import com.bank.pojo.User;
import com.bank.util.Filter;
import com.bank.util.LogHandler;
import com.bank.util.Validator;

public class MainUtil {
	
	private static Scanner scan = new Scanner(System.in);
	private static Logger logger = LogHandler.getLogger(MainUtil.class.getName(), "InputUtilLogs.txt");

	
	public static Transaction getTransactionObj() {
		Transaction trans = new Transaction();
		long accNum = getLong("Enter recepient account number : ");
		trans.setTransAccNum(accNum);
		String iFSC = getString("Enter recepient IFSC code : ");
		trans.setiFSC(iFSC);
		long amount = getLong("Enter amount to transfer : ");
		trans.setAmount(amount);
		String desc = getString("Enter transfer description : ");
		trans.setDescription(desc);
		return trans;
	}

	public static Customer getCustomer() {
		User usr = new Customer();
		getUserDetails(usr);
		long aadhar = getLong("Enter Aadhar Number : ");
		((Customer) usr).setAadharNum(aadhar);
		String pan = getString("Enter PAN Number : ");
		((Customer) usr).setPanNum(pan);
		return (Customer) usr;
	}


	public static void getUserDetails(User user) {
		String name = getString("Enter name : ");
		user.setName(name);
		try {
			String dOB = getString("Enter DOB (YYYY-MM-DD) : ");
			user.setdOB(dOB);
			Validator.validateDOB(dOB);
			String phone = getString("Enter phone number : ");
			Validator.validateMobile(phone);
			user.setPhone(Long.parseLong(phone));
			String eMail = getString("Enter E-Mail : ");
			Validator.validateMail(eMail);
			user.setMail(eMail);
		} catch (InvalidInputException exception) {
			logger.log(Level.WARNING, "Invalid mail", exception);
			System.out.println(exception.getMessage());
		}
		String gender = getString("Gender (Male/Female/Other) : ");
		user.setGender(gender);
		String address = getString("Enter Address : ");
		user.setAddress(address);
	}

	public static Account getAccount() {
		Account account = new Account();
		String type = getString("Enter Account type (Savings/Current): ");
		account.setType(type);
		return account;
	}
	
	static long getLong(String str) {
		System.out.print(str);
		try {
			long num = scan.nextLong();
			if(num<=0) {
				throw new InvalidInputException("Value cannot be zero or negative");
			}
			scan.nextLine();
			return num;
		} catch (InputMismatchException | InvalidInputException exception) {
			scan.nextLine();
			logger.log(Level.WARNING, "Incorrect Input...", exception);
			return getLong("Enter valid Input : ");
		}
	}

	static String getString(String str) {
		System.out.print(str);
		return scan.nextLine();
	}

	static int getInt(String str, int limit) {
		System.out.print(str);
		try {
			int num = scan.nextInt();
			scan.nextLine();
			if (num <= 0 || num > limit) {
				System.out.println("Enter valid Input");
				getInt(str, limit);
			}
			return num;
		} catch (InputMismatchException exception) {
			scan.nextLine();
			logger.log(Level.WARNING, "Incorrect Input...", exception);
			return getInt(str, limit);
		}
	}
	
	static String getType() {
		String type = null;
		int typeChoice = getInt("Enter type [(1) - Credit, (2) - Debit] : ", 2);
		if (typeChoice == 1) {
			type = "Credit";
		} else if (typeChoice == 2) {
			type = "Debit";
		}
		return type;
	}
	
	static Date getDate(){
		String dateStr = getString("Enter Date [YYYY-MM-DD]: ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		try {
			return (Date) sdf.parse(dateStr);
		} catch (ParseException exception) {
			System.out.println("Enter valid date");
			return getDate();
		}
	}

	public static void printList(List<Transaction> list) {
		list.forEach(e -> System.out.println(e));
	}

	public static List<Transaction> filter(List<Transaction> list) {
		List<Transaction> filteredList = null;
		int filterChoice = MainUtil
				.getInt("Filter transactions\n1) By Date\n2) By Type " + "\n3) Don't filter\nEnter choice : ", 3);

		switch (filterChoice) {
		case 1:
			Date date = MainUtil.getDate();
			filteredList = Filter.filterByDate(list, date);
			break;

		case 2:
			String type = MainUtil.getType();
			filteredList = Filter.filterByType(list, type);
			break;

		case 3:
			break;

		}
		return filteredList;
	}
}
