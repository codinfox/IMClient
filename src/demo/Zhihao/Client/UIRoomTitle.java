package demo.Zhihao.Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The title part of the chat room,<br>
 * including the chat room name, description and the user logged in information.
 * @author ben
 * @see UIChatRoom
 */
class UIRoomTitle extends JPanel {
	private final Font titleFont = new Font("Arial", Font.BOLD, 24);
	private final Font descriptFont = new Font("Courier New", Font.PLAIN, 14);
	private JLabel roomName = null;
	private JLabel roomDescription = null;
	private JLabel icon = null;
	private JLabel screenname = null;
	
	/**
	 * Creates a new UIRoomTitle component by specifying room name, room description and user name.<br>
	 * This class should <strong>not</strong> be instantiated elsewhere other than {@link UIChatRoom UIChatRoom}.
	 * @param chatRoomName the name of the chat room.
	 * @param description the description of the chat room.
	 * @param username the user logged in. The username is a unique name after negotiating with the server.
	 * @see UIChatRoom
	 */
	UIRoomTitle(String chatRoomName, String description, String username) {
		JPanel title = new JPanel(new BorderLayout());
		roomName = new JLabel(" " + chatRoomName);
		roomDescription = new JLabel(" " + description);
		ImageIcon ii = new ImageIcon(getClass().getResource("icon.png"));
		icon = new JLabel(ii);
		icon.setPreferredSize(new Dimension(50,36));
		roomName.setFont(titleFont);
		roomDescription.setFont(descriptFont);
		setLayout(new BorderLayout());
		title.add(roomName, BorderLayout.CENTER);
		title.add(roomDescription, BorderLayout.SOUTH);
		this.add(title, BorderLayout.CENTER);
		this.add(icon, BorderLayout.WEST);
		this.add(screenname = new JLabel("Logged in as " + username), BorderLayout.EAST);
	}
	
	/**
	 * Changes the chatroom name.
	 * @param name the new chat room name.
	 */
	public void setChatRoomName(String name) {
		roomName.setText(name);
	}
	
	/**
	 * Changes the chatroom description.
	 * @param des the new chat room description.
	 */
	public void setRoomDescription(String des) {
		roomDescription.setText(" " + des);
	}
	
	/**
	 * Changes the username.
	 * @param name the new username.
	 */
	public void setUsername(String name) {
		screenname.setText(name);
	}
}