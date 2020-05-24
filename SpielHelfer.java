package schiffeversenken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SpielHelfer {

	private int rasterLaenge;
	private int rasterGroe�e;
	private int [] raster;
	private int schiffAnzahl = 0;
	
	
	

	//Die ArrayList enth�lt den bereich an dem sich das Schiff befindet. z.B. bereich(0) = 1010 bereich (1) = 1110 bereich(2) = 1210 ..
	
	public boolean plaziereSchiff(ArrayList <String> bereich) {
		//Hier wird gepr�ft, ob es m�glich ist das Schiff an der entsprechend ausgew�hlten Stelle zu plazieren
		//Returnt true oder false je nachdem ob es M�glich ist so ein Schiff noch zu plazieren
		
		return false;
	}
	
	public String kalkuliereSchiffe(int groe�e) {
		
		int felder = (int) (groe�e*groe�e* 0.20); //Cast auf ganzzahlige Felderanzahl, die 20% entsprechen
		int temp = felder; //Tempor�rer int f�r die zuf�llige Vergabe
		int [] vergabe = new int [4]; //Schiffsverteilung [0] sind die 5er Schiffe [1] die 4rer usw..
		Random random = new Random();
		
		//Bei 5x5 Feld k�nnen keine 5er und 4rer Schiffe drauf deshalb vorgegebene Schiffsmenge
		if(felder == 5) {
			return "0 0 1 1";
		}
		//Bei 6x6 Feld k�nnen keine 5er Schiffe drauf deshalb vorgegebene Schiffsmenge (int) 6*6*0,20 = 7
		if (felder ==7) {
			return "0 0 1 2";
		}
		
		//Ab hier zuf�llige vergabe
		while(felder >0) {
			
			temp = 2 + random.nextInt(4); // nextInt() Liefert zahl von 0-3 mit +2 ergibt sich also 2 - 5 
			
			//Erh�hung der Werte im Anzahlarray
			if(temp == 5)vergabe[0]=vergabe[0]+1;
			if(temp == 4)vergabe[1]=vergabe[1]+1;
			if(temp == 3)vergabe[2]=vergabe[2]+1;
			if(temp == 2)vergabe[3]=vergabe[3]+1;
			
			felder = felder - temp;
		}
		
		//Die M�glichkeit, dass mehr Felder belegt werden als die 20 % besteht durch diese Methode.
		//Es d�rfte sich allerdings ausgleichen, da der (int) Typecast am Anfang die Kommawerte abschneidet und
		//Dadurch auch oft ein Wert unter 20% herauskommt
		
		
		//R�ckgabe im Format "x x x x" der Belegung
		return String.valueOf(vergabe[0])+" "+String.valueOf(vergabe[1])+" "+String.valueOf(vergabe[2])+" "+String.valueOf(vergabe[3]);
	}
	
	//Setter f�r Groe�e und Laenge des Spielfelds
	public void setRasterGroe�e(int rasterGroe�e) {
		this.rasterGroe�e = rasterGroe�e;
	}

	public void setRasterLaenge(int rasterLaenge) {
		this.rasterLaenge = rasterLaenge;
	}

	public void setRaster(int[] raster) {
		this.raster = raster;
	}
	
	

	

}










