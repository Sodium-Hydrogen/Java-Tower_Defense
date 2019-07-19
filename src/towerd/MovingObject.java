package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

	This is for map object that move.

	-- Constructor --
	public MovingObject(double posx, double posy, String objectName, int imageW, int imgCnt, int velocity)

	public int getVelocity() -- returns the object velocity
	public abstract void move() -- moves the object

*/


public abstract class MovingObject extends MapObject {

	protected int velocity;

	public MovingObject(double posx, double posy, String objectName, int imageW, int imgCnt, int velocity){
		super(posx, posy, objectName,  imageW, imgCnt);
		this.velocity=velocity;
	}

	public int getVelocity()	{
		return velocity;
	}

	public abstract void move();

}
