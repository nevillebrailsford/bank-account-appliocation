package com.brailsoft.bank.account.model.events;

import com.brailsoft.bank.account.model.Branch;
import com.brailsoft.bank.account.userinterface.UserInterfaceContract;

public class BranchAlteredEvent implements UserInterfaceContract.EventBranchAltered {
	private boolean wasAdded;
	private Branch branch;

	public BranchAlteredEvent(Branch branch, boolean wasAdded) {
		this.wasAdded = wasAdded;
		this.branch = new Branch(branch);
	}

	@Override
	public Branch getBranch() {
		return new Branch(branch);
	}

	@Override
	public boolean wasAdded() {
		return wasAdded;
	}

	@Override
	public boolean wasRemoved() {
		return !wasAdded;
	}

}
