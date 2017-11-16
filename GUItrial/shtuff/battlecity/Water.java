import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Water {
	public static final int width = 30;
	public static final int length = 30;
	int x, y;
	
	BattleCity tc ;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] waterIcon = null;
	
	static {
		waterIcon = new Image[]{
			tk.getImage(NormalBrick.class.getResource("Images/river.jpg"))
		};
	}
	
	public Water(int x, int y, BattleCity tc) { 
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {        
		g.drawImage(waterIcon[0],x, y, null);
	}
}
