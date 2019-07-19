package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

	This is a class that has all the values needed for any object that will be visible on the map

	-- Constructor --
  public MapObject(double posx, double posy, String objectName, int imageW, int picCnt)

	public double getX() -- returns the center of the object
	public double getY() -- returns the center of the object
	public void setX(double x) -- Sets the center cords of the object
	public void setY(double y) -- Sets the center cords of the object
	public int getImgCount() -- Returns the number of images loaded into the object
	public int getCurrentImgIndex() -- Returns the index of the current selected image
	public Image getImage() -- Returns the Image class that is set for the map object
	public ImageView getImageView() -- Returns the ImageView class of the map object
	public String getName() -- Returns the name of the object/ this is the same as the file name
	public double getObjectWidth() -- returns the width of the object
	public double getObjectHeight() -- returns the height of the object
	public void setDifferentImg(int index) -- Sets a different image

*/

import java.io.File;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class MapObject extends Pane{
	protected double imageW;
	protected double imageH;
	protected int index;
	protected Image [] allImages;
	protected String objectName;
	protected ImageView currentImg;

	public MapObject(double posx, double posy, String objectName, int imageW, int picCnt){
		super();
		// Create 5 spots for items in this pane
		this.getChildren().add(0,new Pane());
		this.getChildren().add(1,new Pane());
		this.getChildren().add(2,new Pane());
		this.getChildren().add(3,new Pane());
		this.getChildren().add(4,new Pane());

		this.imageW = imageW;
		this.imageH = imageW;
		this.objectName = objectName;

		this.setMaxWidth(imageW);
		this.setMaxHeight(imageH);
		// Load images
		allImages = new Image [picCnt];
		try {
			if(picCnt > 1) {
				for(int i = 0; i < picCnt; i++) {
					String imgPath = new File("resources/sprites/" + objectName + String.valueOf(i) + ".png").toURI().toString();
					allImages[i] = new Image(imgPath);
				}
			}else {
				String imgPath = new File("resources/sprites/" + objectName + ".png").toURI().toString();
				allImages[0] = new Image(imgPath);
			}
			index = 0;
		}catch(Exception e) {
			e.printStackTrace();
		}
		// set current Image
		currentImg = new ImageView();
		currentImg.setFitWidth(imageW);
		currentImg.setPreserveRatio(true);
		currentImg.setImage(allImages[index]);
		this.getChildren().set(3, currentImg);

		this.setX(posx);
		this.setY(posy);
	}

	public double getX() {
		return this.getLayoutX() + imageW/2.;
	}

	public double getY() {
		return this.getLayoutY() + imageH/2.;
	}

	public void setX(double x) {
		this.setLayoutX(x-imageW/2.);
	}

	public void setY(double y) {
		this.setLayoutY(y-imageH/2.);
	}

	public int getImgCount() {
		return allImages.length;
	}

	public int getCurrentImgIndex() {
		return index;
	}

	public Image getImage() {
		return allImages[index];
	}

	public ImageView getImageView() {
		return currentImg;
	}

	public String getName() {
		return objectName;
	}

	public double getObjectWidth(){
		return imageW;
	}

	public double getObjectHeight(){
		return imageH;
	}

	public void setDifferentImg(int index) {
		if(index < allImages.length && index >= 0) {
			currentImg.setImage(allImages[index]);
			this.index = index;
		}
	}
}
