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
 * Kontrollerklasse f�r das Fenster wenn man eine Speicheranfrage startet
 *
 */
public class ControllerSpeicherName implements Initializable {
	
	@FXML
	private Button speichernAbrrechen;
	@FXML 
	private TextField speicherName;
	@FXML 
	private Button speichernBestaetigen;
	
	
	private ControllerSpielFeld csf;
	
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
	 */
	public ControllerSpeicherName(ControllerSpielFeld csf) {
		this.csf = csf;
	}
	
	/**
	 * onAction-Methode f�r den Button speichernAbbrechen
	 * Schlie�t das Speicherfenster wieder
	 */
	@FXML 
	private void saveCancel() {
		Stage oldStage = (Stage)speichernAbrrechen.getScene().getWindow();
		oldStage.close();
	}
	
	/**
	 * onAction-Methode f�r den Button speichernBest�tigen
	 * Erzeugt eine Notifikation und ruft die Methode mit den entsprechenden Paramtern des Spielfeldkontrollers auf
	 */
	@FXML
	private void saveCommit() {
		
		Notifier.INSTANCE.notifySuccess("Gespeichert", "Das Spiel wurde erfolgreich gespeichert");
		csf.saveStart(speicherName.getText());
		Stage oldStage = (Stage)speichernBestaetigen.getScene().getWindow();
		oldStage.close();	
		
	}
	

}
