package schiffeversenken;

import java.util.ArrayList;

public class Schiff {

	private ArrayList <String> zellorte;
	private int groe�e;
	private String identifikation;
	
	
	public Schiff(int groe�e, String identifikation) {
		this.groe�e = groe�e;
		this.identifikation = identifikation;
	}


	public int pruefDich(String benutzereingabe) {
		
		//Benutzereingabe in Reihen und Spalten aufteilen
		//String[0] ist die Zeilennummer und String[1] die Spaltennummer
		
		String[]eingabe = benutzereingabe.split(" ");
		
		//Umwandeln der Reiheneingabe in Buchstaben zum anpassen f�r unser Spielfeld und dann zusammensetzen als Eingabe String
		
		String zellenname = String.valueOf(SpielHelfer.alphabet.charAt(Integer.parseInt(eingabe[0])))+eingabe[1];
		
		//Pr�fen ob Eingabe zum Treffer gef�hrt hat
		
		int ergebnis = 0;
		int index = zellorte.indexOf(zellenname);
		if(index>=0) {
			zellorte.remove(index);

			if(zellorte.isEmpty()) {
				ergebnis = 2;
				System.out.println("Schiff mit Namen "+this.identifikation+" wurde versenkt");

			}else {
				ergebnis = 1;
			}
		}
		//R�ckgabe ob Daneben / Treffer / Versenkt
		return ergebnis;
	}

	//Setter&Getter Methoden
	public void setZellorte(ArrayList<String> orte) {

		this.zellorte = orte;

	}

	public int getGroe�e() {
		return this.groe�e;
	}
	
	public String getIdentifikation() {
		return this.identifikation;
	}
	
}