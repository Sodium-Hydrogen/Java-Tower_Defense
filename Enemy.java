//Enemy class for Tower Defense Project
//4-9-19
//Dr. G

import java.awt.image.BufferedImage;

public class Enemy extends MovingObject{
	
	private int hitPoints;
	
	public Enemy(int posx, int posy, BufferedImage bi, int imageW, int imageH, int vx, int vy, int hitPoints)
	{
		super(posx, posy, bi, imageW, imageH, vx, vy);
		this.hitPoints = hitPoints;
	}
	
	public int getHitPoints()
	{
		return hitPoints;
	}

}
