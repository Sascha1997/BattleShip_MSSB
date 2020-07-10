package application;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerStartMenue implements Initializable {	
	
	@FXML
	private Label label1;
	
	@FXML
	private Label label2;
	
	@FXML
	private Button buttonerstellen;
	
	@FXML
	private Button buttonSpielSuchen;
	
	@FXML
	private Button buttonSpielLaden;
	
	@FXML
	private TextField textfieldIP;
	
	@FXML
	private CheckBox checkBoxKI;
	
	private SchiffeVersenken spiel;
	
	private String spielfeld;
	private Connection connection;
	private Files file = new Files();
    
  //Kleine Information ob Server oder Client
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	connection = new Connection();
    	spiel = new SchiffeVersenken(connection);
        System.out.println("View is now loaded!");
        label1.setText(connection.getIp());
        if(StarterKlasse.server) {
        	label2.setText("Wir sind Server");
        	buttonSpielSuchen.setDisable(true);
        	textfieldIP.setVisible(false);
        }else {
        	label2.setText("Wir sind Client");
        	buttonerstellen.setDisable(true);
        	buttonSpielLaden.setDisable(true);
        	textfieldIP.setPromptText("IP-Adresse Server");
        }
        
    }
    
    //ButtonClick bei Spiel erstellen, ändert die Stage auf die Erstellen Maske
    public void onActionSpielErstellen(ActionEvent event) {
    	FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielErstellen.fxml"));
    	
    	ControllerSpielErstellen cse = new ControllerSpielErstellen(connection);
    	fxmlloader.setController(cse);
    	try {
    		Parent root = fxmlloader.load();
    		Scene newScene = new Scene(root);
    		StarterKlasse.primaryStage.setScene(newScene);
    		
    			
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    
    	
    	
    }
    
    @FXML
    private void onActionSpielSuchen(ActionEvent event) throws IOException, UnknownHostException{
    	    
    	FXMLLoader fxmlloaderR = new FXMLLoader(getClass().getResource("RefuseToConnect.fxml"));
	    Parent rootR = fxmlloaderR.load();
	    
	    if(checkBoxKI.isSelected()) {//KI
	    	
    					FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldKI.fxml"));
    					ControllerKI cKI = new ControllerKI(5,2,false);
    					fxmlloader.setController(cKI);
    					try {
    								
    					Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
    					//ControllerObjekt von der nächsten Gui-Oberfläche erzeugen um die SchiffsListe, den in und den Output Reader zu übergeben
    			
	    					Scene newScene = new Scene(root);
	    					StarterKlasse.primaryStage.setScene(newScene);
	    					StarterKlasse.primaryStage.setTitle("Battleship");
    								
    					} catch (IOException e) {
    					// TODO Auto-generated catch block
    						e.printStackTrace();
    					} 
    		
	    }else { //Spieler
	    	
		    //If Spiel geladen dann Konstruktor Spielladen aufrufen
			//else ganz normaler Spielstart
	    	
	    	try {
				connection.connectAsClient();
			} catch (ConnectException e1) {
				Stage newStage = new Stage();
	    		newStage.setTitle("Verbindung Fehlgeschlagen");
	    		newStage.setScene(new Scene(rootR, 500, 350));
	    		newStage.show();
			}
	    	Task<String>task = new Task<String>() {
				@Override
				protected String call() throws Exception {
					spielfeld=connection.read();
			    	if(spielfeld.substring(0, 1).equals("l")) {
			    		String parts[]=spielfeld.split(" ");
			    		System.out.println("Altes Spiel Laden");
			    		file.load(Long.parseLong(parts[1]));
			    		Platform.runLater(new Runnable(){
							@Override
							public void run() {
								FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldClient.fxml"));
								ControllerSpielFeld csf = new ControllerSpielFeld(file.getTurn(),file.getOwnField(),file.getEnemyField(),file.getShips(),file.getDestroyedOwn(),file.getDestroyedEnemy(),file.getPullCount(),file.getSchiffCounter(),connection);
								fxmlloader.setController(csf);
								try {
									
									Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
									//ControllerObjekt von der nächsten Gui-Oberfläche erzeugen um die SchiffsListe, den in und den Output Reader zu übergeben
									
									Scene newScene = new Scene(root);
									StarterKlasse.primaryStage.setScene(newScene);
									StarterKlasse.primaryStage.setTitle("Battleship");
									
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
							}
						});
			    		
			    		
			    	}else {
			    		
			    		Platform.runLater(new Runnable(){
							@Override
							public void run() {
								String parts[]=spielfeld.split(" ");
								FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldClient.fxml"));
								ControllerSpielFeld csf = new ControllerSpielFeld(spiel.getSchiffListe(),spielfeld,spiel,Integer.parseInt(parts[1]),connection);
								fxmlloader.setController(csf);
								try {
									
									Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
									//ControllerObjekt von der nächsten Gui-Oberfläche erzeugen um die SchiffsListe, den in und den Output Reader zu übergeben
									
									Scene newScene = new Scene(root);
									StarterKlasse.primaryStage.setScene(newScene);
									StarterKlasse.primaryStage.setTitle("Battleship");
									
									
									
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
								
							}
							
						});
			    	}
			    	spiel.spielEinrichten(spielfeld);
			    	
					return null;
				}
				
			};
			
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
	    }
	    
    	
			
    	
    }
    
    @FXML
    private void onActionSpielLaden(ActionEvent event) {
    	FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielLaden.fxml"));
		ControllerSpielLaden csl = new ControllerSpielLaden(connection);
		fxmlloader.setController(csl);
		try {
			
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der nächsten Gui-Oberfläche erzeugen um die SchiffsListe, den in und den Output Reader zu übergeben
			
			//Alte Stage die Verlassen wird
			//Stage oldStage=(Stage)buttonSpielLaden.getScene().getWindow();
			Scene newScene = new Scene(root);
			StarterKlasse.primaryStage.setScene(newScene);
			StarterKlasse.primaryStage.setTitle("Altes Spiel Laden");
			//newStage.setTitle("Spiel laden");
			//newStage.setScene(new Scene(root, 800, 550));
			//newStage.show();
			//oldStage.close();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    
   
}
