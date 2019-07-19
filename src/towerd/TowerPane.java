package towerd;
/*
 Author: Michael Julander
 Date: April 25, 2019
 Version: 1

 This class manages all the towers and updates them while the program is
 running.

 -- Constructor --
 public TowerPane()

 public boolean isValidSpot(int x, int y, Level map) -- This checks to see if a tower can be placed at the provided cords
 public boolean isTowerAt(int x, int y, int tolerance) -- This checks to see if a tower is at the cords within the tolerance
 public void addTower(Tower newTower) -- This adds a new tower to be managed
 public Tower [] getTowers() -- returns all towers that are being managed

 private void cleanUp() -- this cleans up any sold towers
*/

import javafx.scene.layout.Pane;
import java.util.ArrayList;

public class TowerPane extends Pane{

  private ArrayList<Tower> towers;

  public TowerPane(){
    super();
    towers = new ArrayList<Tower>();
  }


  // Garbage Collector for towers
  private void cleanUp() {
    Tower [] children = new Tower[towers.size()];
	  for(int i = 0; i < children.length; i++) {
      children[i] = towers.get(i);
    }
	  for(int i = 0; i < children.length; i++) {
      int target = towers.indexOf(children[i]);
		  if(!towers.get(target).isActive()) {
			  this.getChildren().remove(children[i]);
			  towers.remove(target);
			  children[i] = null;
		  }
	  }
  }

  public boolean isValidSpot(int x, int y, Level map){
    //Run cleanup before it checks to see if tower is there
    this.cleanUp();
    if(map.isPathTile(x, y)){
      return false;
    }
    if(this.isTowerAt(x, y, map.getTileSize()/3)){
      return false;
    }
    return true;
  }

  public boolean isTowerAt(int x, int y, int tolerance){
    double upperX = x + tolerance;
    double upperY = y + tolerance;
    double lowerX = x - tolerance;
    double lowerY = y - tolerance;
    for(int i = 0; i < towers.size(); i++){
      double towerX = towers.get(i).getX();
      double towerY = towers.get(i).getY();
      double towerWidth = towers.get(i).getWidth();
      double towerHeight = towers.get(i).getHeight();

      if(upperX >= towerX-towerWidth/2 && lowerX <= towerX+towerWidth/2){
        if(upperY >= towerY-towerHeight/2 && lowerY <= towerY+towerHeight/2){
          return true;
        }
      }
    }
    return false;
  }

  public void addTower(Tower newTower){
    towers.add(newTower);
    this.getChildren().add(newTower);
  }

  public Tower [] getTowers(){
    this.cleanUp();
    Tower [] tmp = new Tower[this.getChildren().size()];
    for(int i = 0; i < tmp.length; i++){
      tmp[i] = (Tower)this.getChildren().get(i);
    }
    return tmp;
  }

}
