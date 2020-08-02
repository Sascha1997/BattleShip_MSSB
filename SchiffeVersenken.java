package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Spielklasse
 * 
 */
public class SchiffeVersenken {
	
	/**
	 * Objekt-Attribute
	 */
	private ArrayList <Schiff> schiffListe = new ArrayList<Schiff>();
	private Connection connection;
	
	/**
	 * Konstruktor 
	 * @param connection
	 */
	public SchiffeVersenken(Connection connection) {
		this.connection = connection;
	}
	/**
	 * Erzeugen der Schiffe mit den Plazierungsparamtern 
	 * @param eingabe der Plazierungsparameter
	 */
	public void spielEinrichten(String eingabe) {
		
		String[]parts = eingabe.split(" ");
		
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
		
		
	}
	
	/**
	 * Beenden des Spiels und Aufruf des Beendenfensters
	 * @param gewonnen
	 * @param stage
	 */
	public void spielBeenden(boolean gewonnen, Stage stage) {
		System.out.println("Spiel ist nun Beendet");
		this.connection.closeConnection();
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("ZurueckZumMenue.fxml"));
		ControllerZurueckZumMenue czzm = new ControllerZurueckZumMenue(gewonnen);
    	
    	
    	try {
    		fxmlloader.setController(czzm);
    		Parent root = fxmlloader.load();
    		Stage newStage = new Stage();
    		newStage.setScene(new Scene(root));
    		newStage.show();
    			
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}
	/**
	 * Aufruf des Spielabbruchfensters
	 */
	public void spielAbbruch() {
		System.out.println("SpielAbbruch");
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielAbbruch.fxml"));
		ControllerSpielAbbruch csa = new ControllerSpielAbbruch(connection);
		
	   						
		try {
			fxmlloader.setController(csa);
			Parent root = fxmlloader.load();			
    		Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Aufruf des Aufgebenfensters
	 */
	public void spielAufgeben() {
		
		System.out.println("SpielAufgeben");
		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("SpielAufgeben.fxml"));
		ControllerSpielAufgeben csa = new ControllerSpielAufgeben(connection);
		
	   						
		try {
			fxmlloader.setController(csa);
			Parent root = fxmlloader.load();
			
    		Stage newStage = new Stage();
			newStage.setScene(new Scene(root));
			newStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter
	 * @return Schiffliste
	 */
	public ArrayList<Schiff> getSchiffListe() {
		return schiffListe;
	}
	
}
