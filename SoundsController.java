package application;

import java.io.File;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Controllerklasse des Fensters, das aufgeht, wenn man den Soundbutton klickt
 */

public class SoundsController extends Parent {
    private static double volume = 1;
    public Slider volumeSlider;
    public Button btnBack;
    MediaPlayer mp;
    private static boolean off = false;
    public static boolean effekte = true;

    /**
     * die Funktion getVolume() gibt den aktuellen double Wert der Lautstärke zurück
     *
     */

    public static double getVolume() {
        return volume;
    }
    /**
     * Konstruktor Sounds Controller
     */
    public SoundsController(){
        String path = new File("src/assets/bgmusic.wav").getAbsolutePath();
        Media me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();
    }
    /**
     * Über den Back Button kann man das Musikfenster verlassen und zu dem vorherigen Bildschirm zurückkehren
     * @param actionEvent
     */
    public void back(ActionEvent actionEvent) {
        Stage oldStage = (Stage)btnBack.getScene().getWindow();
        oldStage.close();
    }

    /**
     * Über die Methode changeVolume regelt man über den Schieberegler die Lautstärke der Hintergrundmusik und der Soundeffekte
     * @param mouseEvent
     */
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

    /**
     * @return
     */
    @Override
    public Node getStyleableNode() {
        return null;
    }

    /**
     * Über den On/Off Button kann man die Hintergrundmusik ein und ausschalten
     * @param event
     */
    public void onOffMusik(ActionEvent event) {
        if (off) {
            mp.play();
            off = false;
        } else {
            mp.pause();
            off = true;
        }

    }
    /**
     * Über den Effekte Button kann man die Soundeffekte ein und ausschalten
     * @param event
     */
    public void onOffEffekte(ActionEvent event) {
        if (effekte) {
            effekte = false;
        }else {
            effekte = true;
        }
    }
}
