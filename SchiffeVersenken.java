package schiffeversenken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;

public class SchiffeVersenken {
	
	//private Objektvariablen, Helfer-Objekt zur Hilfe beim Spiel einrichten, 
	//Liste mit den verschiedenen Schiffen und Versuche
	
	private SpielHelfer helfer = new SpielHelfer();
	private ArrayList <Schiff> schiffListe = new ArrayList <Schiff>();
	private int anzahlVersuche = 0;
	
	
	public void spieleinrichten(String eingabe) {
		
		//Eingabe aufteilen in 5 verschiedene Arrayelemente
		String[]parts = eingabe.split(" ");
		
		//Groeße des Spielfeldes mitteilen
		int groeße = Integer.valueOf(parts[1]);
		helfer.setRasterLaenge(groeße);
		helfer.setRasterGroeße(groeße*groeße);
		helfer.setRaster(new int [groeße*groeße]);
	
		// For Schleifen, die jeweils die Schiffe erzeugen (5er Länge, 4er Länge, etc. )
		
		for (int i=0;i<Integer.valueOf(parts[2]);i++) {
			
			Schiff ship = new Schiff(5,"F"+i+1);
			this.schiffListe.add(ship);
		}
		
		for (int i=0;i<Integer.valueOf(parts[3]);i++) {
			
			Schiff ship = new Schiff(4,"V"+i+1);
			this.schiffListe.add(ship);
		}

		for (int i=0;i<Integer.valueOf(parts[4]);i++) {
	
			Schiff ship = new Schiff(3,"D"+i+1);
			this.schiffListe.add(ship);
		}

		for (int i=0;i<Integer.valueOf(parts[5]);i++) {
	
			Schiff ship = new Schiff(2,"Z"+i+1);
			this.schiffListe.add(ship);
		}
		
		
		System.out.println("Spiel Startet");
		
		//Alle Schiffe in der Liste aufs Feld plazieren
		
		for(Schiff aktuellesSchiff : schiffListe) {
			ArrayList <String> neuerOrt = helfer.plaziereSchiff(aktuellesSchiff.getGroeße()); //Zufällige Plazierung der Schiffe hier
			
			aktuellesSchiff.setZellorte(neuerOrt);
		}
	
	}

	public void beginneSpiel(BufferedReader in, Writer out, String role) {
		
		boolean wirSindDran =false;
		String rateVersuch=null;
		String temp=null;
		int answer = 0;
		
	
		
		//Wenn man Server ist, darf man Anfangen zu schießen, danach läuft es immer abwechselnd 
		if(role.equals("Server")) {
			try {
				//shot 3 4
				out.write("Benutzereingabe");// Hier kommt unsere Benutzereingabe für den ersten Schuss 
				out.flush();
				temp = in.readLine(); //answer 0
				if(temp.equals("Answer 0")) {
					//Erste Schuss vorbei --> Gui muss was sagen
				}else {
					// HIER DANN JE NACH ANTWORT 1 oder 2 WAS AN UNSERER GUI ANZEIGEN, BZW AUF DEM GEGNER FELD MARKIEREN WAS WIR GETROFFEN HABEN
				}
				out.write("pass");	//Pass-Befehl an den anderen schicken 
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //Hier
		}
		
		//Schleife, die sich immer wieder wiederholt bis man alle Schiffe versenkt hat
		while(!schiffListe.isEmpty()) {
			
			
			
			try {
				
				//Warten auf benutzereingabe vom Gegner und überprüfe diese auf Treffer 
				rateVersuch = in.readLine();
				
				//Prüfen, ob der Gegner getroffen hat und ihm die entsprechende Antwort mitteilen
				
				
				switch (this.pruefeRateversuch(rateVersuch)) {
				
				case 0:
					out.write("answer 0");
					answer = 0;
					break;
				
				case 1: 
					out.write("answer 1");
					answer = 1;
					break;
				
				case 2:
					out.write("answer 2");
					answer = 2;
					break;
				}
				//JE NACHDEM WAS DER GEGNER GETROFFEN HAT AUF UNSEREM SPIELFELD MARKIEREN
				
				//Wenn der Gegner nicht getroffen hat und uns dann sein pass schickt
				
				temp = in.readLine();
				if(answer == 0 && temp.equals("pass")) {
					
					wirSindDran=true;
					
					//Schleife läuft solange, bis wir nicht mehr getroffen haben, dann geht es wieder von vorne los in der äußeren Schleife 
					while(wirSindDran) {
						
						out.write("Unsere Benutzereingabe\n"); //Unsere Eingabe natürlich über die Gui
						temp = in.readLine();
						
						switch (temp) {
						
						
						case "answer 0":
							out.write("pass"); 
							//Hier natürlich noch GUI ELemente, dass wir vorbeigeschossen haben
							wirSindDran = false;
							break;
												
						case "answer 1":
							out.write("pass");
							//Hier wird nun alles gemacht was notwendig ist, wenn wir einen Treffer gelandet haben
							//Hier muss noch readLine()
							break;
							
						case "answer 2":
							out.write("pass");
							//Hier wird nun alles gemacht was notwendig ist, wenn wir ein Schiff versenkt haben
							//Hier muss noch readLine()
							break;
							
						}
						
						
					}
					
					
				} //Ansonsten hat er ja getroffen und wir müssen ihm einen pass zurückschicken, hier wird dann gewartet bis der pass kommt und danach darf er wieder spielen
				else if(temp.equals("pass")) {
					out.write("pass");
				}
				
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}
		//Spiel beenden wenn die whileSchleife durchlaufen ist und alle Schiffe versenkt sind
		this.beendeSpiel();
		
	}


	private int pruefeRateversuch(String rateVersuch) {
		
		
		this.anzahlVersuche++;
		int ergebnis = 0;
		
		//Alle Schiffe durchprüfen ob ein Treffer erzielt wurde oder sogar das Schiff versenkt wurde
		for (Schiff aktuellesSchiff : schiffListe) {
			ergebnis = aktuellesSchiff.pruefDich(rateVersuch);
			
			if (ergebnis==1) {
				
				return ergebnis;
			}
			
			if(ergebnis==2) {
				
				
				schiffListe.remove(aktuellesSchiff);
				return ergebnis;
			}
				
		}
		
		return ergebnis;
		
	}


	private void beendeSpiel() {
		
		System.out.println("Alles versenkt");
		System.out.println("Versuche: "+this.anzahlVersuche);
		
	}
}
