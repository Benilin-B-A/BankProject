package com.bank.db.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.db.queries.EmployeeTableQuery;
import com.bank.interfaces.EmployeeAgent;
import com.bank.interfaces.UserAgent;
import com.bank.persistence.util.PersistenceObj;
import com.bank.pojo.Employee;

public class EmployeeDBAgent implements EmployeeAgent {
	
	private EmployeeDBAgent() {
	}
	
	private static class EmployeeDBAgentHolder{
		private final static EmployeeDBAgent accAgent = new EmployeeDBAgent(); 
	}
	
	public static EmployeeDBAgent getEmployeeDBAgent() {
		return EmployeeDBAgentHolder.accAgent;
	}
	
	UserAgent usAgent = PersistenceObj.getUserAgent();

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(ConnectionHandler.getURL(), ConnectionHandler.getUser(),
				ConnectionHandler.getPassword());
	}

	@Override
	public boolean getAdminAccess(long userId) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(EmployeeTableQuery.getAdminAccess)) {
			st.setLong(1, userId);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getBoolean(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch user type", exception);
		}
	}

	@Override
	public long getBranchId(long userId) throws PersistenceException {
		try (Connection connection = connect();
				PreparedStatement st = connection.prepareStatement(EmployeeTableQuery.getBranchId)) {
			st.setLong(1, userId);
			try (ResultSet set = st.executeQuery()) {
				set.next();
				return set.getLong(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch user type", exception);
		}
	}

//	@Override
//	public long addEmployee(Employee emp, String pwd) throws PersistenceException {
//		try (Connection connection = connect();
//				PreparedStatement st = connection.prepareStatement(EmployeeTableQuery.addEmployee)) {
//			long id = usAgent.addUser(emp, pwd);
//			st.setLong(1, id);
//			st.setLong(2, emp.getBranchID());
//			st.setBoolean(3, emp.isAdmin());
//			st.execute();
//			return id;
//		} catch (SQLException exception) {
//			throw new PersistenceException("Error in adding Employee", exception);
//		}
//	}

	@Override
	public Employee getEmployee(long userId) throws PersistenceException {
		try (Connection connection = connect(); 
				PreparedStatement st = connection.prepareStatement(EmployeeTableQuery.getEmployeeProfile)){
			st.setLong(1, userId);
			try(ResultSet set = st.executeQuery()){
				set.next();
				Employee emp = new Employee();
				emp.setName(set.getString(1));
				emp.setdOB(set.getString(2));
				emp.setGender(set.getString(3));
				emp.setAddress(set.getString(4));
				emp.setMail(set.getString(5));
				emp.setPhone(set.getLong(6));
				emp.setStatus(set.getString(7));
				emp.setBranchID(set.getLong(8));
				emp.setAdmin(set.getBoolean(9));
				return emp;
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch customer details",exception);
		}
	}
	
	@Override
	public boolean validateEmployee(long userId) throws PersistenceException{
		try (Connection connection = connect(); 
				PreparedStatement st = connection.prepareStatement(EmployeeTableQuery.validateEmployeePresence)){
			st.setLong(1, userId);
			try(ResultSet set = st.executeQuery()){
				set.next();
				return set.getBoolean(1);
			}
		} catch (SQLException exception) {
			throw new PersistenceException("Couldn't fetch customer details",exception);
		}
	}
}
