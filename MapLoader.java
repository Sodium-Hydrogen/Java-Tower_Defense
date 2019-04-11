//Class object for loading the basic map
//Contains the paint method for add and removing items from the map
//Base code taken from the file reader lab
//3-7-9
//Dr. G

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings({ "serial", "unused" })
public class MapLoader extends JPanel {

	//Instance variables	
	public MapLoader(){

		MyCanvas myCanvas = null; 
		myCanvas = new MyCanvas(4, 4);

		
		//load tile images
		for (int x = 0; x< 4; x++)
			for (int y = 0; y < 4; y++)
				myCanvas.addPicture(x, y,"grass_02.png");
		
		if (myCanvas != null){
			this.add(myCanvas);
		}
		this.setVisible(true);
	}
 
	
	public void createTower(int x, int y, int style)
	{
	//	try {
		
	//	}
	//	catch (IOException e)
	//	{
		System.out.println("Unable to generate tower due to IO exception");
	//	}
	}
	
	public void start()
	{
		
		//try {
		
	//	}
	//	catch (IOException e)
	//	{
			System.out.println("Unable to generate enemies due to IO exception");
	//	}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		try{
			
		//Draw the things that need to be drawn
				
				Thread.sleep(1000);
				repaint();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}


//MyCanvas taken from file reader lab
//Notice that this too is a panel
@SuppressWarnings("serial")
class MyCanvas extends JPanel{
	private BufferedImage[][] imgs;
	private int rows;
	private int cols;
	private final int tileSize = 64;
	public MyCanvas(int rows, int cols){
		super();
		this.rows = rows;
		this.cols = cols;
		imgs = new BufferedImage[rows][cols];
	}
	public void addPicture(int x, int y, String filename){
		if (x < 0 || x >= rows){
			System.err.println("There is no row " + x);
		}
		else if (y < 0 || y >= cols){
			System.err.println("There is no col " + y);
		}
		else{
				try {
					imgs[x][y] = ImageIO.read(new File(filename));
				} catch (IOException e) {
					System.err.println("Unable to read the file: " + filename);
				}
		}
	}
	public void paint(Graphics g){
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				g.drawImage(imgs[i][j], j*tileSize, i*tileSize,null);
			}
		}
	}
}