package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ControllerSpielErstellen implements Initializable {
	
	
	SpielHelfer helfer = new SpielHelfer();
	SchiffeVersenken spiel;
	
	@FXML
	private TextField textfield1;
	
	@FXML 
	private Button spielErstellen;
	
	@FXML
	private RadioButton radioButtonWeVsEnemy;
	
	@FXML
	private RadioButton radioButtonOfflineModus;
	
	@FXML
	private RadioButton radioButtonOurAI;
	
	@FXML
	private Button buttonZurueck;
	
	@FXML
	private ComboBox<String>comboBox;
	@FXML
	private Label schwierigkeitsStufe;
	
	private Connection connection;
	
	private ToggleGroup group = new ToggleGroup();
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		radioButtonWeVsEnemy.setToggleGroup(group);
		radioButtonOurAI.setToggleGroup(group);
		radioButtonOfflineModus.setToggleGroup(group);
		ObservableList<String>options=FXCollections.observableArrayList("Leicht","Mittel","Schwer");
		comboBox.setItems(options);
		comboBox.getSelectionModel().selectFirst();
		comboBox.setVisible(false);
		schwierigkeitsStufe.setVisible(false);
		radioButtonOfflineModus.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				comboBox.setVisible(true);
				schwierigkeitsStufe.setVisible(true);
				
			}
		});
		radioButtonOurAI.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				comboBox.setVisible(true);
				schwierigkeitsStufe.setVisible(true);
				
			}
		});
		radioButtonWeVsEnemy.setOnAction(new EventHandler<ActionEvent>() {
	
			@Override
			public void handle(ActionEvent arg0) {
				comboBox.setVisible(false);
				schwierigkeitsStufe.setVisible(false);
		
			}
		});
		
		
	}
	public ControllerSpielErstellen(Connection connection) {
		this.connection=connection;
		spiel = new SchiffeVersenken(connection);
		
	}
	
	public void erstelleSpiel(ActionEvent event) throws IOException {
		
		
		if(!radioButtonOfflineModus.isSelected()&&!radioButtonOurAI.isSelected()&&!radioButtonWeVsEnemy.isSelected()) {
    		System.out.println("W�hle zuerst einen Spielmodus");
    	}else {
    		final String parameter = textfield1.getText();
    		if(!(parameter.matches("[0-9]+"))) {
    			System.out.println("Nur Zahlen Erlaubt");
    			return;
    		}
    		if(Integer.parseInt(parameter)>70||
    				Integer.parseInt(parameter)<5) {
    			System.out.println("Spielfeldgr��e au�erhalb des erlaubten Bereichs");
    			return;
    		}
    		buttonZurueck.setDisable(true);
    		spielErstellen.setText("Warte auf Gegner...");
    		spielErstellen.setDisable(true);
    		
    		
    		//Zuf�lliges Kalkulieren der Schiffe anhand der Spielfeldgr��e 
    		//Parameter beinhaltet dann 10 2 2 1 0 z.B
    		final String parameterFertig = "setup "+parameter+" "+helfer.kalkuliereSchiffe(Integer.valueOf(parameter));
    		
    		//Spielfeld einrichten
    		System.out.println(parameterFertig);
    		
    		Task<String>task = new Task<String>() {

    			@Override
    			protected String call() throws Exception {
    				connection.createConnection();
    				
    		    	Platform.runLater(new Runnable(){

    					@Override
    					public void run() {
    						//Gegner das SETUP mitteilen
    						
    						connection.write(parameterFertig);
    						
    						//Wenn Client Gejoint ist Spielfeld einrichten und ins n�chste Fenster gehen
    						spiel.spielEinrichten(parameterFertig);
    						
    						FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldServer.fxml"));
    						ControllerSpielFeld csf = new ControllerSpielFeld(spiel.getSchiffListe(),parameterFertig,spiel,Integer.parseInt(parameter),connection);
    						fxmlloader.setController(csf);
    						
    						//Alte Stage die Verlassen wird
    				    	//Stage oldStage = (Stage)spielErstellen.getScene().getWindow();
    						
    						try {
    	    		    		
    							Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
    							//ControllerObjekt von der n�chsten Gui-Oberfl�che erzeugen um die SchiffsListe, den in und den Output Reader zu �bergeben
    							
    				    		Scene newScene = new Scene(root);
    							StarterKlasse.primaryStage.setScene(newScene);
    							StarterKlasse.primaryStage.setTitle("Battleship");
    							//newStage.setScene(new Scene(root, 1000, 600));
    							//oldStage.close();
    							//newStage.show();
    							
    						} catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
    						
    					}

    					
    				});
    		    	
    		    	
    				return null;
    			}
    			
    		};
    		
    		Thread th = new Thread(task);
    		th.setDaemon(true);
    		th.start();
    		
    	}
		
		//KI MODUS
		//
		/*Task<String>task = new Task<String>() {

			@Override
			protected String call() throws Exception {
				
		    	Platform.runLater(new Runnable(){

					@Override
					public void run() {
						//Gegner das SETUP mitteilen
						

						
						
						//Alte Stage die Verlassen wird
				    	//Stage oldStage = (Stage)spielErstellen.getScene().getWindow();
						
						try {
							FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldKI.fxml"));
							ControllerKI cKI = new ControllerKI(60,true);
							fxmlloader.setController(cKI);
							Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
							//ControllerObjekt von der n�chsten Gui-Oberfl�che erzeugen um die SchiffsListe, den in und den Output Reader zu �bergeben
							
				    		Scene newScene = new Scene(root);
							StarterKlasse.primaryStage.setScene(newScene);
							StarterKlasse.primaryStage.setTitle("Battleship");
							//newStage.setScene(new Scene(root, 1000, 600));
							//oldStage.close();
							//newStage.show();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					
				});
		    	
		    	
				return null;
			}
			
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();*/
    	
		
	}
	@FXML
	private void zurueckZumMenue() {
		
		connection.closeConnection();
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("Sample.fxml"));
		ControllerStartMenue csm = new ControllerStartMenue();
		fxmlloader.setController(csm);
		
		Parent root;
		try {
			root = fxmlloader.load();
			Scene newScene = new Scene(root);
			StarterKlasse.primaryStage.setScene(newScene);
			StarterKlasse.primaryStage.setTitle("Hauptmen�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Initialize der Controller Klasse wird schon hier aufgerufen 
		//ControllerObjekt von der n�chsten Gui-Oberfl�che erzeugen um die SchiffsListe, den in und den Output Reader zu �bergeben
		
		
	}
		

}
