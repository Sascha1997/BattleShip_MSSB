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
	//Gegner Schiffe getroffen
	//Gegner Feld verschossen
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
		int i,j,k,l;
		for (i=0;i<Integer.valueOf(parts[2]);i++) {
			
			Schiff ship = new Schiff(5,"F"+i+1);
			this.schiffListe.add(ship);
		}
		
		for (j=0;i<Integer.valueOf(parts[3]);i++) {
			
			Schiff ship = new Schiff(4,"V"+i+1);
			this.schiffListe.add(ship);
		}

		for (k=0;i<Integer.valueOf(parts[4]);i++) {
	
			Schiff ship = new Schiff(3,"D"+i+1);
			this.schiffListe.add(ship);
		}

		for (l=0;i<Integer.valueOf(parts[5]);i++) {
	
			Schiff ship = new Schiff(2,"Z"+i+1);
			this.schiffListe.add(ship);
		}
		/**
		//i,j,k,l enthalten die Infos wie viele von welchen Schiffsgrößen drin sind, dies entsprechend in der GUI bei den Schiffen zum reinziehen hinschreiben
		//i entspricht 5er, j 4rer, k 3er, l 2er
		
		ArrayList <String> bereich = new ArrayList <String>();
		//bereich = Benutzereingabe... in der plaziereSchiff Methode wird dann geprüft ob das Schiff dort plaziert werden kann und liefert boolean zurück
		//Es muss dann noch geprüft werden, welche größe das Schiff hat und entsprechend i,j,k,l verringert werden bzw. am Anfang auch immer abgefragt werden ob die Schiffsgröße noch möglich ist
		this.helfer.plaziereSchiff(bereich);
		
		//Alle Schiffe in der Liste aufs Feld plazieren
		//Hier soll	die Gui Anzeigen, wie viel Schiffe von welcher Größe zu verteilen sind. Per Drag and Drop können diese dann auf die Zellen plaziert werden und es wird helfer.plaziereSchiff(zellenbereich) aufgerufen
		//plaziere Schiff Methode muss dafür noch umgeschrieben werden. Dies erst möglich nach Absprache wie die Guiklassen dort arbeiten. 
		
		//Ganze SchiffsListe plazieren solange bis alle plaziert sind
		**/
		System.out.println("Spiel Startet");
	}

	public void beginneSpiel(BufferedReader in, Writer out, String role) {
		
		boolean wirSindDran =false;
		String rateVersuch=null;
		String temp=null;
		int answer = 0;
		
	
		//Die Gui Muss generell immer Informationen liefern wer dran ist
		//Wenn man Server ist, darf man Anfangen zu schießen, danach läuft es immer abwechselnd 
	
		if(role.equals("Server")) {
			try {
				//shot 3 4
				out.write("Benutzereingabe");// Hier kommt unsere Benutzereingabe für den ersten Schuss 
				out.flush();
				temp = in.readLine(); //answer 0
				if(temp.equals("Answer 0")) {
					out.write("pass\n");	//Pass-Befehl an den anderen schicken 
					//Erste Schuss vorbei --> Gui muss was sagen
				}else {
					// HIER DANN JE NACH ANTWORT 1 oder 2 WAS AN UNSERER GUI ANZEIGEN, BZW AUF DEM GEGNER FELD MARKIEREN WAS WIR GETROFFEN HABEN
					wirSindDran = true;
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //Hier
		}
		
		//Schleife, die sich immer wieder wiederholt bis man alle Schiffe versenkt hat
		while(!schiffListe.isEmpty()) {
			
			try {
				if(wirSindDran) {
					
					
					//Hier kann auch ein Speichervorgang gestartet werden von unserer Seite mit dem Schlüsselwort SAVE
					out.write("Unsere Benutzereingabe\n");
					//Wenn wir speichern drücken muss des hier ablaufen 
					temp = in.readLine();
					
					switch (temp) {
					
					
					case "answer 0":
						out.write("pass"); 
						//Hier natürlich noch GUI ELemente, dass wir vorbeigeschossen haben
						wirSindDran = false;
						break;
											
					case "answer 1":
						//Hier wird nun alles gemacht was notwendig ist, wenn wir einen Treffer gelandet haben
						//Hier muss noch readLine()
						break;
						
					case "answer 2":
						
						//Hier wird nun alles gemacht was notwendig ist, wenn wir ein Schiff versenkt haben
						//Hier muss noch readLine()
						break;
					} 
				
					
				
				
				
				
				}else {
			
				
				//Warten auf benutzereingabe vom Gegner und überprüfe diese auf Treffer 
				rateVersuch = in.readLine();
				
				//Prüfen, ob der Gegner getroffen hat und ihm die entsprechende Antwort mitteilen
				
				
				switch (this.pruefeRateversuch(rateVersuch)) {
				
				case 0:
					out.write("answer 0");
					
					if(in.readLine().equals("pass")) {
						wirSindDran = true;	
					}
					answer = 0;
					break;
				
				case 1: 
					out.write("answer 1");
					answer = 1;
					//Gui 
					break;
				
				case 2:
					out.write("answer 2");
					answer = 2;
					//Gui
					break;
				case 3:
					//Speichervorgang in die Wege leiten
				}
			
				//JE NACHDEM WAS DER GEGNER GETROFFEN HAT AUF UNSEREM SPIELFELD MARKIEREN
				
				//Wenn der Gegner nicht getroffen hat und uns dann sein pass schickt
				
				
				
								
			
				
			
				}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
						
		}
		//Spiel beenden wenn die whileSchleife durchlaufen ist und alle Schiffe versenkt sind
		this.beendeSpiel();
		
	}


	private int pruefeRateversuch(String rateVersuch) {
		
		
		if(rateVersuch.equals("SAVE")) {
			return 3;
		}
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
