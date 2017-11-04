/*
 * GreetingServer.java
 * CMSC137 Example for TCP Socket Programming
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class GreetingServer extends Thread{
    private ServerSocket serverSocket;              
    public static ArrayList threads = new ArrayList<ServerThread>();                                                                                                                                                                                                            

    public GreetingServer(int port) throws IOException{
        //binding a socket to a port
        serverSocket = new ServerSocket(port);
        //serverSocket.setSoTimeout(100000000000);
    }

    public void run(){
        boolean connected = true;
        while(connected){
            try{
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

                /* Start accepting data from the ServerSocket */
                //waits or accepts connection from client
                Socket client;
                //clientArray = new Socket[100]; // up to a hundred clients

                //Socket client = serverSocket.accept();

				//accept then make thread
				client = serverSocket.accept();
				System.out.println("Just connected to " + client.getRemoteSocketAddress());
				ServerThread st = new ServerThread(client);
            	st.start();
                threads.add(st);
                //sendAll("HELLLLOOOOOOOOs");
            }catch(SocketTimeoutException s){
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Input/Output Error!");
                //possible cause: client was disconnected while waiting for input
                break;
            }
        }
    }

    public static void main(String [] args){
        try{
            int port = Integer.parseInt(args[0]);
            Thread t = new GreetingServer(port);
            t.start();
        }catch(IOException e){
            //e.printStackTrace();
            System.out.println("Usage: java GreetingServer <port no.>\n"+
                    "Make sure to use valid ports (greater than 1023)");
        }catch(ArrayIndexOutOfBoundsException e){
            //e.printStackTrace();
            System.out.println("Usage: java GreetingServer <port no.>\n"+
                    "Insufficient arguments given.");
        }
    }

    class ServerThread extends Thread{

    	Socket client;
        String text;

    	public ServerThread(Socket s){
    		this.client = s;
    	}
    	
    	public void run(){
    		while(true){    
                       
            	try{ 
    					/* Read data from the ClientSocket */
    				DataInputStream in = new DataInputStream(client.getInputStream());
                    text = in.readUTF();
                	System.out.println(text); //readUTF waits for input

                	DataOutputStream out = new DataOutputStream(client.getOutputStream());

                        /* Send data to the ClientSocket */
                	out.writeUTF("Thank you for connecting to " + client.getLocalSocketAddress());
                    sendAll(text);
    			} catch(IOException e){

    			}

                
            }
        }

    }

    public void sendAll(String msg) {
        for(int i=0;i < threads.size(); i++) {
            try {
                ServerThread c = (ServerThread)threads.get(i);
                DataOutputStream out2 = new DataOutputStream(c.client.getOutputStream());
                out2.writeUTF(msg); 
            } catch(IOException e){}

        }
    }
}

