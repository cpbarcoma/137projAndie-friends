// CMSC 22 2nd Semester A.Y. 2015-2016
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class MapPanel extends JPanel {
	Rectangle redBase;
	Rectangle water;
	Rectangle blueBase;

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		redBase = new Rectangle(0, 0, 1100, 150);
			// put constants in last 2 params
		g2d.setColor(Color.RED);
		g2d.fill(redBase);

		water = new Rectangle(0, 150, 1100, 200);
			// put constants in last 2 params
		g2d.setColor(Color.BLUE);
		g2d.fill(water);

		blueBase = new Rectangle(0, 350, 1100, 150);
			// put constants in last 2 params
		g2d.setColor(Color.GREEN);
		g2d.fill(blueBase);
	}
}