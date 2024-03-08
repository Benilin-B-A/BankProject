package com.bank.interfaces;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.enums.Status;

public interface UserAgent {

	public String getPassword(long userId) throws PersistenceException;

	public boolean validateUserPresence(long uId) throws PersistenceException;

	public int getAttempts(long uId) throws PersistenceException;

	public void setAttempt(long uId, int attempt) throws PersistenceException;

	public String getStatus(long uId) throws PersistenceException;

	public void setStatus(long uId, Status status)throws PersistenceException;

	public String getUserType(long userId) throws PersistenceException;

	public void changePassword(long userId, String hashPassword) throws PersistenceException;

//	public long addUser(User usr, String password) throws PersistenceException;

}
