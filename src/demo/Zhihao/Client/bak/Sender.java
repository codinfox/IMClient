package demo.Zhihao.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {
	DatagramSocket udpClient;
	InetAddress inetAddress;
	int udpPort;
	public Sender(DatagramSocket udpClient,InetAddress inetAddress,int udpPort){
		this.udpClient = udpClient;
		this.inetAddress = inetAddress;
		this.udpPort = udpPort;
	}
	public void send(byte [] message) throws IOException{
		DatagramPacket packet = new DatagramPacket(message,message.length,inetAddress,udpPort);
		udpClient.send(packet);
	}
}
