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
