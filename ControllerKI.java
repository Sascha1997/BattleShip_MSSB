package application;

import java.awt.Point;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import ki.KI;
/**
 * Kontrollerklasse für das Spielfeld, wenn die KI spielt
 *
 *
 */
public class ControllerKI implements Initializable{
	
	/**
	 * FXML-Attribute
	 */
	@FXML
	private GridPane gridPaneWe;
	@FXML
	private GridPane gridPaneEnemy;
	@FXML
	private Label zuPlazieren;
	@FXML
	private Label spielZug;
	@FXML
	private Label versenktWir;
	@FXML
	private Label versenktGegner;

	/**
	 * Objekt-Attribute
	 */
	private boolean isLoaded=false;
	private boolean isClient=false;
	private int spielFeldGroeße;
	
	/**
	 * Initialize
	 * Setzen einiger GUI Komponenten und Aufruf zum Generieren des Spielfelds
	 */
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		spielZug.setText("1");
		versenktWir.setText("0");
		versenktGegner.setText("0");
		if(!isLoaded)this.spielFeldGenerieren();
		
	}
	
	
	/**
	 * Konstruktor Client
	 * @param spielFeldGroeße
	 * @param kiModus
	 * @param isOffline
	 * @param ip
	 * @throws IOException
	 */
	public ControllerKI(int spielFeldGroeße, int kiModus, boolean isOffline,String ip) throws IOException {
		this.spielFeldGroeße = spielFeldGroeße;
		KI ki;
		this.isClient = true;
		ki = new KI(new Socket(ip,50000),false,kiModus,this,isOffline);
		ki.start();
		
		
		
	}
	/**
	 * Konstruktor Server
	 * @param spielFeldGroeße
	 * @param server
	 * @param kiModus
	 * @param setUp
	 * @param isOffline
	 */
	public ControllerKI(int spielFeldGroeße,boolean server,int kiModus, String setUp,boolean isOffline) {
		this.spielFeldGroeße = spielFeldGroeße;
		KI ki;
		try {
			ki = new KI(new ServerSocket(50000).accept(),server,kiModus,this,setUp,isOffline);
			ki.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Settermethode für die Angabe wie viel Schiffe plaziert wurden
	 * @param setup
	 */
	public void setZuPlazieren(final String setup) {
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				zuPlazieren.setText(setup);
			}
			
		});
		
	}
	
	/**
	 * Markierung der Spielzellen wo ein Schiff von der KI plaziert wurde
	 * @param schiffe
	 */
	public void plaziereSchiffe(ArrayList<Schiff>schiffe) {
		
		for(int i = 0; i<schiffe.size();i++) {
			ArrayList<Point> zellorte = schiffe.get(i).getCords();
			
			for(Node n : gridPaneWe.getChildren()){
				
				Integer rowIndex = GridPane.getRowIndex(n);
				Integer colIndex = GridPane.getColumnIndex(n);
				if(rowIndex!=null&&colIndex!=null) {
					for(int j=0;j<zellorte.size();j++) {
						int rs =(zellorte.get(j).x);
						int cs = (zellorte.get(j).y);
						if(rs==rowIndex.intValue()&&cs==colIndex.intValue()){
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #000000");
						}
					}						
				}
				
				
				
			}
		}
		
	}
	/**
	 * Markierung der Zelle, wenn vorbeigeschossen wurde
	 * @param point
	 */
	public void markiereSchussVorbei(Point p) {
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				spielZug.setText(String.valueOf(Integer.parseInt(spielZug.getText())+1));
			}
			
		});
		
		for(Node n : gridPaneEnemy.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if(p.x==rowIndex.intValue()&&p.y==colIndex.intValue()) {
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #FF0000");
					break;
				}
			}
			
		}
	}
	/**
	 * Markierung der Zelle, wenn getroffen wurde
	 * @param point
	 */
	public void markiereSchussTreffer(Point p) {
		for(Node n : gridPaneEnemy.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if(p.x==rowIndex.intValue()&&p.y==colIndex.intValue()) {
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #00FF00");
					break;
				}
			}
		}
	}
	
	/**
	 * Markierung aller um das Schiff befindlichen Zellen, zur Kennzeichnung, dass dort keines mehr liegen kann
	 * @param points des versenkten Schiffes
	 */
	public void autoFill(ArrayList<Point>points) {
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String s[] = versenktGegner.getText().split(" ");
				versenktGegner.setText(String.valueOf(Integer.parseInt(s[0])+1));
			}
			
		});
		
		for(int i=0;i<points.size();i++) {
					
					for(Node n: gridPaneEnemy.getChildren()) {
						
						Integer rowIndex = GridPane.getRowIndex(n);
						Integer columnIndex = GridPane.getColumnIndex(n);
						
						
						int rowInt = points.get(i).x+1;
						int colInt = points.get(i).y;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x-1;
						colInt = points.get(i).y;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x;
						colInt = points.get(i).y;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x+1;
						colInt = points.get(i).y+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x;
						colInt = points.get(i).y+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x-1;
						colInt = points.get(i).y+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x-1;
						colInt = points.get(i).y-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {;
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x+1;
						colInt = points.get(i).y-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x;
						colInt = points.get(i).y-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setId("cell");
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						
					}
				
			
		}
		
		for(int i=0;i<points.size();i++) {
			for(Node n: gridPaneEnemy.getChildren()) {
				
					Integer rowIndex = GridPane.getRowIndex(n);
					Integer columnIndex = GridPane.getColumnIndex(n);
					
					int rowInt = points.get(i).x;
					int colInt = points.get(i).y;
					if (rowIndex != null && rowIndex.intValue() == rowInt
							  && columnIndex != null && columnIndex.intValue() == colInt) {
						Button b = (Button)n;
						b.setId("cell");
						b.setStyle("-fx-background-color: #00FF00");
					}
				
				
			}
		}
	}
	
	/**
	 * Methode fürs Laden um das gegnerische Spielfeld wieder herzustellen
	 * @param spielfeld
	 */
	public void gegnerSpielFeldWiederherstellen(int [][] spielfeld) {
		
		for(Node n : gridPaneEnemy.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==3){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #00FF00");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==2){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #00FF00");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==1){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #FF0000");
				}
					
			}
			
		}
	}
	/**
	 * Methode fürs laden um das eigene Spielfeld wieder herzustellen
	 * @param spielfeld
	 */
	public void unserSpielFeldWiederherstellen(int[][]spielfeld) {
		for(Node n : gridPaneWe.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==3){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #000000");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==2){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #8A4B08");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==1){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #0B4C5F");
				}
				
			}
			
		}
	}
	
	/**
	 * Erhöhen des Versenkt Counters 
	 */
	public void erhoeheGegnerVersenkt() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String s[] = versenktWir.getText().split(" ");
				versenktWir.setText(String.valueOf(Integer.parseInt(s[0])+1));
			}
			
		});
	}
	/**
	 * Markieren des eigenen Feldes wenn der Gegner vorbeigeschossen hat
	 * @param point
	 */
	public void markiereEigenesFeldVorbei(Point p) {
		for(Node n : gridPaneWe.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if(p.x==rowIndex.intValue()&&p.y==colIndex.intValue()) {
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #0B4C5F");
					break;
				}
			}
		}
	}
	/**
	 * Markieren des eigenen Feldes wenn der Gegner getroffen hat
	 * @param point
	 */
	public void markiereEigenesFeldTreffer(Point p) {
		for(Node n : gridPaneWe.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if(p.x==rowIndex.intValue()&&p.y==colIndex.intValue()) {
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #8A4B08");
					break;
				}
			}
		}
	}
	
	/**
	 * Erzeugen des Spielfelds der KI und des Gegners
	 * Zellen relativ zur Spielfeldgröße skalieren
	 * Gridpane mit Buttons füllen und jedem Button eine Id geben über die er dann angesprochen werden kann
	 */
	public void spielFeldGenerieren() {

		/*
		 * 
		 * UNSER SPIELFELD
		 * 
		 */
		//Nur wegen Szene Builder hier Erst Vorhandenes 1x1 LÃŸschen dann 10x10 erzeugen
		gridPaneWe.getColumnConstraints().remove(0);
		gridPaneWe.getRowConstraints().remove(0);
		
		for(int i = 0;i<this.spielFeldGroeße;i++) {
			ColumnConstraints cc = new ColumnConstraints((int)380/spielFeldGroeße);
			RowConstraints rc = new RowConstraints((int)380/spielFeldGroeße);
			
			gridPaneWe.getColumnConstraints().add(cc);
			gridPaneWe.getRowConstraints().add(rc);
		}
		
		gridPaneWe.setAlignment(Pos.CENTER);
		gridPaneWe.setGridLinesVisible(true);
		
		
		//Erzeugt 100 verschiedene Buttons ins GridPane, die alle Clickable sind und wenn man Sie anklickt Ihren Index zurÃŸck geben
		for(int i=0;i<spielFeldGroeße;i++) {
			for (int j = 0; j<spielFeldGroeße;j++) {
				
				Button b = new Button();
				b.setId("cell");
				b.setMinHeight((int)365/spielFeldGroeße);
				b.setMinWidth((int)365/spielFeldGroeße);
				gridPaneWe.add(b, i, j);
			}
		}
		/* UNSER SPIELFELD ENDE
		 * 
		 * GEGNER SPIELFELD
		 * 
		 */
		
		gridPaneEnemy.getColumnConstraints().remove(0);
		gridPaneEnemy.getRowConstraints().remove(0);
		
		for(int i = 0;i<this.spielFeldGroeße;i++) {
			ColumnConstraints cc = new ColumnConstraints((int)380/spielFeldGroeße);
			RowConstraints rc = new RowConstraints((int)380/spielFeldGroeße);
			
			gridPaneEnemy.getColumnConstraints().add(cc);
			gridPaneEnemy.getRowConstraints().add(rc);
		}
		gridPaneEnemy.setAlignment(Pos.CENTER);
		gridPaneEnemy.setGridLinesVisible(true);
		
		for(int i=0;i<spielFeldGroeße;i++) {
			for (int j = 0; j<spielFeldGroeße;j++) {
				
				Button b = new Button();
				b.setId("cell");
				b.setMinHeight((int)365/spielFeldGroeße);
				b.setMinWidth((int)365/spielFeldGroeße);
				gridPaneEnemy.add(b, i, j);
			}
		}
		
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				
			}
			
		});
		/*
		 * 
		 * 
		 * 
		 * GEGNERSPIELFELD ENDE
		 * 
		 */
	}
	
	/**
	 * Setzen der SpielfeldGröße
	 * @param Spielfeldgröße
	 */
	public void setspielFeldGroeße(String s) {
		
		String parts[]=s.split(" ");
		System.out.println("PARTS[1] "+parts[1]);
		if(!(parts[0].substring(0,1).equals("l"))) {
			this.spielFeldGroeße = Integer.parseInt(parts[1]);
		}
	}
	/**
	 * Setzen des Spielfelds beim Laden 
	 * @param groeße
	 * @param schiffe
	 * @param spielFeldGegner
	 * @param spielFeldWir
	 */
	public void setSpielFeld(int groeße, final ArrayList<Schiff>schiffe,final int[][]spielFeldGegner,final int[][]spielFeldWir) {
		
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				spielFeldGenerieren();
				plaziereSchiffe(schiffe);
	            gegnerSpielFeldWiederherstellen(spielFeldGegner);
	            unserSpielFeldWiederherstellen(spielFeldWir);
				
			}
			
		});
		
	}
	
	
	/**
	 * onAction-Methode für den Soundbutton 
	 * Öffnet das Soundfenster
	 * @param event
	 */
	public void onActionSounds(ActionEvent event) {
		Stage newStage = new Stage();
		newStage.setScene(StarterKlasse.music);
		newStage.show();
	}
	
	/**
	 * Beenden des Spiels wenn gewonnen/verloren wurde
	 * Übergabe der Parameter für die Statistik an das Statistikfenster
	 * @param shots
	 * @param hits
	 * @param fehler
	 * @param quote
	 * @param b
	 */
	public void spielBeendenKI(String shots, String hits, String fehler, String quote,boolean b) {
		
		
		
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("KIBeenden.fxml"));
				ControllerKIBeenden cKIb = new ControllerKIBeenden(shots,hits,fehler,quote,b);
		    	
		    	try {
		    		fxmlloader.setController(cKIb);
		    		Parent root = fxmlloader.load();
		    		Stage newStage = new Stage();
		    		newStage.setScene(new Scene(root));
		    		newStage.show();
		    			
		    	} catch (IOException e) {
		    		// TODO Auto-generated catch block
		    		e.printStackTrace();
		    	}
			}
			
		});
		
	}
	/**
	 * Öffnet das Abbruchfenster für die KI, falls der Gegner einfach mitten im Spiel das Spiel verlässt
	 */
	public void spielAbbruch() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielAbbruch.fxml"));
				ControllerKIAbbruch cKIa = new ControllerKIAbbruch();
		    	
		    	try {
		    		fxmlloader.setController(cKIa);
		    		Parent root = fxmlloader.load();
		    		Stage newStage = new Stage();
		    		newStage.setScene(new Scene(root));
		    		newStage.show();
		    			
		    	} catch (IOException e) {
		    		// TODO Auto-generated catch block
		    		e.printStackTrace();
		    	}
			}
			
		});
	}
	
	/**
	 * Getter und Setter für die Variable Loaded die an verschiedenen Stellen benötigt wird um die richten Methoden aufzurufen
	 */
	public boolean getLoaded() {
		return this.isLoaded;
	}
	
	public void setIsLoaded(boolean b) {
		this.isLoaded = b;
	}
}
