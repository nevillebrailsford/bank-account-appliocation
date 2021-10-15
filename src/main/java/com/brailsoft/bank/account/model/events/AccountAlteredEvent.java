package com.brailsoft.bank.account.model.events;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.userinterface.UserInterfaceContract;

public class AccountAlteredEvent implements UserInterfaceContract.EventAccountAltered {
	private boolean wasAdded;
	private Account account;

	public AccountAlteredEvent(Account account, boolean wasAdded) {
		this.wasAdded = wasAdded;
		this.account = new Account(account);
	}

	@Override
	public Account getAccount() {
		return new Account(account);
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
