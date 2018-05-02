import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
		this.matDescLabel.setPrefWidth(400);
		this.matTypeLabel.setPrefWidth(400);
		this.matWtLabel.setPrefWidth(400);
		this.matVolLabel.setPrefWidth(400);
		this.matNameLabel.setStyle("-fx-border-color: black; -fx-border-width: 1px 0px 1px 0px");
		this.matVolLabel.setStyle("-fx-border-color: black; -fx-border-width: 0px 0px 1px 0px");
		this.searchField.setPrefWidth(352);
		this.searchField.setPromptText("Artikel ID");
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
		root.getChildren().add(searchPane);
		root.getChildren().add(statusLabel);
		root.getChildren().add(this.matNameLabel);
		root.getChildren().add(this.matDescLabel);
		root.getChildren().add(this.matTypeLabel);
		root.getChildren().add(this.matWtLabel);
		root.getChildren().add(this.matVolLabel);
		return root;
	}
	
	public void setInfoVisible(String materialName, SAPController sapController, boolean visible) {
		if(visible){
			this.matNameLabel.setText("Material:\t\t\t\t" + materialName.toUpperCase());
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
