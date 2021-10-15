module bank.account {
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires java.xml;

	opens com.brailsoft.bank.account.model;
	opens com.brailsoft.bank.account.persistence;

	exports com.brailsoft.bank.account.launcher;
}