package schiffeversenken;

import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {

    private int state = 0;          //Ob dieses Feld ein Schiff enth�lt, 0 -> hat kein Schiff, 1 -> hat Schiff
    private final int size = 40;    //Gr�sse des Feldes in Pixel, muss man aber dynamisch zu dem Matchfield anpassen
                                    //ein 30x30 Matchfield mit Feldergr��e 40 ist zu gro� f�r das Fenster
    public Tile(){

        this.setSize(40,40);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return this.state;
    }
}
