package com.brailsoft.bank.account.persistence;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brailsoft.bank.account.model.AccountManager;
import com.brailsoft.bank.account.model.BranchManager;

class LocalStorageFailTest {

	static final String DIRECTORY_TEST = "bank.test";
	File directory = new File(System.getProperty("user.home"), DIRECTORY_TEST);

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "account.dat"));
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath(), "branch.dat"));
		Files.deleteIfExists(Paths.get(directory.getAbsolutePath()));

		AccountManager.getInstance().clear();
		BranchManager.getInstance().clear();
	}

	@Test
	void testTooManyParameters() {
		assertThrows(IllegalArgumentException.class, () -> {
			LocalStorage.getInstance(new File("one"), new File("two"));
		});
	}

	@Test
	void testFilesHaveNotBeenCreated() {
		assertThrows(IOException.class, () -> {
			LocalStorage.getInstance(directory).clearAndLoadManagerWithArchivedData();
		});
	}

}
