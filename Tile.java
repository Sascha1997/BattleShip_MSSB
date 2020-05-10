package schiffeversenken;

import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {

    private int state = 0;          //Ob dieses Feld ein Schiff enthält, 0 -> hat kein Schiff, 1 -> hat Schiff
    private final int size = 40;    //Grösse des Feldes in Pixel, muss man aber dynamisch zu dem Matchfield anpassen
                                    //ein 30x30 Matchfield mit Feldergröße 40 ist zu groß für das Fenster
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
