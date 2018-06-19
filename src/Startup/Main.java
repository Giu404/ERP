package Startup;
import java.io.FileNotFoundException;
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
import Utils.SearchHistorySerializer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				connection = sapController.tryLogin(name, plainPassword);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (connection != null) {
							if(stayLoggedIn) {
								try {
									credentialController.setCredentials(name, plainPassword);
									AppSettings.setStayLoggedIn(true);
									credentialController.storeCredentials();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							scene.setRoot(guiBuilder.getSearchScreen());
							statusLabel.setVisible(false);
						} else {
							statusLabel.setVisible(true);
						}
					}					
				});
				return null;
			}			
		};
		new Thread(task).start();		
	}
	
	public static void handleLogout() {
		try {
			credentialController.deleteCredentials();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		guiBuilder.emptyUserInputFields(true);
		try {
			AppSettings.setStayLoggedIn(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		scene.setRoot(guiBuilder.getLoginScreen());
	}
	
	public static void handleSearch(Label statusLabel) throws InvalidPropertiesFormatException, IOException, InterruptedException, ExecutionException {
		Task<Void> task = new Task<Void>() {
			Material material;
			@Override
			public Void call() throws Exception {				
				material = sapController.getMaterialData(connection, guiBuilder.getSearchField().getText());
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(material.hasUninitializedAttributes()) {
							statusLabel.setVisible(true);
							try {
								guiBuilder.setInfoVisible("", material, false);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {			
							if(useSearchHistory) {				
								searchHistorySerializer.addToHistory(material);
								guiBuilder.updateSearchHistoryGui();
							}
							try {
								guiBuilder.setInfoVisible(guiBuilder.getSearchField().getText(), material, true);
							} catch (IOException e) {
								e.printStackTrace();
							}
							statusLabel.setVisible(false);
						}
						guiBuilder.getSearchField().setText("");
					}					
				});
				return null;
			}
			
		};
		new Thread(task).start();
	}
	
	public static void setTitle() throws InvalidPropertiesFormatException, IOException {
		if(Main.isReady) {
			((Stage)scene.getWindow()).setTitle(Language.get("app_name"));
		}
	}
	
}
