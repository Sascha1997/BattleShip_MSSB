package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SchiffeVersenken {
	
	private SpielHelfer helfer = new SpielHelfer();
	private ArrayList <Schiff> schiffListe = new ArrayList<Schiff>();
	private Connection connection;
	
	public SchiffeVersenken(Connection connection) {
		this.connection = connection;
	}
	public void spielEinrichten(String eingabe) {
		
		//Eingabe aufteilen in 5 verschiedene Arrayelemente
		String[]parts = eingabe.split(" ");
		
		//Groeﬂe des Spielfeldes mitteilen
		int groeﬂe = Integer.valueOf(parts[1]);
		helfer.setRasterLaenge(groeﬂe);
		helfer.setRasterGroeﬂe(groeﬂe*groeﬂe);
		helfer.setRaster(new int [groeﬂe*groeﬂe]);
	
		// For Schleifen, die jeweils die Schiffe erzeugen (5er L‰nge, 4er L‰nge, etc. )
		int i,j,k,l;
		for (i=0;i<Integer.valueOf(parts[2]);i++) {
			
			Schiff ship = new Schiff(5,"F"+(i+1));
			this.schiffListe.add(ship);
		}
		
		for (j=0;j<Integer.valueOf(parts[3]);j++) {
			
			Schiff ship = new Schiff(4,"V"+(j+1));
			this.schiffListe.add(ship);
		}

		for (k=0;k<Integer.valueOf(parts[4]);k++) {
	
			Schiff ship = new Schiff(3,"D"+(k+1));
			this.schiffListe.add(ship);
		}

		for (l=0;l<Integer.valueOf(parts[5]);l++) {
	
			Schiff ship = new Schiff(2,"Z"+(l+1));
			this.schiffListe.add(ship);
		}
		
		System.out.println("Spiel Startet");
		
	}
	
	
	public void spielBeenden(boolean gewonnen, Stage stage) {
		System.out.println("Spiel ist nun Beendet");
		this.connection.closeConnection();
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ZurueckZumMenue.fxml"));
		ControllerZurueckZumMenue czzm = new ControllerZurueckZumMenue(gewonnen);
    	
    	
    	try {
    		fxmlloader.setController(czzm);
    		Parent root = fxmlloader.load();
    		Stage newStage = new Stage();
    		newStage.setTitle("Spielende");
    		newStage.setScene(new Scene(root,600, 400));
    		newStage.show();
    			
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
	}
	
	public void spielAbbruch() {
		System.out.println("SpielAbbruch");
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielAbbruch.fxml"));
		ControllerSpielAbbruch csa = new ControllerSpielAbbruch(connection);
		
	   						
		try {
			fxmlloader.setController(csa);
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der n‰chsten Gui-Oberfl‰che erzeugen um die SchiffsListe, den in und den Output Reader zu ¸bergeben
			
    		Stage newStage = new Stage();
			newStage.setTitle("Spielabbruch");
			newStage.setScene(new Scene(root, 500, 280));
			newStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Schiff> getSchiffListe() {
		return schiffListe;
	}
	
	public void spielAufgeben() {
		
		System.out.println("SpielAufgeben");
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielAufgeben.fxml"));
		ControllerSpielAufgeben csa = new ControllerSpielAufgeben(connection);
		
	   						
		try {
			fxmlloader.setController(csa);
			Parent root = fxmlloader.load();//Initialize der Controller Klasse wird schon hier aufgerufen 
			//ControllerObjekt von der n‰chsten Gui-Oberfl‰che erzeugen um die SchiffsListe, den in und den Output Reader zu ¸bergeben
			
    		Stage newStage = new Stage();
			newStage.setTitle("Spiel aufgeben");
			newStage.setScene(new Scene(root, 500, 280));
			newStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
