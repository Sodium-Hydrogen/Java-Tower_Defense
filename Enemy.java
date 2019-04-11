//Enemy class for Tower Defense Project
//4-9-19
//Dr. G

import javafx.scene.image.Image;

public class Enemy extends MovingObject{

	private int hitPoints;

	public Enemy(int posx, int posy, Image img, int imageW, int imageH, int vx, int vy, int hitPoints){
		super(posx, posy, img, imageW, imageH, vx, vy);
		this.hitPoints = hitPoints;
	}

	public int getHitPoints(){
		return hitPoints;
	}

}
