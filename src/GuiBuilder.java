import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
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
		root.setPadding(new Insets(10, 10, 10, 10));
		root.getChildren().add(nameField);
		root.getChildren().add(passwordField);
		root.getChildren().add(loginButton);
		root.getChildren().add(statusLabel);
		return root;
	}
	
	public Pane buildSearchScreen() {
		VBox root = new VBox(10);
		this.searchField = new TextField();
		Label statusLabel = new Label();
		this.matNameLabel = new Label();
		this.matDescLabel = new Label();
		this.matTypeLabel = new Label();
		this.matWtLabel = new Label();
		this.matVolLabel = new Label();
		
		this.matNameLabel.setPrefWidth(400);
		this.matNameLabel.setStyle("-fx-border-color: black;-fx-border-width: 0 0 1 0");
		this.matDescLabel.setPadding(new Insets(10, 0, 0, 0));
		this.matDescLabel.setPrefWidth(400);
		this.matTypeLabel.setPrefWidth(400);
		this.matWtLabel.setPrefWidth(400);
		this.matVolLabel.setPrefWidth(400);
		this.searchField.setPrefWidth(332);
		this.searchField.setPromptText("Artikelname");
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
					Main.handleSearch(statusLabel);
				}
			}
		};
		this.searchField.addEventHandler(KeyEvent.KEY_RELEASED, keyboardHandler);
		
		Button searchButton = new Button("Suche");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Main.handleSearch(statusLabel);
			}
		});
		
		HBox searchPane = new HBox(searchField, searchButton);
		VBox materialPane = new VBox(this.matNameLabel, this.matDescLabel, 
									this.matTypeLabel, this.matWtLabel, this.matVolLabel);
		searchPane.setPadding(new Insets(10, 10, 10, 10));
		statusLabel.setPadding(new Insets(0, 10, 0, 10));
		materialPane.setPadding(new Insets(0, 10, 0, 10));
		
		root.getChildren().add(searchPane);
		root.getChildren().add(statusLabel);
		root.getChildren().add(materialPane);
		return root;
	}
	
	public void setInfoVisible(String materialName, SAPController sapController, boolean visible) {
		if(visible){
			this.matNameLabel.setText(materialName.toUpperCase());
			this.matDescLabel.setText("Material Description:\t" + sapController.getDataMap().get("MATL_DESC"));
			this.matTypeLabel.setText("Material Type:\t\t\t" + sapController.getDataMap().get("MATL_TYPE"));
			this.matWtLabel.setText("Material Weight:\t\t" + sapController.getDataMap().get("GROSS_WT")+ " " +sapController.getDataMap().get("UNIT_OF_WT"));
			this.matVolLabel.setText("Material Volume:\t\t" + sapController.getDataMap().get("VOLUME")+ " " +sapController.getDataMap().get("VOLUMEUNIT"));
			
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
