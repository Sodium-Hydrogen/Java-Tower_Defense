package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

  This class manges all the bullets that are on the map.
  It is called from the run loop.

  -- Constructor --
  public BulletPane(Level map)

  public void addBullet(Bullet fired) -- Adds bullets to be managed
  public void updateBullets(Enemy [] listEnemies) -- This will check if any enemies are colliding with any bullets
  public void checkCollision(Bullet b, double sx, double sy, double ex, double ey, Enemy [] enmyList) -- called by updateBullets for each bullet
  public void endLevel() -- Clean up and remove everthing from the map.
*/

import javafx.scene.layout.Pane;

public class BulletPane extends Pane{

  private Level map;

  public BulletPane(Level map){
    super();
    this.map = map;
  }

  public void addBullet(Bullet fired){
    this.getChildren().add(fired);
  }

  public void updateBullets(Enemy [] listEnemies){
    for(int i = 0; i < this.getChildren().size(); i++){
      int width = map.getMaze().length*map.getTileSize();
      int height = map.getMaze()[0].length*map.getTileSize();
      double sx = ((Bullet)this.getChildren().get(i)).getX();
      double sy = ((Bullet)this.getChildren().get(i)).getY();
      ((Bullet)this.getChildren().get(i)).move();
      double ex = ((Bullet)this.getChildren().get(i)).getX();
      double ey = ((Bullet)this.getChildren().get(i)).getY();
      if(sx >= 0 && sx <= width && sy >= 0 && sy <= height){
        this.checkCollision((Bullet)this.getChildren().get(i), sx, sy, ex, ey, listEnemies);
      }else{
        Bullet b = (Bullet)this.getChildren().get(i);
        this.getChildren().remove(b);
      }
    }
  }

  public void checkCollision(Bullet b, double sx, double sy, double ex, double ey, Enemy [] enmyList){
    double slope = (sx-ex) / (sy-ey);
    double yint = sy-sx*slope;
    double ux;
    double lx;
    double uy;
    double ly;
    if(sx >= ex){
      ux = sx;
      lx = ex;
    }else{
      ux = ex;
      lx = sx;
    }
    if(sy >= ey){
      uy = sy;
      ly = ey;
    }else{
      uy = ey;
      ly = sy;
    }
    for(Enemy en: enmyList){
      double upperX = en.getX()+en.getObjectWidth();
      double upperY = en.getY()+en.getObjectHeight();
      double lowerX = en.getX()-en.getObjectWidth();
      double lowerY = en.getY()-en.getObjectHeight();
      boolean overlap = false;
      double res = slope*lowerX+yint;
      overlap = overlap || (res >= lowerY && res <= upperY && res >= ly && res <= uy);
      res = slope*upperX+yint;
      overlap = overlap || (res >= lowerY && res <= upperY && res >= ly && res <= uy);
      res = (lowerY-yint)/slope;
      overlap = overlap || (res >= lowerX && res <= upperX && res >= lx && res <= ux);
      res = (upperY-yint)/slope;
      overlap = overlap || (res >= lowerX && res <= upperX && res >= lx && res <= ux);

      if(overlap){
        en.takeDamage(b.getDamage());
        this.getChildren().remove(b);
      }
    }

  }

  public void endLevel(){
    this.getChildren().remove(0, this.getChildren().size());
  }
}
