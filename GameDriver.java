//Demo code to demonstrate GUI Building Part 2
//Tie in to semester long project
//Not necessarily meant to be copied and used directly
//3-9-19
//Dr. G

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameDriver extends JFrame{
	
	public GameDriver() {

		getContentPane().setLayout(null);
		
		JPanel Controls = new JPanel();
		Controls.setBounds(0, 0, 120, 278);
		getContentPane().add(Controls);
		Controls.setLayout(null);
		//Controls.setSize(200,600);
		
		JLabel lblMoney = new JLabel("Money:");
		lblMoney.setBounds(6, 5, 45, 16);
		Controls.add(lblMoney);
		
		JLabel lblLives = new JLabel("Lives: ");
		lblLives.setBounds(6, 22, 40, 16);
		Controls.add(lblLives);
		
		JPanel Map = new MapLoader();
		Map.setBounds(123, 0, 600, 600);
		getContentPane().add(Map);
		Map.setLayout(new GridLayout(1, 0, 0, 0));
		
		
		JButton btnStart = new JButton("Tower 0");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnStart.setBounds(6, 45, 117, 29);
		Controls.add(btnStart);
		
		
		JButton btnNewButton = new JButton("START");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		btnNewButton.setBounds(6, 74, 117, 29);
		Controls.add(btnNewButton);
		
		
	}

	public static void main(String[] args) {
		//Last class: 
			//Learned how to create windows with Elipse's window building editor
			//Learned how to incorporate a Java panel of our own creation 
			//Learned how to draw towers
			//Learned how to make towers fire
		//Today: 
			//Adjust window and tile sizes
			//Draw towers at specific locations
			//Make enemies go across the screen
		
		//1. Return to the design builder make the entire window 720 X 600
		//2. Make the control panel 120 X 600
		//3. Make the map 600 X 600
		//4. In map loader adjust the tiles to be a 5X5 grid with a tile size of 120 and an image size to match
				//Explore what map loader actually does
		//5. Add the ability to record a mouse click
		//6. Create a tower button
		//7. Add a tower at the mouse click location when the tower button is hit
		//8. Create an enemy class that inherits from moving object, but has hit points
		//9. Add a start button that will start an enemy going across the screen
		//10. Answer these questions: 
			//What am I leaving out?
			//How would you check to see if an enemy was hit?
			//How can you change the direction of the bullets to aim at the enemy?
		
		
		GameDriver m = new GameDriver();
		m.setSize(600, 600);
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.setVisible(true);
		

	}
}
