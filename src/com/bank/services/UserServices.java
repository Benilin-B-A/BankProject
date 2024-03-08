package com.bank.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bank.custom.exceptions.BankingException;
import com.bank.custom.exceptions.InvalidInputException;
import com.bank.custom.exceptions.PersistenceException;
import com.bank.interfaces.AccountsAgent;
import com.bank.interfaces.CustomerAgent;
import com.bank.interfaces.EmployeeAgent;
import com.bank.interfaces.TransactionAgent;
import com.bank.interfaces.UserAgent;
import com.bank.persistence.util.PersistenceObj;
import com.bank.pojo.Account;
import com.bank.pojo.Customer;
import com.bank.pojo.Employee;
import com.bank.pojo.Transaction;
import com.bank.util.LogHandler;
import com.bank.util.Validator;

import trial.TransacOperations;

public abstract class UserServices {

	private static AccountsAgent accAgent = PersistenceObj.getAccountsAgent();
	private static TransactionAgent tranAgent = PersistenceObj.getTransactionAgent();
	private static UserAgent userAgent = PersistenceObj.getUserAgent();
	private static CustomerAgent cusAgent = PersistenceObj.getCustmomerAgent();
	private static EmployeeAgent empAgent = PersistenceObj.getEmployeeAgent();

	private static Logger logger = LogHandler.getLogger(UserServices.class.getName(), "UserServices.txt");

	// get the balance of provided acc num
	static long getBalance(long aNum) throws BankingException {
		try {
			return accAgent.getBalance(aNum);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in fetching balance", exception);
			throw new BankingException("Error in fetching balance", exception);
		}
	}

	// get customer id from account number
	static long getCustomerId(long accNum) throws BankingException {
		try {
			return accAgent.getCusId(accNum);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in fetching customerId", exception);
			throw new BankingException("Cannot complete withdrawl. Try again");
		}
	}

	static void withdraw(long accNum, long amount) throws BankingException {
		AuthServices.validateAccount(accNum);
		Transaction trans = new Transaction();
		trans.setAccNumber(accNum);
		trans.setAmount(amount);
		trans.setDescription("Withdrawl");
		trans.setTransactionId(System.currentTimeMillis());
		withdraw(trans);
	}

	private static void withdraw(Transaction trans) throws BankingException {
		long bal = getBalance(trans.getAccNumber());
		long amount = trans.getAmount();
		validateBal(bal, amount);
		trans.setCustomerId(getCustomerId(trans.getAccNumber()));
		trans.setType("Debit");
		trans.setOpeningBal(bal);
		trans.setClosingBal(bal - amount);
		try {
			tranAgent.transfer(trans);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in withdrawl", exception);
			throw new BankingException("Cannot complete withdrawl. Try again");
		}
	}

	static void deposit(long accNum, long amount) throws BankingException {
		AuthServices.validateAccount(accNum);
		Transaction trans = new Transaction();
		trans.setAccNumber(accNum);
		trans.setCustomerId(getCustomerId(trans.getAccNumber()));
		trans.setDescription("Deposit");
		trans.setTransactionId(System.currentTimeMillis());
		trans.setAmount(amount);
		deposit(trans);
	}

	private static void deposit(Transaction trans) throws BankingException {
		long bal = getBalance(trans.getAccNumber());
		long amount = trans.getAmount();
		trans.setType("Credit");
		trans.setOpeningBal(bal);
		trans.setClosingBal(bal + amount);
		try {
			tranAgent.transfer(trans);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in deposit", exception);
			throw new BankingException("cannot complete deposit", exception);
		}
	}

	static void transfer(Transaction trans) throws BankingException {
		try {
			Validator.checkNull(trans);
		} catch (InvalidInputException exception) {
			throw new BankingException("Transaction is null");
		}
		try {
			long senderAccNum = trans.getAccNumber();
			long tId = System.currentTimeMillis();
			trans.setTransactionId(tId);
			String iFSC = trans.getiFSC();
			Transaction recepient = null;
			if (iFSC.startsWith("BOB")) {
				long accNum = trans.getTransAccNum();
				AuthServices.validateAccount(accNum);
				if (!accAgent.checkAccAndIFSC(accNum, iFSC)) {
					throw new BankingException("Invalid IFSC or Account Number");
				}
				recepient = new Transaction();
				recepient.setCustomerId(getCustomerId(accNum));
				recepient.setAccNumber(accNum);
				recepient.setTransAccNum(senderAccNum);
				recepient.setDescription(trans.getDescription());
				recepient.setAmount(trans.getAmount());
				recepient.setTransactionId(tId);
			}
			withdraw(trans);
			if (recepient != null) {
				deposit(recepient);
			}
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error In Transaction", exception);
			throw new BankingException("Couldn't complete Transaction", exception);
		}
	}

	private static void validateBal(long balance, long amount) throws BankingException {
		if (balance < amount) {
			throw new BankingException("Insufficient Funds");
		}
	}

	static void changePassword(long userId, String oldPassword, String newPassword) throws BankingException {
		boolean isValid = AuthServices.authUser(userId, oldPassword);
		try {
			if(isValid) {
				if (oldPassword.equals(newPassword)) {
					throw new BankingException("New password cannot be your previous password");
				}
				Validator.validatePassword(newPassword);
				userAgent.changePassword(userId, AuthServices.hashPassword(newPassword));
			}
			throw new BankingException("Invalid Password");
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in changing password", exception);
			throw new BankingException("Couldn't change password");
		} catch (InvalidInputException exception) {
			logger.log(Level.WARNING, "Invalid Password", exception);
			throw new BankingException("Password must contain minimum of 8 characters including," + "\n*One Uppercase"
					+ "\n*One special character" + "\n*One number");
		}
	}

	static List<Transaction> getAccountStatement(long accNum, int page) throws BankingException {
		try {
			AuthServices.validateAccount(accNum);
			int limit = 10;
			int offset = limit * (page - 1);
			return tranAgent.getAccStatement(accNum, limit, offset);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Couldn't fetch account statement", exception);
			throw new BankingException("Couldn't fetch account statement", exception);
		}
	}

	public static List<Transaction> getTransStatement(long transId) throws BankingException {
		try {
			return tranAgent.getTransStatement(transId);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Couldn't fetch transaction statement", exception);
			throw new BankingException("Couldn't fetch transaction statement", exception);
		}
	}

	static Account getAccountDetails(long accNum) throws BankingException {
		try {
			AuthServices.validateAccount(accNum);
			return accAgent.getAccount(accNum);
		} catch (PersistenceException exception) {
			logger.log(Level.INFO, "Couldn't fetch account", exception);
			throw new BankingException("Couldn't fetch Account for account number : " + accNum, exception);
		}
	}

	static Customer getCustomerDetails(long userId) throws BankingException {
		try {
			AuthServices.validateUserPresence(userId);
			AuthServices.validateCustomer(userId);
			return cusAgent.getCustomer(userId);
		} catch (PersistenceException exception) {
			logger.log(Level.INFO, "Couldn't fetch customer", exception);
			throw new BankingException("Couldn't fetch Customer for userId : " + userId, exception);
		}
	}

	static Employee getEmployeeDetails(long userId) throws BankingException {
		try {
			if (empAgent.validateEmployee(userId)) {
				return empAgent.getEmployee(userId);
			}
			throw new BankingException("Invalid Employee Id");
		} catch (PersistenceException exception) {
			logger.log(Level.INFO, "Couldn't fetch employee", exception);
			throw new BankingException("Couldn't fetch employee for userId : " + userId, exception);
		}
	}

	static long addCustomer(Customer cus, Account account) throws BankingException {
		try {
			Validator.checkNull(cus);
			Validator.checkNull(account);
		} catch (InvalidInputException exception) {
			throw new BankingException("Customer/Account is null");
		}
		try {
			account.setPrimary(true);
			String password = AuthServices.hashPassword(cus.getdOB());
			cus.setUserType("Customer");
//			return cusAgent.addCustomer(cus, account, password);
			return TransacOperations.addCustomer(cus, account, password);
		} catch (PersistenceException exception) {
			exception.printStackTrace();
			logger.log(Level.SEVERE, "Error in adding Customer", exception);
			throw new BankingException("Couldn't add Customer", exception);
		}
	}

	static void addAccount(Account account) throws BankingException {
		try {
			Validator.checkNull(account);
		} catch (InvalidInputException exception) {
			throw new BankingException("Account is null");
		}
		try {
			TransacOperations.addAccount(account);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in adding account", exception);
			throw new BankingException("Couldn't add account");
		}

	}

	static void closeAcc(long accNum) throws BankingException {
		try {
			accAgent.closeAndMove(accNum);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in closing account", exception);
			throw new BankingException("Account cannot be closed", exception);
		}
	}
}
