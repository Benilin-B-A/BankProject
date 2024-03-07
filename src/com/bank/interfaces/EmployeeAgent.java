package com.bank.interfaces;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.pojo.Employee;

public interface EmployeeAgent {

	public boolean getAdminAccess(long userId) throws PersistenceException;

	public long getBranchId(long userId) throws PersistenceException;

	public boolean getEmployeeAccess(long userId, long accNum) throws PersistenceException;

	long addEmployee(Employee emp, String pwd) throws PersistenceException;

	public Employee getEmployee(long userId) throws PersistenceException;

	boolean validateEmployee(long userId) throws PersistenceException;

}
