package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControllerZurueckZumMenue implements Initializable {

	@FXML
	private Button spielBeenden;
	@FXML
	private Button neuesSpiel;
	
	private boolean gewonnen;
	
	@FXML
	private Label text;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		if(gewonnen) {
			text.setText("Glückwunsch gewonnen! Möchten Sie erneut Spielen oder das Spiel beenden?");
		}
		
	}
	public ControllerZurueckZumMenue(boolean gewonnen) {
		this.gewonnen = gewonnen;
	}
	@FXML 
	private void neuesSpiel() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerOrClient.fxml"));
		ControllerServerOrClient csoc = new ControllerServerOrClient();
		fxmlLoader.setController(csoc);
		Parent root;
			try {
				
				root = fxmlLoader.load();
				Scene newScene = new Scene(root);
				StarterKlasse.primaryStage.setScene(newScene);
				StarterKlasse.primaryStage.setTitle("Hauptmenü");
				Stage oldStage = (Stage)neuesSpiel.getScene().getWindow();
				oldStage.close();
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void spielBeenden() {
		System.exit(0);
	}
}
