package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
/**
 * Kontrollerklasse des Fensters spiel laden Fensters
 * 
 */
public class ControllerSpielLaden implements Initializable {

	/**
	 * FXML-Attribute
	 */
	@FXML
	private Button buttonZurueckZumHauptmenue;
	@FXML
	private Button buttonSpielLaden;
	@FXML
	private RadioButton radioButtonKI;
	@FXML
	private RadioButton radioButtonSpieler;
	@FXML
	private ScrollPane scrollPane;
	private ToggleGroup group = new ToggleGroup();
	
	/**
	 * Objekt-Attribte, bei denen die meisten über das Laden gesetzt werden
	 */
	private int counterVersenktGegner;
	private int counterVersenktWir;
	private int counterZug;
	private int schiffCounter;
	private VBox container;
	private boolean isOffline;
	private int[][]unserSpielFeld;
	private int[][]gegnerSpielFeld;
	private boolean b;
	private ArrayList<Schiff>schiffListe;
	private Connection connection;
	private Files file;
	private Long loadID;
	
	/**
	 * Konstruktor
	 * @param connection
	 */
	public ControllerSpielLaden(Connection connection) {
		this.connection=connection;
		this.file=new Files();
	}
	
	/**
	 * Initialize
	 * Setzen der GUI-Komponenten
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		buttonSpielLaden.setDisable(true);
		radioButtonKI.setToggleGroup(group);
		radioButtonSpieler.setToggleGroup(group);
	}
	
	/**
	 * onAction-Methode der Radiobuttons
	 * Anzeigen des ausgewählten Ordners in dem die gespeicherten Dateien sind, je nach Auswahl vom Radiobutton
	 */
	@FXML
	private void showFolder() {
		
		container = new VBox();
		File folder;
		if(radioButtonKI.isSelected()) {
			 folder= new File(System.getProperty("user.dir") + File.separator + "saves" + File.separator + "ki");
		}else {
			folder= new File(System.getProperty("user.dir") + File.separator + "saves" + File.separator + "player");
		}
		for(final File fileEntry : folder.listFiles()) {
			if(fileEntry.isFile()) {
				String s = fileEntry.getName().substring(0, fileEntry.getName().length()-4);
				file.prepareData(s);
				
				final Button b = new Button(file.getName() + " ID: " + file.getID());
				b.setId("ladenButton");
				b.setOnMouseClicked(new EventHandler <MouseEvent>() {

					public void handle(MouseEvent arg0) {
						
						buttonSpielLaden.setDisable(false);
						System.out.println(b.getText());
						loadID = Long.parseLong(file.getID());
						System.out.println(loadID);
					}
					
				});
				
				container.getChildren().add(b);
				
			}
			
		}
		scrollPane.setPannable(true);
		scrollPane.setContent(container);
	}
	/**
	 * onAction-Methode des Buttons spiel Laden
	 * Es werden von der geladenen File alle nötigen Informationen geholt eine Connection erstellt und 
	 * an den Konstruktor der Kontrollerklasse für das Spiel alle Informationen übergeben
	 * Je nach Auswahl des Radiobuttons für KI oder Player, wird entsprechend verzweigt
	 */
	@FXML
	private void spielLaden() {
		
		
		buttonSpielLaden.setText("Warte auf Gegner...");
		buttonSpielLaden.setDisable(true);
		
		file.load(loadID);
		unserSpielFeld=file.getOwnField();
		gegnerSpielFeld=file.getEnemyField();
		b=file.getTurn();
		schiffListe=file.getShips();
		counterVersenktWir = file.getDestroyedOwn();
		counterVersenktGegner = file.getDestroyedEnemy();
		counterZug=file.getPullCount();
		schiffCounter=file.getSchiffCounter();
		isOffline=file.getIsOffline();
		if(radioButtonSpieler.isSelected()) {
			Task<String>task = new Task<String>() {

				
				protected String call() throws Exception {
					
					
					connection.createConnection();
					
					
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							
							connection.write("load "+loadID);
							
							
							FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldServer.fxml"));
							ControllerSpielFeld csf = new ControllerSpielFeld(b,unserSpielFeld,gegnerSpielFeld,schiffListe,counterVersenktWir,counterVersenktGegner,counterZug,schiffCounter,connection,isOffline);
							fxmlloader.setController(csf);
							try {
								
								Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
								Scene newScene = new Scene(root);
								StarterKlasse.primaryStage.setScene(newScene);
								StarterKlasse.primaryStage.setTitle("Schiffe versenken");
								
								
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
			
			if(isOffline) {
				try {
					new ControllerKI(5,2,true,"localhost");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else if(radioButtonKI.isSelected()) {
			file.load(loadID);
			unserSpielFeld=file.getOwnField();
			
			Task<String>task = new Task<String>() {

				
				protected String call() throws Exception {
					
					final ControllerKI cKI = new ControllerKI(unserSpielFeld.length,true,2,"load "+String.valueOf(loadID),false);
					
					Platform.runLater(new Runnable() {

						@Override
						public void run() {

	    					FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldKI.fxml"));
	    					fxmlloader.setController(cKI);
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
							
					return null;
				}
			};
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		
		
	
		
	}
	
	/**
	 * onAction-Methode für den Button zurückZumHauptmenue
	 * Leitet an das Hauptmenüfenster weiter
	 */
	@FXML
	private void zurueckZumHauptmenue() {
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("Sample.fxml"));
		ControllerStartMenue csm = new ControllerStartMenue();
		fxmlloader.setController(csm);
		try {
			
			Parent root = fxmlloader.load();
			Scene scene = new Scene(root);
			StarterKlasse.primaryStage.setScene(scene);
			StarterKlasse.primaryStage.setTitle("Hauptmenü");
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
