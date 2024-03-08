package com.bank.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bank.custom.exceptions.BankingException;
import com.bank.custom.exceptions.InvalidInputException;
import com.bank.custom.exceptions.PersistenceException;
import com.bank.custom.exceptions.PinNotSetException;
import com.bank.interfaces.AccountsAgent;
import com.bank.interfaces.CustomerAgent;
import com.bank.persistence.util.PersistenceObj;
import com.bank.pojo.Account;
import com.bank.pojo.Transaction;
import com.bank.util.LogHandler;
import com.bank.util.Validator;

public class CustomerServices {

	private long userId;
	private long currentAcc;
	private boolean isPinSet;

	public void setPinSet(boolean isPinSet) {
		this.isPinSet = isPinSet;
	}

	public void setCurrentAcc(long accNumber) {
		this.currentAcc = accNumber;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	private static AccountsAgent accAgent = PersistenceObj.getAccountsAgent();
	private static CustomerAgent cusAgent = PersistenceObj.getCustmomerAgent();

	private static Logger logger = LogHandler.getLogger(CustomerServices.class.getName(), "CustomerServices.txt");

	public long getBalance() throws BankingException {
		AuthServices.validateAccount(currentAcc);
		return UserServices.getBalance(currentAcc);
	}

	public void withdraw(long amount, String pin) throws BankingException, InvalidInputException {
		if (isPinSet) {
			AuthServices.authPin(userId, pin);
			UserServices.withdraw(currentAcc, amount);
		}
		throw new PinNotSetException("Set TPIN to continue withdrawl");
	}

	public void deposit(long amount) throws BankingException {
		UserServices.deposit(currentAcc, amount);
	}

	public void transfer(Transaction transaction, String pin) throws BankingException, InvalidInputException {
		if (!isPinSet) {
			AuthServices.authPin(userId, pin);
			transaction.setAccNumber(currentAcc);
			UserServices.transfer(transaction);
		}
		throw new PinNotSetException("Set TPIN to continue transfer");
		
	}

	public void changePassword(String oldPass, String newPass) throws BankingException {
		UserServices.changePassword(userId, oldPass, newPass);
	}

	public void changePin(String oldPin, String newPin) throws BankingException, InvalidInputException {
		AuthServices.authPin(userId, oldPin);
		setPin(newPin);
	}

	public void setPin(String newPin) throws BankingException, InvalidInputException {
		try {
			Validator.validatePin(newPin);
			cusAgent.changePin(newPin, userId);
			isPinSet = true;
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Couldn't set pin", exception);
			throw new BankingException("Couldn't set pin", exception);
		}
	}

	public List<Transaction> getAccountStatement() throws BankingException {
		return getAccountStatement(1);
	}

	public List<Transaction> getAccountStatement(int page) throws BankingException {
		return UserServices.getAccountStatement(currentAcc, page);
	}

	public void switchAccount(long accoNum) throws BankingException {
		validateSwitch(accoNum);
		try {
			accAgent.switchPrimary(userId, currentAcc, accoNum);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in switching account", exception);
			throw new BankingException("Couldn't switch account");
		}
		currentAcc = accoNum;
	}

	private boolean validateSwitch(long accNum) throws BankingException {
		if (!(accNum == currentAcc)) {
			if (UserServices.getCustomerId(accNum) == userId) {
				return true;
			}
			throw new BankingException("No such account");
		}
		throw new BankingException("The entered account is already the primary account");
	}

	public Account getAccount() throws BankingException {
		return UserServices.getAccountDetails(currentAcc);
	}

}
