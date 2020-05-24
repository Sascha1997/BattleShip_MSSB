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
		//Benutzereingabe in Format XXXX bringen z.B. 0508 f�r Zeile 5 Spalte 8
		String zellenname="";
		for(int i=0;i<2;i++) {
			if(eingabe[i].length()==1) {
				zellenname = "0"+eingabe[i];
			}else {
				zellenname = eingabe [i];
			}
		}

		
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