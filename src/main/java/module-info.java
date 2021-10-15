module bank.account {
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires java.xml;
	requires javafx.fxml;

	opens com.brailsoft.bank.account.model;
	opens com.brailsoft.bank.account.persistence;
	opens com.brailsoft.bank.account.controller;

	exports com.brailsoft.bank.account.launcher;
	exports com.brailsoft.bank.account.controller;
}