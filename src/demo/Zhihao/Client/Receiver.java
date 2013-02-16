package demo.Zhihao.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This class is used to retrieve the data sent by the server using TCP protocol.
 * @author ben
 *
 */
public class Receiver {
	private Socket socket = null;
	private BufferedReader reader = null;
	
	/**
	 * Creates a Receiver to retrieve data from the designated socket.<br>
	 * If IOException occurs while retrieving data, stack trace will be printed.
	 * @param socket the socket from which to retrieve the data.
	 */
	public Receiver(Socket socket) {
		this.socket = socket;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve data from server.
	 * @return data received from server.
	 * @throws IOException if IOException occurs while retrieving data.
	 */
	public String receive() throws IOException{
		return reader.readLine(); 
		//readline ends with a '\n', therefore although every line ends with a '\n', it is eliminated here.
	}
	
	/**
	 * Close the socket from which data is received.
	 * @throws IOException if IOException occurs while closing socket.
	 */
	public void close() throws IOException {
		socket.close();
	}
}
