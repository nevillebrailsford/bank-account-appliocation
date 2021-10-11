package com.brailsoft.bank.account.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.brailsoft.bank.account.model.Address;
import com.brailsoft.bank.account.model.Branch;
import com.brailsoft.bank.account.model.BranchManager;
import com.brailsoft.bank.account.model.PostCode;
import com.brailsoft.bank.account.model.SortCode;

public class LocalStorageBranch extends LocalStorageBase {

	private static BranchManager branchManager = BranchManager.getInstance();

	public static void clearAndLoadManagerWithArchivedData(BufferedReader archiveFile) throws IOException {
		branchManager.clear();
		loadManagerWirhArchivedData(archiveFile);
	}

	public static void archiveDataFromManager(PrintWriter archiveFile) throws IOException {
		branchManager.getAllBranches().stream().forEach(branch -> {
			archiveFile.println(formatArchiveEntry(branch));
		});
	}

	private static void loadManagerWirhArchivedData(BufferedReader archiveFile) throws IOException {
		do {
			branchManager.add(buildBranchFromEntry(archiveFile.readLine()));
		} while (archiveFile.ready());
	}

	private static Branch buildBranchFromEntry(String s) throws IOException {
		if (!s.startsWith(createBeginningTab(BRANCH_KEY_WORD)) || !s.endsWith(createEndingTab(BRANCH_KEY_WORD))) {
			throw new IOException("LocalStorageAccount: " + BRANCH_KEY_WORD + " missing from entry");
		}
		String sortcode = extractSortCode(s);
		String bankname = extractBankName(s);
		String address = extractAddress(s);
		String postcode = extractPostCode(address);
		String[] linesOfAddress = extractLinesOfAddress(address);
		Address newAddress = new Address(new PostCode(postcode), linesOfAddress);
		Branch branch = new Branch(new Address(newAddress), new SortCode(sortcode), bankname);
		return branch;
	}

	private static String extractSortCode(String s) {
		String sortcode = extractTab(s, SORTCODE_TAB);
		return sortcode;
	}

	private static String extractBankName(String s) {
		String bankname = extractTab(s, BANKNAME_TAB);
		return bankname;
	}

	private static String extractAddress(String s) {
		String type = extractTab(s, ADDRESS_KEY_WORD);
		return type;
	}

	private static String extractPostCode(String s) {
		String postcode = extractTab(s, POSTCODE_TAB);
		return postcode;
	}

	private static String[] extractLinesOfAddress(String s) {
		String linecount = extractTab(s, LINE_COUNT_TAB);
		int noOfLines = Integer.valueOf(linecount).intValue();
		String[] linesOfAddress = new String[noOfLines];
		for (int i = 0; i < noOfLines; i++) {
			linesOfAddress[i] = extractTab(s, LINE_TAB);
		}
		return linesOfAddress;
	}

	private static String formatArchiveEntry(Branch branch) {
		StringBuilder builder = new StringBuilder();
		builder.append(createBeginningTab(BRANCH_KEY_WORD));
		builder.append(formatArchiveString(SORTCODE_TAB, branch.getSortCode().toString()));
		builder.append(formatArchiveString(BANKNAME_TAB, branch.getBankname()));
		builder.append(formatArchiveEntry(branch.getAddress()));
		builder.append(createEndingTab(BRANCH_KEY_WORD));
		return builder.toString();
	}

	private static String formatArchiveEntry(Address address) {
		StringBuilder builder = new StringBuilder();
		builder.append(createBeginningTab(ADDRESS_KEY_WORD));
		builder.append(formatArchiveString(POSTCODE_TAB, address.getPostCode().toString()));
		String[] linesOfAddress = address.getLinesOfAddress();
		builder.append(formatArchiveString(LINE_COUNT_TAB, String.valueOf(linesOfAddress.length)));
		for (int i = 0; i < linesOfAddress.length; i++) {
			builder.append(formatArchiveString(LINE_TAB, linesOfAddress[i]));
		}
		builder.append(createEndingTab(ADDRESS_KEY_WORD));
		return builder.toString();
	}

}
