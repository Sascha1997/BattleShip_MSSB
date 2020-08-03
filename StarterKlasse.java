package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * Starterklasse
 *
 */
public class StarterKlasse extends Application{
	
	/**
	 * Klassenattribute, die für das ganze Spiel gelten
	 */
	public static boolean server=false;
	public static Stage primaryStage;
	public static Scene music;
	
	/**
	 * Main-Methode 
	 * Erzeugen eines Directorys, wenn noch keines besteht
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args)throws IOException {
    	Files.makeDirectory();
    	launch(args);       
    }
    /**
     * Setzen der primären Stage, Aufrufen des Startfensters und setzen des Spieltitels
     * Setzen des Spielicons und Starten der Musik
     * @param primaryStage
     */
    public void start(final Stage primaryStage) throws Exception{
    	
    	StarterKlasse.primaryStage = primaryStage;
    	
    	 primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
             public void handle(WindowEvent we) {
            	 Platform.exit();
             }
         });
    
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerOrClient.fxml"));
    	ControllerServerOrClient ccos = new ControllerServerOrClient();
    	fxmlLoader.setController(ccos);
    	Parent root;

		FXMLLoader fxmlLoaderSounds = new FXMLLoader(getClass().getResource("Sounds.fxml"));
		Parent root2 = fxmlLoaderSounds.load();

		try {
			music = new Scene(root2);
			root = fxmlLoader.load();
			primaryStage.setScene(new Scene(root, 1080, 720));
			primaryStage.getIcons().add(new Image("file:Icon2.png"));
			StarterKlasse.primaryStage.setTitle("Startauswahl");
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
				
    }
	
        

}
