package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ControllerServerOrClient implements Initializable{
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void alsServerStarten() {
		StarterKlasse.server=true;
    	this.switchScene();
		
	}
	@FXML
	private void alsClientJoinen() {
		StarterKlasse.server=false;
    	this.switchScene();
	}
	
	private void switchScene() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sample.fxml"));
    	ControllerStartMenue csm = new ControllerStartMenue();
    	fxmlLoader.setController(csm);
		Parent root;
			try {
				root = fxmlLoader.load();
				
				StarterKlasse.primaryStage.setTitle("Battleship");
				StarterKlasse.primaryStage.setScene(new Scene(root, 1000, 600));
				StarterKlasse.primaryStage.show();
				
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
