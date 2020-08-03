package application;

import java.net.URL;
import java.util.ResourceBundle;

import eu.hansolo.enzo.notification.Notification.Notifier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * Kontrollerklasse für das Fenster zum Speichername für die Datei eintragen wenn man eine Speicheranfrage bekommt
 * @author Matze
 *
 */
public class ControllerSpeicherAnfrage implements Initializable {
	
	@FXML 
	private TextField speicherName;
	@FXML 
	private Button speichernBestaetigen;
	
	private ControllerSpielFeld csf;
	private String id;
	
	/**
	 * Initialize
	 * setzt die GUI-Komponenten und versieht das Textfeld mit einem Listener, dass den Speicherbutton nur aktiviert wenn was drin steht
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		speicherName.setPromptText("Dateiname");
		speicherName.setFocusTraversable(false);
		speicherName.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(!speicherName.getText().equals("")) {
					speichernBestaetigen.setDisable(false);
				}else {
					speichernBestaetigen.setDisable(true);
				}
				
			}
			
		});
		speichernBestaetigen.setDisable(true);
		
	}
	
	/**
	 * Konstruktor
	 * @param csf
	 * @param id
	 */
	public ControllerSpeicherAnfrage(ControllerSpielFeld csf, String id) {
		this.csf = csf;
		this.id = id;
	}
	
	/**
	 * onAction-Methode für den Button speichernBestätigen
	 * Erzeugt eine Notifikation und ruft die Methode mit den entsprechenden Paramtern des Spielfeldkontrollers auf
	 */
	@FXML
	private void saveCommit() {
		
		Notifier.INSTANCE.notifySuccess("Gespeichert", "Das Spiel wurde erfolgreich gespeichert");
		csf.saveStartAnfrage(speicherName.getText(),this.id);
		Stage oldStage = (Stage)speichernBestaetigen.getScene().getWindow();
		oldStage.close();	
		
	}
	
}
