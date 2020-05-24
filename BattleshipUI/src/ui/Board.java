package ui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Schiff;

import java.util.ArrayList;

public class Board extends Parent{
    public int ships = 5;
    private Zelle cell;
    public VBox rows = new VBox();
    public int boardsize;

    public Board(int boardsize, EventHandler<? super MouseEvent> handler) { //EventHandler<? super MouseEvent> handler
        this.boardsize = boardsize;
        for (int y = 0; y < boardsize; y++) { // GRÖẞE VOM sPIELFELD ENTGEGEN NEHMEN
            HBox row = new HBox();
            //System.out.println("Y. "+y);
            for (int x = 0; x < boardsize; x++) {
                Zelle c = new Zelle( x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }


    public void placeShips(Schiff ship, Zelle cell, boolean vertical) {
        ArrayList<String> cells = new ArrayList<String>();

        try {
            if(vertical) {
                for(int i=0; i < ship.getGroeße(); i++)  {
                    cells.add(String.format("%02d", cell.y) + String.format("%02d", cell.x +i));
                    Zelle c = (Zelle) ((HBox)rows.getChildren().get(cell.y+i)).getChildren().get(cell.x);
                    c.colorizeCell(Color.RED);
                    //System.out.println(String.valueOf(x) + String.valueOf(y));
                }
                System.out.println(cells);
                ship.setZellorte(cells);
            } else {
                for (int i = 0; i < ship.getGroeße(); i++) {
                    cells.add(String.format("%02d", cell.y + i) + String.format("%02d", cell.x));
                    Zelle c = (Zelle) ((HBox)rows.getChildren().get(cell.y)).getChildren().get(cell.x+i);
                    c.colorizeCell(Color.RED);
                    //System.out.println(String.valueOf(x) + String.valueOf(y));
                }
                System.out.println(cells);
                ship.setZellorte(cells);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Schiff " + ship.getIdentifikation() + " passt hier nicht hin!");
            alert(Alert.AlertType.INFORMATION, "Schiff " + ship.getIdentifikation() + " passt hier nicht hin!");
            if(vertical) {
                for(int i=0; i < ship.getGroeße(); i++)  {
                    Zelle c = (Zelle) ((HBox)rows.getChildren().get(cell.y+i)).getChildren().get(cell.x);
                    c.colorizeCell(Color.LIGHTGRAY);
                    //System.out.println(String.valueOf(x) + String.valueOf(y));
                }
                System.out.println(cells);
            } else {
                for (int i = 0; i < ship.getGroeße(); i++) {
                    Zelle c = (Zelle) ((HBox)rows.getChildren().get(cell.y)).getChildren().get(cell.x+i);
                    c.colorizeCell(Color.LIGHTGRAY);
                    //System.out.println(String.valueOf(x) + String.valueOf(y));
                }
                System.out.println(cells);

            }
        }



        System.out.println(ship.getIdentifikation() + " at " + cell.x + " " + cell.y + vertical);
    }


    public void shoot(Zelle cell) {
        Zelle c = (Zelle) ((HBox)rows.getChildren().get(cell.y)).getChildren().get(cell.x);
        cell.colorizeCell(Color.BLUE);
    }

    private void alert(Alert.AlertType a, String message){
        Alert alert = new Alert(a);
        alert.setTitle("Warnung");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.show();
    }

}
/*
            String answer = String.format("%02d", cell.y) + String.format("%02d", cell.x);
            System.out.println(answer);
 */