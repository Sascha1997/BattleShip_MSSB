package ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Form extends StackPane {
    String text;

    public Form(String lable, String hint) {

        Form label = new Form(lable);
        TextField textField = new TextField();
        textField.setPromptText(hint);
        textField.setPrefHeight(40);

        textField.setOnKeyTyped(keyEvent -> {
            text = keyEvent.getText();
        });

        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(label, textField);

    }

    public Form(String text) {
        VBox form = new VBox(20);
        form.setTranslateX(360);
        form.setTranslateY(100);

        Label label = new Label(text);
        getChildren().addAll(label);
    }

    public String getText() {
        return text;
    }

}
