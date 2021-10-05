package com.brailsoft.bank.account.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class BankManager {
	private static BankManager instance = null;

	private ObservableMap<SortCode, ObservableList<Account>> accounts = null;

	private ListChangeListener<? super Account> listener;

	public synchronized static BankManager getInstance() {
		if (instance == null) {
			instance = new BankManager();
		}
		return instance;
	}

	private BankManager() {
		accounts = FXCollections.observableMap(new ConcurrentHashMap<>());
		listener = null;
	}

	public synchronized void addAccountListener(MapChangeListener<? super SortCode, ? super List<Account>> listener) {
		accounts.addListener(listener);
	}

	public synchronized void removeAccountListener(
			MapChangeListener<? super SortCode, ? super List<Account>> listener) {
		accounts.removeListener(listener);
	}

	public synchronized void addAccountListListener(ListChangeListener<? super Account> listener) {
		if (this.listener != null) {
			throw new IllegalStateException("AccountListListener already defined");
		}
		this.listener = listener;
		addListenerToAllLists(listener);
	}

	public synchronized void removeAccountListListener() {
		if (listener == null) {
			throw new IllegalStateException("AccountListListener not defined");
		}
		removeListenerFromAllLists(listener);
		this.listener = null;
	}

	public synchronized void addAccount(Account account) {
		ObservableList<Account> accountsForSortCode = accounts.get(account.getSortCode());
		if (accountsForSortCode == null) {
			accountsForSortCode = FXCollections.observableArrayList();
			accounts.put(new SortCode(account.getSortCode()), accountsForSortCode);
			if (listener != null) {
				accountsForSortCode.addListener(listener);
			}
		}
		if (accountsForSortCode.contains(account)) {
			throw new IllegalStateException("BankManager: account already exists");
		} else {
			accountsForSortCode.add(new Account(account));
		}
	}

	public synchronized void changeAccount(Account oldAccount, Account newAccount) {
		removeAccount(oldAccount);
		addAccount(newAccount);
	}

	public synchronized void removeAccount(Account account) {
		List<Account> accountsForSortCode = accounts.get(account.getSortCode());
		if (accountsForSortCode == null || !accountsForSortCode.contains(account)) {
			throw new IllegalStateException("BankManager: account not found");
		} else {
			accountsForSortCode.remove(account);
		}
	}

	public synchronized void clearAccounts() {
		accounts.clear();
		listener = null;
	}

	public synchronized List<SortCode> getSortCodes() {
		List<SortCode> sortCodeList = new ArrayList<>();
		accounts.keySet().stream().forEach(sortCode -> {
			sortCodeList.add(new SortCode(sortCode));
		});
		Collections.sort(sortCodeList);
		return sortCodeList;
	}

	public synchronized List<Account> getAccountsForSortCode(SortCode sortCode) {
		List<Account> accountList = new ArrayList<>();
		List<Account> accountsForSortCode = accounts.get(sortCode);
		if (accountsForSortCode != null) {
			accountList.addAll(accountsForSortCode);
		}
		Collections.sort(accountList);
		return accountList;
	}

	private ObservableList<Account> getAccountListForSortCode(SortCode sortCode) {
		return accounts.get(sortCode);
	}

	private void addListenerToAllLists(ListChangeListener<? super Account> listener) {
		getSortCodes().stream().forEach(sc -> {
			getAccountListForSortCode(sc).addListener(listener);
		});

	}

	private void removeListenerFromAllLists(ListChangeListener<? super Account> listener) {
		getSortCodes().stream().forEach(sc -> {
			getAccountListForSortCode(sc).removeListener(listener);
		});

	}

}
