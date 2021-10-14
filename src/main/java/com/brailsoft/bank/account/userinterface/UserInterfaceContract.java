package com.brailsoft.bank.account.userinterface;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.Branch;

public interface UserInterfaceContract {
	public interface View {

	}

	public interface EventListener {
		public void onBranchAltered(EventBranchAltered branchAltered);

		public void onAccountAltered(EventAccountAltered accountAltered);
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
}
