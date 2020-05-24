package ui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;

public class Connect implements EventHandler {
    Zelle cell;

    public Connect(Zelle c){
        cell = c;
    }

    @Override
    public void handle(Event event) {
        System.out.println("Penis");
    }

}
