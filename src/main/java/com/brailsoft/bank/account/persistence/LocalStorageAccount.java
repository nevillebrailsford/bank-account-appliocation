package com.brailsoft.bank.account.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.AccountManager;
import com.brailsoft.bank.account.model.AccountType;
import com.brailsoft.bank.account.model.SortCode;

public class LocalStorageAccount {
	private static final String SORTCODE_TAB = "sortcode";
	private static final String NUMBER_TAB = "number";
	private static final String NAME_TAB = "name";
	private static final String TYPE_TAB = "type";
	private static final String ACCOUNT_KEY_WORD = "Account";

	private static AccountManager accountManager = AccountManager.getInstance();

	public static void clearAndLoadManagerWithArchvedData(BufferedReader inputFile) throws IOException {
		accountManager.clear();
		loadManagerWithArchivedData(inputFile);
	}

	private static void loadManagerWithArchivedData(BufferedReader inputFile) throws IOException {
		do {
			accountManager.add(buildAccountFromEntry(inputFile.readLine()));
		} while (inputFile.ready());
	}

	public static void archiveDataFromManager(PrintWriter archiveFile) throws IOException {
		accountManager.getAllAccounts().stream().forEach(account -> {
			archiveFile.println(formatOutput(account));
		});
	}

	private static Account buildAccountFromEntry(String s) throws IOException {
		if (!s.startsWith(createBeginningTab(ACCOUNT_KEY_WORD)) || !s.endsWith(createEndTab(ACCOUNT_KEY_WORD))) {
			throw new IOException("LocalStorageAccount: " + ACCOUNT_KEY_WORD + " missing from entry");
		}
		String type = extractAccountType(s);
		String name = extractName(s);
		String number = extractNumber(s);
		String sortCode = extractSortCode(s);

		return new Account(AccountType.valueOf(type), name, number, new SortCode(sortCode));
	}

	private static String extractAccountType(String s) {
		String type = extractTab(s, createBeginningTab(TYPE_TAB), createEndTab(TYPE_TAB));
		return type;
	}

	private static String extractName(String s) {
		String name = extractTab(s, createBeginningTab(NAME_TAB), createEndTab(NAME_TAB));
		return name;
	}

	private static String extractNumber(String s) {
		String name = extractTab(s, createBeginningTab(NUMBER_TAB), createEndTab(NUMBER_TAB));
		return name;
	}

	private static String extractSortCode(String s) {
		String name = extractTab(s, createBeginningTab(SORTCODE_TAB), createEndTab(SORTCODE_TAB));
		return name;
	}

	private static String extractTab(String s, String tab1, String tab2) {
		if (tab1 == null || tab2 == null) {
			throw new IllegalArgumentException("LocalStorageAccount: corrupt request");
		}
		String tabData;
		int tabStart = s.indexOf(tab1);
		int tabEnd = s.indexOf(tab2);
		if (tabStart < 0) {
			throw new IllegalArgumentException("LocalStorageAccount: tab not found: " + tab1);
		}
		if (tabEnd < 0) {
			throw new IllegalArgumentException("LocalStorageAccount: tab not found: " + tab2);
		}
		tabData = s.substring(tabStart + tab1.length(), tabEnd);
		return tabData;
	}

	private static String formatOutput(Account account) {
		StringBuilder builder = new StringBuilder();
		builder.append(createBeginningTab(ACCOUNT_KEY_WORD));
		builder.append(createBeginningTab(TYPE_TAB)).append(account.getType()).append(createEndTab(TYPE_TAB));
		builder.append(createBeginningTab(NAME_TAB)).append(account.getName()).append(createEndTab(NAME_TAB));
		builder.append(createBeginningTab(NUMBER_TAB)).append(account.getNumber()).append(createEndTab(NUMBER_TAB));
		builder.append(createBeginningTab(SORTCODE_TAB)).append(account.getSortCode())
				.append(createEndTab(SORTCODE_TAB));
		builder.append(createEndTab(ACCOUNT_KEY_WORD));
		return builder.toString();
	}

	private static String createBeginningTab(String tab) {
		return "<" + tab + ">";
	}

	private static String createEndTab(String tab) {
		return "</" + tab + ">";
	}
}
