package com.bank.db.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.db.queries.CustomerTableQuery;
import com.bank.interfaces.AccountsAgent;
import com.bank.interfaces.CustomerAgent;
import com.bank.interfaces.UserAgent;
import com.bank.persistence.util.PersistenceObj;
import com.bank.pojo.Account;
import com.bank.pojo.Customer;

public class CustomerDBAgent implements CustomerAgent {
	
	private CustomerDBAgent() {
	}
	
	private static class CustomerDBAgentHolder{
		private final static CustomerDBAgent accAgent = new CustomerDBAgent(); 
	}
	
	public static CustomerDBAgent getCustomerDBAgent() {
		return CustomerDBAgentHolder.accAgent;
	}

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(ConnectionHandler.getURL(), ConnectionHandler.getUser(),
				ConnectionHandler.getPassword());
	}
	private static UserAgent userAgent = PersistenceObj.getUserAgent();
	private static AccountsAgent accAgent = PersistenceObj.getAccountsAgent();

	@Override
	public String getPin(long userId) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(CustomerTableQuery.getPin)) {
			st.setLong(1, userId);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getString(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch password", exception);
		}
	}
	
	@Override
	public long addCustomer(Customer cus, Account account, String password) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(CustomerTableQuery.addCustomer)) {
			long id = userAgent.addUser(cus, password);
			st.setLong(1, id);
			st.setLong(2, cus.getAadharNum());
			st.setString(3, cus.getPanNum());
			st.execute();
			account.setUId(id);
			accAgent.addAccount(account);
			return id;
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't add Customer", exception);
		}
	}

	@Override
	public void changePin(String newPin, long userId) throws PersistenceException {
		try (Connection connection = connect(); 
				PreparedStatement st = connection.prepareStatement(CustomerTableQuery.changePin)){
			st.setString(1, newPin);
			st.setLong(2, userId);
			st.execute();
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't update status",exception);
		}
	}

	@Override
	public int getTpinAttempts(long customerId) throws PersistenceException {
		try (Connection connection = connect(); 
				PreparedStatement st = connection.prepareStatement(CustomerTableQuery.getAttempts)){
			st.setLong(1, customerId);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getInt(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch attempts",exception);
		}
	}

	@Override
	public void setTpinAttempts(long customerId, int attempt) throws PersistenceException {
		try (Connection connection = connect(); 
				PreparedStatement st = connection.prepareStatement(CustomerTableQuery.setAttempts)){
			st.setInt(1, attempt);
			st.setLong(2, customerId);
			st.execute();
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't update attempts",exception);
		}
		
	}

	@Override
	public Customer getCustomer(long userId) throws PersistenceException {
		try (Connection connection = connect(); 
				PreparedStatement st = connection.prepareStatement(CustomerTableQuery.getCustomerProfile)){
			st.setLong(1, userId);
			try(ResultSet set = st.executeQuery()){
				set.next();
				Customer cus = new Customer();
				cus.setName(set.getString(1));
				cus.setdOB(set.getString(2));
				cus.setGender(set.getString(3));
				cus.setAddress(set.getString(4));
				cus.setMail(set.getString(5));
				cus.setPhone(set.getLong(6));
				cus.setStatus(set.getString(7));
				cus.setAadharNum(set.getLong(8));
				cus.setPanNum(set.getString(9));
				cus.setNoOfAcc(set.getInt(10));
				return cus;
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch customer details",exception);
		}
	}
}
