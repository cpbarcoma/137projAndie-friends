import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
//import java.awt.Toolkit;

/*
dagdag
*/
import java.awt.event.*;
import java.awt.*;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

// Randomly generating numbers for names at the start
import java.util.Random;

/**
 * The game client itself!
 * @author Joseph Anthony C. Hermocilla
 *
 */

// Imports for Timer
import java.util.Timer;
import java.util.TimerTask;

public class CircleWars extends JPanel implements Runnable, Constants{
	/**
	 * Main window
	 */
	JFrame frame= new JFrame();
	
//	Toolkit toolkit = Toolkit.getDefaultToolkit();


	/**
	 * Player position, speed etc.
	 */
	int x=0,y=0, xspeed=4,yspeed=4,prevX,prevY;


	/*
	new specs for tank
	*/
	boolean canShoot = true;
	boolean alive=true;
	int score=0, directionTank=2; //direction tank==2 ay tank na nakaturo upwards
	int team;//0 ay BLUE team, 1 ay RED team
	int health = 10; //
	int total;
	int initPosition = 0;
	/*
	if(team==0){
	int x=500,y=400;
	}*/

	// for holding the players
	String[] playerInfo, playersInfo;

	/**
	 * Game timer, handler receives data from server to update game state
	 */
	Thread t=new Thread(this);

	//for chat client
	ChatClient th;
	
	/**
	 * Nice name!
	 */
	String name="";
	
	/**
	 * Player name of others
	 */
	String pname;
	
	/**
	 * Server to connect to
	 */
	String server="";

	/**
	 * Flag to indicate whether this player has connected or not
	 */
	boolean connected=false;
	
	/**
	 * get a datagram socket
	 */
    DatagramSocket socket = new DatagramSocket();

	
    /**
     * Placeholder for data received from server
     */
	String serverData;
	
	/**
	 * Offscreen image for double buffering, for some
	 * real smooth animation :)
	 */
	BufferedImage offscreen;

	// Additional UI elements
	JPanel northPanel, chatPanel;
	JPanel buttonPanel, scorePanel, timePanel;
	JButton exitBtn, helpBtn, sendChat;
	JTextField chatTextField;
	JTextArea chatArea;
	JScrollPane chatScrollPane;
	JLabel redScore, blueScore, timeRem;
	String redScoreTxt, blueScoreTxt, timeRemTxt, origTime;

	// South panel elements
	JPanel southPanel;
	// Temporary solution: Buttons or pure text
	BasicArrowButton[] instBtn = new BasicArrowButton[4];
	JLabel instMove, instShoot, instChat;
    
    // Used to randomly generate a "name"
    Random rand = new Random();

    TankDrawing td;
    Hook hook;
    Boolean isShooting = false;

	/**
	 * Basic constructor
	 * @param server
	 * @param name
	 * @throws Exception
	 */
	public CircleWars(String server, String portNo) throws Exception{
		this.server = server;
		while (this.name.equals("")) {
			this.name = JOptionPane.showInputDialog(null,
													"Enter your name:",
													"SHOOKT",
													JOptionPane.PLAIN_MESSAGE);
		}

		JOptionPane.showMessageDialog(frame,
					new PopUp().getHelpPanel(),
					"INSTRUCTIONS",
					JOptionPane.PLAIN_MESSAGE);
		
		this.setLayout(new BorderLayout());


		frame.setTitle(APP_NAME+":"+name);
		//set some timeout for the socket
		socket.setSoTimeout(100);
		
		//Some gui stuff i hate.
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(1100, 600);

		// Set up additional UI elements
		northPanel = new JPanel(new GridLayout(1, 3));
		northPanel = setupNorth(northPanel);

		southPanel = new JPanel(new FlowLayout());
		southPanel = setupSouth(southPanel);

		chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		sendChat = new JButton("SEND");
		chatTextField = new JTextField(10);
		chatArea = new JTextArea(33, 20);	//to adjust sizes in chat //33, 20
		//chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		chatScrollPane = new JScrollPane(chatArea);

		//button listener for send
		sendChat.addActionListener(new ActionListener() { 
	  		public void actionPerformed(ActionEvent e) { 
				th.sendChatData(chatTextField.getText()+"\n");				
				chatTextField.setText("");
	  		} 
		} );

		//always set chat to bottom scroll
		chatScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
       		public void adjustmentValueChanged(AdjustmentEvent e) {  
           		e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
        	}
  		});

		//chatTextField.setWrapStyleWord(true);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatTextField.addKeyListener(sendViaEnter());
		
		chatPanel.add(sendChat, BorderLayout.CENTER);
		chatPanel.add(chatTextField, BorderLayout.WEST);
		chatPanel.add(chatScrollPane, BorderLayout.NORTH);
				

		this.add(chatPanel, BorderLayout.EAST);
		this.add(northPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);

		//frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.requestFocus();
		frame.addKeyListener(pressTab());
		
		//create the buffer
		offscreen=(BufferedImage)this.createImage(1100, 600);
		
		//Some gui stuff again...
		frame.addKeyListener(new KeyHandler());		
		// frame.addMouseMotionListener(new MouseMotionHandler());

		// add stuff to draw
		td = new TankDrawing();
		hook = new Hook();

	
		/*
		 * create chatClient. chatClient is now a thread itself
		 */
		th = new ChatClient(server, name, portNo, chatArea);



		//time to play
		t.start();
	}

	//keylistener for Enter button
	private KeyListener sendViaEnter(){
	KeyListener actionPerformed = new KeyListener(){
		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				//System.out.println(chatTextField.getText());				
				th.sendChatData(chatTextField.getText()+"\n");				
				chatTextField.setText("");
				frame.requestFocus();
			}
		}
		@Override
		public void keyTyped(KeyEvent e){}
		@Override
		public void keyReleased(KeyEvent e){}

		};
		return actionPerformed;
	}

	//keylistener for SPACE button
	//set focus on chat
	private KeyListener pressTab(){
	KeyListener actionPerformed = new KeyListener(){
		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_SPACE){			
				chatTextField.requestFocus();
			}
		}
		@Override
		public void keyTyped(KeyEvent e){}
		@Override
		public void keyReleased(KeyEvent e){}

		};
		return actionPerformed;
	}

	

	// set up North panel
	public JPanel setupNorth(JPanel np) {
		redScoreTxt = "0";
		blueScoreTxt = "0";
		timeRemTxt = "";
		PopUp helpPanel = new PopUp();

		// Button panel
		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(52, 56, 63));
		exitBtn = new JButton("EXIT");
		exitBtn.setBackground(new Color(122, 122, 122));
		exitBtn.setForeground(Color.WHITE);
		// Exiting the game
		exitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int exit = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to exit the game?",
					"Exit",
					JOptionPane.YES_NO_OPTION);
				if (exit == 0) {
					frame.dispose();
					System.exit(0);
				} else {
					frame.requestFocus();
				}
			}
		});

		helpBtn = new JButton("HELP");
		helpBtn.setBackground(new Color(122, 122, 122));
		helpBtn.setForeground(Color.WHITE);
		// Opening a new window/showing game mechanics
		helpBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int choice = JOptionPane.showConfirmDialog(frame,
					helpPanel.getHelpPanel(),
					"INSTRUCTIONS",
					JOptionPane.PLAIN_MESSAGE);
				if (choice == JOptionPane.CLOSED_OPTION
					|| choice == JOptionPane.CANCEL_OPTION
					|| choice == JOptionPane.YES_OPTION
					|| choice == JOptionPane.NO_OPTION) {
					frame.requestFocus();
				}
			}
		});

		buttonPanel.add(exitBtn);
		buttonPanel.add(helpBtn);

		// Score panel
		scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		scorePanel.setBackground(new Color(52, 56, 63));
		redScore = new JLabel("RED " + redScoreTxt);
		redScore.setForeground(Color.RED);
		blueScore = new JLabel(blueScoreTxt + " BLUE");
		blueScore.setForeground(Color.CYAN);
		scorePanel.add(redScore);
		scorePanel.add(blueScore);
		
		// Time panel
		timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		timePanel.setBackground(new Color(52, 56, 63));
		timeRem = new JLabel("TIME: " + timeRemTxt);
		timeRem.setForeground(Color.WHITE);
		timePanel.add(timeRem);

		np.add(buttonPanel);
		np.add(scorePanel);
		np.add(timePanel);

		return np;
	}

	// set up South panel
	public JPanel setupSouth(JPanel sp) {
		// Instructions: buttons and text
		// get pictures of arrow buttons
		instBtn[0] = new BasicArrowButton(BasicArrowButton.NORTH,
											new Color(122, 122, 122),
											new Color(122, 122, 122),
											Color.WHITE,
											new Color(132, 132, 132));
		instBtn[1] = new BasicArrowButton(BasicArrowButton.SOUTH,
											new Color(122, 122, 122),
											new Color(122, 122, 122),
											Color.WHITE,
											new Color(132, 132, 132));
		instBtn[2] = new BasicArrowButton(BasicArrowButton.WEST,
											new Color(122, 122, 122),
											new Color(122, 122, 122),
											Color.WHITE,
											new Color(132, 132, 132));
		instBtn[3] = new BasicArrowButton(BasicArrowButton.EAST,
											new Color(122, 122, 122),
											new Color(122, 122, 122),
											Color.WHITE,
											new Color(132, 132, 132));

		instMove = new JLabel("MOVEMENT:");
		instMove.setForeground(Color.WHITE);
		instShoot = new JLabel("SHOOT: /");
		instShoot.setForeground(Color.WHITE);
		instChat = new JLabel("CHAT: SPACEBAR");
		instChat.setForeground(Color.WHITE);

		sp.add(instMove);
		for (int i=0; i<4; i++) {
			sp.add(instBtn[i]);
		}
		sp.add(instShoot);
		sp.add(instChat);

		sp.setBackground(new Color(52, 56, 63));
		return sp; //WAHHHHH
	}
	
	/**
	 * Helper method for sending data to server
	 * @param msg
	 */

	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	
	/**
	 * The juicy part!
	 */

	public void run(){
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			socket.receive(packet);
			}catch(Exception ioe){/*lazy exception handling :)*/}
			


			//dito nakukuha ung data
			serverData=new String(buf);
			serverData=serverData.trim();
			
			if (!serverData.equals("")){
				System.out.println("Server Data:" +serverData);
			}

			// Check timer
			if (serverData.startsWith("SECONDS_REMAINING")) {
				String[] spl = serverData.split(" ");
				timeRemTxt = spl[1];
				origTime = spl[2];

				if(Float.parseFloat(timeRemTxt) == 0) {
					timeRem.setText("TIME'S UP!");
					showEndGamePopUp();
				} else {
					timeRem.setText("TIME: " + timeRemTxt);
				}

			}


			//Study the following kids. 
			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");
			}else if (!connected){
				
				System.out.println("Connecting..");				
				send("CONNECT "+name);
			}else if (connected){
				
				//clears the rectangle
				offscreen.getGraphics().clearRect(0, 0, 1100, 600);
				
				  

				if (serverData.startsWith("PLAYER")){
					playersInfo = serverData.split(":");
					for (int i=0;i<playersInfo.length;i++){
						playerInfo = playersInfo[i].split(" ");
						String pname =playerInfo[1];
						int x = Integer.parseInt(playerInfo[2]);
						int y = Integer.parseInt(playerInfo[3]);
						int directionTank = Integer.parseInt(playerInfo[4]);
						int team = Integer.parseInt(playerInfo[5]);
						int initPosition = Integer.parseInt(playerInfo[6]);
						int health = Integer.parseInt(playerInfo[7].trim());

						
						if(this.name.equals(pname)){
							System.out.println("Name: "+pname+"|Team:"+i%2);
						
							this.team = i%2;
							team = i % 2;
						}

						
						try{
							/*
							BufferedImage imgEnemy = ImageIO.read(new File("tanks/tankEnemy.gif"));						
							
							//kelangan ilagay to para me tank rin ung kabilang team.
							offscreen.getGraphics().drawImage(imgEnemy, x, y, 100, 100, this);
							*///old
							BufferedImage imgUp = ImageIO.read(new File("tanks/tankRu.png"));
					        BufferedImage imgDown = ImageIO.read(new File("tanks/tankRu.png"));
					        BufferedImage imgLeft = ImageIO.read(new File("tanks/tankLeft.png"));
					        BufferedImage imgRight = ImageIO.read(new File("tanks/tankRight.png"));

							BufferedImage imgEnemyUp = ImageIO.read(new File("tanks/tankEnemyDown.png"));
					        BufferedImage imgEnemyDown = ImageIO.read(new File("tanks/tankEnemyDown.png"));
					        BufferedImage imgEnemyLeft = ImageIO.read(new File("tanks/tankEnemyLeft.png"));
					        BufferedImage imgEnemyRight = ImageIO.read(new File("tanks/tankEnemyRight.png"));

		       				BufferedImage green = ImageIO.read(new File("tanks/fullLife.gif"));
					        BufferedImage blue = ImageIO.read(new File("tanks/2Life.gif"));
					        BufferedImage red = ImageIO.read(new File("tanks/3Life.gif"));

					        // BufferedImage water = ImageIO.read(new File("tanks/water.jpg"));
	       					// offscreen.getGraphics().drawImage(water, 0, 200, 1100, 200, this);

					        /*	
						   	if(directionTank==0){	//default			
								offscreen.getGraphics().drawImage(imgEnemyUp, x, y, 100, 100, this);
								}
							*/	

							if(team == 0){ // blue
							 	if(initPosition<2){
							 	x = 100;
							 	y = 80;
							 	}

								if(directionTank==1){				
									offscreen.getGraphics().drawImage(imgEnemyDown, x, y, 100, 100, this);
									}

								if(directionTank==2){				
									offscreen.getGraphics().drawImage(imgEnemyDown, x, y, 100, 100, this);
									}
								
								if(directionTank==3){
									offscreen.getGraphics().drawImage(imgEnemyLeft, x, y, 100, 100, this);
									}

								if(directionTank==4){
									offscreen.getGraphics().drawImage(imgEnemyRight, x, y, 100, 100, this);
									}
							} else if(team==1){
								if(initPosition<2){
									x = 900;
									y = 400;
									}
								if(directionTank==1){				
									offscreen.getGraphics().drawImage(imgUp, x, y, 100, 100, this);
								}
								if(directionTank==2){				
									offscreen.getGraphics().drawImage(imgUp, x, y, 100, 100, this);
									}
								
								if(directionTank==3){
									offscreen.getGraphics().drawImage(imgLeft, x, y, 100, 100, this);
									}

								if(directionTank==4){
									offscreen.getGraphics().drawImage(imgRight, x, y, 100, 100, this);
									}
							}



							
							//DRAWs the health bars	
							if(health >= 7 && health <= 10){			
								offscreen.getGraphics().drawImage(green, x, y+100, 96, 12, this);
								}
							
							if(health >= 4 && health <= 6){
								offscreen.getGraphics().drawImage(blue, x, y+100, 62, 12, this);
								}

							if(health >= 1 && health <= 3){
								offscreen.getGraphics().drawImage(red, x, y+100, 30, 12, this);
								}
							//Draws the health bars
								

								//String Print 
								String healthState="Health: ";
								String scoreState="Score ";
								healthState+=health;
								scoreState+=score;


								offscreen.getGraphics().drawString(pname,x-40,y+45);
								offscreen.getGraphics().drawString(healthState,x-55,y+65);
								offscreen.getGraphics().drawString(scoreState,x-49,y+85);
								
								System.out.println("Player: "+pname+" Team: "+team+" i:"+i);

							 } catch (Exception ex){
						   ex.printStackTrace();
						 }					
					
					}

					System.out.println("===================================");

					//show the changes
					frame.repaint();
				}			
			}			
		}
	}

	public void showEndGamePopUp() {
		PopUp endGame = new PopUp();
		endGame.setWinner(1); // FIX: Should get actual winner
		endGame.setPlayers(playersInfo);
		JOptionPane.showMessageDialog(frame,
			endGame.getEndPanel(),
			"GAME END",
			JOptionPane.PLAIN_MESSAGE);
	}

	public class TankDrawing {

		BufferedImage imgUp;
        BufferedImage imgDown;
        BufferedImage imgLeft;
        BufferedImage imgRight;

        BufferedImage imgEnemyUp;
        BufferedImage imgEnemyDown;
        BufferedImage imgEnemyLeft;
        BufferedImage imgEnemyRight;

        BufferedImage green;
        BufferedImage blue;
        BufferedImage red;

		public void render(Graphics g) {
			try {

				green = ImageIO.read(new File("tanks/fullLife.gif"));
				blue = ImageIO.read(new File("tanks/2Life.gif"));
				red = ImageIO.read(new File("tanks/3Life.gif"));


				imgEnemyUp = ImageIO.read(new File("tanks/tankEnemyDown.png"));
		        imgEnemyDown = ImageIO.read(new File("tanks/tankEnemyDown.png"));
		        imgEnemyLeft = ImageIO.read(new File("tanks/tankEnemyLeft.png"));
		        imgEnemyRight = ImageIO.read(new File("tanks/tankEnemyRight.png"));	
			
				imgUp = ImageIO.read(new File("tanks/tankRu.png"));
		        imgDown = ImageIO.read(new File("tanks/tankRu.png"));
		        imgLeft = ImageIO.read(new File("tanks/tankLeft.png"));
		        imgRight = ImageIO.read(new File("tanks/tankRight.png"));
				
				
				if(team == 0){ // blue
				 	if(initPosition<2){
				 	x = 100;
				 	y = 80;
				 	}

					if(directionTank==1){				
						g.drawImage(imgEnemyDown, x, y, 100, 100, null);
						}

					if(directionTank==2){				
						g.drawImage(imgEnemyDown, x, y, 100, 100, null);
						}
					
					if(directionTank==3){
						g.drawImage(imgEnemyLeft, x, y, 100, 100, null);
						}

					if(directionTank==4){
						g.drawImage(imgEnemyRight, x, y, 100, 100, null);
						}
				
				} else if(team==1){
					if(initPosition<2){
							x = 900;
						y = 400;
						}
					if(directionTank==1){				
						g.drawImage(imgUp, x, y, 100, 100, null);
					}
					if(directionTank==2){				
						g.drawImage(imgUp, x, y, 100, 100, null);
						}
					
					if(directionTank==3){
						g.drawImage(imgLeft, x, y, 100, 100, null);
						}

					if(directionTank==4){
						g.drawImage(imgRight, x, y, 100, 100, null);
						}
				}

				if(health >= 7 && health <= 10){			
					g.drawImage(green, x, y+100, 96, 12, null);
					}
				
				if(health >= 4 && health <= 6){
					g.drawImage(blue, x, y+100, 62, 12, null);
					}

				if(health >= 1 && health <= 3){
					g.drawImage(red, x, y+100, 30, 12, null);
					}
			//Draws the health bars
				

				//String Print 
				String healthState="Health: ";
				String scoreState="Score ";
				healthState+=health;
				scoreState+=score;


				g.drawString(pname,x-40,y+45);
				g.drawString(healthState,x-55,y+65);
				g.drawString(scoreState,x-49,y+85);
				
			} catch(Exception e){}
		}
	}

	public class Hook{
		int x, y, orig;
		Boolean xreverse = false;
		Boolean yreverse = false;

		public void render(Graphics g) {
			g.fillOval(this.x, this.y, 10, 10);
		}

		// side to side
		public void xset(int x, int y) {
			this.x += x;
			this.y = y-20;
		}

		public void xreverse(int x, int y) {
			this.x -= x;
			this.y = y-20;
		}

		// shooting mode
		public void yset(int x, int y) {
			this.x = x;
			this.y -= y;
		}

		public void yreverse(int x, int y) {
			this.x = x;
			this.y += y;
		}

		public int getx() {
			return this.x;
		}

		public int gety() {
			return this.y;
		}

		public void setXReverse(Boolean v) {
			this.xreverse = v;
		}

		public Boolean getXReverse() {
			return this.xreverse;
		}

		public void setYReverse(Boolean v) {
			this.yreverse = v;
		}

		public Boolean getYReverse() {
			return this.yreverse;
		}
	}

	/**
	 * Repainting method for THIS PLAYER ONLY!!
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(offscreen, 0, 0, null);
		try {
		       
		    //use this 
	       	BufferedImage grass = ImageIO.read(new File("tanks/grass.png"));
	       	g.drawImage(grass, 0, 0, 1100, 600, this);
	       	BufferedImage water = ImageIO.read(new File("tanks/water.jpg"));
	       	g.drawImage(water, 0, 200, 1100, 200, this);
	       	
        
        } catch (Exception ex){
        ex.printStackTrace();
    	}

		td.render(g);
		new Thread(new Runnable() {
			public void run() {
				// make this visible to all players
				if (team == 1) { // for red team
					if (isShooting == false) {
						if (hook.getXReverse() == false) {
							while(hook.getx() < x+90) {
								try {
									if (isShooting == true) break;
									hook.xset(1, y);
									hook.render(g); // invokes paintComponent() again
									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							hook.setXReverse(true);
						} else {
							while(hook.getx() > x) {
								try {
									if (isShooting == true) break;
									hook.xreverse(1, y);
									hook.render(g); // invokes paintComponent() again
									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							hook.setXReverse(false);
						}
					} else { // shooting mode
						if (hook.getYReverse() == false) {
							while(hook.gety() > y-300) {
								try {
									hook.yset(hook.getx(), 1);
									hook.render(g); // invokes paintComponent() again

									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							for (int i=0;i<playersInfo.length;i++){
								playerInfo = playersInfo[i].split(" ");
								String pname =playerInfo[1];
								int x = Integer.parseInt(playerInfo[2]);
								int y = Integer.parseInt(playerInfo[3]);
								int directionTank = Integer.parseInt(playerInfo[4]);
								int team = Integer.parseInt(playerInfo[5]);
								int initPosition = Integer.parseInt(playerInfo[6]);
								int health = Integer.parseInt(playerInfo[7].trim());

								// damage to other players
								// must show on their own screens that they are getting hit
								// problem w/ data being updated by send()?
								if (hook.getx() >= x
									&& hook.getx() <= x+100 
									&& hook.gety() >= y
									&& hook.gety() <= y+100) {
									health -=1;
									System.out.println("YOU JUST SHOOKT " + pname);
								}
								send("PLAYER "+pname+" "+x+" "+y+" "+directionTank+" "+team+" "+initPosition+" "+health);
							}
							hook.setYReverse(true);
						} else {
							while(hook.gety() < y) {
								try {
									hook.yreverse(hook.getx(), 1);
									hook.render(g); // invokes paintComponent() again
									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							hook.setYReverse(false);
							isShooting = false;
							canShoot = true;
						}
					}
				}


				else if (team == 0) { // for blue team, who shoots downwards
					int orig = y+100;
					if (isShooting == false) {
						if (hook.getXReverse() == false) {
							while(hook.getx() < x+90) {
								try {
									if (isShooting == true) break;
									hook.xset(1, orig);
									hook.render(g); // invokes paintComponent() again
									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							hook.setXReverse(true);
						} else {
							while(hook.getx() > x) {
								try {
									if (isShooting == true) break;
									hook.xreverse(1, orig);
									hook.render(g); // invokes paintComponent() again
									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							hook.setXReverse(false);
						}
					} else { // shooting mode
						if (hook.getYReverse() == false) {
							while(hook.gety() < orig+300) {
								try {
									hook.yreverse(hook.getx(), 1);
									hook.render(g); // invokes paintComponent() again

									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							for (int i=0;i<playersInfo.length;i++){
								playerInfo = playersInfo[i].split(" ");
								String pname =playerInfo[1];
								int x = Integer.parseInt(playerInfo[2]);
								int y = Integer.parseInt(playerInfo[3]);
								int directionTank = Integer.parseInt(playerInfo[4]);
								int team = Integer.parseInt(playerInfo[5]);
								int initPosition = Integer.parseInt(playerInfo[6]);
								int health = Integer.parseInt(playerInfo[7].trim());

								// damage to other players
								// must show on their own screens that they are getting hit
								// problem w/ data being updated by send()?
								if (hook.getx() >= x
									&& hook.getx() <= x+100 
									&& hook.gety() >= orig
									&& hook.gety() <= orig+300) {
									health -=1;
									System.out.println("YOU JUST SHOOKT " + pname);
								}
								send("PLAYER "+pname+" "+x+" "+y+" "+directionTank+" "+team+" "+initPosition+" "+health);
							}
							hook.setYReverse(true);
						} else {
							while(hook.gety() > orig) {
								try {
									hook.yset(hook.getx(), 1);
									hook.render(g); // invokes paintComponent() again
									Thread.sleep(200);
								} catch(Exception e) {  }
							}
							hook.setYReverse(false);
							isShooting = false;
							canShoot = true;
						}
					}
				}
			}
			
		}).start();
		frame.repaint();
		
	}

	//eto ung para sa motion
	class KeyHandler extends KeyAdapter{
		public void keyPressed(KeyEvent ke){
			
			if(initPosition==0 && team==0){
				x = 100;
				y = 80;
				System.out.println("team 1x: "+x+" y: "+y);
			}
			if(initPosition==0 && team==1){
				x = 900;
				y = 400;
				System.out.println("team2 x: "+x+" y: "+y);
			}

			prevX=x;prevY=y;
			switch (ke.getKeyCode()){
			case KeyEvent.VK_DOWN:
				if(team == 0 && y < 150){
					y+=yspeed;
				}
				else if(team == 1 && y < 550){
					y+=yspeed;
				}

				initPosition+=1;
				directionTank=1;
				break;
			case KeyEvent.VK_UP:
				if(team == 0 && y > 0){
					y-=yspeed;
				}
				else if(team == 1 && y > 350){
					y-=yspeed;
				}

				initPosition+=1;
				directionTank=2;
				break;
			case KeyEvent.VK_LEFT:
				if(x > 0 && isShooting == false){
					x-=xspeed;
				}

				initPosition+=1;
				directionTank=3;
				canShoot = false;
				break;
			case KeyEvent.VK_RIGHT:
				if(x < 1050 && isShooting == false){
					x+=xspeed;
				}

				initPosition+=1;
				directionTank=4;
				canShoot = false;
				break;

			// Temporary shoot button
			case KeyEvent.VK_SLASH:
				System.out.println("FIRE FIRE FIRE FIRE");
				isShooting = true;
				break;
			}



			if (prevX != x || prevY != y){
				send("PLAYER "+name+" "+x+" "+y+" "+directionTank+" "+team+" "+initPosition+" "+health);
			}	
		}
	}

	public static void main(String args[]) throws Exception{
		if (args.length != 2){
			System.out.println("Usage: java -jar circlewars-client <server> <portno>");
			System.exit(1);
		}

		new CircleWars(args[0], args[1]);


	}
}
