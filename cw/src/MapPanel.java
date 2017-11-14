// CMSC 22 2nd Semester A.Y. 2015-2016
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class MapPanel extends JPanel {
	Rectangle redBase;

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		redBase = new Rectangle(100, 100, 100, 100);
			// put constants in last 2 params
		g2d.setColor(Color.RED);
		g2d.fill(redBase);
	}
}