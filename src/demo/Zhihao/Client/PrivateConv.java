package demo.Zhihao.Client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This is the main class for direct connection(using UDP) between 2 clients. <br>
 * This class enables the clients to interact without the interfere of server. <br>
 * This is the model part of the Private Conversation module.
 * @author ben
 *
 */
class PrivateConv extends Thread{
	private DatagramSocket socket = null;
	private UDPSender sender = null;
	private UDPReceiver receiver = null;
	private UIPrivateConversation uipc = null;
	private String otherClient = null;
	
	/**
	 * Creates a Private Conversation instance.<br>
	 * Instance created using this constructor is the client sending the request.
	 * @param otherClient the name of the other client.
	 */
	public PrivateConv(String otherClient) {
		this.otherClient = otherClient;
		try {
			socket = new DatagramSocket();
			receiver = new UDPReceiver(socket);
			this.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			String msg = receiver.receive();
			String msgs[] = msg.split(" ");
			sender = new UDPSender(msgs[0], Integer.parseInt(msgs[1]));
			sender.send("OK");
			uipc = new UIPrivateConversation(otherClient, this);
			new Thread(uipc).start();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a Private Conversation instance. <br>
	 * The instance created using this constructor is the client who answers the request. 
	 * @param otherClient the name of the other client.
	 * @param ip the IP address of the other client.<strong><code>localhost</code> should be 127.0.0.1 instead of 0.0.0.0</strong>
	 * @param port the port number of the client.
	 */
	public PrivateConv(String otherClient, String ip, int port) {
		this.otherClient = otherClient;
		try {
			socket = new DatagramSocket();
			sender = new UDPSender(ip, port);
			receiver = new UDPReceiver(socket);
			String tmpip = socket.getLocalAddress().getHostAddress(); //0.0.0.0 no route to host
			if (tmpip.equals("0.0.0.0"))
				tmpip = "127.0.0.1";
			sender.send(tmpip+" "+socket.getLocalPort());
			if (!receiver.receive().equals("OK"))
				return;
			System.out.println("rec OK");
			uipc = new UIPrivateConversation(otherClient, this);
			System.out.println("uipc");
			new Thread(uipc).start();
			System.out.println("new thread");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to receive messages.
	 * @return the message received.
	 * @throws IOException if IOException occurs while retrieving the message.
	 * @see UDPReceiver
	 */
	public String receive() throws IOException {
		String msg = receiver.receive();
		System.out.println("PC: " + msg);
		if (msg.equals("<sys> BYE"))
			uipc.close();
		return msg;
	}
	
	/**
	 * Used to send messages.
	 * @param msg the message to be sent.
	 * @throws IOException if IOException occurs while retrieving the message.
	 * @see UDPSender
	 */
	public void send(String msg) throws IOException {
		sender.send(msg);
		System.out.println("PC Sent: " +msg);
	}
	
	/**
	 * Gets the local IP address. <code>localhost</code> will be 127.0.0.1 instead of 0.0.0.0
	 * @return the local IP address.
	 */
	public String getLocalIP() {
		String ip = socket.getLocalAddress().getHostAddress();
		if (ip.equals("0.0.0.0"))
			ip = "127.0.0.1";
		return ip;
	}
	
	/**
	 * Gets the local UDP port.
	 * @return the local UDP port.
	 */
	public int getLocalPort() {
		return socket.getLocalPort();
	}
	
	/**
	 * Closes the conversation.
	 */
	public void close() {
		try {
			sender.send("<sys> BYE");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the socket(conversation) is still alive.
	 * @return <code>true</code> indicates the conversation is still alive, vice versa.
	 */
	public boolean isSocketClosed() {
		return socket.isClosed();
	}
}
