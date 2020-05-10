package schiffeversenken;

import javax.swing.*;
import java.awt.*;

public class Matchfield extends JPanel{

    private int size;               //Größe des Matchfields, z.b 5x5, 6x6, ....
    private int panelSize = 400;    //Größe des Panels
    private final Tile[][] arr;     //alle Felder des Matchfields

    public Matchfield(int size){

        this.size = size;
        this.arr = new Tile[size][size];

        setupField();

        setLayout(null);
        setSize(this.panelSize, this.panelSize);
        setBorder(BorderFactory.createLineBorder(Color.black));

        setTileStateRandom();
    }

    public void setupField(){   //

        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                Tile temp = new Tile();
                temp.setLocation(j * 40,i * 40);  //absoluter Standort auf dem Panel gesetzt -> Panel hat kein Layout
                add(temp);                              //Feld wird auf das Panel platziert
                this.arr[i][j] = temp;                  //Feld wird im Array gespeichert
            }
        }
    }

    public void drawField(){                            //Felder werden schwarz oder weiß angemalt
                                                        //Feld enthält Schiff -> Schwarz, hat kein Schiff -> Weiß
        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                if(this.arr[i][j].getState() == 0){
                    this.arr[i][j].setBackground(Color.WHITE);

                }else{
                    this.arr[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    public JPanel getPanel(){                           //Matchfield wird returnt
        return this;
    }

    public void setTileStateRandom(){                         //State wird random ausgewählt, in diesem Fall ist ein Schiff, ein Feld groß
                                                        //State = 1  -> hat Schiff, 0 -> hat kein Schiff
        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                int x = (int) (Math.random() * 6 + 1);
                if(x <= 3){
                    this.arr[i][j].setState(1);
                }else{
                    this.arr[i][j].setState(0);
                }
            }
        }
        drawField();
    }

    public void setField(String input){                 //Über einen String wird das Feld synchronisiert
                                                        //String enthält 0 und 1en, z.b "01110101111...."
                                                        //Wird benötigt um aus Speicherständen zu laden
        for(int i = 0; i < input.length(); i++){
            if(input.charAt(i) == '0'){
                this.arr[i / this.size][i % this.size].setState(0);
            }else{
                this.arr[i / this.size][i % this.size].setState(1);
            }
        }

        drawField();
    }

    public String toString(){                           //Matchfield wird in einen String umgewandelt und returnt
        String s = "";                                  //Wird benötigt um einen Speicherstand zu erstellen

        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                s += this.arr[i][j].getState();
            }
        }
        return s;
    }
}
