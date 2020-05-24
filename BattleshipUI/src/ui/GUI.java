package ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.Schiff;
import ui.components.Form;
import ui.components.MenuButton;


import javax.management.Notification;
import javax.naming.Name;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;


public class GUI extends Application {

    public static ArrayList<Schiff> ships = new ArrayList<Schiff>(); // Muss von Logik kommen

    // Hauptelemente GUI
    public Stage window;
    public Scene gameScene;
    public Scene welcomeScene;
    public Scene formScene;
    public GameMenu gameMenu;
    public FormServer form;
    public FormClient form1;
    public Board p1;
    public Board p2;

    public String playerName;
    public int boardSize;

    public String ip = "127.0.0.1";

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String p) {
        playerName = p;
    }

    public void setBoardSize(int b) {
        boardSize = b;
    }

    //Welcomescreen
    public Parent createWelcomeScene() throws IOException {
        Pane wS = new Pane();

        wS.setPrefSize(1080,720);

        InputStream is = Files.newInputStream(Paths.get("assets/welcome.jpeg"));
        Image bg = new Image(is);
        is.close();

        ImageView imgView = new ImageView(bg);
        imgView.setFitHeight(720);
        imgView.setFitWidth(1080);

        Label title = new Label("Schiffeversenken");

        gameMenu = new GameMenu();

        wS.getChildren().addAll(imgView, title, gameMenu);

        return wS;
    }

    // Spieleinstellungsscreen
    public Parent createParameterScene() throws IOException {
        boolean server = false;
        Pane pS = new Pane();
        pS.setPrefSize(1080,720);

        InputStream is = Files.newInputStream(Paths.get("assets/welcome.jpeg"));
        Image bg = new Image(is);
        is.close();

        ImageView imgView = new ImageView(bg);
        imgView.setFitHeight(720);
        imgView.setFitWidth(1080);

        if (server){
            form = new FormServer();
            pS.getChildren().addAll(imgView, form);
        }else {
            form1 = new FormClient();
            pS.getChildren().addAll(imgView, form1);
        }

        return pS;
    }

    //Spielscreen
    public Parent createGameScene() throws IOException {
        Pane gS = new Pane();

        ships.add(new Schiff(5, "Test1"));
        ships.add(new Schiff(4, "Test2"));
        ships.add(new Schiff(3, "Test3"));
        ships.add(new Schiff(2, "Test4"));

        InputStream is = Files.newInputStream(Paths.get("assets/welcome.jpeg"));
        Image bg = new Image(is);
        is.close();

        ImageView imgView = new ImageView(bg);
        imgView.setFitHeight(720);
        imgView.setFitWidth(1080);

        p1 = new Board(boardSize, mouseEvent -> {

            for (Schiff ship : ships) {
                Zelle cell = (Zelle) mouseEvent.getSource();
                if (mouseEvent.getButton() == MouseButton.SECONDARY){
                    p1.placeShips(ship, cell, false);
                } else {
                    p1.placeShips(ship, cell, true);
                }

                ships.remove(ship);
                if (ships.isEmpty()) {
                    System.out.println("Liste leer");
                }

            }
        });

        p2 = new Board(boardSize, mouseEvent -> {
            Zelle cell = (Zelle) mouseEvent.getSource();
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                p2.shoot(cell);
            }
        });

        HBox players = new HBox();
        players.setSpacing(200);

        VBox player1 = new VBox(20);

        VBox player2 = new VBox(20);

        Text playerName = new Text();
        playerName.setFont(new Font("Roboto", 22));
        playerName.setText(getPlayerName());
        Text enemyName = new Text("Gegner");
        enemyName.setFont(new Font("Roboto", 22));

        player2.getChildren().addAll(enemyName, p2);
        player2.setAlignment(Pos.CENTER_RIGHT);
        player1.getChildren().addAll(playerName, p1);
        player1.setAlignment(Pos.CENTER_LEFT);
        players.getChildren().addAll(player1, player2);
        players.setAlignment(Pos.CENTER);


        gS.setMinWidth(1080);
        gS.setMinHeight(720);
        gS.setPrefSize(1080, 720);

        gS.getChildren().addAll(imgView, players);
        return gS;
    }

    //GUI Startmethode
    @Override
    public void start(Stage myStage) throws Exception{
        window = myStage;

        welcomeScene = new Scene(createWelcomeScene());
        formScene = new Scene(createParameterScene());

        myStage.setTitle("Drecksspiel");
        myStage.setScene(welcomeScene);
        myStage.show();
    }

    private class GameMenu extends Parent {

        public GameMenu() {
            VBox menu = new VBox(20);
            menu.setTranslateX(360);
            menu.setTranslateY(100);

            MenuButton newGame = new MenuButton("Neues Spiel");
            newGame.setOnMouseClicked(mouseEvent -> {
                System.out.println("newGame clicked");
                window.setScene(formScene);
            });

            MenuButton saveGame = new MenuButton("Spiel speichern");
            saveGame.setOnMouseClicked(mouseEvent -> {
                System.out.println("saveGame clicked");
            });

            MenuButton loadGame = new MenuButton("Spiel laden");
            loadGame.setOnMouseClicked(mouseEvent -> {
                System.out.println("loadGame clicked");
            });

            menu.getChildren().addAll(newGame, saveGame, loadGame);

            Rectangle background = new Rectangle(1080, 720);
            background.setFill(Color.GRAY);
            background.setOpacity(0.4);

            getChildren().addAll(background, menu);

        }
    }

    public class FormServer extends Parent {
        public FormServer() {
            VBox box = new VBox(10);
            box.setTranslateX(360);
            box.setTranslateY(200);
            box.setAlignment(Pos.CENTER_LEFT);

            Label title = new Label("Spiel erstellen");
            title.setFont(new Font("Arial", 24));

            Label name = new Label("Name");
            name.setFont(new Font("Roboto", 18));
            TextField nameTextField = new TextField();
            nameTextField.setPrefHeight(30);
            nameTextField.setPromptText("Name eingeben");

            Label size = new Label("Größe");
            size.setFont(new Font("Roboto", 18));
            TextField sizeTextField = new TextField();
            size.setPrefHeight(30);
            sizeTextField.setPromptText("Spielfeldgröße eingeben");

            Label myIP = new Label("Meine IP-Adresse");
            myIP.setFont(new Font("Roboto", 18));
            Text IP = new Text(ip);
            IP.setFont(new Font("Roboto Bold", 18));

            MenuButton joinGame = new MenuButton("Spiel betreten");
            joinGame.setOnMouseClicked(mouseEvent -> {
                System.out.println("joinGame clicked");

                if (Integer.parseInt(sizeTextField.getText()) > 0 && Integer.parseInt(sizeTextField.getText()) <= 30){
                    setBoardSize(Integer.parseInt(sizeTextField.getText()));
                    System.out.println(boardSize);
                    setPlayerName(nameTextField.getText());
                    System.out.println(playerName);
                } else {
                    //Notification
                }
                try {
                    window.setScene(gameScene = new Scene(createGameScene()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            VBox.setVgrow(box, Priority.ALWAYS);
            box.getChildren().addAll(title, name, nameTextField, size, sizeTextField, myIP, IP, joinGame);

            Rectangle background = new Rectangle(400, 720);
            background.setFill(Color.WHITE);
            background.setTranslateX(340);

            background.setOpacity(0.8);

            getChildren().addAll(background, box);

        }
    }

    public class FormClient extends Parent {
        public FormClient() {
            VBox form = new VBox(20);
            form.setTranslateX(360);
            form.setTranslateY(100);

            Label title = new Label("Spiel beitreten");
            title.setFont(new Font("Roboto", 24));

            Label name = new Label("Name");
            name.setFont(new Font("Roboto", 18));
            TextField nameTextField = new TextField();
            nameTextField.setPrefHeight(30);
            nameTextField.setPromptText("Name eingeben");

            TextField enemyIPField = new TextField();
            enemyIPField.setPromptText("IP des Gegners eingeben");
            enemyIPField.setFont(new Font("Roboto", 18));

            MenuButton joinGame = new MenuButton("Spiel betreten");
            joinGame.setOnMouseClicked(mouseEvent -> {
                System.out.println("joinGame clicked");
                if (!nameTextField.getText().isEmpty()) {
                    setBoardSize(10);
                    setPlayerName(nameTextField.getText());
                } else {
                    //Notification
                }

                try {
                    window.setScene(gameScene = new Scene(createGameScene()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            form.getChildren().addAll(title,name, nameTextField, enemyIPField, joinGame);

            Rectangle background = new Rectangle(400, 720);
            background.setFill(Color.WHITE);
            background.setTranslateX(340);

            background.setOpacity(0.8);

            getChildren().addAll(background, form);

        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
