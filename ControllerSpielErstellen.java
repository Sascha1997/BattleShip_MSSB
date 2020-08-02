package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;

/**
 * Kontrollerklasse für das Fenster Spiel erstellen
 * @author Matze
 *
 */
public class ControllerSpielErstellen implements Initializable {
	
	/**
	 * FXML-Attribute
	 */
		
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
	
	/**
	 * Objekt-Attribute
	 */
	private Connection connection;
	private ToggleGroup group = new ToggleGroup();
	private int kiModus;
	private SpielHelfer helfer = new SpielHelfer();
	private SchiffeVersenken spiel;
	
	/**
	 * Initialize
	 * Setzt die entsprechenden GUI-Komponenten und versetzt die Radiobuttons mit einem Eventhandler
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		radioButtonWeVsEnemy.setToggleGroup(group);
		radioButtonOurAI.setToggleGroup(group);
		radioButtonOfflineModus.setToggleGroup(group);
		ObservableList<String>options=FXCollections.observableArrayList("Leicht","Mittel","Schwer");
		comboBox.setItems(options);
		comboBox.getSelectionModel().selectFirst();
		comboBox.setVisible(false);

		radioButtonOfflineModus.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				comboBox.setVisible(true);
				
			}
		});
		radioButtonOurAI.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				comboBox.setVisible(true);
				
			}
		});
		radioButtonWeVsEnemy.setOnAction(new EventHandler<ActionEvent>() {
	
			@Override
			public void handle(ActionEvent arg0) {
				comboBox.setVisible(false);
		
			}
		});
		
		
	}
	/**
	 * Konstruktor 
	 * Neues Spielobjekt wird erzeugt
	 * @param connection
	 */
	public ControllerSpielErstellen(Connection connection) {
		this.connection=connection;
		spiel = new SchiffeVersenken(connection);
		
	}
	/**
	 * onAction-Methode für den Button spielErstellen
	 * Prüft, ob ein Spielmodus gewählt wurde und ob eine gültige Eingabe ins Textfeld gesetzt wurde
	 * Verzweigt dann je nach ausgewähltem Modus und erstellt das Spiel
	 * Sobald connected wurde, wird das Spielfeldfenster aufgehen
	 * @param event
	 * @throws IOException
	 */
	public void erstelleSpiel(ActionEvent event) throws IOException {
		
		
		if(!radioButtonOfflineModus.isSelected()&&!radioButtonOurAI.isSelected()&&!radioButtonWeVsEnemy.isSelected()) {
			Notifications.create()
					.owner(StarterKlasse.primaryStage)
					.title("Zuerst Modus wählen")
					.hideAfter(Duration.seconds(1.5))
					.position(Pos.TOP_CENTER)
					.showWarning();
    		System.out.println("Wähle zuerst einen Spielmodus");
		}else if (!(textfield1.getText().matches("[0-9]+"))){
			Notifications.create()
					.owner(StarterKlasse.primaryStage)
					.title("Es sind nur Zahlen erlaubt")
					.hideAfter(Duration.seconds(1.5))
					.position(Pos.TOP_CENTER)
					.showWarning();
			System.out.println("Nur Zahlen Erlaubt");
			return;
		}else if(Integer.parseInt(textfield1.getText())>70||
        				Integer.parseInt(textfield1.getText())<5){
			Notifications.create()
					.owner(StarterKlasse.primaryStage)
					.title("Spielfeldgröße außerhalb des erlaubten Bereichs")
					.hideAfter(Duration.seconds(1.5))
					.position(Pos.TOP_CENTER)
					.showWarning();
			System.out.println("Spielfeldgröße außerhalb des erlaubten Bereichs");
			return;
		}else {
			
			final String parameter = textfield1.getText();
			buttonZurueck.setDisable(true);
    		spielErstellen.setText("Warte auf Gegner...");
    		spielErstellen.setDisable(true);
    		final String parameterFertig = "setup "+parameter+" "+helfer.kalkuliereSchiffe(Integer.valueOf(parameter));
    		System.out.println(parameterFertig);
    		
			if(radioButtonWeVsEnemy.isSelected()) {
        		
        		Task<String>task = new Task<String>() {

        			@Override
        			protected String call() throws Exception {
        				connection.createConnection();
        		    	Platform.runLater(new Runnable(){

        					@Override
        					public void run() {
        						
        						connection.write(parameterFertig);
        						spiel.spielEinrichten(parameterFertig);
        						
        						FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldServer.fxml"));
        						ControllerSpielFeld csf = new ControllerSpielFeld(spiel.getSchiffListe(),parameterFertig,spiel,Integer.parseInt(parameter),connection,false);
        						fxmlloader.setController(csf);
        						
        						try {
        	    		    		
        							Parent root = fxmlloader.load();
        							
        				    		Scene newScene = new Scene(root);
        							StarterKlasse.primaryStage.setScene(newScene);
        							StarterKlasse.primaryStage.setTitle("Battleship");
        							        							
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
        		
        	}else if(radioButtonOurAI.isSelected()) {
        		kiModus=comboBox.getSelectionModel().getSelectedIndex();
        			
        		Task<String>task = new Task<String>() {
        			
					protected String call() throws Exception {
						final ControllerKI cKI = new ControllerKI(Integer.parseInt(parameter),true,kiModus,parameterFertig,false);
						Platform.runLater(new Runnable() {
							
							public void run() {
								try {
					        		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldKI.fxml"));
					        		
					        		fxmlloader.setController(cKI);
					        		Parent root = fxmlloader.load();
					        							
					        		Scene newScene = new Scene(root);
					        		StarterKlasse.primaryStage.setScene(newScene);
					        		StarterKlasse.primaryStage.setTitle("Battleship");
				        							
				        		} catch (IOException e) {
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
        						
        	}else if(radioButtonOfflineModus.isSelected()) {
        		kiModus=comboBox.getSelectionModel().getSelectedIndex();
    			
        		Task<String>task = new Task<String>() {
        			
					protected String call() throws Exception {
						new ControllerKI(Integer.parseInt(parameter),kiModus,true,"localhost");
						
						return null;
					}
        			
        		};
        		
        		Thread th = new Thread(task);
        		th.setDaemon(true);
        		th.start();
        		
        		Task<String>task2 = new Task<String>() {

        			@Override
        			protected String call() throws Exception {
        				connection.createConnection();
        		    	Platform.runLater(new Runnable(){

        					@Override
        					public void run() {
        						
        						connection.write(parameterFertig);
        						spiel.spielEinrichten(parameterFertig);
        						
        						FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldServer.fxml"));
        						ControllerSpielFeld csf = new ControllerSpielFeld(spiel.getSchiffListe(),parameterFertig,spiel,Integer.parseInt(parameter),connection, true);
        						fxmlloader.setController(csf);
        						
        						
        						try {
        	    		    		
        							Parent root = fxmlloader.load();
        							
        				    		Scene newScene = new Scene(root);
        							StarterKlasse.primaryStage.setScene(newScene);
        							StarterKlasse.primaryStage.setTitle("Battleship");
        						} catch (IOException e) {
        							e.printStackTrace();
        						}
        						
        					}

        					
        				});
        		    	
        		    	
        				return null;
        			}
        			
        		};
        		
        		Thread th2 = new Thread(task2);
        		th2.setDaemon(true);
        		th2.start();
        	}

        			
    	}
		
	}
	
	/**
	 * onAction-Methode für den Button zurueckZumMenue
	 * Connection wird geschlossen und das Fenster es wird zurück aufs Hauptmenü gesetzt
	 */
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
			StarterKlasse.primaryStage.setTitle("Hauptmenü");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
		

}
