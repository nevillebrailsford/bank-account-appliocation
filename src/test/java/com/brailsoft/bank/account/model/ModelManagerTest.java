package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModelManagerTest {

	static final String DIRECTORY_TEST = "bank.test";
	static File directory = new File(System.getProperty("user.home"), DIRECTORY_TEST);

	private ModelManager modelManager = ModelManager.getInstance(directory);

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
//		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "account.dat"));
//		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "branch.dat"));
//		Files.deleteIfExists(Paths.get(directory.getAbsolutePath()));
	}

	@BeforeEach
	void setUp() throws Exception {
		buildAccountHistory();
		buildBranchHistory();
	}

	@AfterEach
	void tearDown() throws Exception {
		modelManager.clear();
	}

	@Test
	void testGetInstance() {
		assertNotNull(modelManager);
		assertNotNull(modelManager.getDirectory());
		assertEquals(directory, modelManager.getDirectory());
		ModelManager mm = ModelManager.getInstance();
		assertTrue(modelManager == mm);
	}

	@Test
	void testAddListener() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadModel() throws IOException {
		modelManager.loadModel();
	}

	@Test
	void testSaveModel() throws IOException {
		modelManager.saveModel();
	}

	@Test
	void testAddAccount() {
		fail("Not yet implemented");
	}

	@Test
	void testChangeAccount() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveAccount() {
		fail("Not yet implemented");
	}

	@Test
	void testAddBranch() {
		fail("Not yet implemented");
	}

	@Test
	void testChangeBranch() {
		fail("Not yet implemented");
	}

	@Test
	void testRemoveBranch() {
		fail("Not yet implemented");
	}

	private void buildAccountHistory() {
		AccountManager accountManager = AccountManager.getInstance();
		accountManager.add(new Account(AccountType.CURRENT, "account1", "12345678", new SortCode("55-55-55")));
		accountManager.add(new Account(AccountType.CURRENT, "account2", "87654321", new SortCode("55-55-55")));
		accountManager.add(new Account(AccountType.CURRENT, "account3", "12345679", new SortCode("55-55-55")));
		accountManager.add(new Account(AccountType.CURRENT, "account1", "12345678", new SortCode("55-55-56")));
	}

	private void buildBranchHistory() {
		BranchManager branchManager = BranchManager.getInstance();
		for (int i = 0; i < 2; i++) {
			branchManager.add(buildABranch(i));
		}
	}

	private Branch buildABranch(int i) {
		String[] bankname = new String[] { "bank1", "bank2" };
		SortCode[] sortcode = new SortCode[2];
		sortcode[0] = new SortCode("55-55-55");
		sortcode[1] = new SortCode("55-55-56");
		PostCode[] postcode = new PostCode[2];
		postcode[0] = new PostCode("WC2H 7LT");
		postcode[1] = new PostCode("WC2H 7LS");
		Address[] address = new Address[2];
		address[0] = new Address(postcode[0], new String[] { "1 the Street", "the Town", "the County", "the Country" });
		address[1] = new Address(postcode[1], new String[] { "2 the Street", "the Town", "the County", "the Country" });
		Branch br = new Branch(address[i], sortcode[i], bankname[i]);
		return br;
	}

}
