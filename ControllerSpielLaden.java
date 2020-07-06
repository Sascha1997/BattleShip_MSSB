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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ControllerSpielLaden implements Initializable {

	@FXML
	private Button buttonZurueckZumHauptmenue;
	
	private int[][]unserSpielFeld;
	private int[][]gegnerSpielFeld;
	private boolean b;
	private ArrayList<Schiff>schiffListe;
	private Connection connection;
	private Files file;
	
	@FXML
	private Button buttonSpielLaden;

	private int counterVersenktGegner;
	private int counterVersenktWir;
	private int counterZug;
	private int schiffCounter;
	private VBox container;
	
	@FXML
	private ScrollPane scrollPane;
	
	private Long loadID;
	
	public ControllerSpielLaden(Connection connection) {
		this.connection=connection;
		this.file=new Files();
	}
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		buttonSpielLaden.setDisable(true);
		container = new VBox();
		File folder = new File(System.getProperty("user.dir") + "\\saves\\player");
		for(final File fileEntry : folder.listFiles()) {
			if(fileEntry.isFile()) {
				final Button b = new Button(fileEntry.getName());
				b.setOnMouseClicked(new EventHandler <MouseEvent>() {

					public void handle(MouseEvent arg0) {
						
						buttonSpielLaden.setDisable(false);
						String parts[]=b.getText().split("\\.");
						loadID = Long.parseLong(parts[0]);
						System.out.println(loadID);
					}
					
				});
				
				container.getChildren().add(b);
				
			}
			
		}
		scrollPane.setPannable(true);
		scrollPane.setContent(container);
		
	}
	
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
		
		Task<String>task = new Task<String>() {

			
			protected String call() throws Exception {
				
				connection.createConnection();
				
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						
						connection.write("load "+loadID);
						
						
						FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielFeldServer.fxml"));
						ControllerSpielFeld csf = new ControllerSpielFeld(b,unserSpielFeld,gegnerSpielFeld,schiffListe,counterVersenktWir,counterVersenktGegner,counterZug,schiffCounter,connection);
						fxmlloader.setController(csf);
						try {
							
							Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
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
		
	
		
	}
	
	
	@FXML
	private void zurueckZumHauptmenue() {
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("Sample.fxml"));
		ControllerStartMenue csm = new ControllerStartMenue();
		fxmlloader.setController(csm);
		try {
			
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der nächsten Gui-Oberfläche erzeugen um die SchiffsListe, den in und den Output Reader zu übergeben
			
			//Alte Stage die Verlassen wird
			Scene scene = new Scene(root);
			StarterKlasse.primaryStage.setScene(scene);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
