package demo.Zhihao.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Used to send datagrams via a specified UDP protocol.
 * @author ben
 *
 */
public class UDPSender {
	private DatagramSocket dataSocket = null;
	private InetAddress ip = null;
	private int port = -1;
	
	/**
	 * Creates a UDPSender by specifying the IP and port number of counterpart(server or other client).
	 * @param ip the IP of the server.
	 * @param port the UDP port of the server.
	 */
	public UDPSender(String ip, int port) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.port = port;
			dataSocket = new DatagramSocket();
		} catch (UnknownHostException | SocketException e) {
			System.err.println("Host cannot be reached.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Send messages to counterpart(server or other client).
	 * @param msg the message to send.
	 * @throws IOException if IOException occurs while sending the message.
	 */
	public void send(String msg) throws IOException {
		DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(), msg.length(), ip, port);
		System.out.println("Sent: " + msg);
		dataSocket.send(datagramPacket);
	}
	
	/**
	 * Closes the UDP connection.
	 */
	public void close() {
		dataSocket.close();
	}
}
