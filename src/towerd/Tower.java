package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

	This is for the tower.

	-- A tower must be either a single shot or range shot
	public static enum towerType {SINGLE, RANGE;}

	-- Constructor --
	public Tower(String towerName, int imageW, int levels, int cost, int range, int speed, towerType tt, Label moneyCounter, BulletPane bulletLayer)

	public int getRange() -- returns the range of the tower
	public void fire() -- fires the tower. If the tower is a range tower it calls .slow() for all enemies in range. Else if creates a new bullet and adds it to the bullet layer
	public Tower copyTower() -- returns a tower that is a copy of itself
	public void selectedAction() -- this sets all the actions for when the tower is selected by the user
	public int getCost() -- returns the cost of the tower
	public void upgrade() -- upgrades the tower by increasing the range, fireSpeed, image index, and total spent
	public void sellTower() -- adds 75% of the total amount spent to the moneyCounter
	public boolean isActive() -- returns if the tower is active and hasn't been sold
	public void makePurchase(int cost) -- this subtracts the provided amount from the moneyCounter
	public void purchaseTower() -- This will make a purchase of the cost of the tower
	public boolean isPurchaseable() -- returns if the play has enough money to purchase the tower
	public void setAngle(double angleTower) -- this sets the angle of the tower
	public double getAngle() -- returns the angle of the tower
	public void checkAllEnemy(Enemy [] enemies) -- this checks to see if any enemies are in range of the tower
	public void targetEnemy() -- this targets the enemy in its range that has been on the field the longest
	public boolean checkEnemy(Enemy enemy) -- Checks to see if a specific enemy is in range of the tower
	public boolean isInRange(double x, double y) -- Checks to see if provided x,y are in range of the tower
*/

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Tower extends MapObject {

	private int cost;
	private int range;
	private int upgradeCost;
	private Label moneyCounter;
	private int upgradeLevel;
	private int totalSpent;
	private int fireSpeed;
	private long lastFire;
	private boolean active;
	private Circle radius;
	private Button upgradeButton;
	private Button sellButton;
	private ArrayList<Enemy> enemiesInRange;
	private EventHandler<MouseEvent> clicked;
	private EventHandler<MouseEvent> exit;
	public static enum towerType {SINGLE, RANGE;}
	private towerType tt;
	private BulletPane bulletLayer;

	public Tower(String towerName, int imageW, int levels, int cost, int range, int speed, towerType tt, Label moneyCounter, BulletPane bulletLayer){
		super(0, 0, towerName, imageW, levels);
		this.cost = cost;
		this.range = range;
		this.moneyCounter = moneyCounter;
		upgradeLevel = 0;
		totalSpent = 0;
		active = true;
		fireSpeed = speed;
		enemiesInRange = new ArrayList<Enemy>();
		lastFire = 0;
		this.tt = tt;
		this.bulletLayer = bulletLayer;
		upgradeCost = (int)(cost*1.25*(upgradeLevel+1));
		this.selectedAction();
	}

	public int getRange(){
		return range;
	}

	//make the tower fire
	public void fire(){
		if(lastFire + fireSpeed < System.currentTimeMillis()){
			if(tt == towerType.SINGLE){
				bulletLayer.addBullet(new Bullet(this.getX(), this.getY(), "pellet", 8, 25*(upgradeLevel+1), 15*(upgradeLevel+1), currentImg.getRotate()));
			}else{
				for(Enemy e: enemiesInRange){
					if(e != null){
						e.slow(upgradeLevel);
					}
				}
			}
			lastFire = System.currentTimeMillis();
		}
	}

	public Tower copyTower(){
		Tower copy = new Tower(this.objectName, (int)this.imageW, this.allImages.length, this.cost, this.range, this.fireSpeed, this.tt, this.moneyCounter, this.bulletLayer);
		copy.setDifferentImg(index);
		copy.setX(this.getX());
		copy.setY(this.getY());
		return copy;
	}

	public void selectedAction() {
		Tower curTower = this;
		radius = new Circle();
		radius.setFill(Color.DARKGREY);
		radius.setCenterX(imageW/2.);
		radius.setCenterY(imageH/2.);
		upgradeButton = new Button("↑ $"+String.valueOf(upgradeCost));
		upgradeButton.setLayoutY(-35);
		upgradeButton.setVisible(false);
		upgradeButton.setMaxWidth(imageW*2);
		upgradeButton.setMinWidth(imageW*2);
		upgradeButton.setLayoutX(-imageW/2.);
		sellButton = new Button("$"+String.valueOf((int)(totalSpent*.75)));
		sellButton.setLayoutY(imageH+5);
		sellButton.setVisible(false);
		sellButton.setMaxWidth(imageW*2);
		sellButton.setMinWidth(imageW*2);
		sellButton.setLayoutX(-imageW/2.);
		this.getChildren().set(0, radius);
		this.getChildren().set(1, upgradeButton);
		this.getChildren().set(2, sellButton);

		clicked = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				curTower.toFront();
				radius.setRadius(range);
				radius.setOpacity(0.2);
				upgradeButton.setVisible(true);
				sellButton.setVisible(true);
				if(upgradeCost > Integer.parseInt(moneyCounter.getText()) || upgradeLevel + 1 == allImages.length){
					upgradeButton.setDisable(true);
				}else {
					upgradeButton.setDisable(false);
				}
			}
		};
		exit = new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent e){
				radius.setOpacity(0);
				radius.setRadius(0);
				upgradeButton.setVisible(false);
				sellButton.setVisible(false);
			}
		};
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, clicked);
		this.addEventHandler(MouseEvent.MOUSE_EXITED, exit);
		upgradeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				curTower.upgrade();
			}
		});
		sellButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				curTower.sellTower();
			}
		});
	}

	public int getCost() {
		return this.cost;
	}

	public void upgrade(){
		if(upgradeLevel < this.getImgCount()-1){
			makePurchase(upgradeCost);
			upgradeLevel++;
			upgradeCost = (int)(cost*1.25*(upgradeLevel+1));
			range *= 1.15 ;
			radius.setRadius(range);
			fireSpeed -= fireSpeed*.25;
			this.setDifferentImg(upgradeLevel);
			upgradeButton.setText("↑ $"+String.valueOf(upgradeCost));
			if(upgradeCost > Integer.parseInt(moneyCounter.getText()) || upgradeLevel + 1 == this.getImgCount()){
				upgradeButton.setDisable(true);
			}else {
				upgradeButton.setDisable(false);
			}
		}else {
			upgradeButton.setDisable(true);
		}
	}

	public void sellTower() {
		makePurchase(-(int)(totalSpent*.75));
		this.getChildren().remove(0, this.getChildren().size());
		this.removeEventHandler(MouseEvent.MOUSE_CLICKED, clicked);
		this.removeEventHandler(MouseEvent.MOUSE_EXITED, exit);
		active = false;
	}

	public boolean isActive() {
		return active;
	}

	public void makePurchase(int cost) {
		int money = Integer.parseInt(moneyCounter.getText());
		money -= cost;
		totalSpent += cost;
		moneyCounter.setText(String.valueOf(money));
		sellButton.setText("$"+String.valueOf((int)(totalSpent*.75)));
	}

	public void purchaseTower() {
		this.makePurchase(this.cost);
	}

	public boolean isPurchaseable() {
		int money = Integer.parseInt(moneyCounter.getText());
		if(cost <= money) {
			return true;
		}
		return false;
	}

	public void setAngle(double angleTower){
		this.getImageView().setRotate(angleTower);
	}

	public double getAngle(){
		return this.getImageView().getRotate();
	}

	public void checkAllEnemy(Enemy [] enemies){
		for(Enemy e: enemies){
			if(this.checkEnemy(e)){
				boolean found = false;
				for(int i = 0; i < enemiesInRange.size(); i++){
					if(e == enemiesInRange.get(i)){
						found = true;
					}
				}
				if(!found && e.getTotalMoved() > e.getObjectWidth()){
					enemiesInRange.add(e);
				}
			}
		}
		this.targetEnemy();
	}

	public void targetEnemy(){
		int longestOnField = 0;
		Enemy target = null;
		for(int i = 0; i < enemiesInRange.size(); i++){
			if(enemiesInRange.get(i) == null || enemiesInRange.get(i).isDead() || !this.checkEnemy(enemiesInRange.get(i))){
				enemiesInRange.set(i, null);
			}else{
				if(enemiesInRange.get(i).getTotalMoved() > longestOnField){
					longestOnField = enemiesInRange.get(i).getTotalMoved();
					target = enemiesInRange.get(i);
				}
			}
		}
		for(int i = 0; i < enemiesInRange.size(); i++){
			if(enemiesInRange.get(i) == null){
				enemiesInRange.remove(i);
			}
		}
		if(target != null){
			double x = target.getX();
			double y = target.getY();
			double dx = Math.abs(x-this.getX());
			double dy = Math.abs(y-this.getY());
			double angle = Math.atan(dy/dx)*180 / Math.PI;
			if(x <= this.getX() && y >= this.getY()){
				angle = 180 - angle;
			}else if(x <= this.getX() && y < this.getY()){
				angle += 180;
			}else if(x > this.getX() && y < this.getY()){
				angle = 360 - angle;
			}
			this.getImageView().setRotate(angle);
			this.fire();
		}
	}

	public boolean checkEnemy(Enemy enemy){
		boolean inRange = false;
		if(enemy != null){
  		double upperX = enemy.getX()+enemy.getObjectWidth()/2.;
  		double upperY = enemy.getY()+enemy.getObjectHeight()/2.;
  		double lowerX = enemy.getX()-enemy.getObjectWidth()/2.;
  		double lowerY = enemy.getY()-enemy.getObjectHeight()/2.;
  		inRange = inRange || this.isInRange(upperX, upperY);
  		inRange = inRange || this.isInRange(upperX, lowerY);
  		inRange = inRange || this.isInRange(lowerX, upperY);
  		inRange = inRange || this.isInRange(lowerX, lowerY);
		}
		return inRange;
	}

	public boolean isInRange(double x, double y){
		double upperX = this.getX() + range;
		double upperY = this.getY() + range;
		double lowerX = this.getX() - range;
		double lowerY = this.getY() - range;
		if(upperX >= x && x >= lowerX){
			if(upperY >= y && y >= lowerY){
				double dx = x-this.getX();
				double dy = y-this.getY();
				double h = Math.sqrt(dx*dx + dy*dy);
				if(range >= h){
					return true;
				}
			}
		}
		return false;
	}
}
