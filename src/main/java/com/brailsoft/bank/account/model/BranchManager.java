package com.brailsoft.bank.account.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class BranchManager {
	private static BranchManager instance = null;

	private ObservableMap<SortCode, Branch> branches = null;

	public synchronized static BranchManager getInstance() {
		if (instance == null) {
			instance = new BranchManager();
		}
		return instance;
	}

	private BranchManager() {
		branches = FXCollections.observableMap(new ConcurrentHashMap<>());
	}

	public synchronized void addMapListener(MapChangeListener<? super SortCode, ? super Branch> listener) {
		branches.addListener(listener);
	}

	public synchronized void removeMapListener(MapChangeListener<? super SortCode, ? super Branch> listener) {
		branches.removeListener(listener);
	}

	public synchronized void add(Branch branch) {
		if (branch == null) {
			throw new IllegalArgumentException("BranchManager: branch was null");
		}
		if (branches.get(branch.getSortCode()) != null) {
			throw new IllegalStateException("BranchManager: branch already exists");
		} else {
			branches.put(new SortCode(branch.getSortCode()), new Branch(branch));
		}
	}

	public synchronized void change(Branch oldBranch, Branch newBranch) {
		if (oldBranch == null) {
			throw new IllegalArgumentException("BranchManager: branch was null");
		}
		if (newBranch == null) {
			throw new IllegalArgumentException("BranchManager: branch was null");
		}
		if (newBranch.equals(branches.get(newBranch.getSortCode()))) {
			throw new IllegalStateException("BranchManager: branch already exists");
		}
		remove(oldBranch);
		add(newBranch);
	}

	public synchronized void remove(Branch branch) {
		if (branch == null) {
			throw new IllegalArgumentException("BranchManager: branch was null");
		}
		if (branches.get(branch.getSortCode()) == null) {
			throw new IllegalStateException("BranchManager: branch not found");
		} else {
			branches.remove(branch.getSortCode(), branch);
		}
	}

	public synchronized void clear() {
		branches.clear();
	}

	public synchronized List<SortCode> getSortCodes() {

		List<SortCode> sortCodeList = new ArrayList<>();
		branches.keySet().stream().forEach(sortCode -> {
			sortCodeList.add(new SortCode(sortCode));
		});
		Collections.sort(sortCodeList);
		return sortCodeList;
	}

	public synchronized Branch getBranchForSortCode(SortCode sortcode) {
		if (sortcode == null) {
			throw new IllegalArgumentException("BranchManager: sort code was null");
		} else {
			return branches.get(sortcode);
		}
	}

	public synchronized List<Branch> getAllBranches() {
		List<Branch> branchList = new ArrayList<>();
		getSortCodes().stream().forEach(sortCode -> {
			branchList.add(new Branch(branches.get(sortCode)));
		});
		Collections.sort(branchList);
		return branchList;
	}
}
