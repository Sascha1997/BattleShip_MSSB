package application;

import java.awt.Point;
import java.util.ArrayList;

public class Schiff {

	private ArrayList <String> zellorte;
	private int groeﬂe;
	private String ident;
	private boolean istPlaziert=false;
	public ArrayList<Point> availableCords = new ArrayList<>();
    private final int initialSize;
    
    public Schiff(ArrayList<Point> c, int ini, String ident){
        for(Point p : c){
            availableCords.add(new Point(p));
        }
        this.initialSize = ini;
        this.ident = ident;
    }
	
	public Schiff(int groeﬂe, String identifikation) {
		this.groeﬂe = groeﬂe;
		this.ident = identifikation;
		this.initialSize=0;//nicht benˆtigt wenn KI nicht spielt
	}
	
	public boolean contains(Point p){
        for(Point x : this.availableCords){
            if(x.x == p.x && x.y == p.y) return true;
        }
        return false;
    }
	
	public int pruefDich(String benutzereingabe) {
		
		
		//Benutzereingabe in Reihen und Spalten aufteilen
		//String[0] ist die Zeilennummer und String[1] die Spaltennummer
		//Benutzereingabe in Format XXXX bringen z.B. 0508 f¸r Zeile 5 Spalte 8
		//Pr¸fen ob Eingabe zum Treffer gef¸hrt hat
		
		int ergebnis = 0;
		int index = zellorte.indexOf(benutzereingabe);
		if(index>=0) {
			zellorte.remove(index);

			if(zellorte.isEmpty()) {
				ergebnis = 2;
				System.out.println("Schiff mit Namen "+this.ident+" wurde versenkt");

			}else {
				ergebnis = 1;
			}
		}
		//R¸ckgabe ob Daneben / Treffer / Versenkt
		return ergebnis;
	}

	 public void removeCord(Point p){
	        for(int i = 0; i < this.availableCords.size(); i++){
	            if(this.availableCords.get(i).x == p.x && this.availableCords.get(i).y == p.y){
	                this.availableCords.remove(i);
	                break;
	            }
	        }
	 }
	
	public boolean checkIfDead(){
        return this.availableCords.size() == 0;
    }
	
	public int getInitialSize(){
        return this.initialSize;
    }
	
	//Setter&Getter Methoden
	public void setZellorte(ArrayList<String> orte) {
		this.zellorte = orte;
	}

	public int getGroeﬂe() {
		return this.groeﬂe;
	}
	
	public void setGroeﬂe(int groeﬂe) {
		this.groeﬂe = groeﬂe;
	}
	public String getIdentification() {
		return this.ident;
	}
	
	public void setIdentifikation(String identifikation) {
		this.ident = identifikation;
	}
	
	public boolean getIstPlaziert() {
		return istPlaziert;
	}


	public void setIstPlaziert(boolean istPlaziert) {
		this.istPlaziert = istPlaziert;
	}


	public ArrayList<String> getZellorte() {
		return zellorte;
	}
	
	public ArrayList<Point>getCords(){
		return this.availableCords;
	}
	
}
