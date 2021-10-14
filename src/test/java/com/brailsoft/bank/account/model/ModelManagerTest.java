package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brailsoft.bank.account.userinterface.UserInterfaceContract;
import com.brailsoft.bank.account.userinterface.UserInterfaceContract.EventAccountAltered;
import com.brailsoft.bank.account.userinterface.UserInterfaceContract.EventBranchAltered;

class ModelManagerTest {

	static final String DIRECTORY_TEST = "bank.test";
	static File directory = new File(System.getProperty("user.home"), DIRECTORY_TEST);

	private ModelManager modelManager = ModelManager.getInstance(directory);

	private UserInterfaceContract.EventListener listener = new UserInterfaceContract.EventListener() {

		@Override
		public void onBranchAltered(EventBranchAltered branchAltered) {
			assertTrue(branchAltered.wasAdded());
			assertEquals(buildNewBranch(), branchAltered.getBranch());
		}

		@Override
		public void onAccountAltered(EventAccountAltered accountAltered) {
			assertTrue(accountAltered.wasAdded());
			assertEquals(buildNewAccount(), accountAltered.getAccount());
		}

	};

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "account.dat"));
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "branch.dat"));
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath()));
		ModelManager.getInstance().removeListeners();
	}

	@BeforeEach
	void setUp() throws Exception {
		buildAccountHistory();
		buildBranchHistory();
	}

	@AfterEach
	void tearDown() throws Exception {
		modelManager.removeListener(listener);
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
		modelManager.addListener(listener);
		modelManager.addAccount(buildNewAccount());
		modelManager.addBranch(buildNewBranch());
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
		assertEquals(4, AccountManager.getInstance().getAllAccounts().size());
		modelManager.addAccount(buildNewAccount());
		assertEquals(5, AccountManager.getInstance().getAllAccounts().size());
	}

	@Test
	void testChangeAccount() {
		assertEquals(4, AccountManager.getInstance().getAllAccounts().size());
		modelManager.changeAccount(buildExistingAccount(), buildNewAccount());
		assertEquals(4, AccountManager.getInstance().getAllAccounts().size());
	}

	@Test
	void testRemoveAccount() {
		assertEquals(4, AccountManager.getInstance().getAllAccounts().size());
		modelManager.removeAccount(buildExistingAccount());
		assertEquals(3, AccountManager.getInstance().getAllAccounts().size());
	}

	@Test
	void testAddBranch() {
		assertEquals(2, BranchManager.getInstance().getAllBranches().size());
		modelManager.addBranch(buildNewBranch());
		assertEquals(3, BranchManager.getInstance().getAllBranches().size());
	}

	@Test
	void testChangeBranch() {
		assertEquals(2, BranchManager.getInstance().getAllBranches().size());
		modelManager.changeBranch(buildExistingBranch(), buildNewBranch());
		assertEquals(2, BranchManager.getInstance().getAllBranches().size());
	}

	@Test
	void testRemoveBranch() {
		assertEquals(2, BranchManager.getInstance().getAllBranches().size());
		modelManager.removeBranch(buildExistingBranch());
		assertEquals(1, BranchManager.getInstance().getAllBranches().size());
	}

	private Account buildNewAccount() {
		return new Account(AccountType.CURRENT, "account1", "12345678", new SortCode("11-11-11"));
	}

	private Account buildExistingAccount() {
		return new Account(AccountType.CURRENT, "account1", "12345678", new SortCode("55-55-55"));
	}

	private Branch buildNewBranch() {
		SortCode sortcode = new SortCode("11-11-11");
		PostCode postcode = new PostCode("WC2H 7LL");
		Address address = new Address(postcode,
				new String[] { "3 the Street", "the Town", "the County", "the Country" });
		return new Branch(address, sortcode, "newbank");
	}

	private Branch buildExistingBranch() {
		SortCode sortcode = new SortCode("55-55-55");
		PostCode postcode = new PostCode("WC2H 7LT");
		Address address = new Address(postcode,
				new String[] { "1 the Street", "the Town", "the County", "the Country" });
		return new Branch(address, sortcode, "bank1");
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
