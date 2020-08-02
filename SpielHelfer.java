package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
/**
 * Spielhelferklasse für unterstützende Operationen
 *
 */
public class SpielHelfer {
	
	/**
	 * Kalkulieren der zu plazierenden Schiffe (Ca. 20% des Spielfelds). Bei Feldergröße 5x5 und 6x6 gibt es eine 
	 * vordefinierte Menge der zu plazierenden Schiffe. Andernfalls werden in der Whileschleife zufällig 2er -5er Schiffe
	 * ermittelt und die Verteilung anschließend als String zurückgegeben.
	 * @param groeße des Spielfelds
	 * @return zu plazierende Schiffe. 
	 * 
	 */
	public String kalkuliereSchiffe(int groeße) {
		
		
		int felder = (int) (groeße*groeße* 0.20); 
		int temp = felder;
		int [] vergabe = new int [4];
		Random random = new Random();
		
		
		if(felder == 5) {
			return "0 0 1 1";
		}
		
		if (felder ==7) {
			return "0 0 1 2";
		}
		
		
		while(felder >0) {
			
			temp = 2 + random.nextInt(4); 
			
			if(temp == 5)vergabe[0]=vergabe[0]+1;
			if(temp == 4)vergabe[1]=vergabe[1]+1;
			if(temp == 3)vergabe[2]=vergabe[2]+1;
			if(temp == 2)vergabe[3]=vergabe[3]+1;
			
			felder = felder - temp;
		}
		
		
		return String.valueOf(vergabe[0])+" "+String.valueOf(vergabe[1])+" "+String.valueOf(vergabe[2])+" "+String.valueOf(vergabe[3]);
	}
	
	/**
	 * Prüfung der vollständigen Plazierung aller Schiffe.
	 * @param schiffListe aller plazierten Schiffe
	 * @return true wenn alle plaziert wurden, false sonst
	 */
	public boolean pruefeVollständigePlazierung(ArrayList<Schiff>schiffListe) {
		boolean b = true;
		for(Schiff aktuellesSchiff: schiffListe) {
			if(!(aktuellesSchiff.getIstPlaziert())) {
				return false;
			}
		}
		return b;
	}
	
	/**
	 * Prüfung der Plazierung eines Schiffes. Es gibt verschiedene Regeln, die nicht verletzt werden dürfen und die hier
	 * in der if-Abfrage alle geprüft werden. Die einzelnen Methoden werden weiter unten erläutert. 
	 * Return true, wenn die Plazierung in Ordnung geht, sonst false.
	 * @param schiffListe aller plazierten Schiffe
	 * @param zellorte des zu plazierenden Schiffs
	 * @return true wenn Plazierung möglich, sonst false
	 */
	public boolean pruefePlazierung(String zellorte, ArrayList<Schiff>schiffListe) {
		
		if(zellorte!=""&&zellorte.length()>7&&pruefeVersetztePlazierung(zellorte,schiffListe)&&pruefeZuVielPlazierung(zellorte,schiffListe)&&
				pruefeZuGroßesSchiff(zellorte,schiffListe)&&pruefeZuKleinesSchiff(zellorte,schiffListe)&&
				pruefeBeruehrungsPunkte(zellorte,schiffListe)) {
			
			return true;
		}
		return false;
	}
	
	/**
	 * Prüfen ob Schiff zu klein ist
	 * @param schiffListe aller plazierten Schiffe
	 * @param zellorte des zu plazierenden Schiffs
	 * @return true wenn >= 2, sonst false
	 */
	private boolean pruefeZuKleinesSchiff(String zellorte, ArrayList<Schiff> schiffListe) {
		
		String parts[] = zellorte.split(" ");
		
		int groeße=parts.length;
		
		if(groeße<2) {
			return false;
		}
		return true;
	}

	/**
	 * Prüfen ob Schiff zu groß ist
	 * @param schiffListe aller plazierten Schiffe
	 * @param zellorte des zu plazierenden Schiffs
	 * @return true wenn <=5, sonst false
	 */
	private boolean pruefeZuGroßesSchiff(String zellorte, ArrayList<Schiff> schiffListe) {
		String parts[] = zellorte.split(" ");
		
		int groeße=parts.length;
		
		if(groeße>5) {
			return false;
		}
		return true;
	}

	/**
	 * Prüfe Berührungspunkt mit anderen Schiffen. Übergabeparameter ist die SchiffListe mit allen bisherigen Schiffen und die 
	 * zellorte, auf die das neue Schiff plaziert werden soll. 
	 * @param schiffListe aller plazierten Schiffe
	 * @param zellorte des zu plazierenden Schiffs
	 * return true wenn alles in Ordnung, false sonst
	 */
	private boolean pruefeBeruehrungsPunkte(String zellorte, ArrayList<Schiff> schiffListe) {
		
		
		boolean isQuer=pruefeAusrichtungQuer(zellorte);
		
		String parts[]=zellorte.split(" ");
		Arrays.sort(parts);
		int groeße = parts.length;
		String [] umfeld=null;
		int row=Integer.parseInt(parts[0].substring(0, 2));
		int col=Integer.parseInt(parts[0].substring(2, 4));
		
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
	
	/**
	 * Prüfen, ob mehr als die zulässige Gesamtzahl eines Schifftyps plaziert wurde.
	 * @param schiffListe aller plazierten Schiffe
	 * @param zellorte des zu plazierenden Schiffs
	 * @return true alles in Ordnung, sonst false
	 */
	private boolean pruefeZuVielPlazierung(String zellorte, ArrayList<Schiff> schiffListe) {
		String parts[] = zellorte.split(" ");
		int groeße=parts.length;
		
		for(Schiff aktuellesSchiff : schiffListe) {
			if(aktuellesSchiff.getGroeße()==groeße &&!(aktuellesSchiff.getIstPlaziert())) {
					return true;
			}
		}
	
		return false;
	}

	/**
	 * Prüfen, ob eine Zelle eines Schiffes versetzt und nicht in einer Linie gesetzt wurde
	 * @param schiffListe aller plazierten Schiffe
	 * @param zellorte des zu plazierenden Schiffs
	 * @return true wenn alles in Ordnung ,sonst false
	 */
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
		
	
		boolean b=false,c = false;
		
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
	/**
	 * Hilfsmethode für die Methode prüfeBerührungspuntke, um feststellen zu können, ob ein Schiff quer oder hochkant ist. 
	 * @param zellorte des zu plazierenden Schiffs
	 * @return true wenn Schiff quer, sonst false
	 */
	private boolean pruefeAusrichtungQuer(String zellorte) {
		String parts[]=zellorte.split(" ");
		int einsRow = Integer.parseInt(parts[0].substring(0, 2));
		int zweiRow = Integer.parseInt(parts[1].substring(0, 2));
		if(einsRow==zweiRow) {
			return true;
		}
		return false;
		
	}
	/**
	 * Prüfe Rateversuch des Gegners
	 * @param rateVersuch des Gegners
	 * @param schiffListe mit allen Schiffen
	 * @return 3, wenn Speicherbefehl
	 * @return 2, wenn Schiff versenkt
	 * @return 1, wenn Schiff getroffen
	 * @return 0 sonst
	 */
	public int pruefeRateVersuch(String rateVersuch, ArrayList<Schiff>schiffListe) {
		
		
		if(rateVersuch.equals("save")) {
			return 3;
		}
		
		int ergebnis = 0;
		
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
	
	/**
	 * Setzen der Zellorte eines Schiffes
	 * @param schiffListe aller plazierten Schiffe
	 * @param zellorte des zu plazierenden Schiffs
	 * @return boolean Array mit b[0] für Plazierung möglich b[1] für alle Schiffe plaziert
	 */
	public boolean [] setZellOrte(ArrayList<Schiff>schiffListe, String zellorte) {
		
		boolean [] b = new boolean[2];
		b[0]=false;
		b[1]=false;
		if(!this.pruefePlazierung(zellorte,schiffListe)) {
			return b;
		}
		b[0]=true;
		
		String[] orte =zellorte.split(" ");	
		ArrayList <String> al = new ArrayList <String>();
		for(int i = 0;i<orte.length;i++) {
			String format= orte[i].substring(0, 2)+" "+orte[i].substring(2, 4);
			
			al.add(format);
		}
		
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
		
		
		b[1]=(this.pruefeVollständigePlazierung(schiffListe));	
		return b;
	}

	/**
	 * Hilfsmethode fürs Spiel, die unterstützende Informationen für einen Autofill liefert beim Versenken eines Schiffes
	 * @param letzter Schuss der zum Versenken geführt hat
	 * @param alle getroffenen Zellen des Gegnerschiffes
	 * @return Zellen, die nicht als Rot markiert werden sollen
	 */
	public String[] adjust(String lastShot, String[]getroffeneZellen) {
		
		String[]selectedCells=new String[10];
		String parts[]=lastShot.split(" ");
		String row=parts[0]; 	
		String col=parts[1]; 
		for(int i=0;i<getroffeneZellen.length;i++) {
			if(getroffeneZellen[i]!=null) {
				if(!(getroffeneZellen[i].substring(0, 2).equals(row))&&!(getroffeneZellen[i].substring(2, 4).equals(col))) {
					selectedCells[i]=getroffeneZellen[i];
				}	
			}
			
		}
		return selectedCells;
	}
		
	
	
}
