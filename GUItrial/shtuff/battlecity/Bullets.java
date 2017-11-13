import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bullets {
	public static  int speedX = 12;
	public static  int speedY = 12;
	public static final int width = 10;
	public static final int length = 10;
	private int x, y;

	private boolean okay;
	private boolean alive = true;

	private BattleCity tc;
	Direction diretion;

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] bulletImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>(); 
	static {
		bulletImages = new Image[] {
				tk.getImage(Bullets.class.getClassLoader().getResource("images/bulletL.gif")),
				tk.getImage(Bullets.class.getClassLoader().getResource("images/bulletU.gif")),
				tk.getImage(Bullets.class.getClassLoader().getResource("images/bulletR.gif")),
				tk.getImage(Bullets.class.getClassLoader().getResource("images/bulletD.gif")),
		};
		
		imgs.put("L", bulletImages[0]); 
		imgs.put("U", bulletImages[1]);
		imgs.put("R", bulletImages[2]);
		imgs.put("D", bulletImages[3]);

	}

	public Bullets(int x, int y, Direction dir) { 
		this.x = x;
		this.y = y;
		this.diretion = dir;
	}

	public Bullets(int x, int y, boolean okay, Direction dir, BattleCity tc) {
		this(x, y, dir);
		this.okay = okay;
		this.tc = tc;
	}

	private void move() {
		switch (diretion) {
		case L:
			x -= speedX;
			break;

		case U:
			y -= speedY;
			break;

		case R:
			x += speedX; 
			break;

		case D:
			y += speedY;
			break;

		case STOP:
			break;
		}

		if (x < 0 || y < 0 || x > BattleCity.frameWidth
				|| y > BattleCity.frameLength) {
			alive = false;
		}
	}

	public void draw(Graphics g) {
		if (!alive) {
			tc.bullets.remove(this);
			return;
		}

		switch (diretion) { 
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;

		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;

		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;

		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;

		} move(); 
	}

	public boolean isalive() { 
		return alive;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}

	public boolean hitTank(Tank t) { 
		if(this.alive && this.getRect().intersects(t.getRect()) && t.isalive() && this.okay != t.isokay()) {
			BombTank e = new BombTank(t.getX(), t.getY(), tc);
			tc.bombTanks.add(e);
			if(t.isokay()) {
				t.setLife(t.getLife() - 1); 
				if (t.getLife() <= 0)
					t.setalive(false); 
			} else {
				t.setalive(false); 
			} this.alive = false;
			return true; 
		}
		return false;
	}

	public boolean TankSUICIDE(Tank t) { 
		if(this.alive && this.getRect().intersects(t.getRect()) && t.isalive()) {
			BombTank e = new BombTank(t.getX(), t.getY(), tc);
			tc.bombTanks.add(e);
			
			
			t.setLife(t.getLife() - 10); 
			if(t.getLife() <= 0){			

			t.setalive(false); 
			this.alive = false;
			return true;
			}
			this.alive = false;
			return false;
		}
		return false;
	}


//MUST HIT THE WALL TEN FN TIMES
public boolean hitWall(NormalBrick w) { 
	if (this.alive && this.getRect().intersects(w.getRect())) {
			this.alive = false;
			
			w.setLife(w.getLife() - 1); 
			if(w.getLife() <= 0){			

			this.tc.otherWall.remove(w); 
			this.tc.baseCampWall.remove(w);
			return true;
			}
			return false;
		}
		return false;
	}
//-------------------------eto na ng tama


//SPECIAL HITTING **Peircing Bullet. Destoys all destroyable objects in a line.	
	public boolean hitWall2(NormalBrick w) { 
		if(this.alive && this.getRect().intersects(w.getRect()) && w.iswhole() && this.okay != w.isokay()) {
			BombTank e = new BombTank(w.getX(), w.getY(), tc); //explodestuff
			tc.bombTanks.add(e);				//also explode stuff

				w.setLife(w.getLife() - 1); 
				if (w.getLife() <= 0)
				w.setalive(false);
				this.tc.otherWall.remove(w); 
				this.tc.baseCampWall.remove(w); 
				return true;
			
		}
		return false;
	}	



	public boolean hitBullet(Bullets w){
		if (this.alive && this.getRect().intersects(w.getRect())){
			this.alive = false;
			this.tc.bullets.remove(w);
			return true;
		}
		return false;
	}
	
	public boolean hitWall(MetalWall w) { 
		if (this.alive && this.getRect().intersects(w.getRect())) {
			this.alive = false;
			return true;
		}
		return false;
	}
	


	public boolean hitBaseCamp() { 
		if (this.alive && this.getRect().intersects(tc.baseCamp.getRect())) {
			this.alive = false;
			this.tc.baseCamp.setalive(false); 
			return true;
		}
		return false;
	}

}
