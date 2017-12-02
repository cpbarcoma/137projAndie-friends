import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread{
    private ServerSocket serverSocketChat;              
    public static ArrayList threads = new ArrayList<ServerThread>();       

	public ChatServer(int port){
		try{		
			serverSocketChat = new ServerSocket(port);
			System.out.println("TCP socket created");
		} catch(IOException e){
			System.out.println("ERROR IN TCP");
			System.out.println(e);
		}		
	}

	public void run(){
        boolean connected = true;
        System.out.println("Waiting for client on port " + serverSocketChat.getLocalPort() + "...");

        while(connected){
            try{
                /* Start accepting data from the ServerSocket */
                //waits or accepts connection from client
                Socket client = serverSocketChat.accept();
				System.out.println("Just connected to " + client.getRemoteSocketAddress());
				ServerThread st = new ServerThread(client);
            	st.start();
                threads.add(st);
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

	class ServerThread extends Thread{

    	Socket client;
        String text;

    	public ServerThread(Socket s){
    		this.client = s;
    	}
    	
    	public void run(){
    		while(true){    
                       
            	try{ 
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    this.text = in.readUTF();
                    System.out.println(text); //readUTF waits for input
                    for(int i=0;i < threads.size(); i++) {
                        ServerThread c = (ServerThread)threads.get(i);
                        if(!c.client.getRemoteSocketAddress().equals(this.client.getRemoteSocketAddress())){
                            DataOutputStream out = new DataOutputStream(c.client.getOutputStream());
                            out.writeUTF(this.text);
                        }
                    }
    			} catch(IOException e){

    			}       
            }
        }
    }



}
