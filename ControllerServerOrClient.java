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
 * Kontrollerklasse des Startmen�es, bei dem man sich f�r Server oder Client entscheiden kann
 *
 */
public class ControllerServerOrClient implements Initializable{
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * onAction-Methode f�r den Button alsServerStarten
	 */
	@FXML
	private void alsServerStarten() {
		StarterKlasse.server=true;
    	this.switchScene();
		
	}
	/**
	 * onAction-Methode f�r den Button alsClientjoinen
	 */
	@FXML
	private void alsClientJoinen() {
		StarterKlasse.server=false;
    	this.switchScene();
	}
	/**
	 * Hilfsmethode f�r die beiden Buttonklicks
	 * Leitet mit den entsprechenden Informationen an das Hautpmen� weiter
	 */
	private void switchScene() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample.fxml"));
    	ControllerStartMenue csm = new ControllerStartMenue();
    	fxmlLoader.setController(csm);
		Parent root;
			try {
				root = fxmlLoader.load();

				StarterKlasse.primaryStage.setScene(new Scene(root, 1080, 720));
				StarterKlasse.primaryStage.setTitle("Hauptmen�");
				StarterKlasse.primaryStage.show();
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * onAction-Methode f�r den Soundbutton
	 * �ffnet das Soundfenster
	 * @param event
	 */
	public void onActionSounds(ActionEvent event) {
		Stage newStage = new Stage();
		newStage.setScene(StarterKlasse.music);
		newStage.show();
	}
}
