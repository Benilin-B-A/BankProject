package com.bank.services;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bank.custom.exceptions.BankingException;
import com.bank.custom.exceptions.InvalidInputException;
import com.bank.custom.exceptions.PersistenceException;
import com.bank.enums.Status;
import com.bank.interfaces.AccountsAgent;
import com.bank.interfaces.BranchAgent;
import com.bank.interfaces.EmployeeAgent;
import com.bank.persistence.util.PersistenceObj;
import com.bank.pojo.Account;
import com.bank.pojo.Branch;
import com.bank.pojo.Customer;
import com.bank.pojo.Employee;
import com.bank.pojo.Transaction;
import com.bank.util.LogHandler;
import com.bank.util.Validator;

public class AdminServices {

	protected long userId;

	public void setUserId(long userId) {
		this.userId = userId;
	}

	private static Logger logger = LogHandler.getLogger(AuthServices.class.getName(), "AdminServicesLogs.txt");

	private static AccountsAgent accAgent = PersistenceObj.getAccountsAgent();
	private static EmployeeAgent empAgent = PersistenceObj.getEmployeeAgent();
	private static BranchAgent branchAgent = PersistenceObj.getBranchAgent();

	public long getBalance(long aNum) throws BankingException {
		try {
			accAgent.validateAccount(aNum);
			return UserServices.getBalance(aNum);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in fetching balance", exception);
			throw new BankingException("Error in fetching balance");
		}
	}

	public void withdraw(long accNum, long amount) throws BankingException {
		UserServices.withdraw(accNum, amount);
	}

	public void deposit(long accNum, long amount) throws BankingException {
		UserServices.deposit(accNum, amount);
	}

	public void transfer(Transaction trans, long accNum) throws BankingException {
		AuthServices.validateAccount(accNum);
		trans.setAccNumber(accNum);
		UserServices.transfer(trans);
	}

	public void changePassword(String oldPass, String newPass) throws BankingException {
		UserServices.changePassword(userId, oldPass, newPass);
	}

	public List<Transaction> getAccountStatement(long accNum) throws BankingException {
		return getAccountStatement(accNum, 1);
	}
	
	public List<Transaction> getAccountStatement(long accNum, int page) throws BankingException {
		return UserServices.getAccountStatement(accNum, page);

	}

	public List<Transaction> getTransStatement(long transId) throws BankingException {
		return UserServices.getTransStatement(transId);
	}

	public Account getAccount(long accNum) throws BankingException{
		AuthServices.validateAccount(accNum);
		return UserServices.getAccountDetails(accNum);
	}

	public Map<Long, Account> getAccounts(long customerId) throws BankingException {
		try {
			AuthServices.validateCustomer(customerId);
			return accAgent.getAccounts(customerId);
		} catch (PersistenceException exception) {
			logger.log(Level.INFO, "Couldn't fetch accounts", exception);
			throw new BankingException("Couldn't fetch accounts for customer ID : " + customerId, exception);
		}
	}
	
	public long addEmployee(Employee emp) throws BankingException, InvalidInputException {
		try {
			Validator.checkNull(emp);
			String password = AuthServices.hashPassword(emp.getdOB());
			emp.setUserType("Employee");
			validateBranch(emp.getBranchID());
			return empAgent.addEmployee(emp, password);
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error in adding Employee", exception);
			throw new BankingException("Couldn't add Employee");
		}
	}
	
	private boolean validateBranch(long branchID) throws BankingException {
		try {
			if(branchAgent.validateBranchId(branchID)) {
				return true;
			}
			throw new BankingException("Invalid BranchId");
		} catch (PersistenceException exception) {
			logger.log(Level.SEVERE, "Error validating BranchId", exception);
			throw new BankingException("Couldn't validate branch ID");
		}
	}

	public void addAccount(Account account, long branchId) throws BankingException {
		validateBranch(branchId);
		account.setBranchId(branchId);
		UserServices.addAccount(account);
	}

	public long addCustomer(Customer cus, Account acc, long branchId) throws BankingException {
		validateBranch(branchId);
		acc.setBranchId(branchId);
		return UserServices.addCustomer(cus, acc);
		
	}
	
	public void addBranch(Branch branch) throws BankingException, InvalidInputException {
		Validator.checkNull(branch);
		try {
			String iFSC = branch.getIFSC();
			Validator.validateAlphaNum(iFSC);
			if(branchAgent.validateIFSC(iFSC)) {
				throw new BankingException("IFSC code already exists");
			}			
			branchAgent.addBranch(branch);
		} catch(PersistenceException exception){
			logger.log(Level.SEVERE,"Error in adding Branch",exception);
			throw new BankingException("Couldn't add branch");
		} catch (InvalidInputException excep) {
			logger.log(Level.WARNING,"Invalid IFSC",excep);
			throw new BankingException("Enter valid IFSC code");
		}
	}

	public void setStatus(long customerId, Status state) throws BankingException {
		AuthServices.setStatus( customerId, state);
	}

	public void setAccountStatus(long accNum, Status status) throws BankingException {
		AuthServices.validateAccountPresence(accNum);
		AuthServices.setAccountStatus(accNum, status);
	}

	public void closeAcc(long accNum) throws BankingException {
		AuthServices.validateAccountPresence(accNum);
		UserServices.closeAcc(accNum);
	}

	public Customer getCustomerDetails(long cusId) throws BankingException {
		return UserServices.getCustomerDetails(cusId);
	}

	public Employee getEmployeeDetails(long empId) throws BankingException{
		return UserServices.getEmployeeDetails(empId);
	}
	
}
