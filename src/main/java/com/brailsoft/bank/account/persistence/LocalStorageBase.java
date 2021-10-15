package com.brailsoft.bank.account.persistence;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class LocalStorageBase {

	static final String SORTCODE_TAG = "sortcode";
	static final String BANKNAME_TAG = "bankname";
	static final String POSTCODE_TAG = "postcode";
	static final String LINE_TAG = "line";
	static final String LINE_COUNT_TAG = "linecount";
	static final String ADDRESS_TAG = "Address";
	static final String BRANCH_KEY_WORD = "Branch";

	static final String ACCOUNT_ROOT_ELEMENT_NAME = "Accounts";
	static final String BRANCH_ROOT_ELEMENT_NAME = "Branches";

	static final String NUMBER_TAG = "number";
	static final String NAME_TAG = "name";
	static final String TYPE_TAG = "type";
	static final String ACCOUNT_KEY_WORD = "Account";

	static Document doc;

	static String createBeginningTab(String tab) {
		return "<" + tab + ">";
	}

	static String createEndingTab(String tab) {
		return "</" + tab + ">";
	}

	static String extractTab(String s, String tab) {
		if (tab == null || tab.isBlank() || tab.isEmpty()) {
			throw new IllegalArgumentException("LocalStorageBase: corrupt request");
		}
		String tab1 = createBeginningTab(tab);
		String tab2 = createEndingTab(tab);
		String tabData;
		int tabStart = s.indexOf(tab1);
		int tabEnd = s.indexOf(tab2);
		if (tabStart < 0) {
			throw new IllegalArgumentException("LocalStorageBase: tab not found: " + tab1);
		}
		if (tabEnd < 0) {
			throw new IllegalArgumentException("LocalStorageBase: tab not found: " + tab2);
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

	static void writeXML(Document doc, OutputStream output) throws IOException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new IOException(e.getMessage());
		}
	}

	static Element buildElement(String tag, String text) {
		Element result = doc.createElement(tag);
		result.setTextContent(text);
		return result;
	}

}
