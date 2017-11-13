import java.awt.*;

public class NormalBrick {
	public static final int width = 22; 
	public static final int length = 21;
	private int life = 10;
	private Boolean whole = true;
	private boolean okay;
	int x, y;


	BattleCity tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] wallIcons = null;
	
	static {
		wallIcons = new Image[] { 
		tk.getImage(NormalBrick.class.getResource("Images/NormalBrick.gif")), };
	}

	public NormalBrick(int x, int y, BattleCity tc) { 
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
	public boolean isokay() { return okay; }

	public int getLife() { return life; }

	public void setLife(int life) { this.life = life; }

	public boolean iswhole() { return whole; }

	public void setalive(boolean whole) { this.whole = whole; }

	public int getX() { return x; }
	
	public int getY() { return y; }

}
