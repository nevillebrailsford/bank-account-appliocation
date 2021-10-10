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

public class AccountManager {
	private static AccountManager instance = null;

	private ObservableMap<SortCode, ObservableList<Account>> accounts = null;

	private ListChangeListener<? super Account> listener;

	public synchronized static AccountManager getInstance() {
		if (instance == null) {
			instance = new AccountManager();
		}
		return instance;
	}

	private AccountManager() {
		accounts = FXCollections.observableMap(new ConcurrentHashMap<>());
		listener = null;
	}

	public synchronized void addMapListener(MapChangeListener<? super SortCode, ? super List<Account>> listener) {
		accounts.addListener(listener);
	}

	public synchronized void removeMapListener(MapChangeListener<? super SortCode, ? super List<Account>> listener) {
		accounts.removeListener(listener);
	}

	public synchronized void addListListener(ListChangeListener<? super Account> listener) {
		if (this.listener != null) {
			throw new IllegalStateException("AccountManager: AccountListListener already defined");
		}
		this.listener = listener;
		addListenerToAllLists(listener);
	}

	public synchronized void removeListListener() {
		if (this.listener == null) {
			throw new IllegalStateException("AccountManager: AccountListListener not defined");
		}
		removeListenerFromAllLists(listener);
		this.listener = null;
	}

	public synchronized void add(Account account) {
		if (account == null) {
			throw new IllegalArgumentException("AccountManager: account was null");
		}
		ObservableList<Account> accountsForSortCode = accounts.get(account.getSortCode());
		if (accountsForSortCode == null) {
			accountsForSortCode = FXCollections.observableArrayList();
			accounts.put(new SortCode(account.getSortCode()), accountsForSortCode);
			if (listener != null) {
				accountsForSortCode.addListener(listener);
			}
		}
		if (accountsForSortCode.contains(account)) {
			throw new IllegalStateException("AccountManager: account already exists");
		} else {
			accountsForSortCode.add(new Account(account));
		}
	}

	public synchronized void change(Account oldAccount, Account newAccount) {
		remove(oldAccount);
		add(newAccount);
	}

	public synchronized void remove(Account account) {
		if (account == null) {
			throw new IllegalArgumentException("AccountManager: account was null");
		}
		ObservableList<Account> accountsForSortCode = accounts.get(account.getSortCode());
		if (accountsForSortCode == null || !accountsForSortCode.contains(account)) {
			throw new IllegalStateException("AccountManager: account not found");
		} else {
			accountsForSortCode.remove(account);
		}
	}

	public synchronized void clear() {
		accounts.clear();
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
		if (sortCode == null) {
			throw new IllegalArgumentException("AccountManager: sortCode was null");
		}
		List<Account> accountList = new ArrayList<>();
		List<Account> accountsForSortCode = accounts.get(sortCode);
		if (accountsForSortCode != null) {
			accountList.addAll(accountsForSortCode);
		}
		Collections.sort(accountList);
		return accountList;
	}

	public synchronized List<Account> getAllAccounts() {
		List<Account> accountList = new ArrayList<>();
		getSortCodes().stream().forEach(sortCode -> {
			accountList.addAll(getAccountsForSortCode(sortCode));
		});
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
