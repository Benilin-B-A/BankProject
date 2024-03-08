package com.bank.db.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.db.queries.AccountsTableQuery;
import com.bank.enums.Status;
import com.bank.interfaces.AccountsAgent;
import com.bank.pojo.Account;

public class AccountsDBAgent implements AccountsAgent {
	
	private AccountsDBAgent() {
	}
	
	private static class AccountsDBAgentHolder{
		private final static AccountsDBAgent accAgent = new AccountsDBAgent(); 
	}
	
	public static AccountsDBAgent getAccountsDBAgent() {
		return AccountsDBAgentHolder.accAgent;
	}

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(ConnectionHandler.getURL(), ConnectionHandler.getUser(),
				ConnectionHandler.getPassword());
	}

	@Override
	public long getBalance(long userId) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.getBalance)) {
			st.setLong(1, userId);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getLong(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch password", exception);
		}
	}

	@Override
	public long getPrimaryAcc(long userId) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.getPrimaryAccount)) {
			st.setLong(1, userId);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getLong("ACC_NUMBER");
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Error in fetching primary account", exception);
		}
	}

	@Override
	public boolean validateAccount(long aNum) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.validateAccount)) {
			st.setLong(1, aNum);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getBoolean(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Error in validating account", exception);
		}
	}

	@Override
	public long getCusId(long accNum) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.getCustomerId)) {
			st.setLong(1, accNum);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getLong(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Error fetching customerId", exception);
		}
	}

	@Override
	public boolean checkAccAndIFSC(long accNum, String iFSC) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.validateAccIFSC)) {
			st.setLong(1, accNum);
			st.setString(2, iFSC);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getBoolean(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Error in validating Account", exception);
		}

	}

	@Override
	public boolean validateCustomerPresence(long cusId) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.validateCustomer)) {
			st.setLong(1, cusId);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getBoolean(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Error in validating customer", exception);
		}
	}

	@Override
	public void switchPrimary(long userId, long currentAcc, long accoNum) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.changePrimaryAcc)) {
			st.setLong(1, accoNum);
			st.setLong(2, currentAcc);
			st.executeUpdate();
		} catch (SQLException exception) {
			throw new PersistenceException("Error in fetching Accounts", exception);
		}

	}

	@Override
	public Account getAccount(long accNum) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.getAccountDetails)) {
			st.setLong(1, accNum);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				Account acc = new Account();
				acc.setAccNum(set.getLong("ACC_NUMBER"));
				acc.setUId(set.getLong("CUSTOMER_ID"));
				acc.setBalance(set.getLong("Balance"));
				acc.setBranchId(set.getInt("BRANCH_ID"));
				acc.setOpenedOn(set.getTimestamp("OPENED_ON"));
				acc.setStatus(set.getString("ACC_STATUS"));
				acc.setType(set.getString("ACCOUNT_TYPE"));
				return acc;
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Error in fetching Accounts", exception);
		}
	}

	@Override
	public Map<Long, Account> getAccounts(long customerId) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.getCustomerAccounts)) {
			st.setLong(1, customerId);
			Map<Long, Account> map = new HashMap<>();
			try (ResultSet set = st.executeQuery()) {
				while (set.next()) {
					long accNum = set.getLong("ACC_NUMBER");
					Account acc = getAccount(accNum);
					map.put(accNum, acc);
				}
				return map;
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Error in fetching Accounts", exception);
		}
	}

//	@Override
//	public void addAccount(Account account) throws PersistenceException {
//		try (Connection connection = connect();
//				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.addAccount)) {
//			st.setLong(1, account.getUId());
//			st.setString(2, account.getType());
//			st.setLong(3, account.getBranchId());
//			st.setBoolean(4, account.isPrimary());
//			st.execute();
//		} catch (SQLException exception) {
//			throw new PersistenceException("Error in adding Account", exception);
//		}
//	}

	@Override
	public String getAccStatus(long accNum) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.getAccountStatus)) {
			st.setLong(1, accNum);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getString(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch status", exception);
		}
	}

	@Override
	public void setAccStatus(long accNum, Status status) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.setAccountStatus)) {
			st.setString(1, status.getState());
			st.setLong(2, accNum);
			st.execute();
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't update status", exception);
		}
	}

	@Override
	public void closeAndMove(long accNum) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st1 = connection.prepareStatement(AccountsTableQuery.moveToClosed);
				PreparedStatement st2 = connection.prepareStatement(AccountsTableQuery.closeAccount)) {
			st1.setLong(1, accNum);
			st2.setLong(1, accNum);
			st1.execute();
			st2.execute();
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't delete account", exception);
		}
	}

	@Override
	public long getBranchId(long accNum) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(AccountsTableQuery.getBranchId)) {
			st.setLong(1, accNum);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getLong(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch branch Id", exception);
		}
	}
}
