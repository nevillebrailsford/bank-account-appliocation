package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {

	private static final String SORT_CODE_VALUE = "55-55-55";
	private static final String DIFF_SORT_CODE_VALUE = "55-55-54";
	private static final SortCode SORT_CODE = new SortCode(SORT_CODE_VALUE);
	private static final SortCode DIFF_SORT_CODE = new SortCode(DIFF_SORT_CODE_VALUE);
	private static final AccountType CURRENT = AccountType.CURRENT;
	private static final String LOWER_NUMBER = "12345677";
	private static final String NUMBER1 = "12345678";
	private static final String HIGHER_NUMBER = "12345679";
	private static final String ACCOUNT1 = "account1";

	Account account = new Account(CURRENT, ACCOUNT1, NUMBER1, SORT_CODE);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testAccountAccountTypeStringStringSortCode() {
		new Account(CURRENT, ACCOUNT1, NUMBER1, SORT_CODE);
	}

	@Test
	void testAccountAccount() {
		new Account(account);
	}

	@Test
	void testGetName() {
		assertEquals(ACCOUNT1, account.getName());
	}

	@Test
	void testGetNumber() {
		assertEquals(NUMBER1, account.getNumber());
	}

	@Test
	void testGetType() {
		assertEquals(CURRENT, account.getType());
	}

	@Test
	void testGetSortCode() {
		assertEquals(new SortCode(SORT_CODE), account.getSortCode());
	}

	@Test
	void testEqualsObject() {
		assertEquals(new Account(CURRENT, ACCOUNT1, NUMBER1, SORT_CODE), account);
	}

	@Test
	void testToString() {
		assertEquals(CURRENT + " account named " + ACCOUNT1 + " numbered " + NUMBER1 + " held in " + SORT_CODE_VALUE,
				account.toString());
	}

	@Test
	void testCompareTo() {
		assertTrue(new Account(CURRENT, ACCOUNT1, NUMBER1, SORT_CODE).compareTo(account) == 0);
		assertTrue(new Account(CURRENT, ACCOUNT1, HIGHER_NUMBER, SORT_CODE).compareTo(account) > 0);
		assertTrue(new Account(CURRENT, ACCOUNT1, LOWER_NUMBER, SORT_CODE).compareTo(account) < 0);
	}

	@Test
	void testCompareInDifferctSortCodes() {
		assertTrue(account.compareTo(new Account(CURRENT, ACCOUNT1, NUMBER1, DIFF_SORT_CODE)) > 0);
	}

	@Test
	void testMissingAccountType() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(null, ACCOUNT1, NUMBER1, SORT_CODE);
		});
	}

	@Test
	void testMissingName() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, null, NUMBER1, SORT_CODE);
		});
	}

	@Test
	void testEmptyName() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, "", NUMBER1, SORT_CODE);
		});
	}

	@Test
	void testBlankName() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, "   ", NUMBER1, SORT_CODE);
		});
	}

	@Test
	void testMissingNumber() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, ACCOUNT1, null, SORT_CODE);
		});
	}

	@Test
	void testEmptyNumber() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, ACCOUNT1, "", SORT_CODE);
		});
	}

	@Test
	void testBlankNumber() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, ACCOUNT1, "     ", SORT_CODE);
		});
	}

	@Test
	void testNonNumericNumber() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, ACCOUNT1, "abcdefgh", SORT_CODE);
		});
	}

	@Test
	void testLongNumericNumber() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, ACCOUNT1, "123456789", SORT_CODE);
		});
	}

	@Test
	void testShortNumericNumber() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, ACCOUNT1, "1234567", SORT_CODE);
		});
	}

	@Test
	void testMissingSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(CURRENT, ACCOUNT1, NUMBER1, null);
		});
	}

	@Test
	void testMissingAccount() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Account(null);
		});
	}

}
