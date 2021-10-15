package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.brailsoft.bank.account.userinterface.UserInterfaceContract.EventSortCodeAltered;

class ModelManagerTest {

	private static final String BRANCH_FILE = "branch.dat";
	private static final String ACCOUNT_FILE = "account.dat";
	private static final String USER_HOME = "user.home";
	private static final String NUMBER1 = "12345678";
	private static final String NUMBER3 = "12345679";
	private static final String NUMBER2 = "87654321";
	private static final String ACCOUNT1 = "account1";
	private static final String ACCOUNT3 = "account3";
	private static final String ACCOUNT2 = "account2";
	private static final String BANK1 = "bank1";
	private static final String BANK2 = "bank2";
	private static final String NEWBANK = "newbank";
	private static final String POST_CODE1 = "WC2H 7LL";
	private static final String POST_CODE2 = "WC2H 7LT";
	private static final String POST_CODE3 = "WC2H 7LS";
	private static final String OLD_SORTCODE = "55-55-55";
	private static final String NEW_SORTCODE = "11-11-11";
	private static final String OTHER_SORTCODE = "55-55-56";
	private static final String DIRECTORY_FOR_TESTING = "bank.test";

	private static File directory = new File(System.getProperty(USER_HOME), DIRECTORY_FOR_TESTING);

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

		@Override
		public void onSortCodeAltered(EventSortCodeAltered sortcodeAltered) {
			assertTrue(sortcodeAltered.wasAdded());
			assertEquals(NEW_SORTCODE, sortcodeAltered.getSortCode().toString());
			assertNotNull(sortcodeAltered.getListOfAccounts());
			assertEquals(0, sortcodeAltered.getListOfAccounts().size());
		}

	};

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), ACCOUNT_FILE));
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), BRANCH_FILE));
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
		modelManager.removeListener();
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
		modelManager.removeListener();
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

	@Test
	void testNullListener() {
		assertThrows(IllegalArgumentException.class, () -> {
			modelManager.addListener(null);
		});
	}

	@Test
	void testDuplicateListener() {
		assertThrows(IllegalStateException.class, () -> {
			modelManager.addListener(listener);
			modelManager.addListener(listener);
		});
		modelManager.removeListener();
	}

	private Account buildNewAccount() {
		return new Account(AccountType.CURRENT, ACCOUNT1, NUMBER1, new SortCode(NEW_SORTCODE));
	}

	private Account buildExistingAccount() {
		return new Account(AccountType.CURRENT, ACCOUNT1, NUMBER1, new SortCode(OLD_SORTCODE));
	}

	private Branch buildNewBranch() {
		SortCode sortcode = new SortCode(NEW_SORTCODE);
		PostCode postcode = new PostCode(POST_CODE1);
		Address address = new Address(postcode,
				new String[] { "3 the Street", "the Town", "the County", "the Country" });
		return new Branch(address, sortcode, NEWBANK);
	}

	private Branch buildExistingBranch() {
		SortCode sortcode = new SortCode(OLD_SORTCODE);
		PostCode postcode = new PostCode(POST_CODE2);
		Address address = new Address(postcode,
				new String[] { "1 the Street", "the Town", "the County", "the Country" });
		return new Branch(address, sortcode, BANK1);
	}

	private void buildAccountHistory() {
		AccountManager accountManager = AccountManager.getInstance();
		accountManager.add(new Account(AccountType.CURRENT, ACCOUNT1, NUMBER1, new SortCode(OLD_SORTCODE)));
		accountManager.add(new Account(AccountType.CURRENT, ACCOUNT2, NUMBER2, new SortCode(OLD_SORTCODE)));
		accountManager.add(new Account(AccountType.CURRENT, ACCOUNT3, NUMBER3, new SortCode(OLD_SORTCODE)));
		accountManager.add(new Account(AccountType.CURRENT, ACCOUNT1, NUMBER1, new SortCode(OTHER_SORTCODE)));
	}

	private void buildBranchHistory() {
		BranchManager branchManager = BranchManager.getInstance();
		for (int i = 0; i < 2; i++) {
			branchManager.add(buildABranch(i));
		}
	}

	private Branch buildABranch(int i) {
		String[] bankname = new String[] { BANK1, BANK2 };
		SortCode[] sortcode = new SortCode[2];
		sortcode[0] = new SortCode(OLD_SORTCODE);
		sortcode[1] = new SortCode(OTHER_SORTCODE);
		PostCode[] postcode = new PostCode[2];
		postcode[0] = new PostCode(POST_CODE2);
		postcode[1] = new PostCode(POST_CODE3);
		Address[] address = new Address[2];
		address[0] = new Address(postcode[0], new String[] { "1 the Street", "the Town", "the County", "the Country" });
		address[1] = new Address(postcode[1], new String[] { "2 the Street", "the Town", "the County", "the Country" });
		Branch br = new Branch(address[i], sortcode[i], bankname[i]);
		return br;
	}

}
