import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class BattleCity extends Frame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static final int frameWidth = 800; 
	public static final int frameLength = 600;
	public static boolean printable = true;
	
	MenuBar menuBase = null;
	Menu menuComp1 = null, menuComp2 = null, menuComp3 = null, menuComp4 = null, menuComp5 = null;
	MenuItem menuPart1 = null, menuPart2 = null, menuPart3 = null, menuPart4 = null, menuPart5 = null,
			menuPart6 = null, menuPart7 = null, menuPart8 = null, menuPart9 = null, menuPart10=null, menuPart11=null;
	Image screenImage = null;
	
	Tank spawnTankPoint = new Tank(280, 560, true, Direction.STOP, this,1);
	Tank spawnTankPoint2 = new Tank(450, 560, true, Direction.STOP, this,2);
	
	Boolean Player2 = false;
	BaseCamp baseCamp = new BaseCamp(373, 557, this);
	Boolean win = false,lose = false;
	
	List<Tank> tanks = new ArrayList<Tank>();
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	List<Bullets> bullets = new ArrayList<Bullets>();
	List<Tree> trees = new ArrayList<Tree>();
	List<Water> water = new ArrayList<Water>();
	List<NormalBrick> baseCampWall = new ArrayList<NormalBrick>();
	List<NormalBrick> otherWall = new ArrayList<NormalBrick>();
	List<MetalWall> metalWall = new ArrayList<MetalWall>();

	public void update(Graphics g) {
		screenImage = this.createImage(frameWidth, frameLength);
		Graphics gps = screenImage.getGraphics();
		Color c = gps.getColor();
		gps.setColor(Color.BLACK);
		gps.fillRect(0, 0, frameWidth, frameLength);
		gps.setColor(c);
		framPaint(gps);
		g.drawImage(screenImage, 0, 0, null);
	}

	public void framPaint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.green); 
		Font f1 = g.getFont();
		
		g.setFont(new Font("Calibri", Font.BOLD, 20));
		if(!Player2) {
			g.drawString("Tanks Left: ", 300, 100);
		} else {
			g.drawString("Tanks Left: ", 200, 100);
		}
		
		g.setFont(new Font("Calibri", Font.ITALIC, 20));
		if(!Player2) {
			g.drawString("" + tanks.size(), 400, 100);
		} else {
			g.drawString("" + tanks.size(), 300, 100);
		}
		
		g.setFont(new Font("Calibri", Font.BOLD, 20));
		if(!Player2) {
			g.drawString("Life: ", 580, 100);
		} else {
			g.drawString("Life: ", 380, 100);
		}
		
		g.setFont(new Font("Calibri", Font.ITALIC, 20));
		if(!Player2) {
			g.drawString("" + spawnTankPoint.getLife(), 630, 100);
		} else {
			g.drawString("Player 1: " + spawnTankPoint.getLife() + "    Player 2: " + spawnTankPoint2.getLife(), 450, 100);
		} g.setFont(f1);
		
	
		if (!Player2){
			if (tanks.size() == 0 && baseCamp.isalive() && spawnTankPoint.isalive() && lose == false) {
				Font f = g.getFont();
				g.setFont(new Font("Calibri", Font.BOLD, 50)); 
				this.otherWall.clear();
				g.drawString("Congrats, War Freak!", 200, 300);
				g.setFont(f);
				win = true;
								
			}

			if (spawnTankPoint.isalive() == false && win == false) {
					Font f = g.getFont();
					g.setFont(new Font("Calibri", Font.BOLD, 50));
					tanks.clear();
					bullets.clear();
					g.drawString("Get back in there, Soldier!", 140, 300);
					lose = true;
					g.setFont(f);
					
				
			}
		} else {
			if (tanks.size() == 0 && baseCamp.isalive() && (spawnTankPoint.isalive()||spawnTankPoint2.isalive()) && lose == false) {
				Font f = g.getFont();
				g.setFont(new Font("Calibri", Font.BOLD, 40));
				this.otherWall.clear();
				g.drawString("Congrats, War Freak! ", 200, 300);
				g.setFont(f);
				win = true;
			}

			if (spawnTankPoint.isalive() == false && spawnTankPoint2.isalive() == false && win == false) {
				Font f = g.getFont();
				g.setFont(new Font("Calibri", Font.BOLD, 50));
				tanks.clear();
				bullets.clear();
				g.drawString("Get back in there, Soldier!", 140, 300);
				System.out.println("2");
				g.setFont(f);
				lose = true;
			}
		}
		g.setColor(c);

		baseCamp.draw(g); 
		spawnTankPoint.draw(g);
		if (Player2) {
			spawnTankPoint2.draw(g);
		}
		
		for (int i = 0; i < bullets.size(); i++) { 
			Bullets m = bullets.get(i);
			m.hitTanks(tanks); 
			m.hitTank(spawnTankPoint);
			m.hitTank(spawnTankPoint2);
			m.hitBaseCamp(); 
			for(int j = 0; j < bullets.size(); j++){
				if (i ==j) continue;
				Bullets bts = bullets.get(j);
				m.hitBullet(bts);
			}
			for (int j = 0; j < metalWall.size(); j++) { 
				MetalWall mw = metalWall.get(j);
				m.hitWall(mw);
			}

			for (int j = 0; j < otherWall.size(); j++) {
				NormalBrick b = otherWall.get(j);
				m.hitWall(b);
			}

			for (int j = 0; j < baseCampWall.size(); j++) {
				NormalBrick nb = baseCampWall.get(j);
				m.hitWall(nb);
			}
			m.draw(g); 
		}

		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i); 

			for (int j = 0; j < baseCampWall.size(); j++) {
				NormalBrick nb = baseCampWall.get(j);
				t.wallCollisionCheck(nb); 
				nb.draw(g);
			}
			for (int j = 0; j < otherWall.size(); j++) { 
				NormalBrick nb = otherWall.get(j);
				t.wallCollisionCheck(nb);
				nb.draw(g);
			}
			for (int j = 0; j < metalWall.size(); j++) {
				MetalWall mw = metalWall.get(j);
				t.wallCollisionCheck(mw);
				mw.draw(g);
			}
			for (int j = 0; j < trees.size(); j++) {
				Tree tr = trees.get(j);
				t.wallCollisionCheck(tr);
				tr.draw(g);
			}
			

			t.tankCollisionCheck(tanks);
			t.baseCampCollisionCheck(baseCamp);
			t.draw(g);
		}

		for (int i = 0; i < trees.size(); i++) { 
			Tree tr = trees.get(i);
			tr.draw(g);
		}

		for (int i = 0; i < bombTanks.size(); i++) { 
			BombTank bt = bombTanks.get(i);
			bt.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) { 
			NormalBrick nb = otherWall.get(i);
			nb.draw(g);
		}

		for (int i = 0; i < metalWall.size(); i++) { 
			MetalWall mw = metalWall.get(i);
			mw.draw(g);
		}

		spawnTankPoint.tankCollisionCheck(tanks);
		spawnTankPoint.baseCampCollisionCheck(baseCamp);
		if (Player2) {spawnTankPoint2.tankCollisionCheck(tanks);
		spawnTankPoint2.baseCampCollisionCheck(baseCamp);}

		for (int i = 0; i < metalWall.size(); i++) {
			MetalWall w = metalWall.get(i);
			spawnTankPoint.wallCollisionCheck(w);
			if (Player2)spawnTankPoint2.wallCollisionCheck(w);
			w.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) {
			NormalBrick nb = otherWall.get(i);
			spawnTankPoint.wallCollisionCheck(nb);
			if (Player2)spawnTankPoint2.wallCollisionCheck(nb);
			nb.draw(g);
		}

		for (int i = 0; i < baseCampWall.size(); i++) {
			NormalBrick w = baseCampWall.get(i);
			spawnTankPoint.wallCollisionCheck(w);
			if (Player2)spawnTankPoint2.wallCollisionCheck(w);
			w.draw(g);
		}

	}

	public BattleCity() {
		menuBase = new MenuBar();
		menuComp1 = new Menu("Game");
		menuComp2 = new Menu("Pause/Continue");
		menuComp3 = new Menu("Help");
		menuComp4 = new Menu("Map");
		menuComp5 = new Menu("Addition");
		menuComp1.setFont(new Font("Calibri", Font.BOLD, 15));
		menuComp2.setFont(new Font("Calibri", Font.BOLD, 15));
		menuComp3.setFont(new Font("Calibri", Font.BOLD, 15));
		menuComp4.setFont(new Font("Calibri", Font.BOLD, 15));
		menuComp5.setFont(new Font("Calibri", Font.BOLD, 15));

		menuPart1 = new MenuItem("New Game");
		menuPart2 = new MenuItem("Exit");
		menuPart3 = new MenuItem("Stop");
		menuPart4 = new MenuItem("Continue");
		menuPart5 = new MenuItem("Game Instructions");
		menuPart6 = new MenuItem("Map 1");
		menuPart7 = new MenuItem("Map 2");
		menuPart8 = new MenuItem("Map 3");
		menuPart9 = new MenuItem("Map 4");
		menuPart10 = new MenuItem("Add Player 2");
		menuPart1.setFont(new Font("Calibri", Font.BOLD, 15));
		menuPart2.setFont(new Font("Calibri", Font.BOLD, 15));
		menuPart3.setFont(new Font("Calibri", Font.BOLD, 15));
		menuPart4.setFont(new Font("Calibri", Font.BOLD, 15));
		menuPart5.setFont(new Font("Calibri", Font.BOLD, 15));

		menuComp1.add(menuPart1);
		menuComp1.add(menuPart2);
		menuComp2.add(menuPart3);
		menuComp2.add(menuPart4);
		menuComp3.add(menuPart5);
		menuComp4.add(menuPart6);
		menuComp4.add(menuPart7);
		menuComp4.add(menuPart8);
		menuComp4.add(menuPart9);
		menuComp5.add(menuPart10);

		menuBase.add(menuComp1);
		menuBase.add(menuComp2);

		menuBase.add(menuComp4);
		menuBase.add(menuComp5);
		menuBase.add(menuComp3);

		menuPart1.addActionListener(this);
		menuPart1.setActionCommand("NewGame");
		menuPart2.addActionListener(this);
		menuPart2.setActionCommand("Exit");
		menuPart3.addActionListener(this);
		menuPart3.setActionCommand("Stop");
		menuPart4.addActionListener(this);
		menuPart4.setActionCommand("Continue");
		menuPart5.addActionListener(this);
		menuPart5.setActionCommand("Game Instructions");
		menuPart6.addActionListener(this);
		menuPart6.setActionCommand("Map1");
		menuPart7.addActionListener(this);
		menuPart7.setActionCommand("Map2");
		menuPart8.addActionListener(this);
		menuPart8.setActionCommand("Map3");
		menuPart9.addActionListener(this);
		menuPart9.setActionCommand("Map4");
		menuPart10.addActionListener(this);
		menuPart10.setActionCommand("Player2");

		this.setMenuBar(menuBase);
		this.setVisible(true);

	//-------------------------------------------this is the map-------------------------	
		for (int i = 0; i < 10; i++) { 
			if (i < 4)
				baseCampWall.add(new NormalBrick(350, 580 - 21 * i, this)); //left up
			else if (i < 7)
				baseCampWall.add(new NormalBrick(372 + 22 * (i - 4), 517, this)); //up horizontal
			else
				baseCampWall.add(new NormalBrick(416, 538 + (i - 7) * 21, this));//right up
		}
	//------------------------------------------BAse CAMP wall ---------------------------	
	
	
		for (int i = 0; i < 32; i++) {
			if (i < 32) {
				trees.add(new Tree(0 + 30 * i, 365, this)); 
				trees.add(new Tree(0 + 30 * i, 335, this)); 
				trees.add(new Tree(0 + 30 * i, 305, this)); 

				//otherWall.add(new NormalBrick(0 + 15 * i, 210, this));
				//otherWall.add(new NormalBrick(0 + 15 * i, 230, this));
				//otherWall.add(new NormalBrick(200, 400 + 21 * i, this));
				//otherWall.add(new NormalBrick(500, 400 + 21 * i, this));
			}//this is the first linear wall next to the base from the left
			
			else if (i < 32) {//this also, for some reason, makes a linear wall of the same size as the horizontal wall next to the base
				otherWall.add(new NormalBrick(220 + 21 * (i - 16), 320, this));//mid horizontal wall upperhalf
				
				otherWall.add(new NormalBrick(320 + 15 * i, 210, this));//horiontal walls
				otherWall.add(new NormalBrick(320 + 15 * i, 230, this));
				
				otherWall.add(new NormalBrick(250, 400 + 21 * (i - 16), this));//vertical walls
				otherWall.add(new NormalBrick(550, 400 + 21 * (i - 16), this));
			}//this is the 2nd linear wall next to the base
		}

		for (int i = 0; i < 10; i++) { 
			if (i < 5) {//makes 5 metal walls on both sides
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
			} else if (i < 10)//puts a gab in between
				metalWall.add(new MetalWall(340 + 30 * (i), 150, this));
		}

		for (int i = 0; i < 8; i++) { 
			if (i < 4) { //-------------prints 4 trees 4 times with spaces in between
				//trees.add(new Tree(0 + 30 * i, 360, this));
				//trees.add(new Tree(220 + 30 * i, 360, this));
				//trees.add(new Tree(440 + 30 * i, 360, this));
				//trees.add(new Tree(670 + 30 * i, 360, this));
			
			//----------these are the four trees next to the metal walls
			} else if (i < 5) {
				//trees.add(new Tree(400 + 61 * i, 150, this));
			} else if (i < 6) {
				//trees.add(new Tree(200 + 51 * i, 150, this));
			} else if (i < 7) {
				//trees.add(new Tree(110 + 31 * i, 150, this));
			} else if (i < 8) {
				//trees.add(new Tree(30 + 11 * i, 150, this));
			}//----------these are the four trees next to the metal walls
		}

		for (int i = 0; i < 15; i++) {
			if(i < 5) {
				tanks.add(new Tank(200 + 70 * i, 40, false, Direction.D, this,0));
			} else if(i < 10) {
				tanks.add(new Tank(700, 300 + 50 * (i - 5), false, Direction.L, this,0));
			} else {
				tanks.add(new Tank(10, 50 + 50 * (i - 5), false, Direction.R, this,0));
			}
		}

		//--------------------------------------------------end of the map
		
		
		this.setSize(frameWidth, frameLength);
		this.setLocation(280, 50); 
		this.setTitle("CMSC137: SHOOKs To GO");

		this.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		this.setResizable(false);
		this.setBackground(Color.GRAY);
		this.setVisible(true);
		this.addKeyListener(new KeyTracker());
		new Thread(new PaintThread()).start(); 
	}

	private class PaintThread implements Runnable {
		public void run() {
			while (printable) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyTracker extends KeyAdapter {
		public void keyReleased(KeyEvent e) { 
			spawnTankPoint.keyReleased(e);
			spawnTankPoint2.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			spawnTankPoint.keyPressed(e);
			spawnTankPoint2.keyPressed(e);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "Confirm", "Cancel" };
			int choice = JOptionPane.showOptionDialog(this, "Are yous sure you want to start a new game?", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
					
			if (choice == 0) {
				printable = true;
				this.dispose();
				new BattleCity();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); 
			}
		} else if (e.getActionCommand().endsWith("Stop")) {
			printable = false;
		} else if (e.getActionCommand().equals("Continue")) {
			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "Confirm", "Cancel" };
			int choice = JOptionPane.showOptionDialog(this, "Leave game?", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (choice == 0) {
				System.out.println("dead");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); 
			}
		} else if(e.getActionCommand().equals("Player2")){
			printable = false;
			Object[] options = { "Confirm", "Cancel" };
			int choice = JOptionPane.showOptionDialog(this, "Add a second player?", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (choice == 0) {
				printable = true;
				this.dispose();
				BattleCity Player2add = new BattleCity();
				Player2add.Player2 = true;
			} else {
				printable = true;
				new Thread(new PaintThread()).start();
			}
		} else if (e.getActionCommand().equals("Game Instructions")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "PLAYER 1: WASD to move and E to fire \nPLAYER 2: Direction Keys to move and spacebar to fire",
					"Game Instructions", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); 
		} else if (e.getActionCommand().equals("Map1")) {
			this.setVisible(false);
			new BattleCity();
		} else if (e.getActionCommand().equals("Map2")) {
			this.setVisible(false);
			new BattleCity();
		} else if (e.getActionCommand().equals("Map3")) {
			this.setVisible(false);
			new BattleCity();
		} else if (e.getActionCommand().equals("Map4")) {
			this.setVisible(false);
			new BattleCity();
		}
	}
}
