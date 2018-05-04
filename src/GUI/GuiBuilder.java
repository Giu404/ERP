package GUI;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

import Languages.Translations;
import Startup.Main;
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
	
	public Pane buildLoginScreen() throws InvalidPropertiesFormatException, IOException {		
		TextField nameField = new TextField();
		nameField.setPromptText(Translations.get("user_name"));
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText(Translations.get("user_password"));
		Button loginButton = new Button();
		loginButton.setText(Translations.get("login"));
		Label statusLabel = new Label();
		VBox root = new VBox(10);
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event){
				if (event.getCode() == KeyCode.ENTER) {
					try {
						Main.handleLogin(nameField, passwordField, statusLabel);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		root.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Main.handleLogin(nameField, passwordField, statusLabel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		root.getChildren().add(nameField);
		root.getChildren().add(passwordField);
		root.getChildren().add(loginButton);
		root.getChildren().add(statusLabel);
		return root;
	}
	
	public Pane buildSearchScreen() throws InvalidPropertiesFormatException, IOException {
		VBox root = new VBox(10);
		Label statusLabel = new Label();
		searchField = new TextField();
		searchField.setPromptText(Translations.get("article_name") + " / " + Translations.get("article_id"));
		
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				//TODO: Maybe input validation (compare with given set of characters)
				if(event.getCode() == KeyCode.ENTER) {
					try {
						Main.handleSearch(statusLabel);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		searchField.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		
		Button searchButton = new Button(Translations.get("search"));
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Main.handleSearch(statusLabel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		root.getChildren().add(searchField);
		root.getChildren().add(searchButton);
		root.getChildren().add(statusLabel);
		return root;
	}
	
	public Pane buildInfoScreen(String materialName, Map<String, String> dataMap) throws InvalidPropertiesFormatException, IOException {
		VBox root = new VBox(10);
		Label matNameLabel = new Label(Translations.get("material") + ": " + materialName.toUpperCase());
		Label matDescLabel = new Label(Translations.get("material_description") + ": " + dataMap.get("MATL_DESC"));
		Label matTypeLabel = new Label(Translations.get("material_type") + ": " + dataMap.get("MATL_TYPE"));
		Label matWtLabel = new Label(Translations.get("material_weight") + ": " + dataMap.get("GROSS_WT") + " " + dataMap.get("UNIT_OF_WT"));
		Label matVolLabel = new Label(Translations.get("material_volume") + ": " + dataMap.get("VOLUME") + " " + dataMap.get("VOLUMEUNIT"));
		TextField searchField = new TextField();
		searchField.setPromptText(Translations.get("article_name") + " / " + Translations.get("article_id"));
		
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
		
		Button searchButton = new Button(Translations.get("search"));
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
