package com.brailsoft.bank.account.persistence;

public abstract class LocalStorage {

	static final String SORTCODE_TAB = "sortcode";
	static final String BANKNAME_TAB = "bankname";
	static final String POSTCODE_TAB = "postcode";
	static final String LINE_TAB = "line";
	static final String LINE_COUNT_TAB = "linecount";
	static final String ADDRESS_KEY_WORD = "Address";
	static final String BRANCH_KEY_WORD = "Branch";

	static final String NUMBER_TAB = "number";
	static final String NAME_TAB = "name";
	static final String TYPE_TAB = "type";
	static final String ACCOUNT_KEY_WORD = "Account";

	static String createBeginningTab(String tab) {
		return "<" + tab + ">";
	}

	static String createEndingTab(String tab) {
		return "</" + tab + ">";
	}

	static String extractTab(String s, String tab) {
		if (tab == null || tab.isBlank() || tab.isEmpty()) {
			throw new IllegalArgumentException("LocalStorage: corrupt request");
		}
		String tab1 = createBeginningTab(tab);
		String tab2 = createEndingTab(tab);
		String tabData;
		int tabStart = s.indexOf(tab1);
		int tabEnd = s.indexOf(tab2);
		if (tabStart < 0) {
			throw new IllegalArgumentException("LocalStorage: tab not found: " + tab1);
		}
		if (tabEnd < 0) {
			throw new IllegalArgumentException("LocalStorage: tab not found: " + tab2);
		}
		tabData = s.substring(tabStart + tab1.length(), tabEnd);
		return tabData;
	}

	static String formatArchiveString(String tab, String sortcode) {
		StringBuilder builder = new StringBuilder();
		builder.append(createBeginningTab(tab));
		builder.append(sortcode);
		builder.append(createEndingTab(tab));
		return builder.toString();
	}

}
