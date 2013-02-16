package demo.Zhihao.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
	private Socket tcpSocket;
	BufferedReader reader;
	public Receiver(Socket tcpSocket) throws IOException{
		this.tcpSocket = tcpSocket;
		reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
	}
	
	public String receive() throws IOException{
		String message = reader.readLine();
		System.out.println(message);
		return message;
	}
}
