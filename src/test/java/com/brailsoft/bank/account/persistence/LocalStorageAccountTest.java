package com.brailsoft.bank.account.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		try (InputStream inputFile = new BufferedInputStream(new FileInputStream(name))) {
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
		try (OutputStream archiveFile = new BufferedOutputStream(
				new FileOutputStream(new File(createTestFileName(NEW_FILE))))) {
			LocalStorageAccount.archiveDataFromManager(archiveFile);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertTrue((Files.exists(Paths.get(getTestDirectory(), NEW_FILE), LinkOption.NOFOLLOW_LINKS)));
	}

	@Test
	void testReadAndWrite() {
		assertEquals(0, manager.getAllAccounts().size());
		manager.add(new Account(AccountType.CURRENT, "account1", "87654321", new SortCode("44-44-44")));
		assertEquals(1, manager.getAllAccounts().size());
		assertFalse((Files.exists(Paths.get(getTestDirectory(), NEW_FILE))));
		try (OutputStream archiveFile = new BufferedOutputStream(
				new FileOutputStream(new File(createTestFileName(NEW_FILE))))) {
			LocalStorageAccount.archiveDataFromManager(archiveFile);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertTrue((Files.exists(Paths.get(getTestDirectory(), NEW_FILE))));
	}

	@Test
	void testGetCorruptAccountData() {
		assertEquals(0, manager.getAllAccounts().size());
		IOException exception = assertThrows(IOException.class, () -> {
			String name = createTestFileName(CORRUPT_ACCOUNT_TAB);
			try (InputStream inputFile = new BufferedInputStream(new FileInputStream(name))) {
				LocalStorageAccount.clearAndLoadManagerWithArchivedData(inputFile);
			} catch (Exception e) {
				throw new IOException("Exception occurred: " + e.getMessage());
			}
		});
		assertEquals("Exception occurred: XML document structures must start and end within the same entity.",
				exception.getMessage());
	}

	@Test
	void testGetCorruptTypeData() {
		assertEquals(0, manager.getAllAccounts().size());
		IOException exception = assertThrows(IOException.class, () -> {
			String name = createTestFileName(CORRUPT_TYPE_TAB);
			try (InputStream inputFile = new BufferedInputStream(new FileInputStream(name))) {
				LocalStorageAccount.clearAndLoadManagerWithArchivedData(inputFile);
			} catch (Exception e) {
				throw new IOException("Exception occurred: " + e.getMessage());
			}
		});
		assertEquals("Exception occurred: The end-tag for element type \"number\" must end with a '>' delimiter.",
				exception.getMessage());
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
