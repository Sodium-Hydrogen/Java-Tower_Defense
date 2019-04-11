//An object on the map that is going to be moving
//3-7-19
//Dr. G

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class MovingObject extends MapObject {
	
	private int vx;
	private int vy;
	
	public MovingObject(int posx, int posy, BufferedImage bi, int imageW, int imageH, int vx, int vy)
	{
		super(posx, posy, bi,  imageW, imageH);
		this.vx=vx;
		this.vy=vy;
	}
	
	public void drawImage(Graphics g)
	{
		g.drawImage(bi,posx+=vx, posy+=vy,imageW,imageH,null);
	}

}
