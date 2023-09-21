
/*
Name: Aditya Rao
Period: 4
Date: 4/10
Time taken: 1 hour

This was a really fun lab for me. At first, I was a little confused on how all 
the classes and methods work. But once I looked through the slides, youtube 
videos, and classes/methods themselves, I understood everything. It also took 
a while to test, as when I found an issue, I had to go back to the code, 
fix it, and run the program again. I made the default grid a 5 by 5, but 
this can be changed by editing the file, and reading the new dimensions 
and grid values from it. I am excited to see what we will add to 
this project in the future, especially the animation!
*/
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.swing.JFileChooser;


public class P4_Rao_Aditya_LifeGUI_1 extends Application implements GenerationListener{
	private P4_Rao_Aditya_LifeModel model;
	private BooleanGridPane gridPane;

	Button clearBtn;
	Button nextGenBtn;
	Label curGenLabel;
	MenuBar menuBar;
	Menu menu;
	MenuItem open;
	MenuItem save;
	Boolean[][]tempArr2D;
	public static void main(String[] args) {
		
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		BorderPane root = new BorderPane();
		HBox top = new HBox();
		root.setTop(top);
		menuBar = new MenuBar();
		menu = new Menu("File");
		open = new MenuItem("Open");
		save = new MenuItem("Save");
		menu.getItems().add(open);
		menu.getItems().add(save);
		menuBar.getMenus().add(menu);
		open.setOnAction(new MenuAction());
		save.setOnAction(new MenuAction());
		
		
		HBox bottom = new HBox();
		root.setBottom(bottom);
		
		clearBtn = new Button("Clear");
		clearBtn.setOnAction(new ButtonHandler());
		nextGenBtn = new Button("Next Generation");
		nextGenBtn.setOnAction(new ButtonHandler());
		bottom.setSpacing(20);
		Slider slider = new Slider();
		Label cellSizeLabel = new Label("Cell size:");
		cellSizeLabel.setAlignment(Pos.CENTER);
		cellSizeLabel.setPrefWidth(260);
		
		curGenLabel = new Label("Generation\n\t" + 0);
		curGenLabel.setFont(Font.font(null, FontWeight.BOLD, 12));
		curGenLabel.setAlignment(Pos.CENTER);
		curGenLabel.setPrefWidth(260);
		
		slider.setMin(0);
		slider.setMax(100);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(10);
		slider.setMinorTickCount(5);
		slider.setValue(slider.getMax()/2);
		slider.setPrefWidth(180);
		
		slider.valueProperty().addListener(new SliderListener());
		VBox vbox = new VBox();
		vbox.getChildren().addAll(cellSizeLabel, slider);
		bottom.getChildren().addAll(clearBtn, nextGenBtn, curGenLabel, vbox);
		top.getChildren().add(menuBar);
		gridPane = new BooleanGridPane();
		model = new P4_Rao_Aditya_LifeModel(new Boolean[][]{
				{false, false, false, false, false},
				{false, false, true, false, false},
				{false, false, true, false, false},
				{false, false, true, false, false},
				{false, false, false, false, false},
		});
		
		gridPane.setOnMouseClicked(new GridMouseHandler());
		gridPane.setOnMouseDragged(new GridMouseHandler());
		gridPane.setModel(model);
		
		StackPane center = new StackPane();
		root.setCenter(center);
		model.addGenerationListener(this);
		center.getChildren().addAll(gridPane);
		Scene scene = new Scene (root, 750, 750);
		stage.setScene(scene);
		stage.show();
	}
	
	private class GridMouseHandler implements EventHandler<MouseEvent>{

		@Override
		public void handle(MouseEvent e) {	
			if(e.getEventType().equals(MouseEvent.MOUSE_DRAGGED)||e.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
				int y = gridPane.rowForYPos(e.getY());
				int x = gridPane.colForXPos(e.getX());
				if((y>=0 && y<model.getNumRows()) && (x>=0 && x< model.getNumCols())){
					if(e.getButton() == MouseButton.PRIMARY){
						 model.setValueAt(y, x, true);
					 }else if(e.getButton() == MouseButton.SECONDARY){
						 model.setValueAt(y, x, false);
					 }
				}
			}
		}
	
	}
	
	private class SliderListener implements ChangeListener<Number>{

		@Override
		public void changed(ObservableValue<? extends Number> observable,
				Number oldValue, Number newValue) {
			gridPane.setTileSize((Double) newValue);
		}
	}
	
	private class ButtonHandler implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent event) {
			if (event.getSource() == clearBtn) {
				model.setGeneration(0);
				for(int row = 0; row<=model.getNumRows()-1; row++){
					for(int col = 0; col<=model.getNumCols()-1; col++){
						model.setValueAt(row, col, false);
					}
				}
			}else if(event.getSource() == nextGenBtn){
				model.nextGeneration();
			}
		}
	}
	
private class MenuAction implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent event) {
			if(event.getSource()==open){
				model.setGeneration(0);
				JFileChooser chooser = new JFileChooser(".");
		        chooser.showOpenDialog(null);
		        File file = chooser.getSelectedFile();
				try {
					Scanner sc = new Scanner(file);
					tempArr2D = new Boolean[sc.nextInt()][sc.nextInt()];
					ArrayList<Boolean> tempArr1D = new ArrayList<Boolean>();
					while(sc.hasNext()){
						if(sc.next().toUpperCase().equals("X")) tempArr1D.add(true);
						else tempArr1D.add(false);
					}
					int i = 0;
					for(int row = 0; row<=tempArr2D.length-1; row++){
						for(int col = 0; col<=tempArr2D[row].length-1; col++){
							tempArr2D[row][col] = tempArr1D.get(i++);
						}
					}
					model.setGrid(tempArr2D);
					sc.close();
				} catch (Exception e) {System.out.println("ERROR: Input format of chosen file");}
		        
			}else if(event.getSource() == save){
				JFileChooser chooser = new JFileChooser(".");
		        chooser.showSaveDialog(null);
		        File file = chooser.getSelectedFile();
		        try {
					FileWriter writer = new FileWriter(file);
					writer.write(model.getNumCols() + " " + model.getNumRows()+"\n");
					for (int i = 0; i < model.getNumRows(); i++) {
						for (int j = 0; j < model.getNumCols(); j++) {
							if(model.getValueAt(i, j) == true) writer.write("X ");
							else writer.write("O ");
						}
						writer.write("\n");
					}
					writer.close();
		        } catch (Exception e) {}
				
			}
			
		}
	}

	@Override
	public void generationChanged(int oldVal, int newVal) {
		
		curGenLabel.setText("Generation\n\t" + newVal);
		
	}
}
