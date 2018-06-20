package GUI;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import Exceptions.AppSettingsException;
import Languages.Language;
import Models.Material;
import Startup.AppSettings;
import Startup.Main;
import Utils.SearchHistorySerializer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
	private CheckBox stayLoggedIn;
	private Label stayLoggedInLabel;
	private ComboBox<String> searchHistory;
	private ComboBox<String> languageDropdownLogin;
	private ComboBox<String> languageDropdownSearch;
	private Label searchStatusLabel;
	private Button searchButton;
	private Button logoutButton;
	
	private Pane loginScreenRoot;
	private Pane searchScreenRoot;
	
	private SearchHistorySerializer searchHistorySerializer;	
	private ChangeListener<String> changeListener;
	
	private String searchHistoryDisplayString = "";
	
	public GuiBuilder(SearchHistorySerializer searchHistorySerializer) throws InvalidPropertiesFormatException, IOException {
		this.searchHistorySerializer = searchHistorySerializer;
		initializeAttributes();
		buildScreens();
	}
	
	public void initializeAttributes() throws InvalidPropertiesFormatException, IOException {
		changeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					setInfoVisible(removeDate(newValue), searchHistorySerializer.getAccumulatedMaterialList().get(removeDate(newValue)), true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				searchStatusLabel.setVisible(false);
			}    
	    };
		searchField = new TextField();
		nameField = new TextField();
		passwordField = new PasswordField();
		loginButton = new Button();
		stayLoggedIn = new CheckBox();
		stayLoggedInLabel = new Label();
		searchHistory = new ComboBox<String>();
		searchHistory.valueProperty().addListener(changeListener);
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
					updateSearchHistoryGui(true);
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
					updateSearchHistoryGui(true);
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
		logoutButton = new Button();
		//TODO: Add action to button
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.handleLogout();
			}
		});
		reloadTranslations();
	}
	
	public void buildScreens() throws InvalidPropertiesFormatException, IOException {
		buildLoginScreen();
		buildSearchScreen();
	}
	
	public Pane getLoginScreen() {
		return loginScreenRoot;
	}
	
	public Pane getSearchScreen() {
		return searchScreenRoot;
	}
	
	public void buildLoginScreen() throws InvalidPropertiesFormatException, IOException {		
		loginScreenRoot = new VBox(10);
		loginStatusLabel.setTextFill(Paint.valueOf("red"));
		loginStatusLabel.setVisible(false);
		stayLoggedIn = new CheckBox();
		stayLoggedIn.setPadding(new Insets(0, 10, 0, 0));
		HBox upperGrouping = new HBox();
		HBox languageSelectionBox = new HBox();
		Region hFiller = new Region();
		HBox.setHgrow(hFiller, Priority.ALWAYS);
		Region hFiller2 = new Region();
		HBox.setHgrow(hFiller2, Priority.ALWAYS);
		Region vFiller1 = new Region();
		VBox.setVgrow(vFiller1, Priority.ALWAYS);
		languageSelectionBox.getChildren().addAll(languageDropdownLogin, hFiller);
		upperGrouping.getChildren().addAll(stayLoggedIn, stayLoggedInLabel, hFiller2, loginStatusLabel);
		//TODO: Add event to checkbox
		EventHandler<KeyEvent> keyboardHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event){
				if (event.getCode() == KeyCode.ENTER) {
					try {
						Main.handleLogin(nameField.getText(), passwordField.getText(), loginStatusLabel, stayLoggedIn.isSelected());
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
					Main.handleLogin(nameField.getText(), passwordField.getText(), loginStatusLabel, stayLoggedIn.isSelected());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		loginScreenRoot.setPadding(new Insets(10, 10, 10, 10));
		loginScreenRoot.getChildren().add(nameField);
		loginScreenRoot.getChildren().add(passwordField);
		loginScreenRoot.getChildren().add(upperGrouping);		
		loginScreenRoot.getChildren().add(loginButton);
		loginScreenRoot.getChildren().add(vFiller1);
		loginScreenRoot.getChildren().add(languageSelectionBox);
	}
	
	public void buildSearchScreen() throws InvalidPropertiesFormatException, IOException {
		searchScreenRoot = new VBox(10);		
		updateSearchHistoryGui(true);
		matNameLabel.setPrefWidth(150);
		matDescLabel.setPrefWidth(150);
		matTypeLabel.setPrefWidth(150);
		matWtLabel.setPrefWidth(150);
		matVolLabel.setPrefWidth(150);
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
					} catch (IOException | InterruptedException | ExecutionException e) {
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
				} catch (IOException | InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});		
		HBox searchPane = new HBox(searchField, searchButton);
		VBox materialPane = new VBox(new HBox(this.matNameLabel, this.matName), new HBox(this.matDescLabel, this.matDesc), 
				new HBox(this.matTypeLabel, this.matType), new HBox(this.matWtLabel, this.matWt), new HBox(this.matVolLabel, this.matVol));
		matNameLabel.setPadding(new Insets(5, 0, 20, 0));
		matName.setPadding(new Insets(5, 0, 20, 0));
		matDescLabel.setPadding(new Insets(0, 0, 5, 0));
		matDesc.setPadding(new Insets(0, 0, 5, 0));
		matTypeLabel.setPadding(new Insets(0, 0, 5, 0));
		matType.setPadding(new Insets(0, 0, 5, 0));
		matWtLabel.setPadding(new Insets(0, 0, 5, 0));
		matWt.setPadding(new Insets(0, 0, 5, 0));
		matVolLabel.setPadding(new Insets(0, 0, 5, 0));
		matVol.setPadding(new Insets(0, 0, 5, 0));
		materialPane.setStyle("-fx-border-color: black;-fx-border-width: 1.1 0 1.1 0");
		searchStatusLabel.setPadding(new Insets(0, 10, 0, 10));
		searchStatusLabel.setTextFill(Paint.valueOf("red"));
		searchStatusLabel.setVisible(false);
		materialPane.setPadding(new Insets(0, 10, 0, 10));
		
		HBox bottomBox = new HBox();
		Region hFiller = new Region();
		HBox.setHgrow(hFiller, Priority.ALWAYS);
		Region vFiller1 = new Region();
		VBox.setVgrow(vFiller1, Priority.ALWAYS);
		bottomBox.getChildren().addAll(languageDropdownSearch, hFiller, logoutButton);
		searchPane.setPadding(new Insets(20, 0, 0, 0));
		
		searchScreenRoot.setPadding(new Insets(10, 10, 10 ,10));
		searchScreenRoot.getChildren().add(searchPane);
		searchScreenRoot.getChildren().add(searchStatusLabel);
		searchScreenRoot.getChildren().add(materialPane);
		searchScreenRoot.getChildren().add(this.searchHistory);
		searchScreenRoot.getChildren().add(vFiller1);
		searchScreenRoot.getChildren().add(bottomBox);
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
		} else {
			//searchHistory.getSelectionModel().clearSelection();
			//TODO: Figure out, how the prompt text can be shown again
		}
	}
	
	public String appendDate(String materialName, String date) {
		return materialName + ", " + date;
	}
	
	public String removeDate(String searchHistoryEntry) {
		return searchHistoryEntry.substring(0, searchHistoryEntry.indexOf(","));
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
		stayLoggedInLabel.setText(Language.get("stay_logged_in"));
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
		logoutButton.setText(Language.get("logout"));
		Main.setTitle();
	}
	
	public String getSearchHistoryDisplayString() {
		String s = searchHistory.getValue();
		if(s == null) {
			return "";
		} else {
			try {
				return removeDate(s);
			} catch(Exception e) {
				return "";
			}
		}
	}
	
	public void updateSearchHistoryGui(boolean calledFromSearchHistory) {
		if(!calledFromSearchHistory) {			
			searchHistoryDisplayString = searchField.getText().toUpperCase();
		} else {
			searchHistoryDisplayString = getSearchHistoryDisplayString();
		}
		searchHistory.valueProperty().removeListener(changeListener);
		searchHistory.getItems().clear();
		Iterator<Entry<String, Material>> it = searchHistorySerializer.getAccumulatedMaterialList().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Material> pair = (Map.Entry<String, Material>)it.next();
	        this.searchHistory.getItems().add(appendDate(pair.getKey(), pair.getValue().getLookupDateTimeLocalizedString()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    if(!searchHistoryDisplayString.equalsIgnoreCase("")) {
	    	searchHistory.setValue(appendDate(searchHistoryDisplayString, searchHistorySerializer.getAccumulatedMaterialList().get(searchHistoryDisplayString).getLookupDateTimeLocalizedString()));
	    	searchHistoryDisplayString = "";
	    }
	    searchHistory.valueProperty().addListener(changeListener);
	}
	
	public void emptyUserInputFields(boolean emptyFields) {
		stayLoggedIn.setSelected(!emptyFields);
		if(emptyFields) {
			loginStatusLabel.setVisible(false);
			nameField.setText("");
			passwordField.setText("");
		}
	}
	
}
