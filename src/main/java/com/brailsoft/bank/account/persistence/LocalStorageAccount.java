package com.brailsoft.bank.account.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.AccountManager;
import com.brailsoft.bank.account.model.AccountType;
import com.brailsoft.bank.account.model.SortCode;

public class LocalStorageAccount extends LocalStorageBase {
	private static final String SORTCODE_TAB = "sortcode";
	private static final String NUMBER_TAB = "number";
	private static final String NAME_TAB = "name";
	private static final String TYPE_TAB = "type";
	private static final String ACCOUNT_KEY_WORD = "Account";

	private static AccountManager accountManager = AccountManager.getInstance();

	public static void clearAndLoadManagerWithArchivedData(BufferedReader archiveFile) throws IOException {
		accountManager.clear();
		loadManagerWithArchivedData(archiveFile);
	}

	public static void archiveDataFromManager(PrintWriter archiveFile) throws IOException {
		accountManager.getAllAccounts().stream().forEach(account -> {
			archiveFile.println(formatOutput(account));
		});
	}

	private static void loadManagerWithArchivedData(BufferedReader archiveFile) throws IOException {
		do {
			String s = archiveFile.readLine();
			if (!(s == null || s.isBlank() || s.isEmpty())) {
				accountManager.add(buildAccountFromEntry(s));
			}
		} while (archiveFile.ready());
	}

	private static Account buildAccountFromEntry(String s) throws IOException {
		if (!s.startsWith(createBeginningTab(ACCOUNT_KEY_WORD)) || !s.endsWith(createEndingTab(ACCOUNT_KEY_WORD))) {
			throw new IOException("LocalStorageAccount: " + ACCOUNT_KEY_WORD + " missing from entry");
		}
		String type = extractAccountType(s);
		String name = extractName(s);
		String number = extractNumber(s);
		String sortCode = extractSortCode(s);

		return new Account(AccountType.valueOf(type), name, number, new SortCode(sortCode));
	}

	private static String extractAccountType(String s) {
		String type = extractTab(s, TYPE_TAB);
		return type;
	}

	private static String extractName(String s) {
		String name = extractTab(s, NAME_TAB);
		return name;
	}

	private static String extractNumber(String s) {
		String name = extractTab(s, NUMBER_TAB);
		return name;
	}

	private static String extractSortCode(String s) {
		String name = extractTab(s, SORTCODE_TAB);
		return name;
	}

	private static String formatOutput(Account account) {
		StringBuilder builder = new StringBuilder();
		builder.append(createBeginningTab(ACCOUNT_KEY_WORD));
		builder.append(formatArchiveString(TYPE_TAB, account.getType().toString()));
		builder.append(formatArchiveString(NAME_TAB, account.getName()));
		builder.append(formatArchiveString(NUMBER_TAB, account.getNumber()));
		builder.append(formatArchiveString(SORTCODE_TAB, account.getSortCode().toString()));
		builder.append(createEndingTab(ACCOUNT_KEY_WORD));
		return builder.toString();
	}

}
