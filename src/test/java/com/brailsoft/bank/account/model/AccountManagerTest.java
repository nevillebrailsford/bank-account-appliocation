package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.MapChangeListener.Change;

class AccountManagerTest {

	private static final String ACCOUNT_NAME_CHANGED = "change";
	private static final String ACCOUNT_NUMBER = "12345678";
	private static final String ACCOUNT_NAME = "account";
	private static final String SORT_CODE_VALUE = "55-55-55";

	private AccountManager accountManager;
	SortCode sortCode = new SortCode(SORT_CODE_VALUE);
	Account account = new Account(AccountType.CURRENT, ACCOUNT_NAME, ACCOUNT_NUMBER, sortCode);
	Account changedAccount = new Account(AccountType.CURRENT, ACCOUNT_NAME_CHANGED, ACCOUNT_NUMBER, sortCode);
	Account accountToBeAdded = null;

	private boolean listenerFailed = false;
	private Change<? extends SortCode, ? extends List<Account>> change = null;

	private MapChangeListener<? super SortCode, ? super List<Account>> listener = (
			MapChangeListener.Change<? extends SortCode, ? extends List<Account>> c) -> {
		if (c.wasAdded()) {
			assertEquals(sortCode, c.getKey());
		} else {
			listenerFailed = true;
			change = c;
		}
	};

	private boolean listListenerFailed = false;
	private ListChangeListener.Change<? extends Account> listChange = null;

	private ListChangeListener<? super Account> listListener = new ListChangeListener<>() {

		@Override
		public void onChanged(ListChangeListener.Change<? extends Account> c) {
			while (c.next()) {
				if (c.wasAdded()) {
					assertTrue(account.equals(c.getAddedSubList().get(0))
							|| changedAccount.equals(c.getAddedSubList().get(0)));
				} else {
					listListenerFailed = true;
					listChange = c;
				}
			}
		}
	};

	@BeforeEach
	void setUp() throws Exception {
		listenerFailed = false;
		accountManager = AccountManager.getInstance();
	}

	@AfterEach
	void tearDown() throws Exception {
		accountManager.clear();
		accountManager.removeMapListener(listener);
		try {
			accountManager.removeListListener();
		} catch (IllegalStateException e) {

		}
	}

	@Test
	void testGetInstance() {
		assertNotNull(accountManager);
		AccountManager bm = AccountManager.getInstance();
		assertNotNull(bm);
		assertTrue(bm == accountManager);
	}

	@Test
	void testAddAccountListener() {
		accountManager.addMapListener(listener);
		accountManager.add(account);
		accountManager.removeMapListener(listener);
		if (listenerFailed) {
			fail("Undexpected operation: " + change);
		}
	}

	@Test
	void testRemoveAccountListener() {
		accountManager.addMapListener(listener);
		accountManager.removeMapListener(listener);
	}

	@Test
	void testAddAccountListListener() {
		accountManager.addListListener(listListener);
		accountManager.add(account);
		accountManager.removeListListener();
		if (listListenerFailed) {
			fail("Undexpected operation: " + listChange);
		}
	}

	@Test
	void testRemoveAccountListListener() {
		accountManager.addListListener(listListener);
		accountManager.removeListListener();
	}

	@Test
	void testAddAccount() {
		assertEquals(0, accountManager.getAccountsForSortCode(sortCode).size());
		accountToBeAdded = account;
		accountManager.add(account);
		assertEquals(1, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, accountManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(1, accountManager.getSortCodes().size());
	}

	@Test
	void testChangeAccount() {
		assertEquals(0, accountManager.getAccountsForSortCode(sortCode).size());
		accountManager.add(account);
		assertEquals(1, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, accountManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(1, accountManager.getSortCodes().size());
		accountManager.change(account, changedAccount);
		assertEquals(1, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(changedAccount, accountManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(1, accountManager.getSortCodes().size());
	}

	@Test
	void testRemoveAccount() {
		assertEquals(0, accountManager.getAccountsForSortCode(sortCode).size());
		accountManager.add(account);
		assertEquals(1, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, accountManager.getAccountsForSortCode(sortCode).get(0));
		accountManager.remove(account);
		assertEquals(0, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(1, accountManager.getSortCodes().size());
	}

	@Test
	void testClearAccounts() {
		assertEquals(0, accountManager.getAccountsForSortCode(sortCode).size());
		accountManager.add(account);
		assertEquals(1, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, accountManager.getAccountsForSortCode(sortCode).get(0));
		accountManager.clear();
		assertEquals(0, accountManager.getSortCodes().size());
	}

	@Test
	void testGetSortCodes() {
		assertEquals(0, accountManager.getAccountsForSortCode(sortCode).size());
		accountManager.add(account);
		assertEquals(1, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, accountManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(sortCode, accountManager.getSortCodes().get(0));
	}

	@Test
	void testGetAccountsForSortCode() {
		assertEquals(0, accountManager.getAccountsForSortCode(sortCode).size());
		accountManager.add(account);
		assertEquals(1, accountManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, accountManager.getAccountsForSortCode(sortCode).get(0));
	}

	@Test
	void testGetAllAccounts() {
		addSeveralAccounts();
		assertEquals(2, accountManager.getSortCodes().size());
		assertEquals(4, accountManager.getAllAccounts().size());
	}

	@Test
	void testDuplicateAddListListener() {
		assertThrows(IllegalStateException.class, () -> {
			accountManager.addListListener(listListener);
			accountManager.addListListener(listListener);
		});
	}

	@Test
	void testMissingAddListListener() {
		assertThrows(IllegalStateException.class, () -> {
			accountManager.removeListListener();
		});
	}

	@Test
	void testAddNullAccount() {
		assertThrows(IllegalArgumentException.class, () -> {
			accountManager.add(null);
		});
	}

	@Test
	void testRemoveNullAccount() {
		assertThrows(IllegalArgumentException.class, () -> {
			accountManager.remove(null);
		});
	}

	@Test
	void testChangeNullOldAccount() {
		assertThrows(IllegalArgumentException.class, () -> {
			accountManager.change(null, changedAccount);
		});
	}

	@Test
	void testChangeNullNewAccount() {
		accountManager.add(account);
		assertThrows(IllegalArgumentException.class, () -> {
			accountManager.change(account, null);
		});
	}

	@Test
	void testChangeNullBothAccounts() {
		assertThrows(IllegalArgumentException.class, () -> {
			accountManager.change(null, null);
		});
	}

	@Test
	void testChangeMissingAccount() {
		assertThrows(IllegalStateException.class, () -> {
			accountManager.change(account, changedAccount);
		});
	}

	private void addSeveralAccounts() {
		accountManager.add(new Account(AccountType.CURRENT, "account1", "12345678", new SortCode("55-55-55")));
		accountManager.add(new Account(AccountType.CURRENT, "account2", "87654321", new SortCode("55-55-55")));
		accountManager.add(new Account(AccountType.CURRENT, "account3", "12345679", new SortCode("55-55-55")));
		accountManager.add(new Account(AccountType.CURRENT, "account1", "12345678", new SortCode("55-55-56")));
	}

}
