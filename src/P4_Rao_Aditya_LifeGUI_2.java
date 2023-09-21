
/*
Name: Aditya Rao
Period: 4
Date: 4/23
Time taken: 2 hours

This was a really fun lab for me. It was nice to build off of the previous lab. 
I was easily able to do all of the required things. So I decided add do some 
extra/optional features. For my sliders, I display the value next to the label 
of the slider. For the cell size slider, I display an number and for the animation 
speed slider, I display a description of the speed in word form. Also, the user 
is able to change the animation slider with the scroll bar. It goes to the left 
if they scroll down, and it goes to the right if they scroll up. I also change 
the text of the toggle button to the opposite of the current state to make it more 
intuitive for the user. 
*/
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.swing.JFileChooser;


public class P4_Rao_Aditya_LifeGUI_2 extends Application implements GenerationListener{
	private P4_Rao_Aditya_LifeModel model;
	private BooleanGridPane gridPane;

	private Button clearBtn;
	private Button nextGenBtn;
	private ToggleButton animationButton;
	private Label curGenLabel;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem open;
	private MenuItem save;
	private Boolean[][]tempArr2D;
	private Number tickInterval;
	private Label animationSpeedLabel;
	private Label cellSizeLabel;
	private Slider speedSlider;
	private boolean timerOn;
	
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
		bottom.setAlignment(Pos.CENTER);
		clearBtn = new Button("Clear Grid");
		clearBtn.setAlignment(Pos.CENTER);
		clearBtn.setOnAction(new ButtonHandler());
		animationButton = new ToggleButton("Play Animation");
		animationButton.setAlignment(Pos.CENTER);
		animationButton.setPrefWidth(150);
		clearBtn.setPrefWidth(150);
		nextGenBtn = new Button("Next Generation");
		nextGenBtn.setOnAction(new ButtonHandler());
		nextGenBtn.setPrefWidth(150);
		nextGenBtn.setAlignment(Pos.CENTER);
		VBox vbox1 = new VBox();
		vbox1.setSpacing(10);
		vbox1.getChildren().addAll(clearBtn, nextGenBtn, animationButton);
		tickInterval = 120;
		final AnimationTimer timer = new AnimationTimer(){
			private long prevTime = 0;
			
			@Override
			public void handle(long now) {
				
				long dt = now - prevTime;
				if(dt > tickInterval.longValue()*1e7){
					
					prevTime = now;
					model.nextGeneration();
					
				}
			}
		};
		
		animationButton.setOnMousePressed(new EventHandler<MouseEvent>(){


			@Override
			public void handle(MouseEvent arg0) {
				if(!timerOn){
					timer.start();
					timerOn = true;
					animationButton.setText("Pause Animation");
				}else{
					timer.stop();
					timerOn = false;
					animationButton.setText("Play Animation");
				}
				
			}
		});
		
		clearBtn.setOnMousePressed(new EventHandler<MouseEvent>(){


			@Override
			public void handle(MouseEvent arg0) {
				if(!timerOn){
					
					model.setGeneration(0);
					for(int row = 0; row<=model.getNumRows()-1; row++){
						for(int col = 0; col<=model.getNumCols()-1; col++){
							model.setValueAt(row, col, false);
						}
					}
					
					timer.stop();
					timerOn = false;
				
					animationButton.setText("Play Animation");
				}else{
					model.setGeneration(0);
					for(int row = 0; row<=model.getNumRows()-1; row++){
						for(int col = 0; col<=model.getNumCols()-1; col++){
							model.setValueAt(row, col, false);
						}
					}
					timer.stop();
					timerOn = false;
					animationButton.fire();
					animationButton.setText("Play Animation");
				}
				
			}
		});
		
		bottom.setSpacing(20);
		
		
		animationSpeedLabel = new Label("Animation Speed: Medium");
		animationSpeedLabel.setAlignment(Pos.CENTER);
		animationSpeedLabel.setPrefWidth(260);
		
		
		cellSizeLabel = new Label("Cell Size: 50");
		cellSizeLabel.setAlignment(Pos.CENTER);
		cellSizeLabel.setPrefWidth(260);
		
		Label blankLabel = new Label(" ");
		cellSizeLabel.setAlignment(Pos.CENTER);
		cellSizeLabel.setPrefWidth(260);
		
		
		
		curGenLabel = new Label("\n\nGeneration: " + 0);
		curGenLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		curGenLabel.setAlignment(Pos.CENTER);
		curGenLabel.setPrefWidth(260);
		
		Slider sizeSlide = new Slider();
		sizeSlide.setMin(0);
		sizeSlide.setMax(100);
		sizeSlide.setShowTickLabels(true);
		sizeSlide.setShowTickMarks(true);
		sizeSlide.setMajorTickUnit(10);
		sizeSlide.setMinorTickCount(5);
		sizeSlide.setValue(sizeSlide.getMax()/2);
		sizeSlide.setPrefWidth(180);
		sizeSlide.valueProperty().addListener(new SizeSliderListener());
		
		speedSlider = new Slider();
		speedSlider.setMin(20);
		speedSlider.setMax(180);
		speedSlider.setShowTickLabels(false);
		speedSlider.setShowTickMarks(false);
		speedSlider.setMajorTickUnit(1);
		speedSlider.setRotate(-180);
		speedSlider.setValue(90);
		speedSlider.setPrefWidth(180);
		speedSlider.valueProperty().addListener(new SpeedSliderListener());
		
		VBox vbox2 = new VBox();
		vbox2.getChildren().addAll(animationSpeedLabel ,speedSlider, blankLabel, cellSizeLabel, sizeSlide);
		bottom.getChildren().addAll(vbox1, curGenLabel, vbox2);
		top.getChildren().add(menuBar);
		bottom.setPadding(new Insets(15, 12, 0, 12));
		gridPane = new BooleanGridPane();
		model = new P4_Rao_Aditya_LifeModel(new Boolean[][]{
				{false, false, false, false, false},
				{false, false, true, false, false},
				{false, false, true, false, false},
				{false, false, true, false, false},
				{false, false, false, false, false},
		});
		
		gridPane.setTileSize(50);
		gridPane.setOnMouseClicked(new GridMouseHandler());
		gridPane.setOnMouseDragged(new GridMouseHandler());
		gridPane.setModel(model);
		
		
		StackPane center = new StackPane();
		root.setCenter(center);
		model.addGenerationListener(this);
		center.getChildren().addAll(gridPane);
		final Scene scene = new Scene (root, 725, 725);
		stage.setScene(scene);
		scene.setOnScroll(new EventHandler<ScrollEvent>(){

			@Override
			public void handle(ScrollEvent event) {
				speedSlider.setValue(speedSlider.getValue() + -event.getDeltaY()/40);	
				
			}
		});
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
	
	
	private class SizeSliderListener implements ChangeListener<Number>{

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			gridPane.setTileSize((Double) newValue);
			cellSizeLabel.setText("Cell size: " + newValue.intValue());
		}
	}
	
	private class SpeedSliderListener implements ChangeListener<Number>{

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			tickInterval = newValue;
			int x = 180-newValue.intValue();
			String y = "";
			if(x<52) y = "Very slow";
			else if(x<84) y = "Slow";
			else if(x<116) y = "Medium";
			else if(x<148) y = "Fast";
			else if(x<180)y = "Very fast";
			animationSpeedLabel.setText("Animation Speed: " + y);
			
		}
	}
	
	private class ButtonHandler implements EventHandler<ActionEvent> {
		
		@Override
		public void handle(ActionEvent event) {
			if(event.getSource() == animationButton){
				model.nextGeneration();
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
		
		curGenLabel.setText("\n\nGeneration: " + newVal);
		
	}
}