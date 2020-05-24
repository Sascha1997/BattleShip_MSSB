package ui.components;

import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// GUI Element MenuButton
public class MenuButton extends StackPane {
    private Text text;

    public MenuButton(String t) {
        text = new Text(t);

        Rectangle buttonBg = new Rectangle(360, 50);
        buttonBg.setOpacity(0.6);
        buttonBg.setFill(Color.BLACK);

        GaussianBlur blur = new GaussianBlur(2);
        buttonBg.setEffect(blur);

        text.setFill(Color.WHITE);

        setAlignment(Pos.CENTER);
        getChildren().addAll(buttonBg, text);

        setOnMouseEntered(mouseEvent -> {
            buttonBg.setFill(Color.WHITE);
            text.setFill(Color.BLACK);
            buttonBg.setTranslateX(10);
            text.setTranslateX(10);

        });

        setOnMouseExited(mouseEvent -> {
            buttonBg.setFill(Color.BLACK);
            text.setFill(Color.WHITE);
            buttonBg.setTranslateX(0);
            text.setTranslateX(0);
        });

    }

}