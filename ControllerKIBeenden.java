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

/**
 * Kontrollerklasse für das Schlussfenster, wenn das Spiel gewonnen/verloren wurde
 */
public class ControllerKIBeenden implements Initializable {
	/**
	 * FXML-Attribute
	 */
	@FXML
	private Button buttonNeuesSpiel;
	@FXML
	private Button buttonSpielBeenden;
	@FXML
	private Label spielAusgang;
	@FXML
	private Label statistikAnzeige;
	
	/**
	 * Objekt-Attribute
	 */
	private boolean b;
	private String shots;
	private String hits;
	private String fehler;
	private String quote;
	
	/**
	 * Text setzen je nach Spielausgang
	 * Statistik setzen
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(b) {
			spielAusgang.setText("Spiel gewonnen!");
		}else {
			spielAusgang.setText("Spiel verloren!");
		}
		
		statistikAnzeige.setText(shots+"\n"+hits+"\n"+fehler+"\n"+quote);
		
	}
	/**
	 * Konstruktor
	 * @param shots
	 * @param hits
	 * @param fehler
	 * @param quote
	 * @param b
	 */
	public ControllerKIBeenden(String shots, String hits, String fehler, String quote,boolean b) {
		this.shots = shots;
		this.hits = hits;
		this.fehler = fehler;
		this.quote = quote;
		this.b = b;
		
	}
	/**
	 * onAction-Methode für den Button neuesSpiel
	 * Leitet an das Startmenü weiter
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
				Stage oldStage = (Stage)buttonNeuesSpiel.getScene().getWindow();
				oldStage.close();
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * onAction-Methode für den Button spielBeenden
	 * Schließt das Spiel
	 */
	@FXML
	private void spielBeenden() {
		System.exit(0);
	}

}
