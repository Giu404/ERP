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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GuiBuilder {

	private TextField searchField;
	private Label matNameLabel;
	private Label matDescLabel;
	private Label matTypeLabel;
	private Label matWtLabel;
	private Label matVolLabel;
	
	//TODO: Improve all the GUI Stuff. Split the labels into a material attribute label and a material value label
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
		this.searchField = new TextField();
		Label statusLabel = new Label();
		this.matNameLabel = new Label();
		this.matDescLabel = new Label();
		this.matTypeLabel = new Label();
		this.matWtLabel = new Label();
		this.matVolLabel = new Label();
		
		this.matNameLabel.setPrefWidth(400);
		this.matDescLabel.setPrefWidth(400);
		this.matTypeLabel.setPrefWidth(400);
		this.matWtLabel.setPrefWidth(400);
		this.matVolLabel.setPrefWidth(400);
		this.matNameLabel.setStyle("-fx-border-color: black; -fx-border-width: 1px 0px 1px 0px");
		this.matVolLabel.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 1px 0px");
		this.searchField.setPrefWidth(352);
		searchField.setPromptText(Translations.get("article_name") + " / " + Translations.get("article_id"));
		this.matNameLabel.setVisible(false);
		this.matDescLabel.setVisible(false);
		this.matTypeLabel.setVisible(false);
		this.matWtLabel.setVisible(false);
		this.matVolLabel.setVisible(false);
		
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
		this.searchField.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		
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
		HBox searchPane = new HBox(searchField, searchButton);
		root.getChildren().add(searchPane);
		root.getChildren().add(statusLabel);
		root.getChildren().add(this.matNameLabel);
		root.getChildren().add(this.matDescLabel);
		root.getChildren().add(this.matTypeLabel);
		root.getChildren().add(this.matWtLabel);
		root.getChildren().add(this.matVolLabel);
		return root;
	}
	
	public void setInfoVisible(String materialName, Map<String, String> dataMap, boolean visible) throws InvalidPropertiesFormatException, IOException {
		if(visible){
			this.matNameLabel.setText(Translations.get("material") + ":\t\t\t\t" + materialName.toUpperCase());
			this.matDescLabel.setText(Translations.get("material_description") + ":\t" + dataMap.get("MATL_DESC"));
			this.matTypeLabel.setText(Translations.get("material_type") + ":\t\t\t" + dataMap.get("MATL_TYPE"));
			this.matWtLabel.setText(Translations.get("material_weight") + ":\t\t" + dataMap.get("GROSS_WT")+ " " + dataMap.get("UNIT_OF_WT"));
			this.matVolLabel.setText(Translations.get("material_volume") + ":\t\t" + dataMap.get("VOLUME")+ " " + dataMap.get("VOLUMEUNIT"));
			
			this.matNameLabel.setVisible(true);
			this.matDescLabel.setVisible(true);
			this.matTypeLabel.setVisible(true);
			this.matWtLabel.setVisible(true);
			this.matVolLabel.setVisible(true);
		} else {
			this.matNameLabel.setVisible(false);
			this.matDescLabel.setVisible(false);
			this.matTypeLabel.setVisible(false);
			this.matWtLabel.setVisible(false);
			this.matVolLabel.setVisible(false);
		}
	}
	
	public TextField getSearchField(){
		return this.searchField;
	}
	
}
