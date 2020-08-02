package application;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * Kontrollerklasse für das Informationsfenster wenn nicht gejoint werden kann
 * @author Matze
 *
 */
public class ControllerRefuseToConnect implements Initializable {
	@FXML 
	private Button erneutVersuchen;
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * onAction-Methode des Buttons erneutVersuchen
	 * Schließt das Informationsfenster
	 * @param event
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@FXML
	private void neuVerbinden(ActionEvent event) throws UnknownHostException, IOException {
		
		Stage oldStage = (Stage)erneutVersuchen.getScene().getWindow();
		oldStage.close();
		
	}

	
	
}
