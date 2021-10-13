package com.brailsoft.bank.account.model;

import java.io.File;
import java.io.IOException;

import com.brailsoft.bank.account.persistence.LocalStorage;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;

public class ModelManager {
	private final static AccountManager accountManager = AccountManager.getInstance();
	private final static BranchManager branchManager = BranchManager.getInstance();
	private LocalStorage localStorage;
	private BranchMapListener branchMapListener;
	private AccountMapListener accountMapListener;
	private AccountListListener accountListListener;

	private static ModelManager instance = null;

	public synchronized static ModelManager getInstance(File... directory) {
		if (instance == null) {
			instance = new ModelManager();
			instance.localStorage = LocalStorage.getInstance(directory);
		}
		return instance;
	}

	private ModelManager() {

	}

	public void loadModel() throws IOException {
		localStorage.clearAndLoadManagerWithArchivedData();
	}

	public void addAccount(Account account) {

	}

	class BranchMapListener implements MapChangeListener<SortCode, Branch> {
		@Override
		public void onChanged(
				javafx.collections.MapChangeListener.Change<? extends SortCode, ? extends Branch> change) {
			// TODO Auto-generated method stub

		}
	}

	class AccountMapListener implements MapChangeListener<SortCode, Account> {
		@Override
		public void onChanged(
				javafx.collections.MapChangeListener.Change<? extends SortCode, ? extends Account> change) {
			// TODO Auto-generated method stub

		}
	}

	class AccountListListener implements ListChangeListener<Account> {

		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends Account> c) {
			// TODO Auto-generated method stub

		}

	}
}
