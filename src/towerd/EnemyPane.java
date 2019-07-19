package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

  This manges and updates all the enemies from the run loop

  -- Constructor --
  public EnemyPane(Label moneyCounter, GridPane lifeCounter)

  public void addEnemy(Enemy newEnemy) -- This adds an enemy to be manged
  public void endLevel() -- This cleans up the level when it is over. Removes all enemies.
  public boolean update(double spacing) -- This will update the enemies and que with the proved spacing
  public Enemy [] getEnemies() -- This returns all the enemies being managed

  private void cleanUp() -- This removes any enemies that are marked to be removed
*/

import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import java.util.ArrayList;

public class EnemyPane extends Pane{

  private ArrayList<Enemy> enemies;
  private Label moneyCounter;
  private GridPane lifeCounter;

  public EnemyPane(Label moneyCounter, GridPane lifeCounter){
    super();
    enemies = new ArrayList<Enemy>();
    this.moneyCounter = moneyCounter;
    this.lifeCounter = lifeCounter;
  }

  public void addEnemy(Enemy newEnemy){
    enemies.add(newEnemy);
    this.getChildren().add(newEnemy);
  }

  public void endLevel(){
    enemies = new ArrayList<Enemy>();
    this.getChildren().remove(0, this.getChildren().size());
  }

  public boolean update(double spacing){
    if(lifeCounter.getChildren().size() > 0 && enemies.size() > 0){
      int prevMoved = -1;
      for(int i = 0; i < enemies.size(); i++){
        if(enemies.get(i).getTotalMoved() > spacing){
          prevMoved = i;
        }
        if(prevMoved + 1 >= i){
          enemies.get(i).move();
        }
      }
      this.cleanUp();
      if(enemies.size() == 0){
        return false;
      }
      return true;
    }
    return false;
  }

  private void cleanUp(){
    for(Object em: enemies.toArray()){
      if(((Enemy)em).needsRemove()){
        if(((Enemy)em).isDead()){
          int money = Integer.parseInt(moneyCounter.getText());
          money += ((Enemy)em).getMaxHP() * .05;
          moneyCounter.setText(String.valueOf(money));
        }else{
          int curLife = lifeCounter.getChildren().size();
          lifeCounter.getChildren().remove(curLife - 1);
        }
        this.getChildren().remove(em);
        enemies.remove(em);
      }
    }
  }

  public Enemy [] getEnemies(){
    Enemy [] tmp = new Enemy[enemies.size()];
    for(int i = 0; i < enemies.size(); i++){
      tmp[i] = enemies.get(i);
    }
    return tmp;
  }

}
