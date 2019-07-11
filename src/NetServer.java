import java.io.*;
import java.net.*;

public class NetServer {
	public NetServer() {
		
	}
	
	public void launch() {
	  new Thread() {
		     public void run() {
		    	 DatagramSocket serverSocket = null;
				try {
					serverSocket = new DatagramSocket(DistributedDriver.portNo);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         byte[] receiveData = new byte[1024];
		         byte[] sendData = new byte[1024];
		         while(true)
		               {
		                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		                  try {
							serverSocket.receive(receivePacket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                  String sentence = new String( receivePacket.getData());
		                  System.out.println("RECEIVED: " + sentence);
		                  InetAddress IPAddress = receivePacket.getAddress();
		                  int port = receivePacket.getPort();
		                  String capitalizedSentence = sentence.toUpperCase();
		                  sendData = capitalizedSentence.getBytes();
		                  DatagramPacket sendPacket =
		                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
		                  try {
							serverSocket.send(sendPacket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		               }
		      }
		  
		}.start();
	
	}
}
