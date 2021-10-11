package com.brailsoft.bank.account.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.AccountManager;
import com.brailsoft.bank.account.model.AccountType;
import com.brailsoft.bank.account.model.SortCode;

class LocalStorageAccountTest {

	private static final String TEST_FILE = "accounts.dat";
	private static final String NEW_FILE = "newaccounts.dat";
	private static final String CORRUPT_ACCOUNT_TAB = "corruptAccount.dat";
	private static final String CORRUPT_TYPE_TAB = "corruptTab.dat";
	private static final String FILE_CONTENTS = "<Account><type>CURRENT</type><name>account1</name><number>87654321</number><sortcode>44-44-44</sortcode></Account>";
	AccountManager manager = AccountManager.getInstance();

	@BeforeEach
	void setUp() throws Exception {
		Files.deleteIfExists(Paths.get(getTestDirectory(), NEW_FILE));
	}

	@AfterEach
	void tearDown() throws Exception {
		manager.clear();
		Files.deleteIfExists(Paths.get(getTestDirectory(), NEW_FILE));
	}

	@Test
	void testGetAccountData() {
		assertEquals(0, manager.getAllAccounts().size());
		String name = createTestFileName(TEST_FILE);
		try (BufferedReader inputFile = new BufferedReader(new FileReader(name))) {
			LocalStorageAccount.clearAndLoadManagerWithArchivedData(inputFile);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertEquals(1, manager.getAllAccounts().size());
	}

	@Test
	void testUpdateAccountData() {
		assertEquals(0, manager.getAllAccounts().size());
		manager.add(new Account(AccountType.CURRENT, "account1", "87654321", new SortCode("44-44-44")));
		assertEquals(1, manager.getAllAccounts().size());
		assertFalse((Files.exists(Paths.get(getTestDirectory(), NEW_FILE), LinkOption.NOFOLLOW_LINKS)));
		try (PrintWriter archiveFile = new PrintWriter(
				new BufferedWriter(new FileWriter(createTestFileName(NEW_FILE))))) {
			LocalStorageAccount.archiveDataFromManager(archiveFile);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertTrue((Files.exists(Paths.get(getTestDirectory(), NEW_FILE), LinkOption.NOFOLLOW_LINKS)));
		int numberOfRecords = 0;
		try (BufferedReader inputFile = new BufferedReader(new FileReader(createTestFileName(NEW_FILE)))) {
			do {
				assertEquals(FILE_CONTENTS, inputFile.readLine());
				numberOfRecords++;
			} while (inputFile.ready());
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertEquals(1, numberOfRecords);
	}

	@Test
	void testReadAndWrite() {
		assertEquals(0, manager.getAllAccounts().size());
		manager.add(new Account(AccountType.CURRENT, "account1", "87654321", new SortCode("44-44-44")));
		assertEquals(1, manager.getAllAccounts().size());
		assertFalse((Files.exists(Paths.get(getTestDirectory(), NEW_FILE), LinkOption.NOFOLLOW_LINKS)));
		try (PrintWriter archiveFile = new PrintWriter(
				new BufferedWriter(new FileWriter(createTestFileName(NEW_FILE))))) {
			LocalStorageAccount.archiveDataFromManager(archiveFile);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertTrue((Files.exists(Paths.get(getTestDirectory(), NEW_FILE), LinkOption.NOFOLLOW_LINKS)));
		String name = createTestFileName(NEW_FILE);
		try (BufferedReader inputFile = new BufferedReader(new FileReader(name))) {
			LocalStorageAccount.clearAndLoadManagerWithArchivedData(inputFile);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertEquals(1, manager.getAllAccounts().size());
	}

	@Test
	void testGetCorruptAccountData() {
		assertEquals(0, manager.getAllAccounts().size());
		IOException exception = assertThrows(IOException.class, () -> {
			String name = createTestFileName(CORRUPT_ACCOUNT_TAB);
			try (BufferedReader inputFile = new BufferedReader(new FileReader(name))) {
				LocalStorageAccount.clearAndLoadManagerWithArchivedData(inputFile);
			} catch (Exception e) {
				throw new IOException("Exception occurred: " + e.getMessage());
			}
		});
		assertEquals("Exception occurred: LocalStorageAccount: Account missing from entry", exception.getMessage());
	}

	@Test
	void testGetCorruptTypeData() {
		assertEquals(0, manager.getAllAccounts().size());
		IOException exception = assertThrows(IOException.class, () -> {
			String name = createTestFileName(CORRUPT_TYPE_TAB);
			try (BufferedReader inputFile = new BufferedReader(new FileReader(name))) {
				LocalStorageAccount.clearAndLoadManagerWithArchivedData(inputFile);
			} catch (Exception e) {
				throw new IOException("Exception occurred: " + e.getMessage());
			}
		});
		assertEquals("Exception occurred: LocalStorageBase: tab not found: </type>", exception.getMessage());
	}

	private String getTestDirectory() {
		String fileName = getClass().getResource(TEST_FILE).toExternalForm().substring(5);
		File file = new File(fileName);
		File directory = file.getParentFile();
		return directory.getAbsolutePath();
	}

	private String createTestFileName(String testFileName) {
		File file = new File(getTestDirectory(), testFileName);
		return file.getAbsolutePath();
	}

}
