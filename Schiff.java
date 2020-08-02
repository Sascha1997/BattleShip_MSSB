package application;

import java.awt.Point;
import java.util.ArrayList;
/**
 * Schiffsklasse
 */
public class Schiff {
	
	/**
	 * Objekt-Attribute
	 */
	private ArrayList <String> zellorte;
	private int groeﬂe;
	private String ident;
	private boolean istPlaziert=false;
	public ArrayList<Point> availableCords = new ArrayList<>();
    private final int initialSize;
    
    /**
     * Konstruktor f¸r den KI-Modus, hier werden mehr informationen benˆtigt, als beim Player-Modus
     * @param c
     * @param ini
     * @param ident
     */
    public Schiff(ArrayList<Point> c, int ini, String ident){
        for(Point p : c){
            availableCords.add(new Point(p));
        }
        this.initialSize = ini;
        this.ident = ident;
    }
	/**
	 * Konstrutkor Player-Modus
	 * @param groeﬂe
	 * @param identifikation
	 */
	public Schiff(int groeﬂe, String identifikation) {
		this.groeﬂe = groeﬂe;
		this.ident = identifikation;
		this.initialSize=0;//nicht ben√ütigt wenn KI nicht spielt
	}
	
	/**
	 * Pr¸fen, ob Schiff auf einem bestimmten Punkt liegt
	 * @param p
	 * @return true, wenn ja
	 */
	public boolean contains(Point p){
        for(Point x : this.availableCords){
            if(x.x == p.x && x.y == p.y) return true;
        }
        return false;
    }
	
	/**
	 * Pr¸fen eines Schusses auf ein Schiff vom Gegner
	 * @param benutzereingabe
	 * @return 2, bei versenkt
	 * @return 1, bei Treffer
	 * @return 0, bei vorbei
	 */
	public int pruefDich(String benutzereingabe) {
		
		
		int ergebnis = 0;
		int index = zellorte.indexOf(benutzereingabe);
		if(index>=0) {
			zellorte.remove(index);

			if(zellorte.isEmpty()) {
				ergebnis = 2;

			}else {
				ergebnis = 1;
			}
		}
		return ergebnis;
	}
	
	/**
	 * Hilfsmethode f¸r die KI, in der eine Koordinate des Schiffes entfernt wird
	 * @param p
	 */
	 public void removeCord(Point p){
	        for(int i = 0; i < this.availableCords.size(); i++){
	            if(this.availableCords.get(i).x == p.x && this.availableCords.get(i).y == p.y){
	                this.availableCords.remove(i);
	                break;
	            }
	        }
	 }
	/**
	 * Pr¸fen f¸r die KI, ob Schiff versenkt ist
	 * @return
	 */
	public boolean checkIfDead(){
        return this.availableCords.size() == 0;
    }
	
	/**
	 * Getter and Settermethoden f¸r alle wichtigen Attribute
	 */
	
	public int getInitialSize(){
        return this.initialSize;
    }
	
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
