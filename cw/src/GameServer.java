import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.io.*;
import java.net.*;
import java.util.*;

// Imports for Timer
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main game server. It just accepts the messages sent by one player to
 * another player
 * @author Joseph Anthony C. Hermocilla
 *
 */

public class GameServer implements Runnable, Constants{
	/**
	 * Placeholder for the data received from the player
	 */	 
	String playerData;
	
	/**
	 * The number of currently connected player
	 */
	int playerCount=0;
	
	/**
	 * The socket
	 */
    DatagramSocket serverSocket = null;
    
    /**
     * The current game state
     */
	GameState game;

	/**
	 * The current game stage
	 */
	int gameStage=WAITING_FOR_PLAYERS;
	
	/**
	 * Number of players
	 */
	int numPlayers;

	/**
	 * For Chat TCP
	 */
	//private ServerSocket serverSocketChat;              
    //public static ArrayList threads = new ArrayList<ServerThread>();   

	public int getNumPlayers(){
		return numPlayers;
	}


	/**
	 * The main game thread
	 */
	Thread t = new Thread(this);
	
	float timeOut;
	float origTime;
	Timer timer;
	
	TimerTask task = new TimerTask(){
		public void run(){
			timeOut--;
			broadcast("SECONDS_REMAINING " + timeOut + " " + origTime);
			if (timeOut == 0){
		   	  timer.cancel();

		  	  timeOut = 0;

		  	  System.out.println("Times Up!");
		    }
		}
	};

	public void timerStart(){
		// second param will tell how long it will start
		// third param is the time increment 
		timer.scheduleAtFixedRate(task, 1000, 1000);
	}

	/**
	 * Simple constructor
	 */
	public GameServer(int numPlayers, float numMinutes){
		this.numPlayers = numPlayers;
		this.timer = new Timer();
		this.timeOut = 60 * numMinutes;
		this.origTime = timeOut;

		try {
            serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(100);
			System.out.println("UDP socket created");
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+PORT);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
		game = new GameState();

		System.out.println("Game created...");
		
		//Start the game thread
		t.start();
	}
	
	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(String msg){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			send(player,msg);	
		}
	}


	/**
	 * Send a message to a player
	 * @param player
	 * @param msg
	 */
	public void send(NetPlayer player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	/**
	 * The juicy part
	 */
	public void run(){
		while(true){
						
			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			/**
			 * Convert the array of bytes to string
			 */
			playerData=new String(buf);
			
			//remove excess bytes. tama naman ung nakukuha.
			playerData = playerData.trim();
			if (!playerData.equals("")){
				System.out.println("Player Data:"+playerData);
			}
		
			// process
			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							NetPlayer player=new NetPlayer(tokens[1],packet.getAddress(),packet.getPort());
							// System.out.println("PACKET PORT THING: " + packet.getPort());
							System.out.println("Player connected: "+tokens[1]);
							game.update(tokens[1].trim(),player);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
					  break;	
				  case GAME_START:
					  System.out.println("Game State: START");
					  broadcast("START");
					  gameStage=IN_PROGRESS;

					  timer.scheduleAtFixedRate(task, 1000, 1000);

					  break;
				  case IN_PROGRESS:
					  //System.out.println("Game State: IN_PROGRESS");
					  
					  //Player data was received!
					  if (playerData.startsWith("PLAYER")){
						  //Tokenize:
						  //The format: PLAYER <player name> <x> <y> <direction>

						  String[] playerInfo = playerData.split(" ");
						  					  
						  String pname =playerInfo[1];
						 
						  int x = Integer.parseInt(playerInfo[2].trim());
						  int y = Integer.parseInt(playerInfo[3].trim());
						  //dagdag
						  int directionTank = Integer.parseInt(playerInfo[4].trim());
						  int team = Integer.parseInt(playerInfo[5].trim());
						  int initPosition = Integer.parseInt(playerInfo[6].trim());
						  int health = Integer.parseInt(playerInfo[7].trim());
						  
						//  System.out.println("Team parse from server: "+team);
						  //Get the player from the game state
						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);					  
						  player.setX(x);
						  player.setY(y);
						  player.setDirection(directionTank);
						//  int temp = playerCount%2;
						  player.setInit(initPosition);
						  player.setTeam(team);
						  if (player.getHealth() > health){
						  	System.out.println("============== U GOT DAMAGED ==============");
						  }
						  player.setHealth(health);

						//  System.out.println("Team parse from server: "+team+" mod: "+playerCount);

						  //Update the game state
						  game.update(pname, player);
						  //Send to all the updated game state
						  broadcast(game.toString());
					  }
					  break;
			}				  
		}
	}	

	public static void main(String args[]){
		if (args.length != 3){
			System.out.println("Usage: java -jar shookt-server <number of players> <number of minutes> <portno>");
			System.exit(1);
		}

		Thread t = new ChatServer(Integer.parseInt(args[2]));
		t.start();
		
		new GameServer(Integer.parseInt(args[0]), Float.parseFloat(args[1]));
	}








}

