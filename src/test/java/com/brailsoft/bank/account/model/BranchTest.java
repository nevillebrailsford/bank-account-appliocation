package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchTest {

	private static final String LINE1_LOWER = "97 The Street";
	private static final String LINE1 = "98 The Street";
	private static final String LINE1_HIGHER = "99 The Street";
	private static final String LINE2 = "The Town";
	private static final String LINE3 = "The County";
	private static final String LINE4 = "Country";
	private static final PostCode POST_CODE = new PostCode("CW3 9SR");
	private static final SortCode LOWER_SORT_CODE = new SortCode("55-55-54");
	private static final SortCode SORT_CODE = new SortCode("55-55-55");
	private static final SortCode HIGHER_SORT_CODE = new SortCode("55-55-56");
	private static final String BANK_NAME = "bankname";
	private static final Address LOWER_ADDRESS = new Address(POST_CODE,
			new String[] { LINE1_LOWER, LINE2, LINE3, LINE4 });
	private static final Address ADDRESS = new Address(POST_CODE, new String[] { LINE1, LINE2, LINE3, LINE4 });
	private static final Address HIGHER_ADDRESS = new Address(POST_CODE,
			new String[] { LINE1_HIGHER, LINE2, LINE3, LINE4 });
	private static final String SORT_CODE_STRING = "Branch at 98 The Street, The Town, The County, Country CW3 9SR with sort code 55-55-55";

	Branch branch = new Branch(ADDRESS, SORT_CODE, BANK_NAME);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testBranchAddress() {
		new Branch(ADDRESS, SORT_CODE, BANK_NAME);
	}

	@Test
	void testBranchBranch() {
		new Branch(branch);
	}

	@Test
	void testGetAddress() {
		assertEquals(ADDRESS, branch.getAddress());
	}

	@Test
	void testGetSortCode() {
		assertEquals(SORT_CODE, branch.getSortCode());
	}

	@Test
	void testGetBankName() {
		assertEquals(BANK_NAME, branch.getBankname());
	}

	@Test
	void testEqualsObject() {
		assertEquals(branch, new Branch(ADDRESS, SORT_CODE, BANK_NAME));
	}

	@Test
	void testToString() {
		assertEquals(SORT_CODE_STRING, branch.toString());
	}

	@Test
	void testCompareTo() {
		assertTrue(branch.compareTo(new Branch(ADDRESS, LOWER_SORT_CODE, BANK_NAME)) > 0);
		assertTrue(branch.compareTo(new Branch(ADDRESS, SORT_CODE, BANK_NAME)) == 0);
		assertTrue(branch.compareTo(new Branch(ADDRESS, HIGHER_SORT_CODE, BANK_NAME)) < 0);
		assertTrue(branch.compareTo(new Branch(LOWER_ADDRESS, SORT_CODE, BANK_NAME)) > 0);
		assertTrue(branch.compareTo(new Branch(HIGHER_ADDRESS, SORT_CODE, BANK_NAME)) < 0);
		assertTrue(branch.compareTo(new Branch(HIGHER_ADDRESS, LOWER_SORT_CODE, BANK_NAME)) > 0);
		assertTrue(branch.compareTo(new Branch(LOWER_ADDRESS, HIGHER_SORT_CODE, BANK_NAME)) < 0);
	}

	@Test
	void testNullAddress() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Branch(null, SORT_CODE, BANK_NAME);
		});
	}

	@Test
	void testNullSortcode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Branch(ADDRESS, null, BANK_NAME);
		});
	}

	@Test
	void testNullBankname() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Branch(ADDRESS, SORT_CODE, null);
		});
	}

	@Test
	void testBlankBankname() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Branch(ADDRESS, SORT_CODE, "");
		});
	}

	@Test
	void testEMptyBankname() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Branch(ADDRESS, SORT_CODE, "    ");
		});
	}

	@Test
	void testNullBranch() {
		assertThrows(IllegalArgumentException.class, () -> {
			Branch branch = null;
			new Branch(branch);
		});
	}

}
