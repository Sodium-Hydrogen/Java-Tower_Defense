//stationary object to add to our window
//This can be anything that you want to place that isn't going to "move"
//3-7-19
//Dr. G

import java.awt.image.BufferedImage;
import java.awt.Graphics;

public abstract class MapObject
{
	protected int posx; 
	protected int posy;
	protected BufferedImage bi; 
	protected int imageW;
	protected int imageH;

	MapObject(int posx, int posy, BufferedImage bi, int imageW, int imageH)
	{
		this.posx = posx;
		this.posy = posy; 
		this.bi= bi;
		this.imageW = imageW; 
		this.imageH = imageH;
	}
	
	//renders our object to the screen
	public void drawImage(Graphics g)
	{
		g.drawImage(bi,posx, posy,imageW,imageH,null);
	}
}