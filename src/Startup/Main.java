package Startup;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

import Controllers.SAPController;
import GUI.GuiBuilder;
import Languages.Translations;
import Models.Material;
import Utils.SearchHistorySerializer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {

	private static SAPController sapController;
	private static SearchHistorySerializer searchHistorySerializer;
	private static GuiBuilder guiBuilder;
	private static Scene scene;
	private static JCoDestination connection;

	public static void main(String[] args) throws JCoException {
		AppSettings.loadAppSettings();
		sapController = new SAPController();
		searchHistorySerializer = new SearchHistorySerializer();
		guiBuilder = new GuiBuilder();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		boolean quickLogin;
		try {
			quickLogin = Boolean.parseBoolean(AppSettings.getProperty("quickLogin"));
		} catch (Exception e) {
			quickLogin = false;
		}
		
		if(quickLogin) {
			connection = sapController.tryLogin(AppSettings.getProperty("username"), AppSettings.getProperty("password"));
			if (connection != null) {
				scene = new Scene(guiBuilder.buildSearchScreen(), 400, 400);
			} else {
				scene = new Scene(guiBuilder.buildLoginScreen(), 400, 400);
			}
		} else {
			scene = new Scene(guiBuilder.buildLoginScreen(), 400, 400);
		}
		
		stage.setTitle(Translations.get("app_name"));
		stage.setScene(scene);
		stage.show();
	}
	
	public static void handleLogin(TextField nameField, PasswordField passwordField, Label statusLabel) throws InvalidPropertiesFormatException, IOException {
		connection = sapController.tryLogin(nameField.getText(), passwordField.getText());
		if (connection != null) {
			scene.setRoot(guiBuilder.buildSearchScreen());
		} else {
			statusLabel.setText(Translations.get("login_fail"));
			statusLabel.setTextFill(Paint.valueOf("red"));
		}
	}
	
	public static void handleSearch(Label statusLabel) throws InvalidPropertiesFormatException, IOException {
		Material material = sapController.getMaterialData(connection, guiBuilder.getSearchField().getText());
		if(material.hasUninitializedAttributes()) {
			statusLabel.setVisible(true);
			statusLabel.setText(Translations.get("id_not_found"));
			statusLabel.setTextFill(Paint.valueOf("red"));
			guiBuilder.setInfoVisible("", material, false);
			searchHistorySerializer.addToHistory(material);
		} else {			
			guiBuilder.setInfoVisible(guiBuilder.getSearchField().getText(), material, true);
			statusLabel.setVisible(false);			
		}
		guiBuilder.getSearchField().setText("");
	}
	
}
