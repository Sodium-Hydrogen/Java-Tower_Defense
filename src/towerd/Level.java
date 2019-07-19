package towerd;
/*
  Author: Michael Julander
  Date: April 25, 2019
  Version: 1

  This reads and parses the level file to generate the playable level

  -- Constructor --
  public Level(File levelFile)

  public int getTileSize() -- returns size of the map tiles
  public char [][] getMaze() -- this returns the maze as it is read from the file
  public int getIndex() -- This returns the index the level will have on the start screen
  public int getStartLives() -- Returns starting life total
  public int getLevelCount() -- Returns level count
  public String getName() -- Returns name of the level
  public int getStartMoney() -- returns the starting money
  public void autoSize() -- This resizes the level pane to fit the maze exactly
  public int [] getStartCords() -- this returns the cords of the tile piece that the enemies start at
  public boolean isPathTile(int x, int y) -- retuns true if the provided cords are within 30% of a tile size of path
  public boolean isPathTile(int x, int y, int tolerance) -- returns if the provided cords are a tile within the tolerance range provided


  -- MyCanvas Class --
  This class reads in an image and sets it to its location in map

  -- Constructor --
  public MyCanvas(int rows, int cols, int tileSize)

  public void addPicture(int x, int y, String filename) -- This adds a tile image at the specified location
*/

import java.util.Scanner;
import java.io.File;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Level extends Pane{

  private MyCanvas levelCanvas;
  private int x;
  private int y;
  private int [] startLocation = new int[2];
  private int tileSize;
  private int numLevels;
  private int startLives;
  private int startMoney;
  private char [][] maze;
  private String name;
  private int levelIndex;

  public Level(File levelFile){
		int seperator = levelFile.getName().indexOf('-');
		levelIndex = Integer.parseInt(levelFile.getName().substring(0, seperator)) - 1;
		name = levelFile.getName().substring(seperator + 1, levelFile.getName().indexOf(".txt"));
    try{
			Scanner readFile = new Scanner(levelFile);

			x = readFile.nextInt();
			y = readFile.nextInt();
			tileSize = readFile.nextInt();
			numLevels = readFile.nextInt();
      startLives = readFile.nextInt();
      startMoney = readFile.nextInt();
			levelCanvas = new MyCanvas(x, y, tileSize);

			readFile.nextLine();

      maze = new char[x][y];
			//load tile images
			for (int yl = 0; yl< y; yl++) {
				String [] text = readFile.nextLine().split(" ");
				for (int xl = 0; xl < x; xl++) {
          maze[xl][yl] = text[xl].charAt(0);
					levelCanvas.addPicture(xl, yl, "resources/textures/" + text[xl] + ".png");
          if(text[xl].matches("E") || text[xl].matches("e")){
            startLocation[0] = xl;
            startLocation[1] = yl;
          }
				}
			}
			readFile.close();
    }catch(Exception e){
      e.printStackTrace();
    }
		if (levelCanvas != null){
			this.getChildren().add(levelCanvas);
		}
		this.setVisible(true);
  }

  public int getTileSize(){
    return tileSize;
  }

  public char [][] getMaze(){
    return maze;
  }

  public int getIndex(){
    return levelIndex;
  }

  public int getStartLives(){
    return startLives;
  }

  public int getLevelCount(){
    return numLevels;
  }

  public String getName(){
    return name;
  }

	public void autoSize(){
		this.setWidth(this.x*this.tileSize);
	}

  public int [] getStartCords(){
    return startLocation;
  }

  public boolean isPathTile(int x, int y){
    return this.isPathTile(x, y, tileSize/3);
  }

  public int getStartMoney(){
    return startMoney;
  }

  public boolean isPathTile(int x, int y, int tolerance){
    int lowerX = (x - tolerance)/tileSize;
    int upperX = (x + tolerance)/tileSize;
    int lowerY = (y - tolerance)/tileSize;
    int upperY = (y + tolerance)/tileSize;
    if (upperX >= this.x){
      upperX = this.x-1;
    }
    if(upperY >= this.y){
      upperY = this.y-1;
    }
    if (maze[lowerX][lowerY] != 'b' || maze[upperX][upperY] != 'b'){
      return true;
    }
    if (maze[lowerX][upperY] != 'b' || maze[upperX][lowerY] != 'b'){
      return true;
    }
    return false;
  }
}

//MyCanvas taken from file reader lab
//Notice that this too is a GridPane
//Modified by Michael Julander
//@SuppressWarnings("serial")
class MyCanvas extends GridPane{
	private ImageView[][] imgs;
	private int rows;
	private int cols;
	private int tileSize;
	public MyCanvas(int rows, int cols, int tileSize){
		super();
		this.rows = rows;
		this.cols = cols;
		this.tileSize = tileSize;
		imgs = new ImageView[rows][cols];
		for(int x = 0; x < imgs.length; x++) {
			for(int y = 0; y < imgs[x].length; y++) {
				imgs[x][y] = new ImageView();
				imgs[x][y].setFitWidth(this.tileSize);
				imgs[x][y].setFitHeight(this.tileSize);
				this.add(imgs[x][y], x, y);
			}
		}
	}
	public void addPicture(int x, int y, String filename){
		File pic = new File(filename);
		if (x < 0 || x >= rows){
			System.err.println("There is no row " + x);
		}else if (y < 0 || y >= cols){
			System.err.println("There is no col " + y);
		}else if (pic.exists()){
			imgs[x][y].setImage(new Image(pic.toURI().toString()));
		}else {
			System.err.println("Unable to read the file: " + filename);
		}
	}
}
