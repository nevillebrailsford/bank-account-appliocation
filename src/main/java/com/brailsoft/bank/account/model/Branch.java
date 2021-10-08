package com.brailsoft.bank.account.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Branch implements Comparable<Branch> {
	private ObjectProperty<Address> address = new SimpleObjectProperty<>(this, "address", null);
	private ObservableList<SortCode> sortCodes = FXCollections.observableArrayList();
	private String comma = "";

	public Branch(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Branch: address must be specified");
		}
		this.address.set(new Address(address));
	}

	public Branch(Branch that) {
		if (that == null) {
			throw new IllegalArgumentException("Branch: branch must be specified");
		}
		this.address.set(new Address(that.address.get()));
		that.sortCodes.stream().forEach(sortcode -> {
			this.sortCodes.add(new SortCode(sortcode));
		});
	}

	public ObjectProperty<Address> addressProperty() {
		return address;
	}

	public Address getAddress() {
		return new Address(address.get());
	}

	public List<SortCode> getSortCodes() {
		List<SortCode> copyList = new ArrayList<>();
		sortCodes.stream().forEach(sortcode -> {
			copyList.add(new SortCode(sortcode));
		});
		Collections.sort(copyList);
		return copyList;
	}

	public void addSortCode(SortCode sortcode) {
		if (sortcode == null) {
			throw new IllegalArgumentException("Branch: sortcode must be specified");
		}
		if (sortCodes.contains(sortcode)) {
			throw new IllegalArgumentException("Branch: sortcode already known");
		}
		sortCodes.add(new SortCode(sortcode));
	}

	public void clear() {
		sortCodes.clear();
	}

	@Override
	public int hashCode() {
		return Objects.hash(address.get(), sortCodes);
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
		return Objects.equals(address.get(), that.address.get()) && Objects.equals(sortCodes, that.sortCodes);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Branch at ").append(address.get().toString());
		if (sortCodes.size() == 0) {
			builder.append(" has no sort codes");
		} else {
			comma = "";
			builder.append(sortCodes.size() == 1 ? " and has sort code: " : " and has sort codes: ");
			sortCodes.stream().forEach(sortcode -> {
				builder.append(comma).append(sortcode.toString());
				comma = ", ";
			});
		}
		return builder.toString();
	}

	@Override
	public int compareTo(Branch that) {
		return this.address.get().toString().compareTo(that.address.get().toString());
	}

}
