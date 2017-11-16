import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class BaseCamp {
	private int x, y;
	private BattleCity tc;
	public static final int width = 43, length = 43; 
	private boolean alive = true;

	private static Toolkit tk = Toolkit.getDefaultToolkit(); 
	private static Image[] baseCampImags = null;
	static {
		baseCampImags = new Image[] { tk.getImage(NormalBrick.class.getResource("Images/baseCamp.jpg")), };
	}

	public BaseCamp(int x, int y, BattleCity tc) {
		this.x = x;
		this.y = y;
		this.tc = tc; 
	}

	public void gameOver(Graphics g) {
		tc.tanks.clear();
		tc.metalWall.clear();
		tc.otherWall.clear();
		tc.bombTanks.clear();
		tc.trees.clear();
		tc.bullets.clear();
		tc.spawnTankPoint.setalive(false);
		
		Color c = g.getColor(); 
		g.setColor(Color.green);
		Font f = g.getFont();
		g.setFont(new Font(" ", Font.PLAIN, 40));
		g.setFont(f);
		g.setColor(c);
	}

	public void draw(Graphics g) {
		if(alive) { 
			g.drawImage(baseCampImags[0], x, y, null);
			for (int i = 0; i < tc.baseCampWall.size(); i++) {
				NormalBrick b = tc.baseCampWall.get(i);
				b.draw(g);
			}
		} else {
			gameOver(g);
		}
	}

	public boolean isalive() { 
		return alive;
	}

	public void setalive(boolean alive) { 
		this.alive = alive;
	}

	public Rectangle getRect() { 
		return new Rectangle(x, y, width, length);
	}
}
