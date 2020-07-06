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

public class ControllerSpielAufgeben implements Initializable {

	@FXML
	private Button buttonAbbrechen;
	
	@FXML
	private Button buttonBestaetigen;
	
	private Connection connection;
	public ControllerSpielAufgeben(Connection connection) {
		this.connection=connection;
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	@FXML
	private void aufgebenBestaetigen() {
		
		connection.closeConnection();
		
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ServerOrClient.fxml"));
		ControllerServerOrClient csoc = new ControllerServerOrClient();
		fxmlloader.setController(csoc);
		
		Stage oldStage = (Stage)buttonBestaetigen.getScene().getWindow();
	   						
		try {
    		
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der nächsten Gui-Oberfläche erzeugen um die SchiffsListe, den in und den Output Reader zu übergeben
			
    		Scene newScene = new Scene(root);
    		StarterKlasse.primaryStage.setScene(newScene);
			StarterKlasse.primaryStage.setTitle("Hauptmenü");
			//newStage.setScene(new Scene(root, 1000, 600));
			//newStage.show();
			oldStage.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	private void aufgebenAbbrechen() {
		Stage oldStage = (Stage)buttonAbbrechen.getScene().getWindow();
		oldStage.close();
	}
}
