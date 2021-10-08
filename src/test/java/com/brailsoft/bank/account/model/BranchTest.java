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
	private static final SortCode SORT_CODE = new SortCode("55-55-55");
	private static final SortCode SORT_CODE_2 = new SortCode("55-55-56");
	private static final Address LOWER_ADDRESS = new Address(POST_CODE,
			new String[] { LINE1_LOWER, LINE2, LINE3, LINE4 });
	private static final Address ADDRESS = new Address(POST_CODE, new String[] { LINE1, LINE2, LINE3, LINE4 });
	private static final Address HIGHER_ADDRESS = new Address(POST_CODE,
			new String[] { LINE1_HIGHER, LINE2, LINE3, LINE4 });
	private static final String NO_SORT_CODE_STRING = "Branch at 98 The Street, The Town, The County, Country CW3 9SR has no sort codes";
	private static final String ONE_SORT_CODE_STRING = "Branch at 98 The Street, The Town, The County, Country CW3 9SR and has sort code: 55-55-55";
	private static final String TWO_SORT_CODES_STRING = "Branch at 98 The Street, The Town, The County, Country CW3 9SR and has sort codes: 55-55-55, 55-55-56";

	Branch branch = new Branch(ADDRESS);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		branch.clear();
	}

	@Test
	void testBranchAddress() {
		new Branch(ADDRESS);
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
	void testGetSortCodes() {
		assertEquals(0, branch.getSortCodes().size());
	}

	@Test
	void testEqualsObject() {
		assertEquals(branch, new Branch(ADDRESS));
	}

	@Test
	void testAddSortCode() {
		assertEquals(0, branch.getSortCodes().size());
		branch.addSortCode(SORT_CODE);
		assertEquals(1, branch.getSortCodes().size());
	}

	@Test
	void testToString() {
		assertEquals(NO_SORT_CODE_STRING, branch.toString());
		branch.addSortCode(SORT_CODE);
		assertEquals(ONE_SORT_CODE_STRING, branch.toString());
		branch.addSortCode(SORT_CODE_2);
		assertEquals(TWO_SORT_CODES_STRING, branch.toString());
	}

	@Test
	void testCompareTo() {
		assertTrue(new Branch(LOWER_ADDRESS).compareTo(branch) < 0);
		assertTrue(new Branch(branch).compareTo(branch) == 0);
		assertTrue(new Branch(HIGHER_ADDRESS).compareTo(branch) > 0);
	}

	@Test
	void testClear() {
		assertEquals(0, branch.getSortCodes().size());
		branch.addSortCode(SORT_CODE);
		assertEquals(1, branch.getSortCodes().size());
		branch.clear();
		assertEquals(0, branch.getSortCodes().size());
	}

	@Test
	void testNullAddress() {
		assertThrows(IllegalArgumentException.class, () -> {
			Address address = null;
			new Branch(address);
		});
	}

	@Test
	void testNullBranch() {
		assertThrows(IllegalArgumentException.class, () -> {
			Branch branch = null;
			new Branch(branch);
		});
	}

	@Test
	void testDuplicateSortCodes() {
		branch.addSortCode(SORT_CODE);
		assertEquals(1, branch.getSortCodes().size());
		assertThrows(IllegalArgumentException.class, () -> {
			branch.addSortCode(SORT_CODE);
		});
		assertEquals(1, branch.getSortCodes().size());
	}

}
