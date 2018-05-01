import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GuiBuilder {

	public Pane buildLoginScreen() {		
		TextField nameField = new TextField();
		nameField.setPromptText("Name");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Passwort");
		Button loginButton = new Button();
		loginButton.setText("Login");
		Label statusLabel = new Label();
		VBox root = new VBox(10);
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					Main.handleLogin(nameField, passwordField, statusLabel);
				}
			}
		};
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.handleLogin(nameField, passwordField, statusLabel);
			}
		});

		root.getChildren().add(nameField);
		root.getChildren().add(passwordField);
		root.getChildren().add(loginButton);
		root.getChildren().add(statusLabel);
		return root;
	}
	
	public Pane buildSearchScreen() {
		VBox root = new VBox(10);
		Label statusLabel = new Label();
		TextField searchField = new TextField();
		searchField.setPromptText("Artikel ID");
		
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				//TODO: Maybe input validation (compare with given set of characters)
				if(event.getCode() == KeyCode.ENTER) {
					Main.handleSearch(statusLabel);
				}
			}
		};
		searchField.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		
		Button searchButton = new Button("Suche");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.handleSearch(statusLabel);
			}
		});
		
		root.getChildren().add(searchField);
		root.getChildren().add(searchButton);
		root.getChildren().add(statusLabel);
		return root;
	}
	
	public Pane buildInfoScreen() {
		VBox root = new VBox(10);
		Label label = new Label("Ich diene zur Unterscheidung :))))");
		TextField searchField = new TextField();
		searchField.setPromptText("Artikel ID");
		
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				//TODO: Maybe input validation (compare with given set of characters)
				if(event.getCode() == KeyCode.ENTER) {
					System.out.println(searchField.getText());
				}
			}
		};
		searchField.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		
		Button searchButton = new Button("Suche");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println(searchField.getText());
			}
		});
		
		root.getChildren().add(searchField);
		root.getChildren().add(searchButton);
		root.getChildren().add(label);
		return root;
	}
	
}
