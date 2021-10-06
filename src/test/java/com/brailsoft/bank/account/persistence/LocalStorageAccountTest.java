package com.brailsoft.bank.account.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brailsoft.bank.account.model.AccountManager;

class LocalStorageAccountTest {

	private static final String FILE_THAT_EXISTS = "/Users/nevil/projects/git-test-repos/bank-account-appliocation/src/test/resources/com/brailsoft/bank/account/persistence/accounts.dat";
	AccountManager manager = AccountManager.getInstance();

	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {
		manager.clear();
	}

	@Test
	void testGetAccountData() throws IOException {
		assertEquals(0, manager.getAllAccounts().size());
		LocalStorageAccount.getAccountData(
				FILE_THAT_EXISTS);
		assertEquals(1, manager.getAllAccounts().size());
	}

	@Test
	void testUpdateAccountData() {
		assertEquals(0, manager.getAllAccounts().size());
	}

}
