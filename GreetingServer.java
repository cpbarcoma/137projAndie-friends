/*
 * GreetingServer.java
 * CMSC137 Example for TCP Socket Programming
 */

import java.net.*;
import java.io.*;

public class GreetingServer extends Thread{
    private ServerSocket serverSocket;

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
                Socket clientArray[];
                clientArray = new Socket[100]; // up to a hundred clients

                Socket client = serverSocket.accept();
                System.out.println("Just connected to " + client.getRemoteSocketAddress());

                while(true){    
                    /* Read data from the ClientSocket */
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    System.out.println(in.readUTF()); //readUTF waits for input

                    DataOutputStream out = new DataOutputStream(client.getOutputStream());

                    /* Send data to the ClientSocket */
                    out.writeUTF("Thank you for connecting to " + client.getLocalSocketAddress() + "\nGoodbye!");
                }
                //client.close();
                // connected = false;
                //System.out.println("Server ended the connection to "+ client.getRemoteSocketAddress());
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
}
