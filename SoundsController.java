package application;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SoundsController extends Parent {
    private static double volume = 1;
    public Slider volumeSlider;
    public Button btnBack;
    MediaPlayer mp;
    private static boolean off = false;
    public static boolean effekte = true;

    public static double getVolume() {
        return volume;
    }

    public SoundsController(){
        String path = new File("src/assets/bgmusic.wav").getAbsolutePath();
        Media me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();
    }

    public void back(ActionEvent actionEvent) {
        Stage oldStage = (Stage)btnBack.getScene().getWindow();
        oldStage.close();
    }

    public void changeVolume(MouseEvent mouseEvent) {
        volumeSlider.setValue(mp.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp.setVolume(volumeSlider.getValue() / 100);
                volume = volumeSlider.getValue() / 100;
            }
        });
    }

    @Override
    public Node getStyleableNode() {
        return null;
    }

    public void onOffMusik(ActionEvent event) {
        if (off) {
            mp.play();
            off = false;
        } else {
            mp.pause();
            off = true;
        }

    }

    public void onOffEffekte(ActionEvent event) {
        if (effekte) {
            effekte = false;
        }else {
            effekte = true;
        }
    }
}
