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
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * Kontrollerklasse f�r das Abbruchfenster der KI
 *	
 */
public class ControllerKIAbbruch implements Initializable{
	
	@FXML
	private Button zurueckZumHauptMenue;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	
	/**
	 * onAction-Methode f�r den zur�ckButton
	 * Leitet ins Startmen� weiter
	 * @param event
	 */
	@FXML
	private void goBack(ActionEvent event) {
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ServerOrClient.fxml"));
		ControllerServerOrClient csoc = new ControllerServerOrClient();
		fxmlloader.setController(csoc);
		Stage oldStage = (Stage)zurueckZumHauptMenue.getScene().getWindow();
	   						
		try {
    		
			Parent root = fxmlloader.load();
    		Scene newScene = new Scene(root);
    		StarterKlasse.primaryStage.setScene(newScene);
			oldStage.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
