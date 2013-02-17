package demo.Zhihao.Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * The GUI of the chatroom.
 * @author ben
 *
 */
public class UIChatRoom extends JFrame {
	private String chatRoomName = null;
	private UIRoomTitle roomTitle = null;
	private ChatClient chatClient = null;
	private UIRoomContent roomContent = null;
	private String name = null;

	/**
	 * Creates a UIChatRoom by specifying ChatClient.
	 * @param _client the model of this view.
	 * @see ChatClient
	 */
	public UIChatRoom(ChatClient _client) {
		super("Chatroom - Unknown");
		this.chatClient = _client;

		menuSetup();
		JPanel content = new JPanel(); // to set layout
		content.setLayout(new BorderLayout());
		content.add(
				roomTitle = new UIRoomTitle("Chatroom", "Chatroom", "Unknown"),
				BorderLayout.NORTH);
		content.add(roomContent = new UIRoomContent(_client),
				BorderLayout.CENTER);

		this.add(content, BorderLayout.CENTER);

		setVisible(true);
		setSize(600, 550);
		setMinimumSize(new Dimension(600, 550));
		// TODO: when exit, logout. here need a window listener

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chatClient.quit();
				System.exit(0);
			}
		});
	}

	/**
	 * Set client's screen when logging in.
	 * @param name the screen name of the client.
	 */
	void setScreenName(String name) {
		// this method is called when chatclient has been istantiated.
		roomTitle.setUsername("Logged in as " + name + " ");
		this.name = name;
		roomContent.setScreenName(name);
		this.setTitle(chatRoomName + " - " + name);
	}
	
	/**
	 * Updates the room information.<br>
	 * This method is called during the initialization process as well as the running process.
	 * @param roomname the new room name.
	 * @param roomdes the new room description.
	 * @param bulletin the new room bulletin.
	 * @see UIRoomTitle
	 */
	public void updateRoomInfo(String roomname, String roomdes, String bulletin) {
		roomTitle.setChatRoomName(roomname);
		roomTitle.setRoomDescription(roomdes);
		roomContent.setBulletin(bulletin);
		chatRoomName = roomname;
		this.setTitle(chatRoomName + " - " + name);
	}
	
	/**
	 * Updates the member list.
	 * @param ml the new member list.
	 */
	public void updateMemberList(String[] ml) {
		roomContent.updateMemberList(ml);
	}

	/**
	 * Displays the messages received from the server on the display box. 
	 * @param msg the message to be appended.
	 * @see UIRoomContent#display(String)
	 */
	public void display(String msg) {
		roomContent.display(msg);
	}

	/**
	 * Shows connection failure message when the connection with server lost.
	 * @see ChatClient.refresh
	 */
	public void connectionFailure() {
		JOptionPane.showMessageDialog(this,
				"Connection Lost.\nClient will exit.", "Failure",
				JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	/**
	 * Shows name conflict failure message when name conflict occurs.<br>
	 * The model part of the program will negotiate the name with server and get the modified unique name.
	 * @param name the modified unique name.
	 */
	public void nameConflict(String name) {
		JOptionPane.showMessageDialog(this,
				"Name confliction occurred.\nYour screen name has been changed to: "
						+ name, "Name Confliction", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Menu setup.
	 */
	private void menuSetup() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu aboutMenu = new JMenu("Func");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem helpItem = new JMenuItem("Screen Shot");
		JMenuItem aboutItem = new JMenuItem("About");
		fileMenu.add(exitItem);
		aboutMenu.add(helpItem);
		aboutMenu.addSeparator();
		aboutMenu.add(aboutItem);
		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);
		this.add(menuBar, BorderLayout.NORTH);
		
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chatClient.quit();
				System.exit(0);				
			}
		});
		
		helpItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				roomContent.outputHistory();
			}
		});
		
		aboutItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "InstantMessager\n      Copyleft Li Zhihao No.1152691\n" +
						"      Source code can be distributed freely but not for commercial use.", "About InstantMessager", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
}