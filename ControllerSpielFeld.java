package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControllerSpielFeld implements Initializable{
	
	private ArrayList<Schiff>schiffListe=new ArrayList<Schiff>();
	@FXML
	private Button testButton;
	@FXML
	private Button ready;
	@FXML
	private GridPane gridPaneWe;
	@FXML
	private GridPane gridPaneEnemy;
	@FXML
	private Label zuPlazieren;
	@FXML
	private Button spielStarten;
	@FXML
	private Label zug;
	@FXML
	private Button fire;
	@FXML
	private Label spielZug;
	@FXML
	private Label versenktWir;
	@FXML
	private Label versenktGegner;
	@FXML
	private Button save;
	@FXML
	private Button buttonAufgeben;
	@FXML 
	private Button buttonUndo;
	
	private int spielZugCounter;
	
	private int versenktWirCounter;
	
	private int versenktGegnerCounter;
	
	private Button tempButton;

	private SchiffeVersenken spiel;
	private SpielHelfer spielHelfer;
	
	private String zellorte="";
	private String schiffVerteilung;

	private String colIndex;
    private String rowIndex;
    private String derZug;
    private String shot;
    
    private int [][]unserSpielfeld;
    private int [][]gegnerSpielfeld;
    private int [][]unserSpielfeldLaden;
    private int [][]gegnerSpielfeldLaden;
    
    private String[] getroffeneZellen=new String[10];
    
    private int schiffCounter;
    private int spielFeldGroeße;
    
    private Connection connection;
    
    //Gibt an ob Spiel erstellt oder geladen wurde
    private boolean isLoaded;
    private boolean isOffline;
    private boolean wirSindDran;
    private boolean spielGestartet = false;
    
    private Files file;
    
    private String schiffMenge;
    private ArrayList<String> allePlazierungen = new ArrayList<String>();

    //Konstruktor Spiel erstellen
    public ControllerSpielFeld(ArrayList<Schiff> schiffListe, String schiffeVerteilung, SchiffeVersenken spiel,int spielFeldGroeße, Connection connection, boolean isOffline) {
    	this.spielHelfer = new SpielHelfer();
    	this.connection=connection;
    	this.schiffVerteilung=schiffeVerteilung;
    	this.spiel= spiel;
    	this.schiffListe=schiffListe;
    	this.schiffCounter = schiffListe.size();
    	this.isLoaded=false;
    	this.isOffline=isOffline;
    	if(StarterKlasse.server) {
    		wirSindDran=true;
    	}
    	this.file=new Files();
    	this.spielFeldGroeße=spielFeldGroeße;

    	
    }
    //Konstruktor Spiel laden
    public ControllerSpielFeld(boolean wirSindDran,int[][]unserSpielfeld,int[][]gegnerSpielfeld,ArrayList<Schiff> schiffListe, int versenktWirCounter, int versenktGegnerCounter,int spielZugCounter,int schiffCounter, Connection connection, boolean isOffline) {
    	this.spielHelfer = new SpielHelfer();
    	this.schiffListe=schiffListe;
    	this.schiffCounter=schiffCounter;
    	this.connection=connection;
    	this.spiel = new SchiffeVersenken(connection);
    	this.isLoaded=true;
    	this.isOffline=isOffline;
    	this.unserSpielfeldLaden=unserSpielfeld;
    	this.gegnerSpielfeldLaden = gegnerSpielfeld;
    	this.wirSindDran = wirSindDran;
    	this.file=new Files();
    	this.versenktWirCounter=versenktWirCounter;
    	this.versenktGegnerCounter=versenktGegnerCounter;
    	this.spielZugCounter=spielZugCounter;
    	this.spielFeldGroeße = unserSpielfeld.length;
    }

	public void initialize(URL arg0, ResourceBundle arg1) {
		spielStarten.setDisable(true);
		buttonUndo.setVisible(false);
		ready.setDisable(true);
		unserSpielfeld = new int[this.spielFeldGroeße][this.spielFeldGroeße];
		gegnerSpielfeld = new int[this.spielFeldGroeße][this.spielFeldGroeße];
		if(this.isLoaded) {
			spielAufbauen(unserSpielfeldLaden,unserSpielfeldLaden,versenktWirCounter,versenktGegnerCounter,spielZugCounter);
			versenktWir.setText(String.valueOf(this.versenktWirCounter));
	    	versenktGegner.setText(String.valueOf(this.versenktGegnerCounter));
	    	spielZug.setText(String.valueOf(this.spielZugCounter));
	    	if(this.wirSindDran) {
	    		zug.setText("Wir sind am Zug");
	    	}else {
	    		zug.setText("Gegner ist am Zug");
	    	}
		}else {
			this.spielGenerieren();
		}
		
		
	}

	public void spielGenerieren() {
		
		String []parts=this.schiffVerteilung.split(" ");
		zuPlazieren.setText(parts[2]+"      "+parts[3]+"      "+parts[4]+"      "+parts[5]);
		schiffMenge=parts[2]+"      "+parts[3]+"      "+parts[4]+"      "+parts[5];
		this.spielFeldGenerieren();
		
	}

	@FXML
	private void setZellorte() {
		//b[0] gibt an ob die Plazierung geklappt hat und b[1] ob alle Schiffe plaziert wurden.
		
		
		boolean[] b = spielHelfer.setZellOrte(schiffListe, zellorte);
		if(b[0]) {//0001 0002 0003
			allePlazierungen.add(zellorte);
			String parts[]=zellorte.split(" ");
			for(int i=0;i<parts.length;i++) {
				unserSpielfeld[Integer.parseInt(parts[i].substring(0, 2))][Integer.parseInt(parts[i].substring(2, 4))]=3;//Fßr Speichern unser Spielfeld markieren wo Plazierung ist
			
			}
			String[] parts2 = zuPlazieren.getText().split("      ");
			switch (parts.length) {
				case 2:
					parts2[3]=String.valueOf(Integer.parseInt(parts2[3])-1);
					break;
				case 3:
					parts2[2]=String.valueOf(Integer.parseInt(parts2[2])-1);
					break;
				case 4:
					parts2[1]=String.valueOf(Integer.parseInt(parts2[1])-1);
					break;
				case 5:
					parts2[0]=String.valueOf(Integer.parseInt(parts2[0])-1);
					break;
			}
			zuPlazieren.setText(parts2[0]+"      "+parts2[1]+"      "+parts2[2]+"      "+parts2[3]);
			this.makeButtonsUnclickable(zellorte);
			zellorte="";
		}
		if(b[1]) {
			spielStarten.setDisable(false);	
			zellorte="";
			this.disableOurField(true);
		}
		
		ready.setDisable(true);


		Notifications.create()
				.owner(StarterKlasse.primaryStage)
				.title("Schiff platziert")
				.text("Schiff wurde erfolgreich plaziert")
				.graphic(null)
				.hideAfter(Duration.seconds(1.5))
				.position(Pos.TOP_CENTER)
				.show();
		
		buttonUndo.setVisible(true);
		

	}

	public void spielStarten() throws IOException, InterruptedException {
		
		buttonUndo.setVisible(false);
		if(!StarterKlasse.server) {
			
			Task<String>task = new Task<String>() {

				@Override
				protected String call() throws Exception {
					
					
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							Notifications.create()
							.owner(StarterKlasse.primaryStage)
							.title("Spiel gestartet")
							.graphic(null)
							.hideAfter(Duration.seconds(1.5))
							.position(Pos.TOP_CENTER)
							.showInformation();
							
						}
						
					});
					connection.write("confirmed");
					System.out.println("Spiel Beginnt");
					
					spielGestartet=true;
					fire.setDisable(true);
					warteAufGegner();
					return null;
				}
				
			};
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
			
			zuPlazieren.setText(schiffMenge);
		}else {
			//Task im extra Thread, damit das nicht im FX thread gemacht wird und die GUI einfriert
			Task<String>task = new Task<String>() {

				@Override
				protected String call() throws Exception {
					
					String temp = connection.read();
					
					if(temp==null) {
						Platform.runLater(new Runnable(){

							@Override
							public void run() {
								spiel.spielAbbruch();
							}	
						});
					}
					
					if(temp.equals("confirmed")) {
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								Notifications.create()
								.owner(StarterKlasse.primaryStage)
								.title("Spiel gestartet")
								.graphic(null)
								.hideAfter(Duration.seconds(1.5))
								.position(Pos.TOP_CENTER)
								.showInformation();
								
								
							}
							
						});
						System.out.println("Spiel Beginnt");
						spielGestartet=true;
						
					}
					
					
					return null;
				}
				
			};
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		
		//SpielInformationen Initialisieren, muss im FX Thread gemacht werden
		if(StarterKlasse.server) {
			zug.setText("Wir sind dran");
		}else {
			zug.setText("Gegner ist dran");
		}
		zuPlazieren.setText(schiffMenge);
		versenktWirCounter = 0;
		versenktGegnerCounter = 0;
		spielZugCounter = 1;
		spielZug.setText("1");
		versenktWir.setText("0");
		versenktGegner.setText("0");
		spielStarten.setDisable(true);
		
		
		
	}
	
	//Zwischen Schuss und Warte auf Gegner wird hin und her gesprungen
	@FXML
	private void schuss() throws IOException, IllegalStateException {

		if(!spielGestartet) {
			Notifications.create()
					.owner(StarterKlasse.primaryStage)
					.title("Vorsicht!")
					.text("Das Spiel muss erst Starten, bevor ein Schuss abgefeuert werden kann")
					.graphic(null)
					.hideAfter(Duration.seconds(1.5))
					.position(Pos.TOP_CENTER)
					.show();
			System.out.println("Das Spiel muss erst Starten, bevor ein Schuss abgefeuert werden kann");
			return;
		}

		//Schusssound
		if(SoundsController.effekte) {
			final Task sound = new Task() {

				@Override
				protected Object call() throws Exception {
					AudioClip audio = new AudioClip(getClass().getResource("/assets/schussSound.wav").toExternalForm());
					audio.setVolume(SoundsController.getVolume());
					audio.play();
					return null;
				}
			};

			Thread thread = new Thread(sound);
			thread.start();
		}

		if(rowIndex==null||rowIndex.equals("")) {
			Notifications.create()
					.owner(StarterKlasse.primaryStage)
					.title("Vorsicht!")
					.text("Wähle eine Zelle aus, auf die du schießen möchtest")
					.graphic(null)
					.hideAfter(Duration.seconds(1.5))
					.position(Pos.TOP_CENTER)
					.show();
			System.out.println("Wßhle eine Zelle aus, auf die du schießen mßchtest");
			return;
		}
		//Gegner den Schuss mitteilen
		shot = this.rowIndex+" "+this.colIndex;
		connection.write("shot "+shot);
		
		//Button erstmal deaktivieren
		fire.setDisable(true);
		
		//Task der im Extra Thread gestartet wird, in dem auf die Antwort vom Gegner gewartet wird. Beugt einfrieren der Oberflßche vor
		Task<String>task = new Task<String>() {

			@Override
			protected String call() throws Exception {
				
				String answer = connection.read();
				if(answer==null) {
					Platform.runLater(new Runnable(){

						@Override
						public void run() {
							spiel.spielAbbruch();
						}	
					});
				}
				//Answer 1 Makiere das Feld vom Gegner mit Grßn und setze den Button wieder auf Enable fßr nochmal schießen
				if(answer.equals("answer 1")) {
					
					gegnerSpielfeld[Integer.parseInt(rowIndex)][Integer.parseInt(colIndex)]=2;
					//Markieren von getroffenen Gegner Zellen
					for(int i=0;i<getroffeneZellen.length;i++) {
						if(getroffeneZellen[i]==null) {
							getroffeneZellen[i]=rowIndex+colIndex;		
							break;
						}	
					}
					
					tempButton.setId("cellGetroffen");
					fire.setDisable(false);
				//Answer 2 ist gleich wie Answer 1 nur dass hier Versenkt wird, die entsprechende Aktuallsierte info wird dann runLater() im FX Thread ergßnzt
				}else if(answer.equals("answer 2")){
					schiffCounter--; //Zßhler fßr Gegnerschiffe versenkt, zum erkennen wann Spiel vorbei ist
					
					gegnerSpielfeld[Integer.parseInt(rowIndex)][Integer.parseInt(colIndex)]=2;
					for(int i=0;i<getroffeneZellen.length;i++) {
						if(getroffeneZellen[i]==null) {
							getroffeneZellen[i]=rowIndex+colIndex;	
							break;
						}	
					}

					//Versenktsound
					if(SoundsController.effekte) {
						final Task sound = new Task() {

							@Override
							protected Object call() throws Exception {
								AudioClip audio = new AudioClip(getClass().getResource("/assets/versenktSound.wav").toExternalForm());
								audio.setVolume(SoundsController.getVolume());
								audio.play();
								return null;
							}
						};

						Thread thread = new Thread(sound);
						thread.start();
					}
					
					System.out.println("Versenkt");
					tempButton.setId("cellGetroffen");
					fire.setDisable(false);
					Platform.runLater(new Runnable(){

						@Override
						public void run() {
							versenktGegnerCounter++;
							versenktGegner.setText(String.valueOf(versenktGegnerCounter));
							checkAutofill(shot);
							
							//Wenn letztes Schiff versenkt wurde befindet sich der Counter bei 0 und das Spiel beendet sich
							if(schiffCounter==0) {
								spiel.spielBeenden(true,(Stage)zug.getScene().getWindow());
							}
						}	
					});
					//Answer 0 wir schreiben PASS an den Gegner, sodass der schießen darf. Feld wird rot markiert, Zugcounter um 1 erhßht
					//Danach gehen wir in die Methode warteAufGegner(); 
					
				}else if(answer.equals("answer 0")) {

					//Verfehltound
					if(SoundsController.effekte) {
						final Task sound = new Task() {

							@Override
							protected Object call() throws Exception {
								AudioClip audio = new AudioClip(getClass().getResource("/assets/danebenSound.wav").toExternalForm());
								audio.setVolume(SoundsController.getVolume());
								audio.play();
								return null;
							}
						};

						Thread thread = new Thread(sound);
						thread.start();
					}

					save.setDisable(true);
					gegnerSpielfeld[Integer.parseInt(rowIndex)][Integer.parseInt(colIndex)]=1;
					tempButton.setId("cellVerfehlt");
					connection.write("pass");
					spielZugCounter++;
					Platform.runLater(new Runnable(){

						@Override
						public void run() {
							spielZug.setText(String.valueOf(spielZugCounter));
							zug.setText("Gegner ist Dran");
							
						}
						
					});
					warteAufGegner();
					
				}
				rowIndex="";
				colIndex="";
				
				return null;
			}
			
		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
				
		
		
	}
	
	private void warteAufGegner() throws IOException, InterruptedException {
		
		//Hier wird wieder im Task im extra Thread auf dem Gegner sein Spielzug gewartet, dass uns nicht die Gui einfriert
		Task<String>task = new Task<String>() {
			
			@Override
			protected String call() throws Exception {
					
					//Fire Button disablen
					int answer=0;
					fire.setDisable(true);
					wirSindDran = false;
					//Solange wir auf den Gegner warten wird die Schleife durchlaufen
					//Schuss vom Gegner wird gelesee und ins richtige Format gebracht
					//Danach wird der RateVersuch geprßft und in die entsprechende case verzweigt
					
					while(!wirSindDran&&schiffListe.size()>0) {
						derZug=connection.read();
						System.out.println("DerZug "+derZug);
						
						if(derZug!=null&&derZug.equals("")) {
							derZug=connection.read();//ßbergangslßsung, nach laden kommt irgendwie immer ein Leer String beim lesen
						}
						
						if(derZug==null) {
							Platform.runLater(new Runnable(){

								@Override
								public void run() {
									spiel.spielAbbruch();
								}	
							});
						}
						
						
						
						String[]parts=derZug.split(" ");
						//Format auf zweistellige Zahl anpassen, falls dies nicht vom Gegner gemacht wird 05 06 z.B.
						if(parts[1].length()==1) {
							parts[1]="0"+parts[1];
						}
						if(parts.length>2&&parts[2].length()==1) {
							parts[2]="0"+parts[2];
						}
						
						if(parts.length>2) {
							markiereGegnerSchuss(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
							answer=spielHelfer.pruefeRateVersuch(parts[1]+" "+parts[2],schiffListe);
							
						}else {
							answer = spielHelfer.pruefeRateVersuch(parts[0],schiffListe);
							
						}
						System.out.println(parts[1]+" ist parts[1]");
						System.out.println(answer + " ist answer");
						
						switch (answer) {
						
						//Case 0 der Gegner schießt vorbei, wir mßssen nur noch auf sein PASS warten und dann sind wir wieder dran
						// Schleife wird dann verlassen
						case 0:
							connection.write("answer 0");
							unserSpielfeld[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])]=1;
							derZug=connection.read();
							if(derZug.equals("pass")) {
								wirSindDran = true;
								Platform.runLater(new Runnable(){

									@Override
									public void run() {
										zug.setText("Wir sind Dran");
										fire.setDisable(false);
										save.setDisable(false);
									}	
								});
								
							}
							
							break;
						//Case 1 Gegner hat getroffen, schleife wieder von vorne anfangen
						case 1:
							
							unserSpielfeld[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])]=2;
							connection.write("answer 1");
							break;
						//Case 2 Gegner hat getroffen, Aktuallisierte ausgabe am Bildschirm von Versenkt und schleife wieder von vorne	
						case 2:
							connection.write("answer 2");
							unserSpielfeld[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])]=2;
							Platform.runLater(new Runnable(){

								@Override
								public void run() {
									
									versenktWirCounter++;
									versenktWir.setText(String.valueOf(versenktWirCounter));
									
									if(schiffListe.size()==0) {
										spiel.spielBeenden(false,(Stage)zug.getScene().getWindow());
										
									}
								}
								
							});
							break;
						case 3:
							System.out.println("Speicher anfrage bekommen");
							
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									speicherAnfrage(parts[1]);									
								}
								
							});
							break;
						}
						
					}
				return null;
			}
			
			
		};
		
		
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		
		
		
		
	}
	//Prßft fßr jedes Schiff in der Schiffsliste, ob der Schuss vom Gegner ein Treffer war oder nicht. Gibt entsprechende Info zurßck
	//Removed dann gleich ein versenktes Schiff

	//Wird bei Versenkt aufgerufen und macht einen Autofill wo keine Schiffe mehr sein kßnnen
	private void checkAutofill(String lastShot) {
		
		String[]aussortierteZellen=spielHelfer.adjust(lastShot, getroffeneZellen);
		for(int i=0;i<aussortierteZellen.length;i++) {
			for(int j=0;j<getroffeneZellen.length;j++) {
				if(aussortierteZellen[i]!=null&&getroffeneZellen[j]!=null) {
					if(getroffeneZellen[j].equals(aussortierteZellen[i])){
						getroffeneZellen[j]=null;
					}
				}
			}
		}
		
		for(int i=0;i<getroffeneZellen.length;i++) {
			if(getroffeneZellen[i]!=null) { 
					
					for(Node n: gridPaneEnemy.getChildren()) {
						
						Integer rowIndex = GridPane.getRowIndex(n);
						Integer columnIndex = GridPane.getColumnIndex(n);
						
						
						int rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2))+1;
						int colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4));
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;

							b.setId("cellVerfehlt");
						}
						
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2))-1;
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4));
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2));
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4));
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2))+1;
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4))+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2));
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4))+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2))-1;
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4))+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2))-1;
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4))-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2))+1;
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4))-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						
						rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2));
						colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4))-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							
							gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=1;
							Button b = (Button)n;
							b.setId("cellVerfehlt");
						}
						
						
					}
					
					
				
			}
			
		}
		
		for(int i=0;i<getroffeneZellen.length;i++) {
			for(Node n: gridPaneEnemy.getChildren()) {
				
				if(getroffeneZellen[i]!=null) {
					Integer rowIndex = GridPane.getRowIndex(n);
					Integer columnIndex = GridPane.getColumnIndex(n);
					
					int rowInt = Integer.parseInt(getroffeneZellen[i].substring(0, 2));
					int colInt = Integer.parseInt(getroffeneZellen[i].substring(2, 4));
					if (rowIndex != null && rowIndex.intValue() == rowInt
							  && columnIndex != null && columnIndex.intValue() == colInt) {
						
						gegnerSpielfeld[rowIndex.intValue()][columnIndex.intValue()]=2;
						Button b = (Button)n;
						b.setId("cellGetroffen");
					}
				}
				
			}
		}
		
		for(int i =0;i<getroffeneZellen.length;i++) {
			getroffeneZellen[i]=null;
		}
		for(int i=0;i<aussortierteZellen.length;i++) {
			if(aussortierteZellen[i]!=null) {
				getroffeneZellen[i]=aussortierteZellen[i];
			}
		}
	}
	
	
	@FXML
	private void saveGameProcess() {
		
		if(!spielGestartet) {
		Notifications.create()
				.owner(StarterKlasse.primaryStage)
				.title("Das Spiel muss erst starten, bevor es gespeichert werden kann")
				.hideAfter(Duration.seconds(1.5))
				.position(Pos.TOP_CENTER)
				.showWarning();
		System.out.println("Das Spiel muss erst starten, bevor es gespeichert werden kann");
		return;
		}
		
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpeicherName.fxml"));
		ControllerSpeicherName csn = new ControllerSpeicherName(this);
		
	   						
		try {
			fxmlloader.setController(csn);
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der nßchsten Gui-Oberflßche erzeugen um die SchiffsListe, den in und den Output Reader zu ßbergeben
			
    		Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.setTitle("Spiel speichern");
			newStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveStart(String speicherName) {
		
		System.out.println("Speichern angefordert");
		
		Task<String>task = new Task<String>() {
			
			@Override
			protected String call() throws Exception {
				String currentTime = String.valueOf(System.currentTimeMillis());
				String save = "save "+currentTime;
				connection.write(save);
				file.save(speicherName,Long.parseLong(currentTime), true, unserSpielfeld, gegnerSpielfeld, schiffListe, versenktWirCounter, versenktGegnerCounter, spielZugCounter,schiffCounter,isOffline);
				if(connection.read().equals("pass")) {
					System.out.println("Gegner hat das Speichern akzeptiert");
				}
				return null;
			}
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		
	}
	private void speicherAnfrage(String id) {
		
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpeicherAnfrage.fxml"));
		ControllerSpeicherAnfrage csa = new ControllerSpeicherAnfrage(this,id);
		
	   						
		try {
			fxmlloader.setController(csa);
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der nßchsten Gui-Oberflßche erzeugen um die SchiffsListe, den in und den Output Reader zu ßbergeben
			
    		Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.setTitle("Spiel speichern");
			newStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void saveStartAnfrage(String speicherName, String id) {
		
		file.save(speicherName,Long.parseLong(id), false, unserSpielfeld, gegnerSpielfeld,schiffListe,versenktWirCounter,versenktGegnerCounter,spielZugCounter,schiffCounter,isOffline);

		System.out.println("Spiel erfolgreich gespeichert");
		System.out.println("Client hat gepasst");
		connection.write("pass");
	}
	
	private void spielAufbauen(int[][]unserSpielFeld,int[][]gegnerSpielFeld, int versenktWirCounter, int versenktGegnerCounter, int spielZugCounter) {
		
		this.spielFeldGenerieren();
		gegnerSpielFeldGenerierenLoad(gegnerSpielFeld);
		unserSpielFeldGenerierenLoad(unserSpielFeld);
		this.disableOurField(true);
		spielGestartet=true;
		Task<String>task = new Task<String>() {
			
			protected String call() throws Exception {
				
				if(StarterKlasse.server) {
					System.out.println("Server Bereit");
					if(connection.read().equals("confirmed")) {
						System.out.println("Spiel ist geladen");
					}
					if(!wirSindDran) {
						save.setDisable(true);
						connection.write("pass");
						warteAufGegner();
					}
				}else {
					connection.write("confirmed");
					System.out.println("Client Bereit");
					if(wirSindDran) {
						if(connection.read().equals("pass")) {
							System.out.println("Wir kßnnen jetzt starten");
						}
					}else {
						warteAufGegner();
						save.setDisable(true);
					}
						
				}
						
				return null;
			}
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		
	}
	
	private void gegnerSpielFeldGenerierenLoad(int[][]gegnerSpielFeld) {

		
		
		for(Node n : gridPaneEnemy.getChildren()){
			
				Integer rowIndex = GridPane.getRowIndex(n);
				Integer colIndex = GridPane.getColumnIndex(n);
				if(rowIndex!=null&&colIndex!=null) {
					if (gegnerSpielfeldLaden[rowIndex.intValue()][colIndex.intValue()]==0){
						
					}
					if (gegnerSpielfeldLaden[rowIndex.intValue()][colIndex.intValue()]==1){
						Button b = (Button)n;
						b.setId("cellVerfehlt");
					}
					if (gegnerSpielfeldLaden[rowIndex.intValue()][colIndex.intValue()]==2){
						Button b = (Button)n;
						b.setId("cellGetroffen");
					}		
				}
				
		}
		
	}
	
	private void unserSpielFeldGenerierenLoad(int[][]unserSpielFeld) {
		for(Node n : gridPaneWe.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if (unserSpielfeldLaden[rowIndex.intValue()][colIndex.intValue()]==3){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #000000");
				}
				if(unserSpielfeldLaden[rowIndex.intValue()][colIndex.intValue()]==1) {
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #0B4C5F");
				}
				if(unserSpielfeldLaden[rowIndex.intValue()][colIndex.intValue()]==2) {
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #8A4B08");
				}
			}
			
		}
	}
	
	private void spielFeldGenerieren() {
		/*
		 * 
		 * 
		 * UNSER SPIELFELD
		 * 
		 * 
		 * 
		 */
		//Nur wegen Szene Builder hier Erst Vorhandenes 1x1 Lßschen dann 10x10 erzeugen
		
		
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
		
		
		//Erzeugt 100 verschiedene Buttons ins GridPane, die alle Clickable sind und wenn man Sie anklickt Ihren Index zurßck geben
		for(int i=0;i<spielFeldGroeße;i++) {
			for (int j = 0; j<spielFeldGroeße;j++) {
				
				Button b = new Button();
				b.setId("cell");
				b.setMinHeight((int)365/spielFeldGroeße);
				b.setMinWidth((int)365/spielFeldGroeße);
				b.setOnMouseClicked(new EventHandler <MouseEvent>() {

					//Alle Buttons Fßhig machen per Klick diese Sachen auszufßhren
					@Override
					public void handle(MouseEvent event) {
						Node clickedNode = (Node)event.getSource();
						Button clickedButton = (Button)event.getSource();
						
						
						if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
							if (((MouseEvent) event).getButton().equals(MouseButton.SECONDARY)) {
								System.out.println("LINKSKLICK");
								clickedButton.setStyle(null);
								clickedButton.setId("cell");
								Integer rowIndex = GridPane.getRowIndex(clickedNode);
							    Integer colIndex = GridPane.getColumnIndex(clickedNode);
							    
							    //Bei LinksKlick ZellOrte korrigieren
							    String parts[]=zellorte.split(" ");
							    
							    String rowString = String.valueOf(rowIndex);
							    String colString = String.valueOf(colIndex);
							    //Row und Col in richtiges Format z.B 04 01 bringen
							    if(rowString.length()==1) {
							    	rowString="0"+rowString;
							    }
							    if(colString.length()==1) {
							    	colString="0"+colString;
							    }
							    for(int j=0;j<parts.length;j++) {
							    	if(parts[j].equals(rowString+colString)) {
							    		parts[j]=null;
							    	}
							    }
							    zellorte="";
							    for(int j=0;j<parts.length;j++) {
							    	if(parts[j]!=null) {
							    		
							    		zellorte = zellorte+parts[j]+" ";
							 
							    	}
							    }
							    System.out.println("Zellorte: "+zellorte);
							    if(spielHelfer.pruefePlazierung(zellorte, schiffListe)) {
									ready.setDisable(false);
								}else {
									ready.setDisable(true);
								}
								
							}else {
								// click on descendant node
								Integer rowIndex = GridPane.getRowIndex(clickedNode);
							    Integer colIndex = GridPane.getColumnIndex(clickedNode);
							    String row = String.valueOf(rowIndex);
							    String col = String.valueOf(colIndex);
							    if(row.length()==1) {
							    	row ="0"+row;
							    }
							    if(col.length()==1) {
							    	col="0"+col;
							    }
							    
							    System.out.println("Mouse clicked cell: " + row + " And: " + col);
								clickedButton.setId("cellMarkiert");
								zellorte = zellorte+row+col+" ";
								if(spielHelfer.pruefePlazierung(zellorte, schiffListe)) {
									ready.setDisable(false);
								}else {
									ready.setDisable(true);
								}
								System.out.println("Zellorte: "+zellorte);
							}
								
						}
						
						
						   
					    
						
					}
					
				});
				gridPaneWe.add(b, i, j);
			}
		}
		/* UNSER SPIELFELD ENDE
		 * 
		 * 
		 * 
		 * 
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
				b.setOnMouseClicked(new EventHandler <MouseEvent>() {
				
					
					
					
					//Alle Buttons Fßhig machen per Klick diese Sachen auszufßhren
					@Override
					public void handle(MouseEvent event) {
						Node clickedNode = (Node)event.getSource();
						tempButton = (Button)event.getSource();
						Integer rowInd = GridPane.getRowIndex(clickedNode);
					    Integer colInd = GridPane.getColumnIndex(clickedNode);
					    rowIndex = String.valueOf(rowInd);
					    colIndex = String.valueOf(colInd);
					    if(rowIndex.length()==1) {
					    	rowIndex = "0"+rowIndex;
					    }
					    if(colIndex.length()==1) {
					    	colIndex = "0"+colIndex;
					    }
					    
						if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
							if (((MouseEvent) event).getButton().equals(MouseButton.SECONDARY)) {
								
							    
							    System.out.println("LINKSKLICK");
								tempButton.setStyle(null);
								
								
							}else {
								
							}
								
						}
					    System.out.println("Mouse clicked cell: " + rowIndex + " And: " + colIndex);
												
					}
					
				});
				gridPaneEnemy.add(b, i, j);
			}
		}
		
		/*
		 * 
		 * 
		 * 
		 * GEGNERSPIELFELD ENDE
		 * 
		 */
	}
	
	@FXML
	private void spielAufgeben() {
		if(!spielGestartet) {
			Notifications.create()
					.owner(StarterKlasse.primaryStage)
					.title("Das Spiel muss erst Starten, bevor man aufgeben kann")
					.hideAfter(Duration.seconds(1.5))
					.position(Pos.TOP_CENTER)
					.showWarning();
			System.out.println("Das Spiel muss erst Starten, bevor man aufgeben kann");
			return;
		}
		if(!wirSindDran) {
			Notifications.create()
					.owner(StarterKlasse.primaryStage)
					.title("Spiel kann nur aufgegeben werden, wenn wir am Zug sind")
					.hideAfter(Duration.seconds(1.5))
					.position(Pos.TOP_CENTER)
					.showWarning();
			System.out.println("Spiel kann nur aufgegeben werden, wenn wir am Zug sind");
		}else {
			spiel.spielAufgeben();
		}
		
	}
	
	private void markiereGegnerSchuss(int row, int col) {
		boolean treffer=false;
		for(Schiff schiff:schiffListe) {
			ArrayList<String>s = schiff.getZellorte();
			
			for(int i=0;i<s.size();i++) {
				String parts[]=s.get(i).split(" ");
				if((Integer.parseInt(parts[0])==row)&&(Integer.parseInt(parts[1])==col)) {
					treffer=true;
				}
			}
		}
		
		for(Node n : gridPaneWe.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if (rowIndex.intValue()==row&&colIndex.intValue()==col){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle("-fx-background-color: #0B4C5F");
					if(treffer) {
						System.out.println("Treffer");
						b.setStyle("-fx-background-color: #8A4B08");
					}
				}
				
			}
		}
		
	}

	private void makeButtonsUnclickable(String buttonMenge) {
		
		String []parts = buttonMenge.split(" ");
		for(Node n : gridPaneWe.getChildren()){
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			for(int i=0;i<parts.length;i++) {
				if(rowIndex!=null&&colIndex!=null) {
					if(Integer.parseInt(parts[i].substring(0, 2))==rowIndex.intValue()&&Integer.parseInt(parts[i].substring(2, 4))==colIndex.intValue()) {
						System.out.println("Unclickable erreicht");
						Button b=(Button)n;
						b.setDisable(true);
					}	
				}
				
			}
			
		}
	}
	
	private void disableOurField(boolean bool) {
		
		
		for(Node n : gridPaneWe.getChildren()){
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				Button b = (Button)n;
				b.setDisable(bool);
			}
		}
		
		
	}
	
	public void setDerZug(String s) {
		this.derZug = s;
	}

	public void onActionSounds(ActionEvent event) {
		Stage newStage = new Stage();
		newStage.setScene(StarterKlasse.music);
		newStage.show();
	}
	@FXML
	
	private void undoPlazierung() {
		
		for(int i=0;i<schiffListe.size();i++) {
			if(schiffListe.get(i)!=null) {
				String koordinaten="";
				if(schiffListe.get(i).getZellorte()!=null) {
					for(int j = 0;j<schiffListe.get(i).getZellorte().size();j++) {
						String []parts = schiffListe.get(i).getZellorte().get(j).split(" ");
						koordinaten = koordinaten + " "+parts[0]+parts[1];
					}
				}
				String koordinaten2 = " "+allePlazierungen.get(allePlazierungen.size()-1);
				koordinaten = koordinaten + " ";
				if(koordinaten2.equals(koordinaten)) {
					String[] parts2 = zuPlazieren.getText().split("      ");
					
					switch (schiffListe.get(i).getZellorte().size()) {
						case 2:
							parts2[3]=String.valueOf(Integer.parseInt(parts2[3])+1);
							break;
						case 3:
							parts2[2]=String.valueOf(Integer.parseInt(parts2[2])+1);
							break;
						case 4:
							parts2[1]=String.valueOf(Integer.parseInt(parts2[1])+1);
							break;
						case 5:
							parts2[0]=String.valueOf(Integer.parseInt(parts2[0])+1);
							break;
						
							
					}
					zuPlazieren.setText(parts2[0]+"      "+parts2[1]+"      "+parts2[2]+"      "+parts2[3]);
					schiffListe.get(i).setZellorte(null);
					schiffListe.get(i).setIstPlaziert(false);
					String [] parts = koordinaten2.split(" ");
					
					for(int j= 1;j<parts.length;j++) {
						this.disableButton(parts[j]);
					}
						
					
					
					
				}
			}
		}
		
		
		allePlazierungen.remove(allePlazierungen.size()-1);
		
		if(allePlazierungen.isEmpty()) {
			buttonUndo.setVisible(false);
		}
	}
	
	private void disableButton(String button) {
		System.out.println("BUTTON : "+button);
		for(Node n : gridPaneWe.getChildren()){
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				
				if (rowIndex.intValue()==Integer.parseInt(button.substring(0, 2))&&colIndex.intValue()==Integer.parseInt(button.substring(2, 4))){
					Button b = (Button)n;
					b.setId("cell");
					b.setStyle(null);
					b.setDisable(false);
				}
				
			}
		}
		if(!spielStarten.isDisable()) {
			this.disableOurField(false);
			this.spielStarten.setDisable(true);
		}
		
	}
	
	
}
