package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.MapChangeListener;

class BranchManagerTest {

	private BranchManager branchManager;
	private static SortCode sortCode = new SortCode("55-55-55");
	private static SortCode changedSortCode = new SortCode("55-55-54");
	private static final PostCode postCode = new PostCode("WC2H 7LT");
	private static final String LINE1 = "99 The Street";
	private static final String DIFFERENT_LINE1 = "98 The Street";
	private static final String LINE2 = "The Town";
	private static final String LINE3 = "The County";
	private static final String LINE4 = "Country";
	private static final String[] linesOfAddress = new String[] { LINE1, LINE2, LINE3, LINE4 };
	private static final String[] diff_linesOfAddress = new String[] { DIFFERENT_LINE1, LINE2, LINE3, LINE4 };
	private static final String BANK_NAME = "Bank Name";
	private Address address = new Address(postCode, linesOfAddress);
	private Address changedAddress = new Address(postCode, diff_linesOfAddress);

	private Branch branch = new Branch(address, sortCode, BANK_NAME);
	private Branch changeBranch1 = new Branch(changedAddress, sortCode, BANK_NAME);
	private Branch changeBranch2 = new Branch(address, changedSortCode, BANK_NAME);

	private boolean listenerFailed = false;
	private MapChangeListener.Change<? extends SortCode, ? extends Branch> change;

	private MapChangeListener<? super SortCode, ? super Branch> listener = (
			MapChangeListener.Change<? extends SortCode, ? extends Branch> c) -> {
		if (c.wasAdded()) {
			assertEquals(sortCode, c.getKey());
		} else {
			listenerFailed = true;
			change = c;
		}
	};

	@BeforeEach
	void setUp() throws Exception {
		listenerFailed = false;
		branchManager = BranchManager.getInstance();
	}

	@AfterEach
	void tearDown() throws Exception {
		branchManager.removeMapListener(listener);
		branchManager.clear();
	}

	@Test
	void testGetInstance() {
		assertNotNull(branchManager);
		BranchManager bm = BranchManager.getInstance();
		assertNotNull(bm);
		assertTrue(bm == branchManager);
	}

	@Test
	void testAddMapListener() {
		branchManager.addMapListener(listener);
		branchManager.add(branch);
		branchManager.removeMapListener(listener);
		if (listenerFailed) {
			fail("Unexpected operation: " + change);
		}
	}

	@Test
	void testRemoveMapListener() {
		branchManager.addMapListener(listener);
		branchManager.removeMapListener(listener);
	}

	@Test
	void testAdd() {
		assertEquals(0, branchManager.getSortCodes().size());
		branchManager.add(branch);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(branch, branchManager.getBranchForSortCode(sortCode));
		assertEquals(1, branchManager.getAllBranches().size());
		assertEquals(branch, branchManager.getAllBranches().get(0));
	}

	@Test
	void testChange() {
		assertEquals(0, branchManager.getSortCodes().size());
		branchManager.add(branch);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(branch, branchManager.getBranchForSortCode(sortCode));
		assertEquals(1, branchManager.getAllBranches().size());
		assertEquals(branch, branchManager.getAllBranches().get(0));

		branchManager.change(branch, changeBranch1);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(changeBranch1, branchManager.getBranchForSortCode(sortCode));

		branchManager.change(changeBranch1, changeBranch2);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(changeBranch2, branchManager.getBranchForSortCode(changedSortCode));
	}

	@Test
	void testRemove() {
		assertEquals(0, branchManager.getSortCodes().size());
		branchManager.add(branch);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(branch, branchManager.getBranchForSortCode(sortCode));
		assertEquals(1, branchManager.getAllBranches().size());
		assertEquals(branch, branchManager.getAllBranches().get(0));
		branchManager.remove(branch);
		assertEquals(0, branchManager.getSortCodes().size());
	}

	@Test
	void testClear() {
		assertEquals(0, branchManager.getSortCodes().size());
		branchManager.add(branch);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(branch, branchManager.getBranchForSortCode(sortCode));
		assertEquals(1, branchManager.getAllBranches().size());
		assertEquals(branch, branchManager.getAllBranches().get(0));
		branchManager.clear();
		assertEquals(0, branchManager.getSortCodes().size());
	}

	@Test
	void testGetSortCodes() {
		assertEquals(0, branchManager.getSortCodes().size());
		branchManager.add(branch);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(sortCode, branchManager.getSortCodes().get(0));
	}

	@Test
	void testGetBranchForSortCode() {
		assertEquals(0, branchManager.getSortCodes().size());
		branchManager.add(branch);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(branch, branchManager.getBranchForSortCode(sortCode));
		assertEquals(1, branchManager.getAllBranches().size());
		assertEquals(branch, branchManager.getAllBranches().get(0));
	}

	@Test
	void testGetAllBranches() {
		assertEquals(0, branchManager.getSortCodes().size());
		branchManager.add(branch);
		assertEquals(1, branchManager.getSortCodes().size());
		assertEquals(branch, branchManager.getBranchForSortCode(sortCode));
		assertEquals(1, branchManager.getAllBranches().size());
		assertEquals(branch, branchManager.getAllBranches().get(0));
	}

	@Test
	void testAddNullBranch() {
		assertThrows(IllegalArgumentException.class, () -> {
			branchManager.add(null);
		});
	}

	@Test
	void testChangeNullBranch() {
		branchManager.add(branch);
		assertThrows(IllegalArgumentException.class, () -> {
			branchManager.change(null, branch);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			branchManager.change(branch, null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			branchManager.change(null, null);
		});
	}

	@Test
	void testRemoveNullBranch() {
		assertThrows(IllegalArgumentException.class, () -> {
			branchManager.remove(null);
		});
	}

	@Test
	void testAddExisitingBranch() {
		branchManager.add(branch);
		assertThrows(IllegalStateException.class, () -> {
			branchManager.add(branch);
		});
	}

	@Test
	void testRemoveMissingBranch() {
		assertThrows(IllegalStateException.class, () -> {
			branchManager.remove(branch);
		});
	}

	@Test
	void testChangeMissingBranch() {
		assertThrows(IllegalStateException.class, () -> {
			branchManager.change(branch, changeBranch1);
		});
	}

	@Test
	void testChangeToExisitingBranch() {
		branchManager.add(branch);
		branchManager.add(changeBranch2);
		assertEquals(2, branchManager.getSortCodes().size());
		assertThrows(IllegalStateException.class, () -> {
			branchManager.change(branch, changeBranch2);
		});
		assertEquals(2, branchManager.getSortCodes().size());
	}

	@Test
	void testMissingSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			branchManager.getBranchForSortCode(null);
		});
	}
}
