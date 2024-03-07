package com.bank.interfaces;

import java.util.List;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.pojo.Transaction;

public interface TransactionAgent {

	void transfer(Transaction trans) throws PersistenceException;

	long getTransBranch(long transId) throws PersistenceException;
	
	List<Transaction> getAccStatement(long accNum, int limit, int offset) throws PersistenceException;

	List<Transaction> getTransStatement(long transId) throws PersistenceException;
	
}
