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

class BankManagerTest {

	private static final String ACCOUNT_NAME_CHANGED = "change";
	private static final String ACCOUNT_NUMBER = "12345678";
	private static final String ACCOUNT_NAME = "account";
	private static final String SORT_CODE_VALUE = "55-55-55";

	private BankManager bankManager;
	SortCode sortCode = new SortCode(SORT_CODE_VALUE);
	Account account = new Account(AccountType.CURRENT, ACCOUNT_NAME, ACCOUNT_NUMBER, sortCode);
	Account changedAccount = new Account(AccountType.CURRENT, ACCOUNT_NAME_CHANGED, ACCOUNT_NUMBER, sortCode);

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
					assertEquals(account, c.getAddedSubList().get(0));
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
		bankManager = BankManager.getInstance();
	}

	@AfterEach
	void tearDown() throws Exception {
		bankManager.removeAccountListener(listener);
		bankManager.clearAccounts();
	}

	@Test
	void testGetInstance() {
		assertNotNull(bankManager);
		BankManager bm = BankManager.getInstance();
		assertNotNull(bm);
		assertTrue(bm == bankManager);
	}

	@Test
	void testAddAccountListener() {
		bankManager.addAccountListener(listener);
		bankManager.addAccount(account);
		bankManager.removeAccountListener(listener);
		if (listenerFailed) {
			fail("Undexpected operation: " + change);
		}
	}

	@Test
	void testRemoveAccountListener() {
		bankManager.addAccountListener(listener);
		bankManager.removeAccountListener(listener);
	}

	@Test
	void testAddAccountListListener() {
		bankManager.addAccountListListener(listListener);
		bankManager.addAccount(account);
		bankManager.removeAccountListListener();
		if (listListenerFailed) {
			fail("Undexpected operation: " + listChange);
		}
	}

	@Test
	void testRemoveAccountListListener() {
		bankManager.addAccountListListener(listListener);
		bankManager.removeAccountListListener();
	}

	@Test
	void testAddAccount() {
		assertEquals(0, bankManager.getAccountsForSortCode(sortCode).size());
		bankManager.addAccount(account);
		assertEquals(1, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, bankManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(1, bankManager.getSortCodes().size());
	}

	@Test
	void testChangeAccount() {
		assertEquals(0, bankManager.getAccountsForSortCode(sortCode).size());
		bankManager.addAccount(account);
		assertEquals(1, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, bankManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(1, bankManager.getSortCodes().size());
		bankManager.changeAccount(account, changedAccount);
		assertEquals(1, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(changedAccount, bankManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(1, bankManager.getSortCodes().size());
	}

	@Test
	void testRemoveAccount() {
		assertEquals(0, bankManager.getAccountsForSortCode(sortCode).size());
		bankManager.addAccount(account);
		assertEquals(1, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, bankManager.getAccountsForSortCode(sortCode).get(0));
		bankManager.removeAccount(account);
		assertEquals(0, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(1, bankManager.getSortCodes().size());
	}

	@Test
	void testClearAccounts() {
		assertEquals(0, bankManager.getAccountsForSortCode(sortCode).size());
		bankManager.addAccount(account);
		assertEquals(1, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, bankManager.getAccountsForSortCode(sortCode).get(0));
		bankManager.clearAccounts();
		assertEquals(0, bankManager.getSortCodes().size());
	}

	@Test
	void testGetSortCodes() {
		assertEquals(0, bankManager.getAccountsForSortCode(sortCode).size());
		bankManager.addAccount(account);
		assertEquals(1, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, bankManager.getAccountsForSortCode(sortCode).get(0));
		assertEquals(sortCode, bankManager.getSortCodes().get(0));
	}

	@Test
	void testGetAccountsForSortCode() {
		assertEquals(0, bankManager.getAccountsForSortCode(sortCode).size());
		bankManager.addAccount(account);
		assertEquals(1, bankManager.getAccountsForSortCode(sortCode).size());
		assertEquals(account, bankManager.getAccountsForSortCode(sortCode).get(0));
	}

	@Test
	void testDuplicateAddListListener() {
		assertThrows(IllegalStateException.class, () -> {
			bankManager.addAccountListListener(listListener);
			bankManager.addAccountListListener(listListener);
		});
	}

	@Test
	void testMissingAddListListener() {
		assertThrows(IllegalStateException.class, () -> {
			bankManager.removeAccountListListener();
		});
	}

}
