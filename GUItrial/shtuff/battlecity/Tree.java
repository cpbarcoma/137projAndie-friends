import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Tree {
	public static final int width = 30;
	public static final int length = 30;
	private int x, y;
	
	BattleCity tc ;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] treeIcon = null;
	
	static {
		treeIcon = new Image[]{
			tk.getImage(NormalBrick.class.getResource("Images/tree.gif"))
		};
	}
	
	public Tree(int x, int y, BattleCity tc) { 
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {        
		g.drawImage(treeIcon[0],x, y, null);
	}
	
	public Rectangle getRect() { 
		return new Rectangle(x, y, width, length);
	}
	
}
