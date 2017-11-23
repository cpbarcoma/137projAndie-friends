import java.net.InetAddress;

/**
 * This class encapsulates a network players
 * @src Joseph Anthony C. Hermocilla
 * @author(s) J.A.C. Hermocilla + Andy and Friends
 *
 */

public class NetPlayer {
	/**
	 * The network address of the player
	 */
	private InetAddress address;
	
	/**
	 * The port number of  
	 */
	private int port;
	
	/**
	 * The name of the player
	 */
	private String name;
	
	/**
	 * The position of player
	 */
	private int x,y;

	/**
	 * The direction of player
	 */
	private int directionTank;


	/**
	 * The state of player
	 */
	private boolean alive;

	/**
	 * The score of player
	 */
	private int score;

	//health of the user
	private int health;

	//from which team is the user
	private int team;
	private int initPosition;

	/**
	 * Constructor
	 * @param name
	 * @param address
	 * @param port
	 */
	public NetPlayer(String name,InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.name = name;
	}

	/**
	 * Returns the address
	 * @return
	 */
	public InetAddress getAddress(){
		return address;
	}

	/**
	 * Returns the port number
	 * @return
	 */
	public int getPort(){
		return port;
	}

	/**
	 * Returns the name of the player
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the X coordinate of the player
	 * @param x
	 */
	public void setX(int x){
		this.x=x;
	}
	
	
	/**
	 * Returns the X coordinate of the player
	 * @return
	 */
	public int getX(){
		return x;
	}
	
	
	/**
	 * Returns the y coordinate of the player
	 * @return
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets the y coordinate of the player
	 * @param y
	 */
	public void setY(int y){
		this.y=y;		
	}

	/**
	 * Returns the state of player
	 * @return
	 */
	public boolean getAlive(){
		return alive;
	}
	
	/**
	 * Sets the state of the player
	 * @param alive
	 */
	public void setAlive(boolean alive){
		this.alive=alive;		
	}



	/**
	 * Returns the score of the player
	 * @return
	 */
	public int getScore(){
		return score;
	}
	
	/**
	 * Sets the score of the player
	 * @param score
	 */
	public void setScore(int score){
		this.score=score;		
	}

	public int getInit(){
		return score;
	}
	
	/**
	 * Sets the score of the player
	 * @param score
	 */
	public void setInit(int initPosition){
		this.initPosition=initPosition;		
	}

	/**
	 * Returns the direction of the player
	 * @return
	 */
	public int getDirection(){
		return directionTank;
	}
	
	/**
	 * Sets the score of the player
	 * @param score
	 */
	public void setDirection(int directionTank){
		this.directionTank=directionTank;		
	}


	public int getHealth(){
		return health;
	}

	public void setHealth(int health){
		this.health=health;
	}

	public int getTeam(){
		return health;
	}

	public void setTeam(int team){
		this.team=team;
	}



	/**
	 * String representation. used for transfer over the network
	 */
	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=x+" ";
		retval+=y+" ";
		retval+=directionTank+" ";
		retval+=team+" ";
		retval+=initPosition+" ";	
		retval+=health;		
		return retval;
	}	
}
