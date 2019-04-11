//Class object for towers
//Needs to contain all characteristics of the tower objects
//3-7-19
//Dr. G

import java.awt.Graphics;
import javafx.scene.image.Image;

public class Tower extends MapObject{

	public Tower(int posx, int posy, Image bi, int imageW, int imageH)
	{
		super(posx, posy, bi, imageW, imageH);
	}

	//make the tower fire
	public void fire(Graphics g)
	{
	}

}
