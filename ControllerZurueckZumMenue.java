package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controllerklasse des Fensters, das aufgeht, wenn ein Spiel gewonnen/verloren wurde
 */	
public class ControllerZurueckZumMenue implements Initializable {
	
	
	@FXML
	private Button spielBeenden;
	@FXML
	private Button neuesSpiel;
	@FXML
	private Label text;

	
	private boolean gewonnen;
	
	/**
	 * Setzen des Textes des Labels je nach Ausgang des Spiels
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(gewonnen) {
			text.setText("Glückwunsch gewonnen! Möchten Sie erneut Spielen oder das Spiel beenden?");
		}
	}
	
	/**
	 * Konstruktor
	 * @param gewonnen
	 */
	public ControllerZurueckZumMenue(boolean gewonnen) {
		this.gewonnen = gewonnen;
	}
	
	/**
	 * onAction-Methode für den Button neues Spiel. 
	 * Leitet an das Startmenüfenster weiter.
	 */
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
				Stage oldStage = (Stage)neuesSpiel.getScene().getWindow();
				oldStage.close();
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * onAction-Methode für den Button spiel beenden. 
	 * Schließt das Spiel
	 */
	@FXML
	private void spielBeenden() {
		Platform.exit();
	}
}
