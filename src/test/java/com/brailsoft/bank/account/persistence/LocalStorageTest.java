package com.brailsoft.bank.account.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.AccountManager;
import com.brailsoft.bank.account.model.AccountType;
import com.brailsoft.bank.account.model.Address;
import com.brailsoft.bank.account.model.Branch;
import com.brailsoft.bank.account.model.BranchManager;
import com.brailsoft.bank.account.model.PostCode;
import com.brailsoft.bank.account.model.SortCode;

class LocalStorageTest {

	static final String DIRECTORY_TEST = "bank.test";
	static final String DIRECTORY_SWITCH = "bank.switch";
	File directory = new File(System.getProperty("user.home"), DIRECTORY_TEST);
	File directory_switch = new File(System.getProperty("user.home"), DIRECTORY_SWITCH);
	LocalStorage storage = LocalStorage.getInstance(directory);

	private static final String ACCOUNT_NUMBER = "12345678";
	private static final String ACCOUNT_NAME = "account";
	private static final String SORT_CODE_VALUE = "55-55-55";

	SortCode sortCode = new SortCode(SORT_CODE_VALUE);
	Account account = new Account(AccountType.CURRENT, ACCOUNT_NAME, ACCOUNT_NUMBER, sortCode);

	private static final PostCode postCode = new PostCode("WC2H 7LT");
	private static final String LINE1 = "99 The Street";
	private static final String LINE2 = "The Town";
	private static final String LINE3 = "The County";
	private static final String LINE4 = "Country";
	private static final String[] linesOfAddress = new String[] { LINE1, LINE2, LINE3, LINE4 };
	private static final String BANK_NAME = "Bank Name";
	private Address address = new Address(postCode, linesOfAddress);

	private Branch branch = new Branch(address, sortCode, BANK_NAME);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "account.dat"));
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "branch.dat"));
		Files.deleteIfExists(Paths.get(directory_switch.getAbsolutePath(), "account.dat"));
		Files.deleteIfExists(Paths.get(directory_switch.getAbsolutePath(), "branch.dat"));
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath()));
		Files.deleteIfExists(Paths.get(directory_switch.getAbsolutePath()));

		AccountManager.getInstance().clear();
		BranchManager.getInstance().clear();
	}

	@Test
	void testLocalStorageGetInstance() {
		assertNotNull(storage);
		LocalStorage ls = LocalStorage.getInstance();
		assertNotNull(ls);
		assertTrue(storage == ls);
		assertEquals(storage.getDirectory(), ls.getDirectory());
		assertTrue(storage.getDirectory().getAbsolutePath().endsWith(DIRECTORY_TEST));
		assertTrue(ls.getDirectory().getAbsolutePath().endsWith(DIRECTORY_TEST));
	}

	@Test
	void testChangeDirectory() {
		assertTrue(storage.getDirectory().getAbsolutePath().endsWith(DIRECTORY_TEST));
		LocalStorage ls = LocalStorage.getInstance(directory_switch);
		assertTrue(ls.getDirectory().getAbsolutePath().endsWith(DIRECTORY_SWITCH));
		assertTrue(storage.getDirectory().getAbsolutePath().endsWith(DIRECTORY_SWITCH));
	}

	@Test
	void testArchiveDataFromManager() throws IOException {
		AccountManager.getInstance().add(account);
		BranchManager.getInstance().add(branch);
		storage.archiveDataFromManager();
		assertTrue(fileExistsAndIsValid(new File(directory, "account.dat")));
		assertTrue(fileExistsAndIsValid(new File(directory, "branch.dat")));
	}

	@Test
	void testClearAndLoadManagerWithArchivedData() throws IOException {
		AccountManager.getInstance().add(account);
		BranchManager.getInstance().add(branch);
		storage.archiveDataFromManager();
		storage.clearAndLoadManagerWithArchivedData();
		assertEquals(1, AccountManager.getInstance().getAllAccounts().size());
		assertEquals(account, AccountManager.getInstance().getAllAccounts().get(0));
		assertEquals(1, BranchManager.getInstance().getAllBranches().size());
		assertEquals(branch, BranchManager.getInstance().getAllBranches().get(0));
	}

	private boolean fileExistsAndIsValid(File file) {
		if (file.exists()) {
			int noOfRecords = 0;
			try (BufferedReader inputFile = new BufferedReader(new FileReader(file))) {
				do {
					inputFile.readLine();
					noOfRecords++;
				} while (inputFile.ready());
			} catch (Exception e) {
				return false;
			}
			if (noOfRecords != 1) {
				return false;
			}
			return true;
		}
		return false;
	}

}
