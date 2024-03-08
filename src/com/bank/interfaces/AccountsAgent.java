package com.bank.interfaces;

import java.util.Map;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.enums.Status;
import com.bank.pojo.Account;

public interface AccountsAgent {

	long getBalance(long userId) throws PersistenceException;

	long getPrimaryAcc(long userId) throws PersistenceException;

	boolean validateAccount(long aNum) throws PersistenceException;

	long getCusId(long accNum) throws PersistenceException;

	boolean checkAccAndIFSC(long accNum, String iFSC) throws PersistenceException;

	boolean validateCustomerPresence(long cusId) throws PersistenceException;

	void switchPrimary(long userId, long currentAcc, long accoNum) throws PersistenceException;

	Account getAccount(long accNum) throws PersistenceException;

	Map<Long, Account> getAccounts(long customerId) throws PersistenceException;

//	void addAccount(Account account) throws PersistenceException;

	public Object getAccStatus(long accNum) throws PersistenceException;

	public void setAccStatus(long accNum, Status status) throws PersistenceException;

	void closeAndMove(long accNum) throws PersistenceException;

	long getBranchId(long accNum) throws PersistenceException;
}
