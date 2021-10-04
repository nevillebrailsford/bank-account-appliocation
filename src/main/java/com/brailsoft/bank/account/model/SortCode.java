package com.brailsoft.bank.account.model;

import java.util.Objects;

public class SortCode implements Comparable<SortCode> {
	private static final String sortCodeRegularExpression = "^[0-9]{2}[-][0-9]{2}[-][0-9]{2}$";
	private final String value;

	public SortCode(String value) {
		if (!value.matches(sortCodeRegularExpression)) {
			throw new IllegalArgumentException(" is not a valid sort code");
		}
		this.value = value;
	}

	public SortCode(SortCode that) {
		if (that == null) {
			throw new IllegalArgumentException("SortCode: sort code must be supplied");
		}
		this.value = that.value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SortCode that = (SortCode) obj;
		return Objects.equals(value, that.value);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int compareTo(SortCode that) {
		return this.value.compareTo(that.value);
	}

}
