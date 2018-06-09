package Startup;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

import Controllers.CredentialController;
import Controllers.SAPController;
import GUI.GuiBuilder;
import Languages.Language;
import Models.Material;
import Utils.EncryptionUtils;
import Utils.SearchHistorySerializer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {

	public static boolean isReady = false;
	
	private static SAPController sapController;
	private static CredentialController credentialController;
	private static SearchHistorySerializer searchHistorySerializer;
	private static GuiBuilder guiBuilder;
	private static Scene scene;
	private static JCoDestination connection;
	private static boolean useSearchHistory;

	public static void main(String[] args) throws JCoException, InvalidPropertiesFormatException, IOException {
		AppSettings.loadAppSettings();
		sapController = new SAPController();
		try {
			useSearchHistory = Boolean.parseBoolean(AppSettings.getProperty("useSearchHistory"));
		} catch(Exception e) {
			useSearchHistory = false;
		}
		if(useSearchHistory) {
			searchHistorySerializer = new SearchHistorySerializer();
		}
		credentialController = new CredentialController();
		credentialController.loadCredentials();
		guiBuilder = new GuiBuilder(searchHistorySerializer);
//		String pw = "mysecurepw";
//		System.out.println("lululu");
//		System.out.println(EncryptionUtils.decrypt(EncryptionUtils.encrypt(pw)));
//		String encryptedpw = EncryptionUtils.encrypt(pw);
//		System.out.println(encryptedpw);
//		String decryptedpw = EncryptionUtils.decrypt(encryptedpw);
//		System.out.println(decryptedpw);
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		boolean stayLoggedIn;
		try {
			stayLoggedIn = Boolean.parseBoolean(AppSettings.getProperty("stay_logged_in"));
		} catch (Exception e) {
			stayLoggedIn = false;
		}
		
		if(stayLoggedIn) {
			connection = sapController.tryLogin(credentialController.getLoginName(), credentialController.getDecryptedPassword());
			if (connection != null) {
				scene = new Scene(guiBuilder.getSearchScreen(), 400, 400);
			} else {
				scene = new Scene(guiBuilder.getLoginScreen(), 400, 400);
			}
		} else {
			scene = new Scene(guiBuilder.getLoginScreen(), 400, 400);
		}
		
		stage.setTitle(Language.get("app_name"));
		stage.setScene(scene);
		Main.isReady = true;
		stage.show();
	}
	
	public static void handleLogin(String name, String plainPassword, Label statusLabel, boolean stayLoggedIn) throws InvalidPropertiesFormatException, IOException {
		connection = sapController.tryLogin(name, plainPassword);
		if (connection != null) {
			if(stayLoggedIn) {
				credentialController.setCredentials(name, plainPassword);
				credentialController.storeCredentials();
			}
			scene.setRoot(guiBuilder.getSearchScreen());
			statusLabel.setVisible(false);
		} else {
			statusLabel.setVisible(true);
		}
	}
	
	public static void handleSearch(Label statusLabel) throws InvalidPropertiesFormatException, IOException, InterruptedException, ExecutionException {
		Material material = sapController.getMaterialData(connection, guiBuilder.getSearchField().getText());
		if(material.hasUninitializedAttributes()) {
			statusLabel.setVisible(true);
			guiBuilder.setInfoVisible("", material, false);
		} else {			
			if(useSearchHistory) {				
				searchHistorySerializer.addToHistory(material);
				guiBuilder.updateSearchHistoryGui();
			}
			guiBuilder.setInfoVisible(guiBuilder.getSearchField().getText(), material, true);
			statusLabel.setVisible(false);
		}
		guiBuilder.getSearchField().setText("");
	}
	
	public static void setTitle() throws InvalidPropertiesFormatException, IOException {
		if(Main.isReady) {
			((Stage)scene.getWindow()).setTitle(Language.get("app_name"));
		}
	}
	
}
