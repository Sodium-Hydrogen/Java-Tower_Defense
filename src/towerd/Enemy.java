package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

  -- Constructor --
	public Enemy(String enemyName, int imageW, int velocity, int hitPoints, Level map)

	public int takeDamage(int damage) -- Decreases the enemies health by provided amount
	public boolean needsRemove() -- This is true when the enemy is dead or off screen and needs to be removed
	public Enemy copyEnemy() -- Returns an Enemy with the same values as the instance
	public boolean isDead() -- Returns true if the enemies health hit zero
	public int getMaxHP() -- Returns the inital hp of the enemy from when it was created
	public int getTotalMoved() -- This counts all the pixels the enemy has moved
	public void slow(int power) -- Towers call this when they target the enemy to slow it down. This changes the images and velocity and deals damage to itself
	public void move() -- This will move the enemy along the map keeping it in the paths. It moves by the velocity

	private void moveSprite(int amount) -- This moves the sprite in a straight line in the current direction by the provided amount
	private void updateDirection() -- Figures out the new direction of the enemy along the maze
	private void getNextSpot() -- This calculates how the enemy moves along the maze
*/

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.File;
import javafx.scene.image.Image;

public class Enemy extends MovingObject{

	private int hitPoints;
	private int initHP;
	private int direction;
	private int totalMoved;
	private int originalVelocity;
	private long whenFree;
	private char currentSquare;
	private char nextSquare;
	private Level map;
	private boolean remove;
	private boolean killed;
	private Image [] originalImg;
	private Image [][] slowedImg;
	private Rectangle lifeBar;
	private int [][] tileLoc = new int[2][2];

	public Enemy(String enemyName, int imageW, int velocity, int hitPoints, Level map){
		super(0, 0, enemyName, imageW, 4, velocity);
		this.map = map;
		int posx = map.getStartCords()[0];
		int posy = map.getStartCords()[1];
		double x = posx * map.getTileSize();
		double y = posy * map.getTileSize();
		tileLoc[0][0] = posx;
		tileLoc[0][1] = posy;
		tileLoc[1][0] = posx;
		tileLoc[1][1] = posy;
		if(map.getMaze()[posx][posy] == 'e'){
			currentSquare = 'e';
			nextSquare = map.getMaze()[posx + 1][posy];
			tileLoc[1][0]++;
			y += (map.getTileSize()/2.);
			x -= map.getTileSize();
			direction = 1;
		}else{
			currentSquare = 'E';
			nextSquare = map.getMaze()[posx][posy - 1];
			tileLoc[1][1]--;
			x += (map.getTileSize()/2.);
			y += (map.getTileSize()*2.);
			direction = 0;
		}
		this.setX(x);
		this.setY(y);
		initHP = hitPoints;
		this.hitPoints = hitPoints;
		this.setDifferentImg(direction);
		remove = false;
		killed = false;
		whenFree = 0;
		originalVelocity = velocity;
		originalImg = allImages.clone();
		String [] names = {"scared", "slowed"};
		slowedImg = new Image[2][4];
		for(int i = 0; i < names.length; i++){
			for(int n = 0; n < slowedImg[i].length; n++){
				String imgPath = new File("resources/sprites/" + names[i] + String.valueOf(n) + ".png").toURI().toString();
				slowedImg[i][n] = new Image(imgPath);
			}
		}
		Rectangle backgroundRect = new Rectangle(0, imageH+1, imageW, 3);
		backgroundRect.setFill(Color.grayRgb(80));
		backgroundRect.setVisible(false);
		lifeBar = new Rectangle(0, imageH+1, imageW, 3);
		lifeBar.setFill(Color.MEDIUMSEAGREEN);
		lifeBar.setVisible(false);
		this.getChildren().add(0, backgroundRect);
		this.getChildren().add(1, lifeBar);
	}

	public int takeDamage(int damage){
		hitPoints -= damage;
		if(hitPoints < initHP){
			this.getChildren().get(0).setVisible(true);
			this.getChildren().get(1).setVisible(true);
		}
		lifeBar.setWidth(((double)hitPoints/initHP)*imageW);
		if(hitPoints <= 0){
			killed = true;
			remove = true;
			this.setVisible(false);
			hitPoints = 0;
		}
		return hitPoints;
	}

	public boolean needsRemove(){
		return remove;
	}

	public Enemy copyEnemy(){
		return new Enemy(objectName, (int)imageW, velocity, hitPoints, map);
	}

	public boolean isDead(){
		return killed;
	}

	public int getMaxHP(){
		return initHP;
	}

	public int getTotalMoved(){
		return totalMoved;
	}

	public void slow(int power){
		if(power < 2){
			allImages = slowedImg[0];
			velocity = 2;
			whenFree = System.currentTimeMillis() + (300*(power+1));
		}else{
			allImages = slowedImg[1];
			velocity = 4 - power;
			this.takeDamage(5*power);
			whenFree = System.currentTimeMillis() + 450;
		}
		this.setDifferentImg(direction);
	}

	public void move(){
		if(!remove){
			if(whenFree != 0 && System.currentTimeMillis() > whenFree){
				velocity = originalVelocity;
				allImages = originalImg;
				this.setDifferentImg(direction);
			}
			int v = velocity;
			int toNextTile = (int)Math.abs(this.getX() - (tileLoc[0][0]*map.getTileSize() + map.getTileSize()/2.));
			if(direction == 0 || direction == 2){
				toNextTile = (int)Math.abs(this.getY() - (tileLoc[0][1]*map.getTileSize() + map.getTileSize()/2.));
			}
			if(v > toNextTile){
				v -= toNextTile;
				this.moveSprite(toNextTile);
				this.updateDirection();
				this.getNextSpot();
				this.setDifferentImg(direction);
			}
			this.moveSprite(v);
		}
	}

	private void moveSprite(int amount){
		if(direction == 0){
			this.setY(this.getY()-amount);
		}else if(direction == 1){
			this.setX(this.getX()+amount);
		}else if(direction == 2){
			this.setY(this.getY()+amount);
		}else{
			this.setX(this.getX()-amount);
		}
		totalMoved += amount;
	}

	private void updateDirection(){
		if(currentSquare == 'L'){
			if(direction == 1){
				direction = 0;
			}else if(direction == 2){
				direction = 3;
			}
		}else if(currentSquare == 'l'){
			if(direction == 1){
				direction = 2;
			}else if(direction == 0){
				direction = 3;
			}
		}else if(currentSquare == 'R'){
			if(direction == 3){
				direction = 0;
			}else if(direction == 2){
				direction = 1;
			}
		}else if(currentSquare == 'r'){
			if(direction == 3){
				direction = 2;
			}else if(direction == 0){
				direction = 1;
			}
		}else if(currentSquare == 'F'){
			if(direction == 3){
				direction = 1 - tileLoc[0][1] + tileLoc[1][1];
			}else{
				direction = 1;
			}
		}else if(currentSquare == 'f'){
			if(direction == 1){
				direction = 1 - tileLoc[0][1] + tileLoc[1][1];
			}else{
				direction = 3;
			}
		}else if(currentSquare == 'S'){
			if(direction == 2){
				direction = 2 + tileLoc[0][0] - tileLoc[1][0];
			}else{
				direction = 0;
			}
		}else if(currentSquare == 's'){
			if(direction == 0){
				direction = 2 + tileLoc[0][0] - tileLoc[1][0];
			}else{
				direction = 2;
			}
		}else if(currentSquare == 'D'){
			remove = true;
		}
	}

	private void getNextSpot(){
		currentSquare = nextSquare;
		tileLoc[0][0] = tileLoc[1][0];
		tileLoc[0][1] = tileLoc[1][1];
		int randLoc = (int)(Math.random()*2)*2 - 1;
		if(currentSquare == 'h'){
			tileLoc[1][0] += 2-direction;
		}else if(currentSquare == 'v'){
			tileLoc[1][1] -= 1-direction;
		}else if(currentSquare == 'L'){
			if(direction == 1){
				tileLoc[1][1] -= 1;
			}else if(direction == 2){
				tileLoc[1][0] -= 1;
			}
		}else if(currentSquare == 'l'){
			if(direction == 1){
				tileLoc[1][1] += 1;
			}else if(direction == 0){
				tileLoc[1][0] -= 1;
			}
		}else if(currentSquare == 'R'){
			if(direction == 3){
				tileLoc[1][1] -= 1;
			}else if(direction == 2){
				tileLoc[1][0] += 1;
			}
		}else if(currentSquare == 'r'){
			if(direction == 3){
				tileLoc[1][1] += 1;
			}else if(direction == 0){
				tileLoc[1][0] += 1;
			}
		}else if(currentSquare == 'F'){
			if(direction == 3){
				tileLoc[1][1] += randLoc;
			}else{
				tileLoc[1][0] += 1;
			}
		}else if(currentSquare == 'f'){
			if(direction == 1){
				tileLoc[1][1] += randLoc;
			}else{
				tileLoc[1][0] -= 1;
			}
		}else if(currentSquare == 'S'){
			if(direction == 2){
				tileLoc[1][0] += randLoc;
			}else{
				tileLoc[1][1] -= 1;
			}
		}else if(currentSquare == 's'){
			if(direction == 0){
				tileLoc[1][0] += randLoc;
			}else{
				tileLoc[1][1] += 1;
			}
		}
		if(currentSquare != 'D'){
			nextSquare = map.getMaze()[tileLoc[1][0]][tileLoc[1][1]];
		}
		if(currentSquare == 'x' || currentSquare == 'X'){
			tileLoc[1][1-direction] += direction*2 -1;
			nextSquare = 'D';
		}
	}
}
