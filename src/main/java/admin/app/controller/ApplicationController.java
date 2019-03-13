package admin.app.controller;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationController {

	private Stage stage;

	public void init(Stage stage) {
		this.stage = stage;
		Group root = new Group();
		this.stage.setScene(new Scene(root));
	}
	
	public void loadView(String fxmlPath) {
		
	}
	
}
