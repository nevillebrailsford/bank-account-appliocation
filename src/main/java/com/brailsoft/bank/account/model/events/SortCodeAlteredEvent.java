package com.brailsoft.bank.account.model.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.SortCode;
import com.brailsoft.bank.account.userinterface.UserInterfaceContract;

public class SortCodeAlteredEvent implements UserInterfaceContract.EventSortCodeAltered {
	private boolean wasadded;
	private SortCode sortcode;
	private List<Account> listofaccounts;

	public SortCodeAlteredEvent(SortCode sortcode, List<Account> listofaccounts, boolean wasadded) {
		this.wasadded = wasadded;
		this.sortcode = sortcode;
		this.listofaccounts = listofaccounts;
	}

	@Override
	public SortCode getSortCode() {
		return new SortCode(sortcode);
	}

	@Override
	public List<Account> getListOfAccounts() {
		List<Account> copyList = new ArrayList<>();
		listofaccounts.stream().forEach(account -> {
			copyList.add(new Account(account));
		});
		Collections.sort(copyList);
		return copyList;
	}

	@Override
	public boolean wasAdded() {
		return wasadded;
	}

	@Override
	public boolean wasRemoved() {
		return !wasadded;
	}

}
