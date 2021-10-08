package com.brailsoft.bank.account.model;

import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Address {
	private ObservableList<String> linesOfAddress = FXCollections.observableArrayList();
	private ObjectProperty<PostCode> postCode = new SimpleObjectProperty<>(this, "postCode", null);
	private String comma = "";

	public Address(PostCode postCode, String[] linesOfAddress) {
		if (postCode == null) {
			throw new IllegalArgumentException("Address: post code must be specified");
		}
		if (linesOfAddress == null) {
			throw new IllegalArgumentException("Address: lines of address must be specified");
		}
		if (linesOfAddress.length == 0) {
			throw new IllegalArgumentException("Address: lines of address must have contents");
		}
		this.postCode.set(postCode);
		for (int i = 0; i < linesOfAddress.length; i++) {
			this.linesOfAddress.add(linesOfAddress[i]);
		}
	}

	public Address(Address that) {
		if (that == null) {
			throw new IllegalArgumentException("Address: address must be specified");
		}
		this.postCode.set(new PostCode(that.postCode.get()));
		that.linesOfAddress.stream().forEach(line -> {
			this.linesOfAddress.add(line);
		});
	}

	public String[] getLinesOfAddress() {
		String[] lines = new String[linesOfAddress.size()];
		int index = 0;
		for (String line : linesOfAddress) {
			lines[index++] = line;
		}
		return lines;
	}

	public PostCode getPostCode() {
		return new PostCode(postCode.get());
	}

	public ObjectProperty<PostCode> postCodeProperty() {
		return postCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(linesOfAddress, postCode.get());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address that = (Address) obj;
		return Objects.equals(linesOfAddress, that.linesOfAddress)
				&& Objects.equals(postCode.get(), that.postCode.get());
	}

	@Override
	public String toString() {
		comma = "";
		StringBuilder builder = new StringBuilder();
		linesOfAddress.stream().forEach(line -> {
			builder.append(comma).append(line);
			comma = ", ";
		});
		builder.append(" ").append(postCode.get());
		return builder.toString();
	}

}
