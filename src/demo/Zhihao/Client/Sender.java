package demo.Zhihao.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

/**
 * This class is used to send messages using TCP protocol.<br>
 * This class should <strong>never</strong> be instantiated.
 * @author ben
 */
public abstract class Sender {
	
	/**
	 * Use this method to send message to a socket using TCP.
	 * @param socket to which the message is sent.
	 * @param msg the content of the message.
	 * @throws IOException if an IOException occurs while sending the data. 
	 */
	public static void send(Socket socket, String msg) throws IOException {
		PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
		writer.println(msg);
		writer.flush();
		//writer.close();
	}
}
