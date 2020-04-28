# Spezifikation GUI BattleShip

In diesem Dokument werden die grundlegenden Eigenschaften der Benutzeroberfläche des Spiels BattleShip festgeschrieben. Außerdem werden die Designelemente und genutzte Software festgelegt.

## Überblick

Die GUI besteht aus den Fenstern: 

1. WeclomeScreen
2. GameScreen

und den Modals:

1. CreateGame
2. ResultScreen

| Screens          | Beschreibung                                                 | leer |
| ---------------- | :----------------------------------------------------------- | ---- |
| WelcomeScreen    | Wird beim Start der GUI angezeigt. <br />Zeigt: <br />- Bild eines Kriegsschiffes mit Begrüßungstext |      |
| GameScreen       | Zeigt:<br />- Überschrift<br />- Info Textfeld<br />- Spielernamen<br />- Spielfelder<br />- Anzahl Schiffe<br />- Feuern Button<br />- Spiel starten Button (Erst wenn alle Schiffe gesetzt sind) |      |
| CreateGameScreen | Zeigt: <br />- Name Textfeld<br />- Feldgröße Textfeld<br />- Gegner wählen Dropdown<br />- Spiel betreten Button (Erst wenn alle Felder ausgefüllt sind) |      |
| ResultScreen     | Zeigt:<br />- "Gewonnen" --> Im Fall eines Sieges<br />- "Verloren" --> Im Fall eine Verloren zu haben |      |
| **Menü**         | **Beschreibung**                                             |      |
| Spiel erstellen  | CreateGameScreen wird aufgerufen                             |      |
| Spiel laden      | GameScreen wird aufgerufen                                   |      |
| Spiel speichern  | --> Spiel wird gespeichert                                   |      |
|                  |                                                              |      |

### Welcome Screen

![](/home/sascha/Dokumente/02_Studium/HSAalen/Semester3/05_Programmierprojekt/BattleShip_GUI/assets/WelcomeScreen.png)

### Game Screen

![](/home/sascha/Dokumente/02_Studium/HSAalen/Semester3/05_Programmierprojekt/BattleShip_GUI/assets/GameScreen.png)

### CreateScreen

![](/home/sascha/Dokumente/02_Studium/HSAalen/Semester3/05_Programmierprojekt/BattleShip_GUI/assets/CreateGameScreen.png)

### ResultScreen

![](/home/sascha/Dokumente/02_Studium/HSAalen/Semester3/05_Programmierprojekt/BattleShip_GUI/assets/ResultScreen.png)

## Schnittstellen

