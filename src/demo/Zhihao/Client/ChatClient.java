package demo.Zhihao.Client;

import java.io.IOException;
import java.net.Socket;

/**
 * The model of the client.<br>
 * <i>The design pattern of the client is <strong>MVC</strong></i>
 * @author ben
 *
 */
public class ChatClient {
	private UDPSender udpSender = null;
	private Receiver receiver = null;
	private String name = null;
	private UIChatRoom uiChatRoom = null;
	private boolean active = true;

	/**
	 * Creates a ChatClient by specifying client name, server IP and server TCP port.<br>
	 * If Exception occurs during this process, stack trace will be printed.<br>
	 * During this process, client will negotiate client's screen name with server to avoid name conflict.<br>
	 * When name conflict occurs occurs, a message will be displayed.
	 * @param name the screen name of the client.
	 * @param ip the IP address of the server.
	 * @param port the given TCP port number of server.
	 * @see UIChatRoom#nameConflict(String)
	 */
	public ChatClient(String name, String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			System.out.println(socket.getLocalPort());
			receiver = new Receiver(socket);
			Sender.send(socket, "HELO " + name + "\n");
			String udpPort = receiver.receive();
			udpPort = udpPort.substring("WELCOME ".length(), udpPort.length());
			udpSender = new UDPSender(ip, Integer.parseInt(udpPort));
			System.out.println("bbb");
			uiChatRoom = new UIChatRoom(this);
			this.name = receiver.receive();
			this.name = this.name.substring("WELCOME USERNAME: ".length()); // confirm the screen name
			if (!this.name.equals(name))
				uiChatRoom.nameConflict(this.name);
			uiChatRoom.setScreenName(this.name);
			System.out.println("aaa");
			new refresh().start();
		} catch (NumberFormatException e) {
			System.err.println("param error.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Sends messages to server.
	 * @param msg the message to be sent.
	 */
	public void send(String msg) {
		try {
			udpSender.send(name + ": " + msg); // FIXME: terminate with a '\n'
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves messages from the server.<br>
	 * If IOException occurs during this process, stack trace will be printed.
	 * @return the message received or null if Exception occurs. 
	 */
	private String receive() {
		String msg = null;
		try {
			msg = receiver.receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Private inner class used to refresh the display box.<br>
	 * This class will automatically retrieve messages from the server and display them in the display box.<br>
	 * This class can also be used to update the room information.
	 * @see #updateRoomInfo
	 * This class can also deal with some other system infos, like connection info.
	 * @see UIChatRoom#connectionFailure()
	 * @author ben
	 *
	 */
	private class refresh extends Thread {
		@Override
		public void run() {
			active = true;
			String msg = null;
			while (active) {
				msg = receive();
				/*Below are different ways to deal with different kind of infos.*/
				if (msg.equals("QUIT SERVER")) { // the server quits.
					uiChatRoom.connectionFailure();//server quits
					break;
				}
				if (msg.startsWith("<sysinfo>")) {  // system info messages.
					// this is system info, like room name and bulletins. 
					System.out.println("sysinfo: " + msg);
					msg = msg.substring("<sysinfo>".length());
					String[] info = msg.split("\\|");
					System.out.println(info[0] + "==" + info[1] + "==" + info[2]);
					updateRoomInfo(info[0], info[1], info[2]);
					continue;
				}
				if (msg.startsWith("<member-info> ")) { // member info messages.
					System.out.println("member update");
					String[] info = msg.split("\\|");
					for (String i : info) 
						System.out.println("minfo: " + i);
					updateMemberList(info);
					continue;
				}
				if (msg.startsWith("<conn-info> ")) { // direct connect requests.
					System.out.println("connection info: " + msg);
					String[] info = msg.split(" ");
					for (String i : info) 
						System.out.println("conn info: " + i);
					new PrivateConv(info[1], info[2], Integer.parseInt(info[3]));
					System.out.println("conn est done");
					continue;
				}
				uiChatRoom.display(msg + "\n");// add a newline
				System.out.println("refresh: " + msg);
			}
		}
	}

	/**
	 * Cuts down the connections and quit.<br>
	 * Also tells the server. 
	 */
	public void quit() {
		try {
			active = false;
			receiver.close();
			udpSender.send("QUIT: " + name + "\n");
			udpSender.close();
			// TODO: operate the GUI
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launches UDP direct connection with another client. <br>
	 * @param otherClient the name of the other client.
	 */
	public void launchDirectConn(String otherClient) {
		PrivateConv pc = new PrivateConv(otherClient);
		String addr = pc.getLocalIP();
		int port = pc.getLocalPort();
		try {
			String msg = "<conn-info> " + otherClient + " " + name + " " + addr + " " + port;
			udpSender.send(msg + "\n");
			System.out.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * Update the room information in the GUI.
	 * @param roomname the room name of the chat room.
	 * @param roomdes the room description of the chat room(right below the room name).
	 * @param bulletin the bulletin of the chat room.
	 * @see UIChatRoom#updateRoomInfo(String, String, String)
	 */
	private void updateRoomInfo(String roomname, String roomdes, String bulletin) {
		uiChatRoom.updateRoomInfo(roomname, roomdes, bulletin);
	}
	
	/**
	 * Updates the member list.
	 * @param ml new member list
	 */
	private void updateMemberList(String[] ml) {
		uiChatRoom.updateMemberList(ml);
	}

	/**
	 * Main method. If parameter is set error, program will exit improperly. 
	 * @param args the arguments should follow the following format:<br>
	 * <screen name> <server ip> <TCP port number>
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("param error.");
			return;
		}

		new ChatClient(args[0], args[1], Integer.parseInt(args[2]));
	}

}
