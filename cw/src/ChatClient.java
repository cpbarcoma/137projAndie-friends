import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient extends Thread{
	public static String clientName = "";
	public String serverName = "";
	int port;

	public ChatClient(String server, String name, String portno){
		this.serverName = server;
		this.clientName = name;		
		this.port = Integer.parseInt(portno);
		System.out.println("Chat Client created!");
	}

	public void run(){
		try{
            //String serverName = args[0]; //get IP address of server from first param
			//String serverName = "127.0.0.1";
            //int port = Integer.parseInt(args[1]); //get port from second param
            //String message = args[2]; //get message from the third param
            //clientName = args[2];
	
			

            String message;
            Scanner scan = new Scanner(System.in);

            /* Open a ClientSocket and connect to ServerSocket */
            System.out.println("Connecting to " + serverName + " on port " + port);
			//System.out.println("Connecting to server on port " + port);
            
			//creating a new socket for client and binding it to a port
            Socket server = new Socket(serverName, port);
			//Socket server = new Socket(serverName, port);
            ClientThread cs = new ClientThread(server);
            cs.start();

            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            
            System.out.println("\nWELCOME " + clientName + "!");

            while(true){	        
                //System.out.print(clientName + ": ");
                message = scan.nextLine();

                /* Send data to the ServerSocket */
                OutputStream outToServer = server.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(clientName+": " +message);
            }

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

        public void run() {
            while(true) {
                try {
                    /* Receive data from the ServerSocket */
                    InputStream inFromServer = server.getInputStream();
                    DataInputStream in = new DataInputStream(inFromServer);
                    System.out.println(in.readUTF());
                } catch (IOException e) {}
            }
        }
    }




}
