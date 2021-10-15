package com.brailsoft.bank.account.userinterface;

import java.util.List;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.Branch;
import com.brailsoft.bank.account.model.SortCode;

public interface UserInterfaceContract {
	public interface View {

	}

	public interface EventListener {
		public void onBranchAltered(EventBranchAltered branchAltered);

		public void onAccountAltered(EventAccountAltered accountAltered);

		public void onSortCodeAltered(EventSortCodeAltered sortcodeAltered);
	}

	public interface EventBranchAltered {
		public Branch getBranch();

		public boolean wasAdded();

		public boolean wasRemoved();
	}

	public interface EventAccountAltered {
		public Account getAccount();

		public boolean wasAdded();

		public boolean wasRemoved();
	}

	public interface EventSortCodeAltered {
		public SortCode getSortCode();

		public List<Account> getListOfAccounts();

		public boolean wasAdded();

		public boolean wasRemoved();
	}
}
