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

import com.brailsoft.bank.account.model.Address;
import com.brailsoft.bank.account.model.Branch;
import com.brailsoft.bank.account.model.BranchManager;
import com.brailsoft.bank.account.model.PostCode;
import com.brailsoft.bank.account.model.SortCode;

class LocalStorageBranchTest {

	private static final String TEST_FILE = "branches.dat";
	private static final String NEW_FILE = "newbranches.dat";
	private static final String CORRUPT_BRANCH_TAB = "corruptBranch.dat";
	private static final String CORRUPT_SORTCODE_TAB = "corruptSortCode.dat";
	private static final String FILE_CONTENTS = "<Branch><sortcode>55-55-55</sortcode><bankname>bankname</bankname><Address><postcode>CW3 9ST</postcode><linecount>4</linecount><line>99 The Street</line><line>The Town</line><line>The County</line><line>Country</line></Address></Branch>";

	private static final String BANK_NAME = "bankname";
	private static final PostCode POST_CODE = new PostCode("CW3 9ST");
	private static final String LINE1 = "99 The Street";
	private static final String LINE2 = "The Town";
	private static final String LINE3 = "The County";
	private static final String LINE4 = "Country";
	private String[] lines = new String[] { LINE1, LINE2, LINE3, LINE4 };

	Address address = new Address(POST_CODE, lines);
	SortCode sortcode = new SortCode("55-55-55");

	BranchManager manager = BranchManager.getInstance();

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
	void testClearAndLoadManagerWithArchivedData() {
		assertEquals(0, manager.getAllBranches().size());
		String name = createTestFileName(TEST_FILE);
		try (BufferedReader inputFile = new BufferedReader(new FileReader(name))) {
			LocalStorageBranch.clearAndLoadManagerWithArchivedData(inputFile);
		} catch (Exception e) {
			fail("Exception occurred: " + e.getMessage());
		}
		assertEquals(1, manager.getAllBranches().size());
	}

	@Test
	void testArchiveDataFromManager() {
		assertEquals(0, manager.getAllBranches().size());
		manager.add(new Branch(address, sortcode, BANK_NAME));
		assertEquals(1, manager.getAllBranches().size());
		assertFalse((Files.exists(Paths.get(getTestDirectory(), NEW_FILE), LinkOption.NOFOLLOW_LINKS)));
		try (PrintWriter archiveFile = new PrintWriter(
				new BufferedWriter(new FileWriter(createTestFileName(NEW_FILE))))) {
			LocalStorageBranch.archiveDataFromManager(archiveFile);
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
	void testCorruptBranchData() {
		assertEquals(0, manager.getAllBranches().size());
		IOException exception = assertThrows(IOException.class, () -> {
			String name = createTestFileName(CORRUPT_BRANCH_TAB);
			try (BufferedReader inputFile = new BufferedReader(new FileReader(name))) {
				LocalStorageBranch.clearAndLoadManagerWithArchivedData(inputFile);
			} catch (Exception e) {
				throw new IOException("Exception occurred: " + e.getMessage());
			}
		});
		assertEquals("Exception occurred: LocalStorageAccount: Branch missing from entry", exception.getMessage());

	}

	@Test
	void testGetCorruptTypeData() {
		assertEquals(0, manager.getAllBranches().size());
		IOException exception = assertThrows(IOException.class, () -> {
			String name = createTestFileName(CORRUPT_SORTCODE_TAB);
			try (BufferedReader inputFile = new BufferedReader(new FileReader(name))) {
				LocalStorageBranch.clearAndLoadManagerWithArchivedData(inputFile);
			} catch (Exception e) {
				throw new IOException("Exception occurred: " + e.getMessage());
			}
		});
		assertEquals("Exception occurred: LocalStorageBase: tab not found: </sortcode>", exception.getMessage());
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
