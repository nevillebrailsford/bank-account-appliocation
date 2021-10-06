package com.brailsoft.bank.account.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.AccountManager;
import com.brailsoft.bank.account.model.AccountType;
import com.brailsoft.bank.account.model.SortCode;

public class LocalStorageAccount {
	private static final String SORTCODE_TAB = ":sortcode:";
	private static final String NUMBER_TAB = ":number:";
	private static final String NAME_TAB = ":name:";
	private static final String TYPE_TAB = ":type:";
	private static final String ACCOUNT_KEY_WORD = "${Account}";

	private static AccountManager manager = AccountManager.getInstance();

	public static void getAccountData(String fileName) throws IOException {
		Path filePath = Paths.get(fileName);
		if (Files.exists(filePath)) {
			manager.clear();
			try (BufferedReader inputFile = new BufferedReader(new FileReader(fileName))) {
				do {
					String s = inputFile.readLine();
					manager.add(buildAccountFromString(s));
				} while (inputFile.ready());
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException("LocalStorageAccount: exception occurred.");
			}
		} else {
			throw new IOException("LocalStorageAccount: file does not exist.");
		}
	}

	public static void updateAccountData(String fileName) throws IOException {
		try (PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
			manager.getAllAccounts().stream().forEach(account -> {
				outputFile.println(formatOutput(account));
			});
		} catch (Exception e) {
			throw new IOException("LocalStorageAccount: exception occurred.");
		}
	}

	private static Account buildAccountFromString(String s) {
		if (!s.startsWith(ACCOUNT_KEY_WORD)) {
			throw new IllegalStateException("LocalStorageAccount: corrupt file");
		}
		String type = extractAccountType(s);
		String name = extractName(s);
		String number = extractNumber(s);
		String sortCode = extractSortCode(s);

		return new Account(AccountType.valueOf(type), name, number, new SortCode(sortCode));
	}

	private static String extractAccountType(String s) {
		String type = extractTab(s, TYPE_TAB, NAME_TAB);
		return type;
	}

	private static String extractName(String s) {
		String name = extractTab(s, NAME_TAB, NUMBER_TAB);
		return name;
	}

	private static String extractNumber(String s) {
		String name = extractTab(s, NUMBER_TAB, SORTCODE_TAB);
		return name;
	}

	private static String extractSortCode(String s) {
		String name = extractTab(s, SORTCODE_TAB, null);
		return name;
	}

	private static String extractTab(String s, String tab1, String tab2) {
		String name;
		int tabStart = s.indexOf(tab1);
		if (tab2 != null) {
			int tabEnd = s.indexOf(tab2);
			name = s.substring(tabStart + tab1.length(), tabEnd);
		} else {
			name = s.substring(tabStart + tab1.length());
		}
		return name;
	}

	private static String formatOutput(Account account) {
		StringBuilder builder = new StringBuilder(ACCOUNT_KEY_WORD);
		builder.append(TYPE_TAB).append(account.getType());
		builder.append(NAME_TAB).append(account.getName());
		builder.append(NUMBER_TAB).append(account.getNumber());
		builder.append(SORTCODE_TAB).append(account.getSortCode());
		return builder.toString();
	}
}
