package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

  This manges everything and runs in a background thread to update the gui.

  -- Constructor --
  public GameManager(Level map, EnemyPane enemyPane, TowerPane towerPane, BulletPane bulletPane, GridPane lifeCounter, Label moneyCounter, Label waveCounter)

  public void run() -- This is what runs when you call .start(). It runs until the end of the game
  public void prepareLevel(Button startButton) -- This sets up a level when the start button is pressed
  public void endLevel() -- This runs after every wave/level and cleans things up
  public boolean levelInProgress() -- Returns true when a level is running
  public void pause() -- This will toggle whether the game is paused
  public void killThread() -- Call this to end the run loop

  private void checkTowerEnemy() -- This checks if an enemy is within range of a tower
*/

import java.lang.Thread;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class GameManager extends Thread{

  private int level;
  private double enemySpacing;
  private boolean run;
  private boolean levelIP;
  private boolean paused;
  private Label moneyCounter;
  private Label waveCounter;
  private Level map;
  private GridPane lifeCounter;
  private EnemyPane enemyPane;
  private TowerPane towerPane;
  private BulletPane bulletPane;
  private Button startButton;
  private Enemy [] ghosts;

  public GameManager(Level map, EnemyPane enemyPane, TowerPane towerPane, BulletPane bulletPane, GridPane lifeCounter, Label moneyCounter, Label waveCounter){
    super();
    level = 1;
    run = true;
    levelIP = false;
    paused = false;
    this.map = map;
    this.enemyPane = enemyPane;
    this.towerPane = towerPane;
    this.bulletPane = bulletPane;
    this.moneyCounter = moneyCounter;
    this.waveCounter = waveCounter;
    this.lifeCounter = lifeCounter;
    ghosts = new Enemy[5];
    ghosts[0] = new Enemy("clyde", (int)(map.getTileSize()*.7), 5, 80, map);
    ghosts[1] = new Enemy("pinky", (int)(map.getTileSize()*.7), 8, 250, map);
    ghosts[2] = new Enemy("inky", (int)(map.getTileSize()*.7), 10, 400, map);
    ghosts[3] = new Enemy("blinky", (int)(map.getTileSize()*.7), 12, 550, map);
    ghosts[4] = new Enemy("eyes", (int)(map.getTileSize()*.8), 22, 200, map);
  }

  public void run(){
    long lastUpdate = 0;
    while(run){
      if(System.currentTimeMillis() > lastUpdate && !paused && levelIP){
        Platform.runLater(() -> {
          bulletPane.updateBullets(enemyPane.getEnemies());
          this.checkTowerEnemy();
          if(!enemyPane.update(enemySpacing)){
            levelIP = false;
            this.endLevel();
          }
        });
        lastUpdate = System.currentTimeMillis()+80;
      }
    }
  }

  public void prepareLevel(Button startButton){
    double doubleSpace = ghosts[0].getObjectWidth()*2;
    levelIP = true;
    this.startButton = startButton;
    double [] spacing = {0, doubleSpace, doubleSpace, doubleSpace, doubleSpace*.5};
    int [] ghostAmount = {1, 5, 10, 15, 25};
    for(int i = 0; i < ghostAmount[(level-1)%5]; i++){
      for(int n = 0; n < ghosts.length; n++){
        if((((level-1)/5 + 1) & (int)Math.pow(2, n)) > 0){
          enemyPane.addEnemy(ghosts[n].copyEnemy());
        }
      }
    }
    enemySpacing = spacing[(level-1)%5] / (enemyPane.getEnemies().length/ghostAmount[(level-1)%5]);
  }

  public void endLevel(){
    if(map.getLevelCount() > level && lifeCounter.getChildren().size() > 0){
      startButton.setText("START");
      waveCounter.setText(String.valueOf(++level));
      int money = Integer.parseInt(moneyCounter.getText());
      money += 100;
      moneyCounter.setText(String.valueOf(money));
      bulletPane.endLevel();
      enemyPane.endLevel();
    }else{
      lifeCounter.getChildren().remove(0, lifeCounter.getChildren().size());
      lifeCounter.add(new Label("GAME OVER"), 0, 0);
      run = false;
    }
  }

  public boolean levelInProgress(){
    return levelIP;
  }

  public void pause(){
    paused = !paused;
  }

  public void killThread(){
    run = false;
  }

  private void checkTowerEnemy(){
    for(Tower t: towerPane.getTowers()){
      t.checkAllEnemy(enemyPane.getEnemies());
    }
  }
}
