package com.brailsoft.bank.account.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BankAccount extends Application {

	@Override
	public void start(Stage primaryStage) {
		Label label = new Label();
		label.setText("Bank Account UI goes here");
		Scene scene = new Scene(label, 200, 200);
		primaryStage.setTitle("Bank Account");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
