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

	private TextField searchField;
	
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
		searchField = new TextField();
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
	
	public Pane buildInfoScreen(String materialName, SAPController sapController) {
		VBox root = new VBox(10);
		Label matNameLabel = new Label("Material: " + materialName.toUpperCase());
		Label matDescLabel = new Label("Material Description: " + sapController.getDataMap().get("MATL_DESC"));
		Label matTypeLabel = new Label("Material Type: " + sapController.getDataMap().get("MATL_TYPE"));
		Label matWtLabel = new Label("Material Weight: " + sapController.getDataMap().get("GROSS_WT") + " " + sapController.getDataMap().get("UNIT_OF_WT"));
		Label matVolLabel = new Label("Material Volume: " + sapController.getDataMap().get("VOLUME") + " " + sapController.getDataMap().get("VOLUMEUNIT"));
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
		root.getChildren().add(matNameLabel);
		root.getChildren().add(matDescLabel);
		root.getChildren().add(matTypeLabel);
		root.getChildren().add(matWtLabel);
		root.getChildren().add(matVolLabel);
		return root;
	}
	
	public TextField getSearchField(){
		return this.searchField;
	}
	
}
