package com.brailsoft.bank.account.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LocalStorage {
	private static LocalStorage instance = null;

	private static final String DEFAULT_DIRECTORY = "bank.archive";
	private static final String ACCOUNT_FILE = "account.dat";
	private static final String BRANCH_FILE = "branch.dat";
	private File directory = null;

	public synchronized static LocalStorage getInstance(File... directory) {
		if (directory.length > 1) {
			throw new IllegalArgumentException("LocalStorage: too many parameters");
		}
		if (instance == null) {
			instance = new LocalStorage();
			if (directory.length == 0) {
				instance.updateDirectory(new File(System.getProperty("user.home"), DEFAULT_DIRECTORY));
			} else {
				instance.updateDirectory(directory[0]);
			}
		} else if (directory.length == 1) {
			instance.updateDirectory(directory[0]);
		}
		return instance;
	}

	private LocalStorage() {
	}

	private void updateDirectory(File directory) {
		this.directory = directory;
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public void clearAndLoadManagerWithArchivedData() throws IOException {
		File branchFile = new File(directory, BRANCH_FILE);
		File accountFile = new File(directory, ACCOUNT_FILE);
		System.out.println(branchFile);
		if (!branchFile.exists()) {
			throw new IOException("Branch file not found");
		}
		if (!accountFile.exists()) {
			throw new IOException("File storage: Account file not found");
		}
		try (BufferedReader archiveFile = new BufferedReader(new FileReader(branchFile))) {
			LocalStorageBranch.clearAndLoadManagerWithArchivedData(archiveFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("FileStorage: Exception occurred: " + e.getMessage());
		}
		try (BufferedReader archiveFile = new BufferedReader(new FileReader(accountFile))) {
			LocalStorageAccount.clearAndLoadManagerWithArchivedData(archiveFile);
		} catch (Exception e) {
			throw new IOException("FileStorage: Exception occurred: " + e.getMessage());
		}
	}

	public void archiveDataFromManager() throws IOException {
		File branchFile = new File(directory, BRANCH_FILE);
		File accountFile = new File(directory, ACCOUNT_FILE);
		try (PrintWriter archiveFile = new PrintWriter(new BufferedWriter(new FileWriter(branchFile)))) {
			LocalStorageBranch.archiveDataFromManager(archiveFile);
		} catch (Exception e) {
			throw new IOException("FileStorage: Exception occurred: " + e.getMessage());
		}
		try (PrintWriter archiveFile = new PrintWriter(new BufferedWriter(new FileWriter(accountFile)))) {
			LocalStorageAccount.archiveDataFromManager(archiveFile);
		} catch (Exception e) {
			throw new IOException("FileStorage: Exception occurred: " + e.getMessage());
		}
	}

	public File getDirectory() {
		return directory;
	}
}
