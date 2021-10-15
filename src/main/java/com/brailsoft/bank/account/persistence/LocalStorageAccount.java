package com.brailsoft.bank.account.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.brailsoft.bank.account.model.Account;
import com.brailsoft.bank.account.model.AccountManager;
import com.brailsoft.bank.account.model.AccountType;
import com.brailsoft.bank.account.model.SortCode;

public class LocalStorageAccount extends LocalStorageBase {

	private static AccountManager accountManager = AccountManager.getInstance();

	public static void clearAndLoadManagerWithArchivedData(InputStream archiveFile) throws IOException {
		accountManager.clear();
		loadManagerWithArchivedData(archiveFile);
	}

	public static void archiveDataFromManager(OutputStream archiveFile) throws IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element rootElement = doc.createElement(ACCOUNT_ROOT_ELEMENT_NAME);
			doc.appendChild(rootElement);

			accountManager.getAllAccounts().stream().forEach(account -> {
				Element accountElement = buildAccountElement(account);
				rootElement.appendChild(accountElement);
			});

			writeXML(doc, archiveFile);
		} catch (ParserConfigurationException e1) {
			throw new IOException(e1.getMessage());
		}

	}

	private static void loadManagerWithArchivedData(InputStream archiveFile) throws IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.parse(archiveFile);

			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName(ACCOUNT_KEY_WORD);

			for (int index = 0; index < list.getLength(); index++) {
				Node node = list.item(index);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element accountElement = (Element) node;
					Account account = buildAccountFromElement(accountElement);
					AccountManager.getInstance().add(account);
				}
			}
		} catch (ParserConfigurationException e) {
			throw new IOException(e.getMessage());
		} catch (SAXException e) {
			throw new IOException(e.getMessage());
		}

	}

	private static Account buildAccountFromElement(Element accountElement) {
		Account account = null;
		String type = accountElement.getElementsByTagName(TYPE_TAG).item(0).getTextContent();
		String name = accountElement.getElementsByTagName(NAME_TAG).item(0).getTextContent();
		String number = accountElement.getElementsByTagName(NUMBER_TAG).item(0).getTextContent();
		String sortcode = accountElement.getElementsByTagName(SORTCODE_TAG).item(0).getTextContent();

		account = new Account(AccountType.valueOf(type), name, number, new SortCode(sortcode));
		return account;
	}

	private static Element buildAccountElement(Account account) {
		Element accountElement = doc.createElement(ACCOUNT_KEY_WORD);
		accountElement.appendChild(buildElement(TYPE_TAG, account.getType().toString()));
		accountElement.appendChild(buildElement(NAME_TAG, account.getName()));
		accountElement.appendChild(buildElement(NUMBER_TAG, account.getNumber()));
		accountElement.appendChild(buildElement(SORTCODE_TAG, account.getSortCode().toString()));
		return accountElement;
	}

}
