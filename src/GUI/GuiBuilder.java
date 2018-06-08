package GUI;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Exceptions.AppSettingsException;
import Languages.Language;
import Models.Material;
import Startup.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
	
	private TextField nameField;
	PasswordField passwordField;
	Button loginButton;
	Label statusLabel;
	ComboBox<String> combo;
	Label searchStatusLabel;
	Button searchButton;
	
	public GuiBuilder() throws InvalidPropertiesFormatException, IOException {
		nameField = new TextField();
		passwordField = new PasswordField();
		loginButton = new Button();
		combo = new ComboBox<String>();
		searchStatusLabel = new Label();
		matNameLabel = new Label();
		matDescLabel = new Label();
		matTypeLabel = new Label();
		matWtLabel = new Label();
		matVolLabel = new Label();
		searchButton = new Button(Language.get("search"));
		
		reloadTranslations();
	}
	
	//TODO: Improve all the GUI Stuff. Split the labels into a material attribute label and a material value label
	public Pane buildLoginScreen() throws InvalidPropertiesFormatException, IOException {		
		VBox root = new VBox(10);
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event){
				if (event.getCode() == KeyCode.ENTER) {
					try {
						Main.handleLogin(nameField, passwordField, statusLabel);
					} catch (IOException e) {
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
					e.printStackTrace();
				}
			}
		});
		root.setPadding(new Insets(10, 10, 10, 10));
		root.getChildren().add(nameField);
		root.getChildren().add(passwordField);
		root.getChildren().add(loginButton);
		root.getChildren().add(statusLabel);
		return root;
	}
	
	public Pane buildSearchScreen(Map<String, Material> searchHistory) throws InvalidPropertiesFormatException, IOException {
		VBox root = new VBox(10);
		
		Iterator<Entry<String, Material>> it = searchHistory.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Material> pair = (Map.Entry<String, Material>)it.next();
	        combo.getItems().add(pair.getKey());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		this.searchField = new TextField();
		
		
		this.matNameLabel.setPrefWidth(400);
		this.matNameLabel.setStyle("-fx-border-color: black;-fx-border-width: 0 0 1 0");
		this.matDescLabel.setPadding(new Insets(10, 0, 0, 0));
		this.matDescLabel.setPrefWidth(400);
		this.matTypeLabel.setPrefWidth(400);
		this.matWtLabel.setPrefWidth(400);
		this.matVolLabel.setPrefWidth(400);
		this.matNameLabel.setStyle("-fx-border-color: black; -fx-border-width: 1px 0px 1px 0px");
		this.matVolLabel.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 1px 0px");
		this.searchField.setPrefWidth(332);
		
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
						e.printStackTrace();
					}
				}
			}
		};
		this.searchField.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		

		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Main.handleSearch(statusLabel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		HBox searchPane = new HBox(searchField, searchButton);
		VBox materialPane = new VBox(this.matNameLabel, this.matDescLabel, 
									this.matTypeLabel, this.matWtLabel, this.matVolLabel);
		searchPane.setPadding(new Insets(10, 10, 10, 10));
		searchStatusLabel.setPadding(new Insets(0, 10, 0, 10));
		materialPane.setPadding(new Insets(0, 10, 0, 10));
		
		root.getChildren().add(searchPane);
		root.getChildren().add(statusLabel);
		root.getChildren().add(materialPane);
		root.getChildren().add(combo);
		return root;
	}
	
	//TODO: Maybe use this to display the user that the app.config file is corrupted.
	public Pane buildErrorScreen(List<AppSettingsException> exceptions) {
		return new Pane();
	}
	
	public void setInfoVisible(String materialName, Material material, boolean visible) throws InvalidPropertiesFormatException, IOException {
		if(visible){

			this.matNameLabel.setText(Language.get("material") + ":\t\t\t\t" + materialName.toUpperCase());
			this.matDescLabel.setText(Language.get("material_description") + ":\t" + material.getDescription());
			this.matTypeLabel.setText(Language.get("material_type") + ":\t\t\t" + material.getType());
			this.matWtLabel.setText(Language.get("material_weight") + ":\t\t" + material.getWeight() + " " + material.getUnitOfWeight());
			this.matVolLabel.setText(Language.get("material_volume") + ":\t\t" + material.getVolume() + " " + material.getVolumeUnit());
			
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
	
	//TODO: Might also need a thread
	public void reloadTranslations() throws InvalidPropertiesFormatException, IOException {
		nameField.setPromptText(Language.get("user_name"));
		passwordField.setPromptText(Language.get("user_password"));
		loginButton.setText(Language.get("login"));
		searchField.setPromptText(Language.get("article_name") + " / " + Language.get("article_id"));
		combo.setPromptText("Search history");
		searchButton.setText(Language.get("search"));
	}
	
}
