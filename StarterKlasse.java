package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StarterKlasse extends Application {
	
	public static boolean server=false;
	public static Stage primaryStage;
	
	
	//Ermitteln Ob Server oder nicht anhand der Args Länge, danach GUI Starten
    public static void main(String[] args)throws IOException {
    	Files.makeDirectory();
    	launch(args);       
    }
    //
    //FXML Laden und auf die Stage hauen
    public void start(final Stage primaryStage) throws Exception{
    	
    	StarterKlasse.primaryStage = primaryStage;
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerOrClient.fxml"));
    	ControllerServerOrClient ccos = new ControllerServerOrClient();
    	fxmlLoader.setController(ccos);
    	Parent root;
		try {
			root = fxmlLoader.load();
			primaryStage.setTitle("Joinauswahl");
			primaryStage.setScene(new Scene(root, 600, 400));
			primaryStage.show();
			
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
				
    }
        

}
