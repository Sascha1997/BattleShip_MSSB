package application;

import java.awt.Point;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import ki.KI;

public class ControllerKI implements Initializable{
	
	private int spielFeldGroeﬂe;
	
	@FXML
	private GridPane gridPaneWe;
	@FXML
	private GridPane gridPaneEnemy;
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(StarterKlasse.server) {
			
		}else {
			
		}
		this.spielFeldGenerieren();
	}
	public ControllerKI(int spielFeldGroeﬂe) throws UnknownHostException, IOException {
		this.spielFeldGroeﬂe = spielFeldGroeﬂe;
		KI ki = new KI(new Socket("localhost",50000),false,2,this);
		ki.start();
	}
	public ControllerKI(int spielFeldGroeﬂe,boolean server) throws IOException {
		this.spielFeldGroeﬂe = spielFeldGroeﬂe;
		KI ki = new KI(new ServerSocket(50000).accept(),true,1,this,"setup 60 100 80 60 40");
		ki.start();
	}
	
	public void plaziereSchiffe(ArrayList<Schiff>schiffe) {
		
		for(int i = 0; i<schiffe.size();i++) {
			ArrayList<Point> zellorte = schiffe.get(i).getCords();
			
			for(Node n : gridPaneWe.getChildren()){
				
				Integer rowIndex = GridPane.getRowIndex(n);
				Integer colIndex = GridPane.getColumnIndex(n);
				if(rowIndex!=null&&colIndex!=null) {
					for(int j=0;j<zellorte.size();j++) {
						int rs =(zellorte.get(j).x);
						int cs = (zellorte.get(j).y);
						if(rs==rowIndex.intValue()&&cs==colIndex.intValue()){
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #000000");
						}
					}						
				}
				
				
				
			}
		}
		
		//Sobald die KI Schiffe plaziert hat kann die Information geholt werden und es geht hier
		//weiter und sie werden auf der GUI auch plaziert
	}
	public void markiereSchussVorbei(Point p) {
		for(Node n : gridPaneEnemy.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if(p.x==rowIndex.intValue()&&p.y==colIndex.intValue()) {
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #FF0000");
					break;
				}
			}
			
		}
	}
	public void markiereSchussTreffer(Point p) {
		for(Node n : gridPaneEnemy.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if(p.x==rowIndex.intValue()&&p.y==colIndex.intValue()) {
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #00FF00");
					break;
				}
			}
		}
	}
	
	public void autoFill(ArrayList<Point>points) {
		
		for(int i=0;i<points.size();i++) {
					
					for(Node n: gridPaneEnemy.getChildren()) {
						
						Integer rowIndex = GridPane.getRowIndex(n);
						Integer columnIndex = GridPane.getColumnIndex(n);
						
						
						int rowInt = points.get(i).x+1;
						int colInt = points.get(i).y;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x-1;
						colInt = points.get(i).y;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x;
						colInt = points.get(i).y;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x+1;
						colInt = points.get(i).y+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x;
						colInt = points.get(i).y+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x-1;
						colInt = points.get(i).y+1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x-1;
						colInt = points.get(i).y-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {;
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						rowInt = points.get(i).x+1;
						colInt = points.get(i).y-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						rowInt = points.get(i).x;
						colInt = points.get(i).y-1;
						
						if (rowIndex != null && rowIndex.intValue() == rowInt
								  && columnIndex != null && columnIndex.intValue() == colInt) {
							Button b = (Button)n;
							b.setStyle("-fx-background-color: #FF0000");
						}
						
						
					}
				
			
		}
		
		for(int i=0;i<points.size();i++) {
			for(Node n: gridPaneEnemy.getChildren()) {
				
					Integer rowIndex = GridPane.getRowIndex(n);
					Integer columnIndex = GridPane.getColumnIndex(n);
					
					int rowInt = points.get(i).x;
					int colInt = points.get(i).y;
					if (rowIndex != null && rowIndex.intValue() == rowInt
							  && columnIndex != null && columnIndex.intValue() == colInt) {
						Button b = (Button)n;
						b.setStyle("-fx-background-color: #00FF00");
					}
				
				
			}
		}
	}
	
	public void gegnerSpielFeldWiederherstellen(int [][] spielfeld) {
		
		for(Node n : gridPaneEnemy.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==3){
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #00FF00");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==2){
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #00FF00");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==1){
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #FF0000");
				}
					
			}
			
		}
	}
	public void unserSpielFeldWiederherstellen(int[][]spielfeld) {
		for(Node n : gridPaneWe.getChildren()){
			
			Integer rowIndex = GridPane.getRowIndex(n);
			Integer colIndex = GridPane.getColumnIndex(n);
			if(rowIndex!=null&&colIndex!=null) {
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==3){
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #000000");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==2){
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #8A4B08");
				}
				if (spielfeld[rowIndex.intValue()][colIndex.intValue()]==1){
					Button b = (Button)n;
					b.setStyle("-fx-background-color: #0B4C5F");
				}
				
			}
			
		}
	}
	
	@FXML
	private void setZellorte() {
		
	}
	@FXML
	private void schuss() {
		
	}
	@FXML
	private void saveGameProcess() {
		
	}
	@FXML
	private void spielAufgeben() {
		
	}
	@FXML
	private void spielStarten() {
		
	}

	private void spielFeldGenerieren() {
		/*
		 * 
		 * UNSER SPIELFELD
		 * 
		 */
		//Nur wegen Szene Builder hier Erst Vorhandenes 1x1 Lˆschen dann 10x10 erzeugen
		gridPaneWe.getColumnConstraints().remove(0);
		gridPaneWe.getRowConstraints().remove(0);
		
		for(int i = 0;i<this.spielFeldGroeﬂe;i++) {
			ColumnConstraints cc = new ColumnConstraints((int)380/spielFeldGroeﬂe);
			RowConstraints rc = new RowConstraints((int)380/spielFeldGroeﬂe);
			
			gridPaneWe.getColumnConstraints().add(cc);
			gridPaneWe.getRowConstraints().add(rc);
		}
		
		gridPaneWe.setAlignment(Pos.CENTER);
		gridPaneWe.setGridLinesVisible(true);
		
		
		//Erzeugt 100 verschiedene Buttons ins GridPane, die alle Clickable sind und wenn man Sie anklickt Ihren Index zur¸ck geben
		for(int i=0;i<spielFeldGroeﬂe;i++) {
			for (int j = 0; j<spielFeldGroeﬂe;j++) {
				
				Button b = new Button();
				b.setMinHeight((int)365/spielFeldGroeﬂe);
				b.setMinWidth((int)365/spielFeldGroeﬂe);
				gridPaneWe.add(b, i, j);
			}
		}
		/* UNSER SPIELFELD ENDE
		 * 
		 * GEGNER SPIELFELD
		 * 
		 */
		
		gridPaneEnemy.getColumnConstraints().remove(0);
		gridPaneEnemy.getRowConstraints().remove(0);
		
		for(int i = 0;i<this.spielFeldGroeﬂe;i++) {
			ColumnConstraints cc = new ColumnConstraints((int)380/spielFeldGroeﬂe);
			RowConstraints rc = new RowConstraints((int)380/spielFeldGroeﬂe);
			
			gridPaneEnemy.getColumnConstraints().add(cc);
			gridPaneEnemy.getRowConstraints().add(rc);
		}
		gridPaneEnemy.setAlignment(Pos.CENTER);
		gridPaneEnemy.setGridLinesVisible(true);
		
		for(int i=0;i<spielFeldGroeﬂe;i++) {
			for (int j = 0; j<spielFeldGroeﬂe;j++) {
				
				Button b = new Button();
				b.setMinHeight((int)365/spielFeldGroeﬂe);
				b.setMinWidth((int)365/spielFeldGroeﬂe);
				gridPaneEnemy.add(b, i, j);
			}
		}
		
		/*
		 * 
		 * 
		 * 
		 * GEGNERSPIELFELD ENDE
		 * 
		 */
	}
}
