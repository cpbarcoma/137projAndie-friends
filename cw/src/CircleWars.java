import java.awt.Graphics;
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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
	int x=1200,y=700, xspeed=4,yspeed=4,prevX,prevY;


	/*
	new specs for tank
	*/
	boolean alive=true;
	int score=0, directionTank=2; //direction tank==2 ay tank na nakaturo upwards
	int team = 0;//0 ay RED team, 1 ay Green Team
	int health = 3; //
	int total;
	/*
	if(team==0){
	int x=500,y=400;
	}*/

	/**
	 * Game timer, handler receives data from server to update game state
	 */
	Thread t=new Thread(this);
	
	/**
	 * Nice name!
	 */
	String name="Joseph";
	
	/**
	 * Player name of others
	 */
	String pname;
	
	/**
	 * Server to connect to
	 */
	String server="localhost";

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
	JPanel northPanel;
	JPanel buttonPanel, scorePanel;
	JButton exitBtn, helpBtn;
	JLabel redScore, blueScore, timeRem;
	String redScoreTxt, blueScoreTxt, timeRemTxt;

	// South panel elements
	JPanel southPanel;
	// Temporary solution: Buttons or pure text
	JButton[] instBtn = new JButton[5];
	JLabel[] instText = new JLabel[5];
    
	
	/**
	 * Basic constructor
	 * @param server
	 * @param name
	 * @throws Exception
	 */
	public CircleWars(String server,String name) throws Exception{
		this.server=server;
		this.name=name;
		
		this.setLayout(new BorderLayout());


		frame.setTitle(APP_NAME+":"+name);
		//set some timeout for the socket
		socket.setSoTimeout(100);
		
		//Some gui stuff i hate.
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 600);

		// Set up additional UI elements
		northPanel = new JPanel(new GridLayout(1, 3));
		northPanel = setupNorth(northPanel);

		southPanel = new JPanel(new FlowLayout());
		southPanel = setupSouth(southPanel);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		frame.setFocusable(true);
		frame.setVisible(true);
		
		//create the buffer
		offscreen=(BufferedImage)this.createImage(1100, 600);
		
		//Some gui stuff again...
		frame.addKeyListener(new KeyHandler());		
		// frame.addMouseMotionListener(new MouseMotionHandler());

		//tiime to play
		t.start();		
	}

	// set up North panel
	public JPanel setupNorth(JPanel np) {
		redScoreTxt = "0";
		blueScoreTxt = "0";
		timeRemTxt = "0:00";

		buttonPanel = new JPanel();
		exitBtn = new JButton("EXIT");
		helpBtn = new JButton("HELP");
		buttonPanel.add(exitBtn);
		buttonPanel.add(helpBtn);

		scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		redScore = new JLabel("RED " + redScoreTxt);
		blueScore = new JLabel(blueScoreTxt + " BLUE");
		scorePanel.add(redScore);
		scorePanel.add(blueScore);
		
		timeRem = new JLabel("TIME: " + timeRemTxt);

		np.add(buttonPanel);
		np.add(scorePanel);
		np.add(timeRem);

		return np;
	}

	// set up South panel
	public JPanel setupSouth(JPanel sp) {
		// Instructions: buttons and text
		instBtn[0] = new JButton("UP");
		instBtn[1] = new JButton("DOWN");
		instBtn[2] = new JButton("LEFT");
		instBtn[3] = new JButton("RIGHT");
		instBtn[4] = new JButton("E");

		instText[0] = new JLabel("UP");
		instText[1] = new JLabel("LEFT");
		instText[2] = new JLabel("DOWN");
		instText[3] = new JLabel("RIGHT");
		instText[4] = new JLabel("SHOOT");

		for (int i=0; i<5; i++) {
			sp.add(instBtn[i]);
			sp.add(instText[i]);
		}
		return sp;
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
					String[] playersInfo = serverData.split(":");
					for (int i=0;i<playersInfo.length;i++){
						String[] playerInfo = playersInfo[i].split(" ");
						String pname =playerInfo[1];
						int x = Integer.parseInt(playerInfo[2]);
						int y = Integer.parseInt(playerInfo[3]);
						int directionTank = Integer.parseInt(playerInfo[4]);
						int team = Integer.parseInt(playerInfo[5]);
						int health = Integer.parseInt(playerInfo[6].trim());

						total = playersInfo.length;
						team = i%2;
						System.out.println("THE CURRENT TEAM: "+team+"| TOTAL: "+total);
					//draw on the offscreen image
						//nagdrawing ng oval
						//offscreen.getGraphics().fillOval(x, y, 20, 20);
						
						try{
							/*
							BufferedImage imgEnemy = ImageIO.read(new File("tanks/tankEnemy.gif"));						
							
							//kelangan ilagay to para me tank rin ung kabilang team.
							offscreen.getGraphics().drawImage(imgEnemy, x, y, 100, 100, this);
							*///old
							 BufferedImage imgUp = ImageIO.read(new File("tanks/tankUp.gif"));
					        BufferedImage imgDown = ImageIO.read(new File("tanks/tankDown.gif"));
					        BufferedImage imgLeft = ImageIO.read(new File("tanks/tankLeft.gif"));
					        BufferedImage imgRight = ImageIO.read(new File("tanks/tankRight.gif"));

							BufferedImage imgEnemyUp = ImageIO.read(new File("tanks/tankEnemyUp.gif"));
					        BufferedImage imgEnemyDown = ImageIO.read(new File("tanks/tankEnemyDown.gif"));
					        BufferedImage imgEnemyLeft = ImageIO.read(new File("tanks/tankEnemyLeft.gif"));
					        BufferedImage imgEnemyRight = ImageIO.read(new File("tanks/tankEnemyRight.gif"));

		       				BufferedImage green = ImageIO.read(new File("tanks/fullLife.gif"));
					        BufferedImage blue = ImageIO.read(new File("tanks/2Life.gif"));
					        BufferedImage red = ImageIO.read(new File("tanks/3Life.gif"));

					        /*	
						   	if(directionTank==0){	//default			
								offscreen.getGraphics().drawImage(imgEnemyUp, x, y, 100, 100, this);
								}
							*/	

							 if(team == 0){
							 	x = 100;
							 	y = 100;

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
								}
								else if(team==1){
								x = 900;
								y = 400;

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
							if(health==3){			
								offscreen.getGraphics().drawImage(green, x, y+100, 96, 12, this);
								}
							
							if(health==2){
								offscreen.getGraphics().drawImage(blue, x, y+100, 62, 12, this);
								}

							if(health==1){
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
					//show the changes
					frame.repaint();
				}			
			}			
		}
	}
	
	/**
	 * Repainting method
	 */
	public void paintComponent(Graphics g){
		g.drawImage(offscreen, 0, 0, null);


		/* for indiv player
		//dito inuupdate ung para sa player para makagalaw sia.
		try {
		       
   		       //use this 
		        BufferedImage imgUp = ImageIO.read(new File("tanks/tankUp.gif"));
		        BufferedImage imgDown = ImageIO.read(new File("tanks/tankDown.gif"));
		        BufferedImage imgLeft = ImageIO.read(new File("tanks/tankLeft.gif"));
		        BufferedImage imgRight = ImageIO.read(new File("tanks/tankRight.gif"));

		       
		    switch (directionTank) {		
			case 1:				
				g.drawImage(imgDown, x, y, 100, 100, this);
				break;

			case 2:
				g.drawImage(imgUp, x, y, 100, 100, this);
				break;
			
			case 3:
				g.drawImage(imgLeft, x, y, 100, 100, this);
				break;

			case 4:
				g.drawImage(imgRight, x, y, 100, 100, this);
				break;
			}


		    } catch (Exception ex){
		        ex.printStackTrace();
		    }*/
	}
	
	/*
	class MouseMotionHandler extends MouseMotionAdapter{
		public void mouseMoved(MouseEvent me){
			x=me.getX();y=me.getY();
			if (prevX != x || prevY != y){
				send("PLAYER "+name+" "+x+" "+y+" "+directionTank);
			}				
		}
	}
	*/



	//eto ung para sa motion
	class KeyHandler extends KeyAdapter{
		public void keyPressed(KeyEvent ke){
			prevX=x;prevY=y;
			switch (ke.getKeyCode()){
			case KeyEvent.VK_DOWN:
				y+=yspeed;
				directionTank=1;
				System.out.println("DOWN !!!");
				break;
			case KeyEvent.VK_UP:
				y-=yspeed;
				directionTank=2;
				break;
			case KeyEvent.VK_LEFT:
				x-=xspeed;
				directionTank=3;
				break;
			case KeyEvent.VK_RIGHT:
				x+=xspeed;
				directionTank=4;
				break;
			}
			if (prevX != x || prevY != y){
				send("PLAYER "+name+" "+x+" "+y+" "+directionTank+" "+team+" "+health);
			}	
		}
	}

	public static void main(String args[]) throws Exception{
		if (args.length != 2){
			System.out.println("Usage: java -jar circlewars-client <server> <player name>");
			System.exit(1);
		}

		new CircleWars(args[0],args[1]);


	}
}
