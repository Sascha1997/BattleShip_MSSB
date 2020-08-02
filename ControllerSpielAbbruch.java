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
 * Kontrollerklasse für das Fenster wenn das Spiel abgebrochen wurde
 *
 */
	
public class ControllerSpielAbbruch implements Initializable{
	
	@FXML
	private Button zurueckZumHauptMenue;
	
	
	private Connection connection;
	
	/**
	 * Konstruktor
	 * @param connection
	 */
	public ControllerSpielAbbruch(Connection connection) {
		this.connection=connection;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * onAction-Methode für den Button zureuckZumHauptMenue
	 * Leitet an das Startmenü weiter und schließt die Connection
	 * @param event
	 */
	@FXML
	private void goBack(ActionEvent event) {
		
		connection.closeConnection();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
