import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class BombTank {
	private int x, y;
	private boolean alive = true; 
	private BattleCity tc;
	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] tankExplosion = {
			tk.getImage(BombTank.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/10.gif")), };
	int move = 0;

	public BombTank(int x, int y, BattleCity tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	public void draw(Graphics g) { 
		if (!alive) { 
			tc.bombTanks.remove(this);
			return;
		}
		
		if (move == tankExplosion.length) {
			alive = false;
			move = 0;
			return;
		}
		
		g.drawImage(tankExplosion[move], x, y, null);
		move++;
	}
}
