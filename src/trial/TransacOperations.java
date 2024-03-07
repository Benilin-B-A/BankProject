//package trial;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import com.bank.custom.exceptions.PersistenceException;
//import com.bank.db.implementation.ConnectionHandler;
//import com.bank.db.queries.AccountsTableQuery;
//import com.bank.db.queries.CustomerTableQuery;
//import com.bank.db.queries.UserTableQuery;
//import com.bank.pojo.Account;
//import com.bank.pojo.Customer;
//import com.bank.pojo.User;
//
//public class TransacOperations {
//
//	private Connection connect() throws SQLException {
//		return DriverManager.getConnection(ConnectionHandler.getURL(), ConnectionHandler.getUser(),
//				ConnectionHandler.getPassword());
//	}
//
//	public void addCustomer(Customer customer, Account account, String password) throws PersistenceException {
//		Connection connection;
//		try{
//			connection = connect();
//			connection.setAutoCommit(false);
//			long id = addUser(connection, customer, password);
//			addCustomer(connection, customer, id);
//			addAccount(connection, account, id);
//			connection.commit();
//		} catch (SQLException exception) {
//			connection.rollback();
//			throw new PersistenceException("Error in adding Customer", exception);
//		}
//	}
//
//	private void addAccount(Connection connection, Account account, long customerId) throws SQLException {
//		try (PreparedStatement st = connection.prepareStatement(AccountsTableQuery.addAccount)) {
//			st.setLong(1, customerId);
//			st.setString(2, account.getType());
//			st.setLong(3, account.getBranchId());
//			st.setBoolean(4, account.isPrimary());
//			st.execute();
//		} 
//	}
//
//	private void addCustomer(Connection connection, Customer customer, long id) throws SQLException {
//		try (PreparedStatement st = connection.prepareStatement(CustomerTableQuery.addCustomer)) {
//			st.setLong(1, id);
//			st.setLong(2, customer.getAadharNum());
//			st.setString(3, customer.getPanNum());
//			st.execute();
//		} 
//	}
//
//	public long addUser(Connection connection, User usr, String password) throws SQLException {
//		try (PreparedStatement statement = connection.prepareStatement(UserTableQuery.addUser)) {
//			statement.setString(1, usr.getName());
//			statement.setString(2, usr.getdOB());
//			statement.setLong(3, usr.getPhone());
//			statement.setString(4, usr.getMail());
//			statement.setString(5, usr.getGender());
//			statement.setString(6, usr.getUserType());
//			statement.setString(7, usr.getAddress());
//			statement.setString(8, password);
//			statement.execute();
//		}
//	}
//
//}