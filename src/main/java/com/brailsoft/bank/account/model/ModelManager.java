package com.brailsoft.bank.account.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.brailsoft.bank.account.model.events.AccountAlteredEvent;
import com.brailsoft.bank.account.model.events.BranchAlteredEvent;
import com.brailsoft.bank.account.model.events.SortCodeAlteredEvent;
import com.brailsoft.bank.account.persistence.LocalStorage;
import com.brailsoft.bank.account.userinterface.UserInterfaceContract;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;

public class ModelManager {
	private final static AccountManager accountManager = AccountManager.getInstance();
	private final static BranchManager branchManager = BranchManager.getInstance();
	private LocalStorage localStorage;
	private UserInterfaceContract.EventListener listener;
	private BranchMapListener branchMapListener = new BranchMapListener();
	private AccountListListener accountListListener = new AccountListListener();
	private AccountMapListener accountMapListener = new AccountMapListener();

	private static ModelManager instance = null;

	public synchronized static ModelManager getInstance(File... directory) {
		if (instance == null) {
			instance = new ModelManager();
			instance.localStorage = LocalStorage.getInstance(directory);
			branchManager.addMapListener(instance.branchMapListener);
			accountManager.addListListener(instance.accountListListener);
			accountManager.addMapListener(instance.accountMapListener);
		}
		return instance;
	}

	private ModelManager() {
	}

	public synchronized void clear() {
		accountManager.clear();
		branchManager.clear();
	}

	public void removeListeners() {
		branchManager.removeMapListener(branchMapListener);
		accountManager.removeListListener();
		accountManager.removeMapListener(accountMapListener);
	}

	public File getDirectory() {
		return localStorage.getDirectory();
	}

	public void addListener(UserInterfaceContract.EventListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("ModelManager: listener is null");
		}
		if (this.listener != null) {
			throw new IllegalStateException("Modelmanager: listener already defined");
		}
		this.listener = listener;
	}

	public void removeListener() {
		this.listener = null;
	}

	public void loadModel() throws IOException {
		localStorage.clearAndLoadManagerWithArchivedData();
	}

	public void saveModel() throws IOException {
		localStorage.archiveDataFromManager();
	}

	public void addAccount(Account account) {
		accountManager.add(new Account(account));
	}

	public void changeAccount(Account oldAccount, Account newAccount) {
		accountManager.change(new Account(oldAccount), new Account(newAccount));
	}

	public void removeAccount(Account account) {
		accountManager.remove(new Account(account));
	}

	public void addBranch(Branch branch) {
		branchManager.add(new Branch(branch));
	}

	public void changeBranch(Branch oldBranch, Branch newBranch) {
		branchManager.change(new Branch(oldBranch), new Branch(newBranch));
	}

	public void removeBranch(Branch branch) {
		branchManager.remove(new Branch(branch));
	}

	private class BranchMapListener implements MapChangeListener<SortCode, Branch> {
		@Override
		public void onChanged(
				javafx.collections.MapChangeListener.Change<? extends SortCode, ? extends Branch> change) {
			BranchAlteredEvent event;
			if (change.wasAdded()) {
				event = new BranchAlteredEvent(new Branch(change.getValueAdded()), true);
			} else {
				event = new BranchAlteredEvent(new Branch(change.getValueRemoved()), false);
			}
			if (ModelManager.this.listener != null) {
				ModelManager.this.listener.onBranchAltered(event);
			}
			try {
				ModelManager.this.saveModel();
			} catch (IOException e) {
				throw new IllegalStateException("ModelManager: IO exception has occurred : " + e.getMessage());
			}
		}
	}

	private class AccountMapListener implements MapChangeListener<SortCode, List<Account>> {
		@Override
		public void onChanged(
				javafx.collections.MapChangeListener.Change<? extends SortCode, ? extends List<Account>> change) {
			SortCodeAlteredEvent event;
			if (change.wasAdded()) {
				event = new SortCodeAlteredEvent(change.getKey(), change.getValueAdded(), true);
			} else {
				event = new SortCodeAlteredEvent(change.getKey(), change.getValueRemoved(), false);
			}
			if (ModelManager.this.listener != null) {
				ModelManager.this.listener.onSortCodeAltered(event);
			}
			try {
				ModelManager.this.saveModel();
			} catch (IOException e) {
				throw new IllegalStateException("ModelManager: IO exception has occurred : " + e.getMessage());
			}
		}
	}

	private class AccountListListener implements ListChangeListener<Account> {

		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends Account> change) {
			AccountAlteredEvent event;
			change.next();
			do {
				if (change.wasAdded()) {
					event = new AccountAlteredEvent(new Account(change.getAddedSubList().get(0)), true);
				} else {
					event = new AccountAlteredEvent(new Account(change.getRemoved().get(0)), false);
				}
			} while (change.next());
			if (ModelManager.this.listener != null) {
				ModelManager.this.listener.onAccountAltered(event);
			}
			try {
				ModelManager.this.saveModel();
			} catch (IOException e) {
				throw new IllegalStateException("ModelManager: I/O exception has occurred : " + e.getMessage());
			}
		}

	}

}
