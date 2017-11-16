import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class MetalWall {
	public static final int width = 36; 
	public static final int length = 37;
	private int x, y;
	
	BattleCity tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] wallIcons = null;
	
	static {
		wallIcons = new Image[] { 
			tk.getImage(NormalBrick.class.getResource("Images/metalWall.gif"))
		};
	}

	public MetalWall(int x, int y, BattleCity tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) { 
		g.drawImage(wallIcons[0], x, y, null);
	}

	public Rectangle getRect() { 
		return new Rectangle(x, y, width, length);
	}
}
