package com.bank.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bank.adapter.JSONAdapter;
import com.bank.cache.AccountCache;
import com.bank.cache.CustomerAccountsCache;
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
	private boolean isPinSet;
	private Account currentAccount;
	
	static CustomerAccountsCache cacheAgent = CustomerAccountsCache.getInstance();
	static AccountCache accCache = AccountCache.getInstance();

	public void setPinSet(boolean isPinSet) {
		this.isPinSet = isPinSet;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setCurrentAccount(Account account) {
		this.currentAccount = account;
	}

	private static AccountsAgent accAgent = PersistenceObj.getAccountsAgent();
	private static CustomerAgent cusAgent = PersistenceObj.getCustmomerAgent();

	private static Logger logger = LogHandler.getLogger(CustomerServices.class.getName(), "CustomerServices.txt");

	public List<Long> getAccounts() throws BankingException{
		return cacheAgent.getAccount(userId);
	}
	
	public long getBalance() throws BankingException {
		long accNum = currentAccount.getAccNum();
		AuthServices.validateAccount(accNum);
		return UserServices.getBalance(accNum);
	}

	public boolean withdraw(long amount, String pin) throws BankingException, InvalidInputException {
		if (isPinSet) {
			AuthServices.authPin(userId, pin);
			UserServices.withdraw(currentAccount.getAccNum(), amount);
			return true;
		}
		throw new PinNotSetException("Set TPIN to continue withdrawl");
	}

	public void deposit(long amount) throws BankingException {
		UserServices.deposit(currentAccount.getAccNum(), amount);
	}

	public boolean transfer(Transaction transaction, String pin, boolean withinBank) throws BankingException, InvalidInputException {
		if (isPinSet) {
			AuthServices.authPin(userId, pin);
			transaction.setAccNumber(currentAccount.getAccNum());
			UserServices.transferMoney(transaction, withinBank);
			return true;
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
			cusAgent.setPin(AuthServices.hashPassword(newPin), userId);
			isPinSet = true;
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Couldn't set pin", exception);
			throw new BankingException("Couldn't set pin", exception);
		}
	}

	public JSONArray getAccountStatement() throws BankingException {
		return getAccountStatement(1);
	}

	public JSONArray getAccountStatement(int page) throws BankingException {
		return UserServices.getAccountStatement(currentAccount.getAccNum(), page);
	}

	public void switchAccount(long accoNum) throws BankingException {
		validateSwitch(accoNum);
		try {
			accAgent.switchPrimary(userId, currentAccount.getAccNum(), accoNum);
			currentAccount = accCache.getAccount(accoNum);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in switching account", exception);
			throw new BankingException("Couldn't switch account");
		}
	}

	private boolean validateSwitch(long accNum) throws BankingException {
		if (!(accNum == currentAccount.getAccNum())) {
			if (UserServices.getCustomerId(accNum) == userId) {
				return true;
			}
			throw new BankingException("No such account");
		}
		throw new BankingException("The entered account is already the primary account");
	}

	public  JSONObject getAccount() throws BankingException {
		return JSONAdapter.objToJSONObject(currentAccount);
	}


}
