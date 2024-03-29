package com.bank.interfaces;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.pojo.Customer;

public interface CustomerAgent {

	String getPin(long userId) throws PersistenceException;

//	long addCustomer(Customer cus, Account account, String password) throws PersistenceException;

	void changePin(String newPin, long userId) throws PersistenceException;

	int getTpinAttempts(long customerId) throws PersistenceException;
	
	void setTpinAttempts(long customerId, int attempt) throws PersistenceException;

	Customer getCustomer(long userId) throws PersistenceException;

}
