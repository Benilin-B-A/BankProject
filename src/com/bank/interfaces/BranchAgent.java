package com.bank.interfaces;

import com.bank.custom.exceptions.PersistenceException;
import com.bank.pojo.Branch;

public interface BranchAgent {

	void addBranch(Branch branch) throws PersistenceException;

	boolean validateIFSC(String iFSC) throws PersistenceException;

	boolean validateBranchId(long branchID) throws PersistenceException;

}
