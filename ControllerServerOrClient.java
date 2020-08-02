package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Kontrollerklasse des Startmenües, bei dem man sich für Server oder Client entscheiden kann
 *
 */
public class ControllerServerOrClient implements Initializable{
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * onAction-Methode für den Button alsServerStarten
	 */
	@FXML
	private void alsServerStarten() {
		StarterKlasse.server=true;
    	this.switchScene();
		
	}
	/**
	 * onAction-Methode für den Button alsClientjoinen
	 */
	@FXML
	private void alsClientJoinen() {
		StarterKlasse.server=false;
    	this.switchScene();
	}
	/**
	 * Hilfsmethode für die beiden Buttonklicks
	 * Leitet mit den entsprechenden Informationen an das Hautpmenü weiter
	 */
	private void switchScene() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample.fxml"));
    	ControllerStartMenue csm = new ControllerStartMenue();
    	fxmlLoader.setController(csm);
		Parent root;
			try {
				root = fxmlLoader.load();

				StarterKlasse.primaryStage.setScene(new Scene(root, 1080, 720));
				StarterKlasse.primaryStage.setTitle("Hauptmenü");
				StarterKlasse.primaryStage.show();
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * onAction-Methode für den Soundbutton
	 * Öffnet das Soundfenster
	 * @param event
	 */
	public void onActionSounds(ActionEvent event) {
		Stage newStage = new Stage();
		newStage.setScene(StarterKlasse.music);
		newStage.show();
	}
}
