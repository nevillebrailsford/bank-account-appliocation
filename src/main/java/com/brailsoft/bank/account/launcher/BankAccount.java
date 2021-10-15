package com.brailsoft.bank.account.launcher;

import java.io.IOException;

import com.brailsoft.bank.account.controller.BankAccountController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BankAccount extends Application {

	private static BankAccountController appCtrl;;

	@Override
	public void start(Stage primaryStage) throws IOException {
		Scene scene = new Scene(loadFXML("BankAccount"));
		primaryStage.setScene(scene);
		primaryStage.setTitle("Bank Account");
		primaryStage.setScene(scene);
		primaryStage.show();
		scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				if (!appCtrl.shutdown()) {
					event.consume();
				} else {
					appCtrl.saveIniFile();
					Platform.exit();
				}

			}
		});
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader loader = new FXMLLoader(BankAccount.class.getResource(fxml + ".fxml"));
		Parent root;
		root = loader.load();
		appCtrl = loader.getController();
		return root;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
