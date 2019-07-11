import java.io.*;
import java.net.*;

public class NetClient {
	
	public NetClient() {
		
	}
	
	public String send(String sentence) throws Exception {
		
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, DistributedDriver.portNo);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		clientSocket.close();
		return "FROM SERVER:" + modifiedSentence;
		
	}
}