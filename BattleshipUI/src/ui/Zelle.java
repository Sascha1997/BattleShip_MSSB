package ui;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Zelle extends Rectangle {
    int x, y;
    Board b;

    public Zelle(int x, int y, Board board) {
        super((Integer)(300/board.boardsize),(Integer)(300/board.boardsize));
        setFill(Color.LIGHTBLUE);
        setStroke(Color.BLACK);
        setOpacity(0.7);
        this.x = x;
        this.y = y;
        this.b = board;

        this.setOnMouseEntered(mouseEvent -> {
            if (!getFill().equals(Color.RED)) {
                setFill(Color.GREEN);
            }
        });

        this.setOnMouseExited(mouseEvent -> {
            if (!getFill().equals(Color.RED)) {
                setFill(Color.LIGHTBLUE);
            }
        });

    }

    public void colorizeCell(Color c) {
        setFill(c);
    }

}