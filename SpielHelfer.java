package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SpielHelfer {
	private int rasterLaenge;
	private int rasterGroeße;
	private int [] raster;
	private int schiffAnzahl = 0;
	
	public String kalkuliereSchiffe(int groeße) {
		
		int felder = (int) (groeße*groeße* 0.05); //Cast auf ganzzahlige Felderanzahl, die 20% entsprechen
		int temp = felder; //Temporärer int für die zufällige Vergabe
		int [] vergabe = new int [4]; //Schiffsverteilung [0] sind die 5er Schiffe [1] die 4rer usw..
		Random random = new Random();
		
		//Bei 5x5 Feld können keine 5er und 4rer Schiffe drauf deshalb vorgegebene Schiffsmenge
		if(felder == 5) {
			return "0 0 1 1";
		}
		//Bei 6x6 Feld können keine 5er Schiffe drauf deshalb vorgegebene Schiffsmenge (int) 6*6*0,20 = 7
		if (felder ==7) {
			return "0 0 1 2";
		}
		
		//Ab hier zufällige vergabe
		while(felder >0) {
			
			temp = 2 + random.nextInt(4); // nextInt() Liefert zahl von 0-3 mit +2 ergibt sich also 2 - 5 
			
			//Erhöhung der Werte im Anzahlarray
			if(temp == 5)vergabe[0]=vergabe[0]+1;
			if(temp == 4)vergabe[1]=vergabe[1]+1;
			if(temp == 3)vergabe[2]=vergabe[2]+1;
			if(temp == 2)vergabe[3]=vergabe[3]+1;
			
			felder = felder - temp;
		}
		
		//Die Möglichkeit, dass mehr Felder belegt werden als die 20 % besteht durch diese Methode.
		//Es dürfte sich allerdings ausgleichen, da der (int) Typecast am Anfang die Kommawerte abschneidet und
		//Dadurch auch oft ein Wert unter 20% herauskommt
		
		
		//Rückgabe im Format "x x x x" der Belegung
		return String.valueOf(vergabe[0])+" "+String.valueOf(vergabe[1])+" "+String.valueOf(vergabe[2])+" "+String.valueOf(vergabe[3]);
	}
	
	public boolean pruefeVollständigePlazierung(ArrayList<Schiff>schiffListe) {
		boolean b = true;
		for(Schiff aktuellesSchiff: schiffListe) {
			if(!(aktuellesSchiff.getIstPlaziert())) {
				return false;
			}
		}
		return b;
	}
	
	public boolean pruefePlazierung(String zellorte, ArrayList<Schiff>schiffListe) {
		
		if(zellorte!=""&&zellorte.length()>7&&pruefeVersetztePlazierung(zellorte,schiffListe)&&pruefeZuVielPlazierung(zellorte,schiffListe)&&
				pruefeZuGroßesSchiff(zellorte,schiffListe)&&pruefeZuKleinesSchiff(zellorte,schiffListe)&&
				pruefeBeruehrungsPunkte(zellorte,schiffListe)) {
			
			return true;
		}
		return false;
	}
	
	private boolean pruefeZuKleinesSchiff(String zellorte, ArrayList<Schiff> schiffListe) {
		
		String parts[] = zellorte.split(" ");
		
		int groeße=parts.length;
		
		if(groeße<2) {
			return false;
		}
		return true;
	}

	private boolean pruefeZuGroßesSchiff(String zellorte, ArrayList<Schiff> schiffListe) {
		String parts[] = zellorte.split(" ");
		
		int groeße=parts.length;
		
		if(groeße>5) {
			return false;
		}
		return true;
	}

	private boolean pruefeBeruehrungsPunkte(String zellorte, ArrayList<Schiff> schiffListe) {
		
		//Prüfe Ausrichtung gibt True zurück wenn das SchiffQuer liegt sonst false bei Hochkant
		boolean isQuer=pruefeAusrichtungQuer(zellorte);
		
		String parts[]=zellorte.split(" ");
		Arrays.sort(parts);
		int groeße = parts.length;
		String [] umfeld=null;
		int row=Integer.parseInt(parts[0].substring(0, 2));
		int col=Integer.parseInt(parts[0].substring(2, 4));
		
		System.out.println("Plazierungsprüfung ROW "+row+" COL "+col);
		//Umfeld wird alle Felder bekommen, die neben dem Schiff liegen können, je nachdem wie groß das Schiff ist, ist das Umfeld größer
		if(groeße==5) {
			umfeld=new String[16];
			if(isQuer) {
				
				umfeld[0]=(row)+" "+(col-1);
				umfeld[1]=(row-1)+" "+(col-1);
				umfeld[2]=(row+1)+" "+(col-1);
				umfeld[3]=(row+1)+" "+(col);
				umfeld[4]=(row-1)+" "+(col);
				umfeld[5]=(row-1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col+1);
				umfeld[7]=(row-1)+" "+(col+2);
				umfeld[8]=(row+1)+" "+(col+2);
				umfeld[9]=(row-1)+" "+(col+3);
				umfeld[10]=(row+1)+" "+(col+3);
				umfeld[11]=(row-1)+" "+(col+4);
				umfeld[12]=(row+1)+" "+(col+4);
				umfeld[13]=(row-1)+" "+(col+5);
				umfeld[14]=(row+1)+" "+(col+5);
				umfeld[15]=(row)+" "+(col+5);
			}else {
				umfeld[0]=(row-1)+" "+(col);
				umfeld[1]=(row-1)+" "+(col+1);
				umfeld[2]=(row-1)+" "+(col-1);
				umfeld[3]=(row)+" "+(col+1);
				umfeld[4]=(row)+" "+(col-1);
				umfeld[5]=(row+1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col-1);
				umfeld[7]=(row+2)+" "+(col+1);
				umfeld[8]=(row+2)+" "+(col-1);
				umfeld[9]=(row+3)+" "+(col+1);
				umfeld[10]=(row+3)+" "+(col-1);
				umfeld[11]=(row+4)+" "+(col+1);
				umfeld[12]=(row+4)+" "+(col-1);
				umfeld[13]=(row+5)+" "+(col+1);
				umfeld[14]=(row+5)+" "+(col-1);
				umfeld[15]=(row+5)+" "+(col);
			}
		}else if(groeße==4) {
			umfeld=new String[14];
			if(isQuer) {
				umfeld[0]=(row)+" "+(col-1);
				umfeld[1]=(row-1)+" "+(col-1);
				umfeld[2]=(row+1)+" "+(col-1);
				umfeld[3]=(row+1)+" "+(col);
				umfeld[4]=(row-1)+" "+(col);
				umfeld[5]=(row-1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col+1);
				umfeld[7]=(row-1)+" "+(col+2);
				umfeld[8]=(row+1)+" "+(col+2);
				umfeld[9]=(row-1)+" "+(col+3);
				umfeld[10]=(row+1)+" "+(col+3);
				umfeld[11]=(row-1)+" "+(col+4);
				umfeld[12]=(row+1)+" "+(col+4);
				umfeld[13]=(row)+" "+(col+4);
			}else {
				umfeld[0]=(row-1)+" "+(col);
				umfeld[1]=(row-1)+" "+(col+1);
				umfeld[2]=(row-1)+" "+(col-1);
				umfeld[3]=(row)+" "+(col+1);
				umfeld[4]=(row)+" "+(col-1);
				umfeld[5]=(row+1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col-1);
				umfeld[7]=(row+2)+" "+(col+1);
				umfeld[8]=(row+2)+" "+(col-1);
				umfeld[9]=(row+3)+" "+(col+1);
				umfeld[10]=(row+3)+" "+(col-1);
				umfeld[11]=(row+4)+" "+(col+1);
				umfeld[12]=(row+4)+" "+(col-1);
				umfeld[13]=(row+4)+" "+(col);
			}
		}else if(groeße==3) {
			umfeld=new String[12];
			if(isQuer) {
				umfeld[0]=(row)+" "+(col-1);
				umfeld[1]=(row-1)+" "+(col-1);
				umfeld[2]=(row+1)+" "+(col-1);
				umfeld[3]=(row+1)+" "+(col);
				umfeld[4]=(row-1)+" "+(col);
				umfeld[5]=(row-1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col+1);
				umfeld[7]=(row-1)+" "+(col+2);
				umfeld[8]=(row+1)+" "+(col+2);
				umfeld[9]=(row-1)+" "+(col+3);
				umfeld[10]=(row+1)+" "+(col+3);
				umfeld[11]=(row)+" "+(col+3);
			}else {
				umfeld[0]=(row-1)+" "+(col);
				umfeld[1]=(row-1)+" "+(col-1);
				umfeld[2]=(row-1)+" "+(col+1);
				umfeld[3]=(row)+" "+(col+1);
				umfeld[4]=(row)+" "+(col-1);
				umfeld[5]=(row+1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col-1);
				umfeld[7]=(row+2)+" "+(col-1);
				umfeld[8]=(row+2)+" "+(col+1);
				umfeld[9]=(row+3)+" "+(col-1);
				umfeld[10]=(row+3)+" "+(col+1);
				umfeld[11]=(row+3)+" "+(col);
			}
		}else if(groeße==2) {
			umfeld=new String[10];
			if(isQuer) {
				umfeld[0]=(row)+" "+(col-1);
				umfeld[1]=(row-1)+" "+(col-1);
				umfeld[2]=(row+1)+" "+(col-1);
				umfeld[3]=(row+1)+" "+(col);
				umfeld[4]=(row-1)+" "+(col);
				umfeld[5]=(row-1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col+1);
				umfeld[7]=(row-1)+" "+(col+2);
				umfeld[8]=(row+1)+" "+(col+2);
				umfeld[9]=(row)+" "+(col+2);
			}else {
				umfeld[0]=(row-1)+" "+(col);
				umfeld[1]=(row-1)+" "+(col-1);
				umfeld[2]=(row-1)+" "+(col+1);
				umfeld[3]=(row)+" "+(col+1);
				umfeld[4]=(row)+" "+(col-1);
				umfeld[5]=(row+1)+" "+(col+1);
				umfeld[6]=(row+1)+" "+(col-1);
				umfeld[7]=(row+2)+" "+(col-1);
				umfeld[8]=(row+2)+" "+(col+1);
				umfeld[9]=(row+2)+" "+(col);
			}
		}
		
		for(int i = 0;i<umfeld.length;i++) {
			String partss[]=umfeld[i].split(" ");
			if (partss[0].length()==1) {
				partss[0]="0"+partss[0];
			}
			if(partss[1].length()==1) {
				partss[1]="0"+partss[1];
			}
			umfeld[i]=partss[0]+partss[1];
		}
		
		//Alle Felder Drumrum sind jetzt im String Umfeld gespeichert jetzt mit den Schiffszellorten vergleichen
		
		for(int i=0;i<schiffListe.size();i++) {
			ArrayList <String> zellorteSchiff = schiffListe.get(i).getZellorte();
			System.out.println("ZellorteSchiff: "+zellorteSchiff);
			if(zellorteSchiff!=null) {
				for(int j=0;j<zellorteSchiff.size();j++) {
					for(int h=0;h<umfeld.length;h++) {
						if(umfeld[h].equals(zellorteSchiff.get(j).replaceAll("\\s+",""))){
							return false;
						}
					}
				}	
			}
			
		}
		return true;
	}

	private boolean pruefeZuVielPlazierung(String zellorte, ArrayList<Schiff> schiffListe) {
		String parts[] = zellorte.split(" ");
		int groeße=parts.length;
		//Wenn ein Schiff in der SchiffsListe gefunden wird, dass der Größe entspricht die eingetragen werden möchte
	
		//und es noch nicht Plaziert ist liefer True sonst ist kein Platz mehr für so ein Schiff und es wird false geliefert
		for(Schiff aktuellesSchiff : schiffListe) {
			if(aktuellesSchiff.getGroeße()==groeße &&!(aktuellesSchiff.getIstPlaziert())) {
					return true;
			}
		}
	
		return false;
	}

	private boolean pruefeVersetztePlazierung(String zellorte, ArrayList<Schiff> schiffListe) {

		String parts[] = zellorte.split(" ");
		
		String row="";
		String col="";
		for(int i =0;i<parts.length;i++) {
			row=row+parts[i].substring(0,2);
			col=col+parts[i].substring(2,4);
		}
		
		int[]rowArray=new int[parts.length];
		int[]colArray=new int[parts.length];
		//row hat hier die länge 2
		for(int i = 0;i<row.length();i=i+2) {
			int temp = Integer.parseInt(row.substring(i, i+2));
			rowArray[i/2]=temp;
			temp=Integer.parseInt(col.substring(i, i+2));
			colArray[i/2]=temp;
		}
		
		Arrays.sort(rowArray);
		Arrays.sort(colArray);
		
		for(int i = 0;i<rowArray.length;i++) {
			System.out.println("RowArray: "+rowArray[i]);
			
		}
		for(int i = 0;i<rowArray.length;i++) {
			System.out.println("colArray: "+colArray[i]);	
		}
		
		
		//Prüfung auf dass das Schiff nicht in einer Reihe oder Spalte liegt, Zahl darf sich nur bei einem Ändern
		
		boolean b=false,c = false;
		//Prüfen ob Lücken im Schiff sind auf die Länge gesehen, aber nur wenn boolean true ist, heißt wenn das Schiff da
		//Aufsteigende Werte hat
		
		for(int i = 0;i<rowArray.length-1;i++) {
			int eins=rowArray[i];
			int zwei=rowArray[i+1];
			if(eins!=zwei)b=true;
			if((eins+1)!=zwei&&b) {
				return false;
			}
			
			eins=colArray[i];
			zwei=colArray[i+1];
			if(eins!=zwei)c=true;
			if((eins+1)!=zwei&&c) {
				return false;
			}
			
		}
		if(b&&c)return false;
		
		return true;
		
	}

	private boolean pruefeAusrichtungQuer(String zellorte) {
		String parts[]=zellorte.split(" ");
		int einsRow = Integer.parseInt(parts[0].substring(0, 2));
		int zweiRow = Integer.parseInt(parts[1].substring(0, 2));
		if(einsRow==zweiRow) {
			return true;
		}
		return false;
		
	}
	public int pruefeRateVersuch(String rateVersuch, ArrayList<Schiff>schiffListe) {
		
		
		if(rateVersuch.equals("SAVE")) {
			return 3;
		}
		
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
	
	public boolean [] setZellOrte(ArrayList<Schiff>schiffListe, String zellorte) {
		//Prüfen ob noch ein Schiff der Größe x möglich ist
		boolean [] b = new boolean[2];
		b[0]=false;
		b[1]=false;
		if(!this.pruefePlazierung(zellorte,schiffListe)) {
			System.out.println("Plazierung nicht möglich");
			
			return b;
		}
		b[0]=true;
		
		String[] orte =zellorte.split(" ");	
		ArrayList <String> al = new ArrayList <String>();
		for(int i = 0;i<orte.length;i++) {
			String format= orte[i].substring(0, 2)+" "+orte[i].substring(2, 4);
			
			al.add(format);
		}
		
		//Zellorte für die Schiffobjekte setzen
		switch (orte.length) {
		
			case 2:
				for(int i=0;i<schiffListe.size();i++) {
					
					boolean isTrue=schiffListe.get(i).getIdentification().startsWith("Z")&& !schiffListe.get(i).getIstPlaziert();
					System.out.println("IsTrue: "+isTrue);
					
					if(isTrue) {
						schiffListe.get(i).setZellorte(al);
						schiffListe.get(i).setIstPlaziert(true);
						break;
					}
				}
				break;
			
			case 3:
				for(int i=0;i<schiffListe.size();i++) {
					
					boolean isTrue=schiffListe.get(i).getIdentification().startsWith("D")&& !schiffListe.get(i).getIstPlaziert();
					System.out.println("IsTrue: "+isTrue);
					if(isTrue) {
						schiffListe.get(i).setZellorte(al);
						schiffListe.get(i).setIstPlaziert(true);
						break;
					}
				}
				break;
				
			case 4:
				for(int i=0;i<schiffListe.size();i++) {
					
					boolean isTrue=schiffListe.get(i).getIdentification().startsWith("V")&& !schiffListe.get(i).getIstPlaziert();
					System.out.println("IsTrue: "+isTrue);
					if(isTrue) {
						schiffListe.get(i).setZellorte(al);
						schiffListe.get(i).setIstPlaziert(true);
						break;
					}
				}
				break;
				
			case 5:
				for(int i=0;i<schiffListe.size();i++) {
					
					boolean isTrue=schiffListe.get(i).getIdentification().startsWith("F")&& !schiffListe.get(i).getIstPlaziert();
					System.out.println("IsTrue: "+isTrue);
					if(isTrue) {
						schiffListe.get(i).setZellorte(al);
						schiffListe.get(i).setIstPlaziert(true);
						break;
					}
				}
				break;
				
		}
		
		for(int i = 0;i<schiffListe.size();i++) {
			System.out.println(i+": "+schiffListe.get(i).getIdentification()+" Größe: "+
				schiffListe.get(i).getGroeße() +" Ist Plaziert: "+
				schiffListe.get(i).getIstPlaziert());
				
		}
		for(int i = 0;i<schiffListe.size();i++) {
			if(schiffListe.get(i).getZellorte()!=null) {
				for (int j=0;j<schiffListe.get(i).getZellorte().size();j++) {
					System.out.println("Zellort "+j+": "+" von Schiff "+i+"   "+schiffListe.get(i).getZellorte().get(j)+" ");
				}
		}
			
			
		}
		
		b[1]=(this.pruefeVollständigePlazierung(schiffListe));	
		return b;
	}
	
	public String[] adjust(String lastShot, String[]getroffeneZellen) {
		
		String[]selectedCells=new String[10];
		String parts[]=lastShot.split(" ");
		String row=parts[0]; //02	
		String col=parts[1]; //03
		for(int i=0;i<getroffeneZellen.length;i++) {
			if(getroffeneZellen[i]!=null) {
				if(!(getroffeneZellen[i].substring(0, 2).equals(row))&&!(getroffeneZellen[i].substring(2, 4).equals(col))) {
					selectedCells[i]=getroffeneZellen[i];
				}	
			}
			
		}
		for(int i=0;i<selectedCells.length;i++) {
			if(selectedCells[i]!=null) {
				System.out.println("Aussortierte Zelle "+selectedCells[i]);
			}
		}
		
		return selectedCells;
	}
	
	//Setter für Groeße und Laenge des Spielfelds
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
