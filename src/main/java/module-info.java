module bank.account {
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires javafx.base;

	opens com.brailsoft.bank.account.model;

	exports com.brailsoft.bank.account.launcher;
}