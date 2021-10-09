package com.brailsoft.bank.account.model;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class BranchManager {
	private static BranchManager instance = null;

	private ObservableMap<String, ObservableList<SortCode>> sortcodes = null;

	private ListChangeListener<? super SortCode> listener;

	public synchronized static BranchManager getInstance() {
		if (instance == null) {
			instance = new BranchManager();
		}
		return instance;
	}

	private BranchManager() {
		sortcodes = FXCollections.observableMap(new ConcurrentHashMap<>());
		listener = null;
	}

	public synchronized void addMapListener(MapChangeListener<? super String, ? super List<SortCode>> listener) {
		sortcodes.addListener(listener);
	}

	public synchronized void removeMapListener(MapChangeListener<? super String, ? super List<SortCode>> listener) {
		sortcodes.removeListener(listener);
	}

	public synchronized void addListListener(ListChangeListener<? super SortCode> listener) {
		if (this.listener != null) {
			throw new IllegalStateException("BranchManager: BranchListListener already defined");
		}
		this.listener = listener;
		// addListenerToAllLists(listener);
	}

	public synchronized void removeListListener() {
		if (this.listener == null) {
			throw new IllegalStateException("BranchManager: BranchListListener not defined");
		}
		// removeListenerFromAllLists(listener);
		this.listener = null;
	}

}
