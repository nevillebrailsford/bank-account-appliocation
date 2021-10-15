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

import com.brailsoft.bank.account.model.Address;
import com.brailsoft.bank.account.model.Branch;
import com.brailsoft.bank.account.model.BranchManager;
import com.brailsoft.bank.account.model.PostCode;
import com.brailsoft.bank.account.model.SortCode;

public class LocalStorageBranch extends LocalStorageBase {

	private static BranchManager branchManager = BranchManager.getInstance();

	public static void clearAndLoadManagerWithArchivedData(InputStream archiveFile) throws IOException {
		branchManager.clear();
		loadManagerWirhArchivedData(archiveFile);
	}

	public static void archiveDataFromManager(OutputStream archiveFile) throws IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element rootElement = doc.createElement(BRANCH_ROOT_ELEMENT_NAME);
			doc.appendChild(rootElement);

			branchManager.getAllBranches().stream().forEach(branch -> {
				Element branchElement = buildBranchElement(branch);
				rootElement.appendChild(branchElement);
			});

			writeXML(doc, archiveFile);
		} catch (ParserConfigurationException e1) {
			throw new IOException(e1.getMessage());
		}

	}

	private static void loadManagerWirhArchivedData(InputStream archiveFile) throws IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.parse(archiveFile);

			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName(BRANCH_KEY_WORD);

			for (int index = 0; index < list.getLength(); index++) {
				Node node = list.item(index);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element branchElement = (Element) node;
					Branch branch = buildBranchFromElement(branchElement);
					BranchManager.getInstance().add(branch);
				}
			}
		} catch (ParserConfigurationException e) {
			throw new IOException(e.getMessage());
		} catch (SAXException e) {
			throw new IOException(e.getMessage());
		}

	}

	private static Branch buildBranchFromElement(Element branchElement) {
		String sortcode = branchElement.getElementsByTagName(SORTCODE_TAG).item(0).getTextContent();
		String bankname = branchElement.getElementsByTagName(BANKNAME_TAG).item(0).getTextContent();
		Address address = buildAddressFromElement((Element) branchElement.getElementsByTagName(ADDRESS_TAG).item(0));
		Branch branch = new Branch(address, new SortCode(sortcode), bankname);
		return branch;
	}

	private static Address buildAddressFromElement(Element addressElement) {
		String postcode = addressElement.getElementsByTagName(POSTCODE_TAG).item(0).getTextContent();
		NodeList list = addressElement.getElementsByTagName(LINE_TAG);
		String[] addressLine = new String[list.getLength()];
		for (int i = 0; i < list.getLength(); i++) {
			addressLine[i] = list.item(i).getTextContent();
		}
		return new Address(new PostCode(postcode), addressLine);
	}

	private static Element buildBranchElement(Branch branch) {
		Element branchElement = doc.createElement(BRANCH_KEY_WORD);

		branchElement.appendChild(buildElement(SORTCODE_TAG, branch.getSortCode().toString()));
		branchElement.appendChild(buildElement(BANKNAME_TAG, branch.getBankname()));
		branchElement.appendChild(buildAddressElement(branch.getAddress()));

		return branchElement;
	}

	private static Element buildAddressElement(Address address) {
		Element addressElement = doc.createElement(ADDRESS_TAG);

		addressElement.appendChild(buildElement(POSTCODE_TAG, address.getPostCode().toString()));
		for (int i = 0; i < address.getLinesOfAddress().length; i++) {
			addressElement.appendChild(buildElement(LINE_TAG, address.getLinesOfAddress()[i]));
		}
		return addressElement;
	}

}
