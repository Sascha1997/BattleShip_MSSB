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

	
public class ControllerSpielAbbruch implements Initializable{
	@FXML
	private Button zurueckZumHauptMenue;
	
	private Connection connection;
	public ControllerSpielAbbruch(Connection connection) {
		this.connection=connection;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void goBack(ActionEvent event) {
		
		connection.closeConnection();
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ServerOrClient.fxml"));
		ControllerServerOrClient csoc = new ControllerServerOrClient();
		fxmlloader.setController(csoc);
		
		Stage oldStage = (Stage)zurueckZumHauptMenue.getScene().getWindow();
	   						
		try {
    		
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der nächsten Gui-Oberfläche erzeugen um die SchiffsListe, den in und den Output Reader zu übergeben
			
    		Scene newScene = new Scene(root);
    		StarterKlasse.primaryStage.setScene(newScene);
			StarterKlasse.primaryStage.setTitle("Hauptmenü");
			oldStage.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
