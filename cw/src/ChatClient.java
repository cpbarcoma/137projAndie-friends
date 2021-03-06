import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JTextArea;

public class ChatClient extends Thread{
	public static String clientName = "";
	public String serverName = "";
	int port;
	private static JTextArea chatArea;

	Socket server;

	public ChatClient(String server, String name, String portno, JTextArea chatArea){
		this.serverName = server;
		this.clientName = name;		
		this.port = Integer.parseInt(portno);
		this.chatArea = chatArea; 	//passes methods of chatArea from CW to this class
		System.out.println("Chat Client created!");

		this.run();	
	}

	public void run(){
		try{
            String message;
            Scanner scan = new Scanner(System.in);

            /* Open a ClientSocket and connect to ServerSocket */
            System.out.println("Connecting to " + serverName + " on port " + port);
			//System.out.println("Connecting to server on port " + port);
            
			//creating a new socket for client and binding it to a port
            server = new Socket(serverName, port);
			//Socket server = new Socket(serverName, port);
            ClientThread cs = new ClientThread(server);
            cs.start();

            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            
            System.out.println("\nWELCOME " + clientName + "!");

			//closing the socket of the client
            //server.close();
        
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Cannot find (or disconnected from) Server");
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Usage: java GreetingClient <server ip> <port no.> '<your message to the server>'");
        }

	}

	// Reads inputs from other clients
    static class ClientThread extends Thread {
        Socket server;

        public ClientThread(Socket s) {
            this.server = s;
        }

		//gets input
        public void run() {
            while(true) {
                try {
                    /* Receive data from the ServerSocket */
                    InputStream inFromServer = server.getInputStream();
                    DataInputStream in = new DataInputStream(inFromServer);
                    showMessage(in.readUTF());
                } catch (IOException e) {}
            }
        }
    }

	//used to get input from textField
	public void sendChatData(String msg){
		/* Send data to the ServerSocket */
        try{
			OutputStream outToServer = server.getOutputStream();
	        DataOutputStream out = new DataOutputStream(outToServer);
    	    out.writeUTF(clientName+": " +msg);
		} catch(IOException e){}	
	}

	private static void showMessage(String msg){
		chatArea.append(msg);
	}


}
