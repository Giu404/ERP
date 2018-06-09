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
import Startup.AppSettings;
import Startup.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.paint.Paint;

public class GuiBuilder {

	private TextField searchField;
	private Label matNameLabel;
	private Label matDescLabel;
	private Label matTypeLabel;
	private Label matWtLabel;
	private Label matVolLabel;
	
	private Label matName;
	private Label matDesc;
	private Label matType;
	private Label matWt;
	private Label matVol;
	
	private TextField nameField;
	private PasswordField passwordField;
	private Button loginButton;
	private Label loginStatusLabel;
	private ComboBox<String> searchHistory;
	private ComboBox<String> languageDropdownLogin;
	private ComboBox<String> languageDropdownSearch;
	private Label searchStatusLabel;
	private Button searchButton;
	private Button logoutButton;
	
	private Pane loginScreenRoot;
	private Pane searchScreenRoot;
	
	public GuiBuilder(Map<String, Material> map) throws InvalidPropertiesFormatException, IOException {
		initializeAttributes();
		buildScreens(map);
	}
	
	public void initializeAttributes() throws InvalidPropertiesFormatException, IOException {
		searchField = new TextField();
		nameField = new TextField();
		passwordField = new PasswordField();
		loginButton = new Button();
		searchHistory = new ComboBox<String>();
		searchHistory.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println(newValue);
			}    
	    });
		languageDropdownLogin = new ComboBox<String>();
		languageDropdownSearch = new ComboBox<String>();
		List<String> supportedLanguages = Language.languagesAsList();
		for(int i = 0; i < supportedLanguages.size(); i++) {
		    languageDropdownLogin.getItems().add(supportedLanguages.get(i));
		    languageDropdownSearch.getItems().add(supportedLanguages.get(i));
		}
		languageDropdownLogin.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					AppSettings.setClientLanguage(newValue);
					reloadTranslations();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}    
	    });
		languageDropdownSearch.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					AppSettings.setClientLanguage(newValue);
					reloadTranslations();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}    
	    });
	    loginStatusLabel = new Label();
		searchStatusLabel = new Label();
		matNameLabel = new Label();
		matDescLabel = new Label();
		matTypeLabel = new Label();
		matWtLabel = new Label();
		matVolLabel = new Label();
		searchButton = new Button();
		matName = new Label();
		matDesc = new Label();
		matType = new Label();
		matWt = new Label();
		matVol = new Label();		
		reloadTranslations();
	}
	
	public void buildScreens(Map<String, Material> map) throws InvalidPropertiesFormatException, IOException {
		buildLoginScreen();
		buildSearchScreen(map);
	}
	
	public Pane getLoginScreen() {
		return loginScreenRoot;
	}
	
	public Pane getSearchScreen() {
		return searchScreenRoot;
	}
	
	public void buildLoginScreen() throws InvalidPropertiesFormatException, IOException {		
		loginScreenRoot = new VBox(10);
		loginStatusLabel.setPadding(new Insets(10, 10, 10, 10));
		loginStatusLabel.setTextFill(Paint.valueOf("red"));
		loginStatusLabel.setVisible(false);
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event){
				if (event.getCode() == KeyCode.ENTER) {
					try {
						Main.handleLogin(nameField, passwordField, loginStatusLabel);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};		
		loginScreenRoot.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);		
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Main.handleLogin(nameField, passwordField, loginStatusLabel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		loginScreenRoot.setPadding(new Insets(10, 10, 10, 10));
		loginScreenRoot.getChildren().add(nameField);
		loginScreenRoot.getChildren().add(passwordField);
		loginScreenRoot.getChildren().add(loginButton);
		loginScreenRoot.getChildren().add(loginStatusLabel);
		loginScreenRoot.getChildren().add(languageDropdownLogin);
	}
	
	public Pane buildSearchScreen() {
		return new Pane();
	}
	
	public void buildSearchScreen(Map<String, Material> searchHistory) throws InvalidPropertiesFormatException, IOException {
		searchScreenRoot = new VBox(10);		
		Iterator<Entry<String, Material>> it = searchHistory.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Material> pair = (Map.Entry<String, Material>)it.next();
	        this.searchHistory.getItems().add(pair.getKey());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		matNameLabel.setPrefWidth(400);
		matNameLabel.setStyle("-fx-border-color: black;-fx-border-width: 0 0 1 0");
		matDescLabel.setPadding(new Insets(10, 0, 0, 0));
		matDescLabel.setPrefWidth(400);
		matTypeLabel.setPrefWidth(400);
		matWtLabel.setPrefWidth(400);
		matVolLabel.setPrefWidth(400);
		matNameLabel.setStyle("-fx-border-color: black; -fx-border-width: 1px 0px 1px 0px");
		matVolLabel.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 1px 0px");
		searchField.setPrefWidth(332);		
		matNameLabel.setVisible(false);
		matDescLabel.setVisible(false);
		matTypeLabel.setVisible(false);
		matWtLabel.setVisible(false);
		matVolLabel.setVisible(false);		
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER) {
					try {
						Main.handleSearch(searchStatusLabel);
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
					Main.handleSearch(searchStatusLabel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});		
		HBox searchPane = new HBox(searchField, searchButton);
		VBox materialPane = new VBox(this.matNameLabel, this.matName, this.matDescLabel, this.matDesc, 
									this.matTypeLabel, this.matType, this.matWtLabel, this.matWt, this.matVolLabel, this.matVol);
		searchPane.setPadding(new Insets(10, 10, 10, 10));
		searchStatusLabel.setPadding(new Insets(0, 10, 0, 10));
		searchStatusLabel.setTextFill(Paint.valueOf("red"));
		searchStatusLabel.setVisible(false);
		materialPane.setPadding(new Insets(0, 10, 0, 10));
		searchScreenRoot.getChildren().add(searchPane);
		searchScreenRoot.getChildren().add(searchStatusLabel);
		searchScreenRoot.getChildren().add(materialPane);
		searchScreenRoot.getChildren().add(this.searchHistory);
		searchScreenRoot.getChildren().add(languageDropdownSearch);
	}
	
	//TODO: Maybe use this to display the user that the app.config file is corrupted.
	public Pane buildErrorScreen(List<AppSettingsException> exceptions) {
		return new Pane();
	}
	
	public void setInfoVisible(String materialName, Material material, boolean visible) throws InvalidPropertiesFormatException, IOException {
		matNameLabel.setVisible(visible);
		matName.setVisible(visible);
		matDescLabel.setVisible(visible);
		matDesc.setVisible(visible);
		matTypeLabel.setVisible(visible);
		matType.setVisible(visible);
		matWtLabel.setVisible(visible);
		matWt.setVisible(visible);
		matVolLabel.setVisible(visible);
		matVol.setVisible(visible);
		if(visible) {
			matName.setText(materialName.toUpperCase());
			matDesc.setText(material.getDescription());
			matType.setText(material.getType());
			matWt.setText(material.getWeight() + " " + material.getUnitOfWeight());
			matVol.setText(material.getVolume() + " " + material.getVolumeUnit());
		}
	}
	
	public TextField getSearchField(){
		return this.searchField;
	}
	
	//TODO: search history update, threading, ui reordering
	
	//TODO: Might also need a thread
	public void reloadTranslations() throws InvalidPropertiesFormatException, IOException {
		nameField.setPromptText(Language.get("user_name"));
		passwordField.setPromptText(Language.get("user_password"));
		loginButton.setText(Language.get("login"));
		loginStatusLabel.setText(Language.get("login_fail"));
		searchField.setPromptText(Language.get("article_name") + " / " + Language.get("article_id"));
		searchHistory.setPromptText(Language.get("search_history"));
		languageDropdownLogin.setPromptText(AppSettings.CLIENT_LANGUAGE);
		languageDropdownSearch.setPromptText(AppSettings.CLIENT_LANGUAGE);
		searchButton.setText(Language.get("search"));
		matNameLabel.setText(Language.get("material"));
		matDescLabel.setText(Language.get("material_description"));
		matTypeLabel.setText(Language.get("material_type"));
		matWtLabel.setText(Language.get("material_weight"));
		matVolLabel.setText(Language.get("material_volume"));
		searchStatusLabel.setText(Language.get("id_not_found"));
		Main.setTitle();
	}
	
	public void updateSearchHistory() {
		
	}
	
}
