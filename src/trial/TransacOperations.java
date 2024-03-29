package trial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.db.implementation.ConnectionHandler;
import com.bank.db.queries.AccountsTableQuery;
import com.bank.db.queries.CustomerTableQuery;
import com.bank.db.queries.EmployeeTableQuery;
import com.bank.db.queries.UserTableQuery;
import com.bank.pojo.Account;
import com.bank.pojo.Customer;
import com.bank.pojo.Employee;
import com.bank.pojo.User;

public class TransacOperations {

	private static Connection connect() throws SQLException {
		return DriverManager.getConnection(ConnectionHandler.getURL(), ConnectionHandler.getUser(),
				ConnectionHandler.getPassword());
	}

	public static long addCustomer(Customer customer, Account account, String password) throws PersistenceException {
		Connection connection = null;
		try{
			connection = connect();
			connection.setAutoCommit(false);
			long id = addUser(connection, customer, password);
			addCustomer(connection, customer, id);
			account.setUId(id);
			addAccount(connection, account);
			connection.commit();
			return id;
		} catch (SQLException exception) {
			try {
				connection.rollback();
			} catch (SQLException excep) {
				throw new PersistenceException("Couldn't complete adding customer", excep);
			}
			throw new PersistenceException("Error in adding Customer", exception);
		}
	}
	
	public static long addEmployee(Employee employee, String password) throws PersistenceException {
		Connection connection = null;
		try{
			connection = connect();
			connection.setAutoCommit(false);
			long id = addUser(connection, employee, password);
			addEmployee(connection, employee, id);
			connection.commit();
			return id;
		} catch (SQLException exception) {
			try {
				connection.rollback();
			} catch (SQLException excep) {
				throw new PersistenceException("Couldn't complete adding customer", excep);
			}
			throw new PersistenceException("Error in adding Customer", exception);
		}
	}

	public static void addAccount(Account account) throws PersistenceException{
		try (Connection connection = connect()) {
			addAccount(account);
		} catch (SQLException exception) {
			throw new PersistenceException("Error in adding Account", exception);
		}
	}
	
	private static void addAccount(Connection connection, Account account) throws SQLException {
		try (PreparedStatement st = connection.prepareStatement(AccountsTableQuery.addAccount)) {
			st.setLong(1, account.getUId());
			st.setString(2, account.getType());
			st.setLong(3, account.getBranchId());
			st.setBoolean(4, account.isPrimary());
			st.execute();
		}
	}

	private static void addCustomer(Connection connection, Customer customer, long id) throws SQLException {
		try (PreparedStatement st = connection.prepareStatement(CustomerTableQuery.addCustomer)) {
			st.setLong(1, id);
			st.setLong(2, customer.getAadharNum());
			st.setString(3, customer.getPanNum());
			st.execute();
		} 
	}

	public static void addEmployee(Connection con, Employee emp, long id) throws SQLException {
		try (PreparedStatement st = con.prepareStatement(EmployeeTableQuery.addEmployee)) {
			st.setLong(1, id);
			st.setLong(2, emp.getBranchID());
			st.setBoolean(3, emp.isAdmin());
			st.execute();
		}
	}
	
	private static long addUser(Connection connection, User usr, String password) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(UserTableQuery.addUser, java.sql.Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, usr.getName());
			statement.setString(2, usr.getdOB());
			statement.setLong(3, usr.getPhone());
			statement.setString(4, usr.getMail());
			statement.setString(5, usr.getGender());
			statement.setString(6, usr.getUserType());
			statement.setString(7, usr.getAddress());
			statement.setString(8, password);
			statement.executeUpdate();
			try(ResultSet set = statement.getGeneratedKeys()){
				set.next();
				return set.getLong(1);
			}
		}
	}


}