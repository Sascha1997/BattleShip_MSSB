package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SpielHelfer {

	
	static final String alphabet = "abcdefghijklmnopqrstuvwxyz���*";
	private int rasterLaenge;
	private int rasterGroeße;
	private int [] raster;
	private int schiffAnzahl = 0;
	
	
	//Lie�t die Benutzereingabe ein und gibt diese zur�ck
	public String getBenutzereingabe(String eingabe) {
		
		String eingabeZeile = null;
		System.out.println(eingabe +" ");
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
			eingabeZeile = is.readLine();
			if(eingabeZeile.length()==0) return null;
			
		}catch (IOException e) {
			System.out.println("IOException: "+e);
		}
		
		return eingabeZeile.toLowerCase();
	}

	//Aktuell werden Schiffe nur zuf�llig plaziert, Methode muss umgeschrieben werden auf vorgeschriebene Orte
	
	public ArrayList<String> plaziereSchiff(int schiffGroeße) {
		
		ArrayList <String> alphaZellen = new ArrayList <String>(); //Koordinaten der Form wie z.b. a1, a2, e5 usw..
		String temp = null;
		int [] koordinaten = new int [schiffGroeße];		//aktuelle Koordinatenm�glichkeiten
		int versuche = 0;									//Versuchsz�hler
		boolean erfolg = false;								//Wurde ein Ort gefunden?
		int ort = 0;										//aktuelle Startposition
		
		schiffAnzahl++; 									//Erh�hung Anzahl Schiffe, variable enth�lt Anzahl der bereits plazierten
		int inkrement = 1;
		
		if((schiffAnzahl%2)==1) {
			inkrement = rasterLaenge;	//Plaziert Schiffe vertikal oder horizontal je nachdem das wie vielte es ist
		}
		
		while(!erfolg&versuche++<200) {
			ort = (int) (Math.random()*rasterGroeße); //Sucht einen zuf�lligen Ort und holt daf�r zuf�lligen Startpunkt
			int x = 0;
			erfolg = true;
			while (erfolg && x < schiffGroeße) {
				if(this.raster[ort]==0) {
					koordinaten[x++]=ort;
					ort +=inkrement;
					if(ort>=rasterGroeße) {
						erfolg =false;
					}
					if(x>0 &(ort%this.rasterLaenge==0)) {
						erfolg =false;
					}
				}else {
					erfolg = false;
				}
			}
		}
		
		//Ort in alphabetische Koordinaten umwandeln
		
		int x = 0;
		int zeile = 0;
		int spalte = 0;
		
		while (x<schiffGroeße) {
			this.raster[koordinaten[x]]= 1;				//Punkte im Raster als verwendet markieren
			zeile = (int) (koordinaten[x]/rasterLaenge); //Zeilenwert holen
			spalte = koordinaten[x] % this.rasterLaenge; //numerischen Spaltenwert holen
			temp = String.valueOf(SpielHelfer.alphabet.charAt(spalte)); //in alphabetisches Zeichen umwandeln
			
			alphaZellen.add(temp.concat(Integer.toString(zeile)));
			x++;
			
			System.out.println(" Koordianten "+x+" = "+ alphaZellen.get(x-1)); //Zeigt wo das Schiff plaziert wurde
		}
		
		System.out.println("\n");
		return alphaZellen;
	}
	
	public String kalkuliereSchiffe(int groeße) {
		
		int felder = (int) (groeße*groeße* 0.20); //Cast auf ganzzahlige Felderanzahl, die 20% entsprechen
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
	public void setRasterGroeße(int rasterGroeße) {
		this.rasterGroeße = rasterGroeße;
	}

	public void setRasterLaenge(int rasterLaenge) {
		this.rasterLaenge = rasterLaenge;
	}

	public void setRaster(int[] raster) {
		this.raster = raster;
	}
	
	

	

}










