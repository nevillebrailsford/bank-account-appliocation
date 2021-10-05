package com.brailsoft.bank.account.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SortCodeTest {

	private static final String ZEROES_SORT_CODE = "00-00-00";
	private static final String LOWER_SORT_CODE = "55-55-54";
	private static final String SAMPLE_SORT_CODE = "55-55-55";
	private static final String HIGHER_SORT_CODE = "55-55-56";
	private static final String NINES_SORT_CODE = "99-99-99";

	SortCode sortcodeToBeTested = new SortCode(SAMPLE_SORT_CODE);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testValidSortCode() {
		new SortCode(SAMPLE_SORT_CODE);
	}

	@Test
	void testZeroesSortCode() {
		new SortCode(ZEROES_SORT_CODE);
	}

	@Test
	void testNinesSortCode() {
		new SortCode(NINES_SORT_CODE);
	}

	@Test
	void testGetValue() {
		assertEquals(SAMPLE_SORT_CODE, sortcodeToBeTested.getValue());
	}

	@Test
	void testEqualsObject() {
		assertEquals(sortcodeToBeTested, new SortCode(SAMPLE_SORT_CODE));
		assertNotEquals(sortcodeToBeTested, new SortCode(LOWER_SORT_CODE));
		assertNotEquals(sortcodeToBeTested, new SortCode(HIGHER_SORT_CODE));
	}

	@Test
	void testToString() {
		assertEquals(SAMPLE_SORT_CODE, sortcodeToBeTested.toString());
	}

	@Test
	void testCompareTo() {
		assertTrue(sortcodeToBeTested.compareTo(new SortCode(LOWER_SORT_CODE)) > 0);
		assertTrue(sortcodeToBeTested.compareTo(new SortCode(SAMPLE_SORT_CODE)) == 0);
		assertTrue(sortcodeToBeTested.compareTo(new SortCode(HIGHER_SORT_CODE)) < 0);
	}

	@Test
	void testCloneSortCode() {
		assertEquals(SAMPLE_SORT_CODE, new SortCode(sortcodeToBeTested).getValue());
	}

	@Test
	void testMissingSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("");
		});
	}

	@Test
	void testBlankSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode(" ");
		});
	}

	@Test
	void testSingleDigitSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("5-55-55");
		});
	}

	@Test
	void testIllegalSymbolSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("55-55/55");
		});
	}

	@Test
	void testNonDigitSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("aa-aa-aa");
		});
	}

	@Test
	void testMissingLstValueSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("55-55-");
		});
	}

	@Test
	void testTooManyDigitsSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("55-55-555");
		});
	}

	@Test
	void testTooManyHyphensSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("55-55--55");
		});
	}

	@Test
	void testTooManyValuesSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SortCode("55-55-55-55");
		});
	}

	@Test
	void testNullSortCode() {
		assertThrows(IllegalArgumentException.class, () -> {
			SortCode missing = null;
			new SortCode(missing);
		});
	}

}
