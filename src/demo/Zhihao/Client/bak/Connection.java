package demo.Zhihao.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
	private Socket tcpClient;
	private String screenName;
	private int udpPort;
	private InetAddress inetAddress;
	private DatagramSocket udpClient;
	private Sender sender;
	private Receiver receiver;
	public Connection(String screenName, String ip, int port) throws UnknownHostException, IOException{
		this.screenName = screenName;
		tcpClient = new Socket(ip, port);
		udpClient = new DatagramSocket();
		inetAddress = InetAddress.getByName(ip);
		BufferedReader reader = new BufferedReader(new InputStreamReader(tcpClient.getInputStream()));
		udpPort = Integer.parseInt(reader.readLine());
		sender = new Sender(udpClient,inetAddress, udpPort);
		receiver = new Receiver(tcpClient);
	}
	
	public void receive() throws IOException{
		String message;
		while(!tcpClient.isClosed()){
			message = receiver.receive();
			if(message.equals("QUIT")){
				tcpClient.close();
				break;
			}
		}
	}
	
	public void send() throws IOException{
		try{
			sender.send(screenName.getBytes());
		}catch(Exception e){
			System.out.println("socket has been closed");
		}
	}
	
	public boolean isActive(){
		return !tcpClient.isClosed();
	}
}
