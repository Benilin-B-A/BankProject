package com.bank.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import com.bank.custom.exceptions.BankingException;
import com.bank.custom.exceptions.PersistenceException;
import com.bank.enums.Status;
import com.bank.interfaces.AccountsAgent;
import com.bank.interfaces.CustomerAgent;
import com.bank.interfaces.EmployeeAgent;
import com.bank.interfaces.UserAgent;
import com.bank.persistence.util.PersistenceObj;
import com.bank.util.LogHandler;

public class AuthServices {

	private static Logger logger = LogHandler.getLogger(AuthServices.class.getName(), "AuthServicesLogs.txt");

	private static UserAgent userAgent = PersistenceObj.getUserAgent();
	private static EmployeeAgent empAgent = PersistenceObj.getEmployeeAgent();
	private static CustomerAgent cusAgent = PersistenceObj.getCustmomerAgent();
	private static AccountsAgent accAgent = PersistenceObj.getAccountsAgent();

	// checks user validity, status and login attempts
	public boolean login(long uId, String password) throws BankingException {
		try {
			validateUser(uId);
			int attempt = userAgent.getAttempts(uId);
			if (attempt < 3) {
				if (authUser(uId, password)) {
					setAttempt(uId, 1);
					return true;
				}
				setAttempt(uId, attempt + 1);
				logger.warning("Incorrect login attempt on user id : "+ uId);
				throw new BankingException("Incorrect login credential :" + (3 - attempt) + " attempt(s) left");
			}
			setStatus(uId, Status.BLOCKED);
			logger.warning("User id : "+ uId + "blocked exceeding login attempt limit");
			throw new BankingException("User Blocked : Contact nearby BOB authority to regain access");
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Login error", exception);
			throw new BankingException("Login error : Try again later", exception);
		}
	}

	// checks input password against stored password
	static boolean authUser(long userId, String password) throws BankingException {
		try {
			boolean isValid = BCrypt.checkpw(password, userAgent.getPassword(userId));
			if (isValid) {
				return true;
			}
			return false;
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in authenticating user", exception);
			throw new BankingException("Authentication Error", exception);
		}
	}

	// checks input transaction pin against stored pin
	static boolean authPin(long userId, String pin) throws BankingException {
		try {
			int attempts = cusAgent.getTpinAttempts(userId);
			if (attempts < 3) {
				if (BCrypt.checkpw(pin, cusAgent.getPin(userId))) {
					return true;
				}
				setTpinAttempt(userId, attempts+1);
				logger.warning("Incorrect tpin attempt on user id : "+ userId);
				throw new BankingException("Incorrect pin : " + (3 - attempts) + " attempt(s) left");
			}
			setStatus(userId, Status.BLOCKED);
			logger.warning("User id : "+ userId + "blocked exceeding tpin attempt limit");
			throw new BankingException("User Blocked : Contact nearby BOB authority to regain access");
		} catch (PersistenceException | BankingException exception) {
			logger.log(Level.SEVERE, "Error in authenticating user", exception);
			throw new BankingException("Authentication Error", exception);
		}
	}

	// hashes password during password change and user creation
	static String hashPassword(String plainPassword) {
		String salt = BCrypt.gensalt();
		return BCrypt.hashpw(plainPassword, salt);
	}
	
	// update login attempts
	private static void setAttempt(long userId, int attempt) throws BankingException {
		try {
			userAgent.setAttempt(userId, attempt);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in updating failed attempts", exception);
			throw new BankingException("Login Attempt update error", exception);
		}
	}
	
	private static void setTpinAttempt(long userId, int attempt) throws BankingException {
		try {
			cusAgent.setTpinAttempts(userId, attempt);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in updating failed tpin attempts", exception);
			throw new BankingException("Tpin Attempt update error", exception);
		}
	}

	// upadate user status
	public static void setStatus(long uId, Status status) throws BankingException {
		try {
			validateUserPresence(uId);
			String presentState = userAgent.getStatus(uId);
			String state = status.getState();
			if (presentState.equals(state)) {
				throw new BankingException("User already " + state);
			}
			userAgent.setStatus(uId, status);
			if (presentState.equals("Blocked") && state.equals("Active")) {
				setAttempt(uId, 1);
				setTpinAttempt(uId, 1);
			}
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in setting user status", exception);
			throw new BankingException("Status Update Error", exception);
		}
	}

	static void setAccountStatus(long accNum, Status status) throws BankingException {
		try {
			validateAccountPresence(accNum);
			String state = status.getState();
			if (accAgent.getAccStatus(accNum).equals(state)) {
				throw new BankingException("User already " + state);
			}
			accAgent.setAccStatus(accNum, status);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in setting account status", exception);
			throw new BankingException("Status Update Error", exception);
		}
	}

	static void validateUserPresence(long userId) throws BankingException {
		try {
			boolean isValidUser = userAgent.validateUserPresence(userId);
			if (!isValidUser) {
				throw new BankingException("Invalid user Id");
			}
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in validating user", exception);
			throw new BankingException("Couldn't validate user");
		}
	}

	// validate if a user is present or not and if yes checks status
	static void validateUser(long userId) throws BankingException {
		try {
			validateUserPresence(userId);
			if (!userAgent.getStatus(userId).equals("Active")) {
				throw new BankingException("User Blocked/Inactive");
			}
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Couldn't validate user", exception);
			throw new BankingException("Couldn't validate user", exception);
		}
	}

	static void validateAccountPresence(long accNum) throws BankingException {
		try {
			boolean isValid = accAgent.validateAccount(accNum);
			if (!isValid) {
				throw new BankingException("Invalid account number");
			}
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in validating account", exception);
			throw new BankingException("Couldn't validate account");
		}
	}

	// validate if an account is present or not and if yes checks status
	static void validateAccount(long accNum) throws BankingException {
		try {
			validateAccountPresence(accNum);
			if (!accAgent.getAccStatus(accNum).equals("Active")) {
				throw new BankingException("Account Blocked/Inactive");
			}
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Couldn't validate account", exception);
			throw new BankingException("Couldn't validate account", exception);
		}
	}

	// returns user object after login to access services
	public static Object getUser(long userId) throws BankingException {
		try {
			String type = userAgent.getUserType(userId);
			if (type.equals("Employee")) {
				boolean isAdmin = empAgent.getAdminAccess(userId);
				if (isAdmin) {
					AdminServices admin = new AdminServices();
					admin.setUserId(userId);
					return admin;
				}
				EmployeeServices emp = new EmployeeServices();
				emp.setUserId(userId);
				emp.setBranchId(empAgent.getBranchId(userId));
				return emp;
			}
			CustomerServices cus = new CustomerServices();
			cus.setUserId(userId);
			String pin = cusAgent.getPin(userId);
			if(!(pin == null)) {
				cus.setPinSet(true);
			}
			cus.setCurrentAcc(accAgent.getPrimaryAcc(userId));
			return cus;
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in determining employee type", exception);
			throw new BankingException("Cannot fetch available services at the moment... Try again");
		}
	}

	// validate if a customer is present (check if the user is a customer)
	static boolean validateCustomer(long cusId) throws BankingException {
		try {
			boolean isValid = accAgent.validateCustomerPresence(cusId);
			if (!isValid) {
				throw new BankingException("Invalid Customer Id");
			}
			return true;
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Couldn't validate customer", exception);
			throw new BankingException("Cannot validate customer");
		}
	}
}