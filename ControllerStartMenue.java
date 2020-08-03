package application;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Kontrollerklasse des Fensters im Startmenü
 */	
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
	@FXML
	private Label label3;
	@FXML
	private ComboBox<String>comboBox;
	@FXML
	private Label label4;
	
	private SchiffeVersenken spiel;
	private String spielfeld;
	private Connection connection;
	private Files file = new Files();
	
    
  	/**
  	 * Initialize
  	 * Setzt je nach Server oder Client die entsprechenden GUI-Komponenten auf Disabled oder nicht
  	 * Holt außerdem die IP-Adresse, um diese dem Client mitzuteilen.
  	 */
    public void initialize(URL location, ResourceBundle resources) {
    	connection = new Connection();
    	spiel = new SchiffeVersenken(connection);
    	
        
        if(StarterKlasse.server) {
        	label1.setText(connection.getIp());
        	label2.setText("Wir sind Server");
        	buttonSpielSuchen.setDisable(true);
        	checkBoxKI.setVisible(false);
        	textfieldIP.setVisible(false);
        }else {
        	label2.setText("Wir sind Client");
        	label4.setVisible(false);
        	label1.setVisible(false);
        	buttonerstellen.setDisable(true);
        	buttonSpielLaden.setDisable(true);
        	
        	textfieldIP.setFocusTraversable(false);
        	textfieldIP.setPromptText("IP-Adresse Server");
        	textfieldIP.setOnMouseClicked(e->{
        	label3.setVisible(false);
        	});
        	
        }
        ObservableList<String>options=FXCollections.observableArrayList("Leicht","Mittel","Schwer");
		comboBox.setItems(options);
		comboBox.getSelectionModel().selectFirst();
		comboBox.setVisible(false);
    }
    
    /**
     * onAction-Methode für den Button spielErstellen.
     * Leitet weiter an das Fenster, in dem man ein Spiel erstellen kann.
     * @param event
     */
    public void onActionSpielErstellen(ActionEvent event) {
    	FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielErstellen.fxml"));
    	
    	ControllerSpielErstellen cse = new ControllerSpielErstellen(connection);
    	fxmlloader.setController(cse);
    	try {
    		Parent root = fxmlloader.load();
    		Scene newScene = new Scene(root);
    		StarterKlasse.primaryStage.setScene(newScene);
    		StarterKlasse.primaryStage.setTitle("Spiel erstellen");
    		
    			
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    
    }
    /**
     * onAction-Handler der CheckBox
     * Reagiert auf Klicks und setzt entsprechend die Combobox der KI-Schwierigkeit sichtbar oder nicht
     */
    @FXML
    private void checkBoxClick() {
    	if(comboBox.isVisible()) {
    		comboBox.setVisible(false);
    	}else {
    		comboBox.setVisible(true);
    	}
    	
    }
    
    /**
     * onAction-Methode des Buttons spielSuchen. 
     * Prüft, ob es sich um eine gültige IP-Adresse handelt und verzweigt je nach dem was in der Checkbox ausgewählt wurde
     * in den KI-Modus oder in den Player-Modus
     * Ruft anschließend das Fenster des Spielfelds auf 
     * @param event
     * @throws IOException
     */
    @FXML
    private void onActionSpielSuchen(ActionEvent event) throws IOException{
    	
    	if(textfieldIP.getText().equals("")) {
    		label3.setVisible(true);
    		label3.setText("Ungültige IP-Adresse!");
    		return;
    	}
    	FXMLLoader fxmlloaderR = new FXMLLoader(getClass().getResource("RefuseToConnect.fxml"));
	    Parent rootR = fxmlloaderR.load();
	    
	    if(checkBoxKI.isSelected()) {//KI
	    				
	    				int kiModus=comboBox.getSelectionModel().getSelectedIndex();
    					FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldKI.fxml"));
    					try{
    						ControllerKI cKI = new ControllerKI(5,kiModus,false,textfieldIP.getText());
    						fxmlloader.setController(cKI);
    					} catch (ConnectException e1) {
    						Stage newStage = new Stage();
    			    		newStage.setScene(new Scene(rootR, 500, 350));
    			    		newStage.show();
    			    		return;
    					}
    			    	catch (SocketException e2) {
    						label3.setVisible(true);
    						label3.setText("Ungültige IP-Adresse!");
    			    		return;
    					}catch(UnknownHostException e3){
    						label3.setVisible(true);
    						label3.setText("Ungültige IP-Adresse!");
    			    		return;
    					}
    					
    					try {
    								
    					Parent root = fxmlloader.load();
    			
	    					Scene newScene = new Scene(root);
	    					StarterKlasse.primaryStage.setScene(newScene);
    								
    					} catch (IOException e) {
    					// TODO Auto-generated catch block
    						e.printStackTrace();
    					} 
    		
	    }else { 
	    	
	    	try {
				connection.connectAsClient(textfieldIP.getText());
			} catch (ConnectException e1) {
				Stage newStage = new Stage();
	    		newStage.setScene(new Scene(rootR, 500, 350));
	    		newStage.show();
			}catch (SocketException e2) {
				label3.setVisible(true);
				label3.setText("Ungültige IP-Adresse!");
	    		return;
			}catch(UnknownHostException e3){
				label3.setVisible(true);
				label3.setText("Ungültige IP-Adresse!");
	    		return;
			}
	    	Task<String>task = new Task<String>() {
				@Override
				protected String call() throws Exception {
					spielfeld=connection.read();
					
			    	if(spielfeld.substring(0, 1).equals("l")) {
			    		String parts[]=spielfeld.split(" ");
			    		file.load(Long.parseLong(parts[1]));
			    		Platform.runLater(new Runnable(){
							@Override
							public void run() {
								FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldClient.fxml"));
								ControllerSpielFeld csf = new ControllerSpielFeld(file.getTurn(),file.getOwnField(),file.getEnemyField(),file.getShips(),file.getDestroyedOwn(),file.getDestroyedEnemy(),file.getPullCount(),file.getSchiffCounter(),connection,false);
								fxmlloader.setController(csf);
								try {
									
									Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
									//ControllerObjekt von der nï¿½chsten Gui-Oberflï¿½che erzeugen um die SchiffsListe, den in und den Output Reader zu ï¿½bergeben
									
									Scene newScene = new Scene(root);
									StarterKlasse.primaryStage.setScene(newScene);
									StarterKlasse.primaryStage.setTitle("Schiffe versenken");
									
									
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
								ControllerSpielFeld csf = new ControllerSpielFeld(spiel.getSchiffListe(),spielfeld,spiel,Integer.parseInt(parts[1]),connection,false);
								fxmlloader.setController(csf);
								try {
									
									Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
									//ControllerObjekt von der nï¿½chsten Gui-Oberflï¿½che erzeugen um die SchiffsListe, den in und den Output Reader zu ï¿½bergeben
									
									Scene newScene = new Scene(root);
									StarterKlasse.primaryStage.setScene(newScene);
									StarterKlasse.primaryStage.setTitle("Schiffe versenken");

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
    
    /**
     * onAction-Methode des Buttons spielLaden
     * Es wird an das Fenster, in dem man Spiele laden kann weitergeleitet
     * @param event
     */
    @FXML
    private void onActionSpielLaden(ActionEvent event) {
    	FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielLaden.fxml"));
		ControllerSpielLaden csl = new ControllerSpielLaden(connection);
		fxmlloader.setController(csl);
		try {
			
			Parent root = fxmlloader.load();
			Scene newScene = new Scene(root);
			StarterKlasse.primaryStage.setScene(newScene);
			StarterKlasse.primaryStage.setTitle("Spiel laden");
						
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }

    /**
     * onAction-Methode beim Klick auf das Musiksymbol
     * Es wird das Fenster der Musikeinstellungen aufgerufen
     * @param event
     */
	public void onActionSounds(ActionEvent event) {
		Stage newStage = new Stage();
		newStage.setScene(StarterKlasse.music);
		newStage.show();
	}
}
