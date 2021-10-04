package com.brailsoft.bank.account.model;

import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Account implements Comparable<Account> {
	private StringProperty name = new SimpleStringProperty(this, "name", "");
	private StringProperty number = new SimpleStringProperty(this, "number", "");
	private final SortCode sortCode;
	private AccountType type;

	public Account(AccountType type, String name, String number, SortCode sortCode) {
		if (type == null) {
			throw new IllegalArgumentException("Account: type must be specified");
		}
		if (sortCode == null) {
			throw new IllegalArgumentException("Account: sort code must be specified");
		}
		if (name == null || name.isEmpty() || name.isBlank()) {
			throw new IllegalArgumentException("Account: name must be specified");
		}
		if (number == null || number.isEmpty() || number.isBlank()) {
			throw new IllegalArgumentException("Account: number must be specified");
		}
		if (!number.matches("^[0-9]{8}$")) {
			throw new IllegalArgumentException("Account: number must be 8 digits long");
		}
		this.type = type;
		this.name.set(name);
		this.number.set(number);
		this.sortCode = new SortCode(sortCode);
	}

	public Account(Account that) {
		if (that == null) {
			throw new IllegalArgumentException("Account: account must be supplied");
		}
		this.type = that.type;
		this.name.set(that.name.get());
		this.number.set(that.number.get());
		this.sortCode = new SortCode(that.sortCode);
	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getNumber() {
		return number.get();
	}

	public StringProperty numberProperty() {
		return number;
	}

	public AccountType getType() {
		return type;
	}

	public SortCode getSortCode() {
		return new SortCode(sortCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, number, sortCode, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account that = (Account) obj;
		return Objects.equals(this.name.get(), that.name.get()) && Objects.equals(this.number.get(), that.number.get())
				&& Objects.equals(this.sortCode, that.sortCode) && this.type == that.type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(type).append(" account");
		builder.append(" named ").append(name.get());
		builder.append(" numbered ").append(number.get());
		builder.append(" held in ").append(sortCode);
		return builder.toString();
	}

	@Override
	public int compareTo(Account that) {
		if (!this.sortCode.equals(that.sortCode)) {
			throw new IllegalStateException("Can only compare accounts with the same sort code");
		}
		if (this.type.compareTo(that.type) == 0) {
			return this.number.get().compareTo(that.number.get());
		}
		return this.type.compareTo(that.type);
	}
}
