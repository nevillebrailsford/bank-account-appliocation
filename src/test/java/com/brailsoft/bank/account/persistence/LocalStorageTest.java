package com.brailsoft.bank.account.persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalStorageTest {

	File directory = new File(System.getProperty("user.home"), "bank.test");
	LocalStorage storage = LocalStorage.getInstance(directory);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testLocalStorageGetInstance() {
		assertNotNull(storage);
		LocalStorage ls = LocalStorage.getInstance(directory);
		assertNotNull(ls);
		assertTrue(storage == ls);
	}

}
