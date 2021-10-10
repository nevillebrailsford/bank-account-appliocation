package com.brailsoft.bank.account.model;

import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Branch implements Comparable<Branch> {
	private ObjectProperty<Address> address = new SimpleObjectProperty<>(this, "address", null);
	private ObjectProperty<SortCode> sortcode = new SimpleObjectProperty<>(this, "sortcode", null);
	private StringProperty bankname = new SimpleStringProperty(this, "bankname", null);

	public Branch(Address address, SortCode sortcode, String bankname) {
		if (address == null) {
			throw new IllegalArgumentException("Branch: address must be specified");
		}
		if (sortcode == null) {
			throw new IllegalArgumentException("Branch: sort code must be specified");
		}
		if (bankname == null || bankname.isBlank() || bankname.isEmpty()) {
			throw new IllegalArgumentException("Branch: bank name must be specified");
		}
		this.address.set(new Address(address));
		this.sortcode.set(sortcode);
		this.bankname.set(bankname);
	}

	public Branch(Branch that) {
		if (that == null) {
			throw new IllegalArgumentException("Branch: branch must be specified");
		}
		this.address.set(new Address(that.address.get()));
		this.sortcode.set(that.sortcode.get());
		this.bankname.set(that.bankname.get());
	}

	public StringProperty banknameProperty() {
		return bankname;
	}

	public String getBankname() {
		return bankname.get();
	}

	public ObjectProperty<Address> addressProperty() {
		return address;
	}

	public Address getAddress() {
		return new Address(address.get());
	}

	public ObjectProperty<SortCode> sortcodeProperty() {
		return sortcode;
	}

	public SortCode getSortCode() {
		return new SortCode(sortcode.get());
	}

	@Override
	public int hashCode() {
		return Objects.hash(address.get(), sortcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Branch that = (Branch) obj;
		return Objects.equals(address.get(), that.address.get()) && Objects.equals(sortcode.get(), that.sortcode.get())
				&& Objects.equals(bankname.get(), that.bankname.get());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Branch at ").append(address.get().toString());
		builder.append(" with sort code ").append(sortcode.get().toString());
		builder.append(" for bank ").append(bankname.get());
		return builder.toString();
	}

	@Override
	public int compareTo(Branch that) {
		int retCode = this.bankname.get().compareTo(that.bankname.get());
		if (retCode == 0) {
			retCode = this.sortcode.get().compareTo(that.sortcode.get());
			if (retCode == 0) {
				retCode = this.address.get().toString().compareTo(that.address.get().toString());
			}
		}
		return retCode;
	}

}
