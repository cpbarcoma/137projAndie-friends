import javax.swing.*; 
import java.applet.*; 
import java.awt.*; 
import java.awt.event.*; 

public class BattleCityStart extends JFrame { 
	JButton start = new JButton("Start!"); 
	JButton exit = new JButton("Exit");

public BattleCityStart() { 
	setLayout(null);
	start.setFont(new Font("Calibri", Font.PLAIN, 25));
	start.setBounds(290, 320, 200, 40); 
	start.setFocusable(true); 
	
	exit.setFont(new Font("Calibri", Font.PLAIN, 25));
	exit.setBounds(290, 370, 200, 40);
	exit.setFocusable(true);
	add(start); 
	add(exit);
	
	start.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent epa) { 
			new BattleCity();
			setVisible(false);
		} 
	});

	exit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent epa) { 
			new BattleCity();
			System.exit(0);
		} 
	});

	 
} 

public static void main(String[] args) { 
	BattleCityStart sl = new BattleCityStart(); 
		sl.setSize(800, 600); 
		sl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sl.setVisible(true); 
	} 
}