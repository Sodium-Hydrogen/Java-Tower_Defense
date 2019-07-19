package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

	This is the main running class for the program

	-- Main Loop --
	public static void main(String[] args)

	public void start(Stage stage) -- This is called from the main loop
	public void splashScreen() -- This loads the splash screen
	public void loadGame(int selectedLevel) -- This loads the main game screen from the selected level
*/

import javafx.stage.WindowEvent;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class GameDriver extends Application{

	private Stage primaryStage;
	private Level [] map;

	@Override
	public void start(Stage stage){
		try{
			primaryStage = stage;
			primaryStage.setTitle("Pac-Man Tower Defense");
			primaryStage.show();
			splashScreen();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// This builds the splash screen scene to be displayed
	public void splashScreen() {

		Pane root = new Pane();
		root.setId("border-on-pane");
		Scene scene = new Scene(root,300,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		ImageView logo = new ImageView();
		logo.setImage(new Image(new File("resources/sprites/logo.png").toURI().toString()));
		logo.setFitWidth(250);
		logo.setPreserveRatio(true);

		ComboBox<String> levelSelect = new ComboBox<String>();
		levelSelect.setPromptText("Select Level");
		try {
			File [] availableLevels = (new File("resources/levels/")).listFiles();
			map = new Level[availableLevels.length];
			levelSelect.getItems().addAll(new String[availableLevels.length]);
			for(int i = 0; i < availableLevels.length; i++) {
				map[i] = new Level(availableLevels[i]);
				levelSelect.getItems().set(map[i].getIndex(), map[i].getName());
			}
		}catch(Exception e){e.printStackTrace();}

		Button startButton = new Button();
		startButton.setText("START");
		startButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				if(levelSelect.getValue() != null) {
					String name = levelSelect.getValue();
					for(int i = 0; i < map.length; i++){
						if(map[i].getName() == name){
							loadGame(i);
						}
					}
				}
			}
		});
		GridPane sprites = new GridPane();
		String [] spriteList = {"pacman2.png", "blinky3.png", "inky3.png", "pinky3.png", "clyde3.png"};
		int [] rot = {180, 0, 0, 0, 0};
		for(int i = 0; i < spriteList.length; i++) {
			String sp = spriteList[i];
			Image stmp = new Image(new File("resources/sprites/" + sp).toURI().toString());
			ImageView tmp = new ImageView(stmp);
			tmp.setFitWidth(25);
			tmp.setPreserveRatio(true);
			tmp.setRotate(rot[i]);
			sprites.add(tmp, i, 0);
		}

		root.getChildren().add(logo);
		root.getChildren().add(startButton);
		root.getChildren().add(levelSelect);
		root.getChildren().add(sprites);

		primaryStage.setScene(scene);

		double center = scene.getWidth()/2;
		logo.setX(center - logo.getFitWidth()/2);
		logo.setY(10);
		levelSelect.setLayoutX(center - levelSelect.getWidth()/2);
		levelSelect.setLayoutY(200);
		startButton.setLayoutX(center - startButton.getWidth()/2);
		startButton.setLayoutY(250);
		sprites.setLayoutX(center - sprites.getWidth()/2);
		sprites.setLayoutY(350);
	}

	public void loadGame(int selectedLevel) {
		Level map = this.map[selectedLevel];
		Tower [] selectedTower = new Tower[1];
		selectedTower[0] = null;

		Pane root = new Pane();

		Pane control = new Pane();
		control.setId("border-on-pane");
		int paneWidth = 200;
		control.setMaxWidth(paneWidth);
		control.setMinWidth(paneWidth);

		GridPane lifeCounter = new GridPane();
		lifeCounter.setLayoutX(10);
		lifeCounter.setLayoutY(10);
		Image lifeLogo = new Image(new File("resources/sprites/pacman2.png").toURI().toString());
		for(int i = 0; i < map.getStartLives(); i++){
			ImageView lifeToken = new ImageView();
			lifeToken.setImage(lifeLogo);
			lifeToken.setFitHeight(10);
			lifeToken.setPreserveRatio(true);
			lifeCounter.add(lifeToken, i, 0);
		}

		GridPane money = new GridPane();
		Label moneyCounter = new Label(String.valueOf(map.getStartMoney()));
		money.add(new Label("Money: $"), 0, 0);
		money.add(moneyCounter , 1, 0);
		money.setLayoutX(10);
		money.setLayoutY(30);

		GridPane wave = new GridPane();
		Label waveCounter = new Label("1");
		wave.add(new Label("Wave: "), 0, 0);
		wave.add(waveCounter, 1, 0);
		wave.add(new Label(" of " + String.valueOf(map.getLevelCount())), 2, 0);
		wave.setLayoutX(10);
		wave.setLayoutY(50);

		Button ctrl = new Button("START");
		ctrl.setId("white-text");
		ctrl.setLayoutX(10);
		ctrl.setLayoutY(80);

		BulletPane bulletLayer = new BulletPane(map);

		int towerSize = (int)(map.getTileSize()*.8);
		GridPane towers = new GridPane();
		Tower [][] availableTowers = {
			{new Tower("pacman", towerSize, 4, 100, 100, 800, Tower.towerType.SINGLE, moneyCounter, bulletLayer),
			new Tower("powerPellet", towerSize, 4, 75, 75, 1500, Tower.towerType.RANGE, moneyCounter, bulletLayer)}
		};
		for(int y = 0; y < availableTowers.length; y++){
			for(int x = 0; x < availableTowers[y].length; x++){
				int currentx = x;
				int currenty = y;
				Button towerButton = new Button();
				towerButton.setId("tower-button");
				towerButton.setGraphic(availableTowers[currenty][currentx].getImageView());
				towerButton.setText("$" + String.valueOf(availableTowers[currenty][currentx].getCost()));
				towerButton.setContentDisplay(ContentDisplay.TOP);
				towerButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent e){
						if(availableTowers[currenty][currentx].isPurchaseable()) {
							Image img = ((ImageView)towerButton.getGraphic()).getImage();
							root.setCursor(new ImageCursor(img, img.getWidth()/2., img.getHeight()/2.));
							selectedTower[0] = availableTowers[currenty][currentx];
						}
					}
				});
				towers.add(towerButton, x, y);
			}
		}

		towers.setLayoutY(200);

		control.getChildren().add(lifeCounter);
		control.getChildren().add(money);
		control.getChildren().add(wave);
		control.getChildren().add(ctrl);
		control.getChildren().add(towers);

		Pane mapContainer = new Pane();
		map.autosize();

		Pane spriteLayer = new Pane();
		EnemyPane enemyLayer = new EnemyPane(moneyCounter, lifeCounter);
		enemyLayer.setPrefWidth(map.getWidth());
		enemyLayer.setPrefHeight(map.getHeight());
		spriteLayer.getChildren().add(enemyLayer);
		bulletLayer.setPrefWidth(map.getWidth());
		bulletLayer.setPrefHeight(map.getHeight());
		spriteLayer.getChildren().add(bulletLayer);
		TowerPane towerLayer = new TowerPane();
		towerLayer.setPrefWidth(map.getWidth());
		towerLayer.setPrefHeight(map.getHeight());
		spriteLayer.getChildren().add(towerLayer);
		Circle radius = new Circle();
		radius.setFill(Color.DARKGREY);
		GameManager gm = new GameManager(map, enemyLayer, towerLayer, bulletLayer, lifeCounter, moneyCounter, waveCounter);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent e){
				gm.killThread();
			}
		});

		mapContainer.setLayoutX(paneWidth);
		mapContainer.setMaxWidth(map.getWidth());
		mapContainer.setMaxHeight(map.getHeight());
		mapContainer.getChildren().add(map);
		mapContainer.getChildren().add(radius);
		mapContainer.getChildren().add(spriteLayer);

		control.setMinHeight(map.getHeight());
		control.setMaxHeight(map.getHeight());

		root.getChildren().add(mapContainer);
		root.getChildren().add(control);

		Scene scene = new Scene(root, map.getWidth()+paneWidth,map.getHeight());
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);

		ctrl.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				//System.out.println(gm.levelInProgress());
				if(gm.levelInProgress()){
					gm.pause();
					if(ctrl.getText() == "PAUSE"){
						ctrl.setText("START");
					}else{
						ctrl.setText("PAUSE");
					}
				}else{
					gm.prepareLevel(ctrl);
					ctrl.setText("PAUSE");
					if(!gm.isAlive()){
						gm.start();
					}
				}
			}
		});

		control.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				root.setCursor(Cursor.DEFAULT);
				control.getChildren();
				selectedTower[0] = null;
			}
		});

		mapContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(selectedTower[0] != null && towerLayer.isValidSpot((int)e.getX(), (int)e.getY(), map)) {
					Tower tmp = (Tower)selectedTower[0].copyTower();
					tmp.setX(e.getX());
					tmp.setY(e.getY());
					tmp.purchaseTower();
					towerLayer.addTower(tmp);
					selectedTower[0] = null;
					root.setCursor(Cursor.DEFAULT);
					radius.setOpacity(0);
					radius.setRadius(0);
				}
			}
		});

		mapContainer.setOnMouseMoved(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				if(selectedTower[0] != null){
					radius.setCenterX(e.getX());
					radius.setCenterY(e.getY());
					radius.setRadius(selectedTower[0].getRange());
					radius.setOpacity(.3);
					if(!towerLayer.isValidSpot((int)e.getX(), (int)e.getY(), map)){
						radius.setFill(Color.RED);
					}else{
						radius.setFill(Color.DARKGREY);
					}
				}else{
					radius.setOpacity(0);
					radius.setRadius(0);
				}
			}
		});

		mapContainer.setOnMouseExited(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				radius.setOpacity(0);
				radius.setRadius(0);
			}
		});

		primaryStage.setResizable(false);

	}

	public static void main(String[] args) {
		launch(args);
	}
}
