import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {

	private static SAPController sapController;
	private static GuiBuilder guiBuilder;
	private static Scene scene;
	private static JCoDestination connection;

	public static void main(String[] args) throws JCoException {
		AppSettings.loadAppSettings();
		sapController = new SAPController();
		guiBuilder = new GuiBuilder();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		boolean mockLogin;
		try {
			mockLogin = Boolean.parseBoolean(AppSettings.getProperty("mockLogin"));
		} catch (Exception e) {
			mockLogin = false;
		}
		
		if(mockLogin) {
			connection = sapController.tryLogin(AppSettings.getProperty("username"), AppSettings.getProperty("password"));
			if (connection != null) {
				scene = new Scene(guiBuilder.buildSearchScreen(), 400, 400);
			} else {
				scene = new Scene(guiBuilder.buildLoginScreen(), 400, 400);
			}
		} else {
			scene = new Scene(guiBuilder.buildLoginScreen(), 400, 400);
		}
		
		stage.setTitle("Logistik SAP Tool");
		stage.setScene(scene);
		stage.show();
	}
	
	public static void handleLogin(TextField nameField, PasswordField passwordField, Label statusLabel) {
		connection = sapController.tryLogin(nameField.getText(), passwordField.getText());
		if (connection != null) {
			scene.setRoot(guiBuilder.buildSearchScreen());
	/*		try {
				sapController.step3SimpleCall(connection);
			} catch (JCoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		} else {
			statusLabel.setText("Login NOT successful!");
			statusLabel.setTextFill(Paint.valueOf("red"));
		}
	}
	
	public static void handleSearch(Label statusLabel) {
		//TODO: Send BAPI request here
		boolean success = true;
		//sapController.callFunction(connection);
		sapController.callFunction(connection, guiBuilder.getSearchField().getText());
		
		if(!success) {
			statusLabel.setText("ID konnte nicht gefunden werden");
			statusLabel.setTextFill(Paint.valueOf("red"));
		} else {			
			scene.setRoot(guiBuilder.buildInfoScreen());
		}
	}
	
}
