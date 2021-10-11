package com.brailsoft.bank.account.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LocalStorage {
	private static LocalStorage instance = null;

	private static final String ACCOUNT_FILE = "account.dat";
	private static final String BRANCH_FILE = "branch.dat";
	private File directory = null;

	public synchronized static LocalStorage getInstance(File directory) {
		if (instance == null) {
			if (directory == null) {
				throw new IllegalArgumentException("LocalStorage: directory is missing");
			}
			instance = new LocalStorage();
			instance.updateDirectory(directory);
		}
		return instance;
	}

	private LocalStorage() {
	}

	private void updateDirectory(File directory) {
		if (this.directory == null || !this.directory.getAbsolutePath().equals(directory.getAbsolutePath())) {
			this.directory = directory;
			if (!directory.exists()) {
				directory.mkdirs();
			}
		}
	}

	public void clearAndLoadManagerWithArchivedData() throws IOException {
		File branchFile = new File(directory, BRANCH_FILE);
		if (!branchFile.exists()) {
			throw new IOException("Branch file not found");
		}
		File accountFile = new File(directory, ACCOUNT_FILE);
		if (!accountFile.exists()) {
			throw new IOException("File storage: Account file not found");
		}
		try (BufferedReader archiveFile = new BufferedReader(new FileReader(branchFile))) {
			LocalStorageBranch.clearAndLoadManagerWithArchivedData(archiveFile);
		} catch (Exception e) {
			throw new IOException("FileStorage: Exception occurred: " + e.getMessage());
		}
		try (BufferedReader archiveFile = new BufferedReader(new FileReader(accountFile))) {
			LocalStorageAccount.clearAndLoadManagerWithArchivedData(archiveFile);
		} catch (Exception e) {
			throw new IOException("FileStorage: Exception occurred: " + e.getMessage());
		}
	}
}
