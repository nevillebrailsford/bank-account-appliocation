package com.brailsoft.bank.account.userinterface;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.Branch;

public interface UserInterfaceContract {
	interface View {

	}

	interface EventListener {
		public void onBranchAltered(Branch... branch);

		public void onAccountAltered(Account... account);
	}
}
