import com.sap.conn.jco.JCoException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<KeyEvent>{
	
	private static TextField nameField;
	private static PasswordField passwordField;
	private static Button button;
	private static SAPController sapController;

	public static void main(String[] args) throws JCoException {
		sapController = new SAPController();
		launch(args);
	}
	


	@Override
	public void start(Stage stage) throws Exception {
		VBox root = new VBox(3);
        nameField = new TextField();
        nameField.setPromptText("Name");
        passwordField = new PasswordField();
        passwordField.setPromptText("Passwort");
        button = new Button();
        button.setText("Login");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	boolean loginSuccess = sapController.tryLogin(nameField.getText(), passwordField.getText());
    			System.out.println(loginSuccess ? "Login successful!" : "Login NOT successful!");
            }
        });
        
        root.getChildren().add(nameField);
        root.getChildren().add(passwordField);
        root.getChildren().add(button);
        stage.setTitle("Logistik SAP Tool");        
        Scene scene = new Scene(root, 400, 400);
        scene.setOnKeyPressed(this);
        stage.setScene(scene);        
        stage.show();    
	}

	@Override
	public void handle(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			boolean loginSuccess = sapController.tryLogin(nameField.getText(), passwordField.getText());
			System.out.println(loginSuccess ? "Login successful!" : "Login NOT successful!");
		}
	}
}
