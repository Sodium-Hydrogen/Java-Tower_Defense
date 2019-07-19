package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

  This class if for the bullets.

  -- Constructor --
  public Bullet(double posx, double posy, String objectName, int imageW, int velocity, int damage, double angle)

  public int getDamage()  -- Returns damage dealt by bullet and makes sure damage isn't dealt twice
  public void move()      -- Moves the bullet along its tragectory
*/

public class Bullet extends MovingObject{

  private int damage;
  private double angle;
  private boolean damageDealt;

  public Bullet(double posx, double posy, String objectName, int imageW, int velocity, int damage, double angle){
    super(posx, posy, objectName, imageW, 1, velocity);
    this.angle = angle * Math.PI / 180;
    this.damage = damage;
    damageDealt = false;
  }

  public int getDamage(){
    if(!damageDealt){
      damageDealt = true;
      this.setVisible(false);
      return damage;
    }
    return 0;
  }

  public void move(){
    double xdist = velocity * Math.cos(angle);
    double ydist = velocity * Math.sin(angle);
    this.setX(this.getX() + xdist);
    this.setY(this.getY() + ydist);
  }

}
