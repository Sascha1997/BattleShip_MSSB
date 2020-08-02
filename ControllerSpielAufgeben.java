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
import javafx.stage.Stage;

/**
 * Kontrollerklasse f�r das Fenster wenn man ein Spiel aufgeben m�chte
 * @author Matze
 *
 */
public class ControllerSpielAufgeben implements Initializable {
	
	@FXML
	private Button buttonAbbrechen;
	@FXML
	private Button buttonBestaetigen;
	
	private Connection connection;
	
	/**
	 * Konstruktor
	 * @param connection
	 */
	public ControllerSpielAufgeben(Connection connection) {
		this.connection=connection;
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	/**
	 * onAction-Methode f�r den Button best�gtigen
	 * Schlie�t die Connection und leitet zur�ck ins Startmen�
	 */
	@FXML
	private void aufgebenBestaetigen() {
		
		connection.closeConnection();
		
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ServerOrClient.fxml"));
		ControllerServerOrClient csoc = new ControllerServerOrClient();
		fxmlloader.setController(csoc);
		
		Stage oldStage = (Stage)buttonBestaetigen.getScene().getWindow();
	   						
		try {
    		
			Parent root = fxmlloader.load();
    		Scene newScene = new Scene(root);
    		StarterKlasse.primaryStage.setScene(newScene);
			oldStage.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * onAction-Methode f�r den Button abbrechen
	 * Schlie�t das Aufgeben Fenster wieder
	 */
	@FXML
	private void aufgebenAbbrechen() {
		Stage oldStage = (Stage)buttonAbbrechen.getScene().getWindow();
		oldStage.close();
	}
}
