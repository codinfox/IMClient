package demo.Zhihao.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * This is the content part of the client GUI<br>
 * including display box, bulletin, message box, send button, member list.
 * This should not be instantiated elsewhere other than {@link UIChatRoom UIChatRoom}.
 * @author ben
 * @see UIChatRoom
 */
class UIRoomContent extends JPanel {
	private JTextArea bulletinContent = new JTextArea();
	private DefaultListModel<String> memberModel = new DefaultListModel<>();
	private TitledBorder border = BorderFactory
			.createTitledBorder("Members Online");
	private JTextPane messageDisplay = new JTextPane();
	private JTextPane messageSender = new JTextPane();
	private ChatClient chatClient = null;
	private JPanel messageSenderPanel = null;
	private String name = null;
	
	private SimpleAttributeSet sysNotice = new SimpleAttributeSet();
	private SimpleAttributeSet selfMsg = new SimpleAttributeSet();
	private SimpleAttributeSet username = new SimpleAttributeSet();
	private SimpleAttributeSet selfUsername = new SimpleAttributeSet();

	/**
	 * Creates a new UIRoomContent component by specifying the model. 
	 * @param chatClient the model part of the view.
	 */
	UIRoomContent(ChatClient chatClient) {
		this.chatClient = chatClient;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createEtchedBorder());
		this.add(new rightPanel(), BorderLayout.EAST);
		this.add(new centralPanel(), BorderLayout.CENTER);
		
		bulletinContent.setEditable(false);
		bulletinContent.setLineWrap(true);
		
		// Font
		StyleConstants.setForeground(sysNotice, Color.RED);
		StyleConstants.setFontFamily(sysNotice, "Arial");
		StyleConstants.setBold(sysNotice, true);
		StyleConstants.setForeground(selfMsg, Color.BLUE);
		StyleConstants.setBold(username, true);
		StyleConstants.setForeground(selfUsername, Color.BLUE);
		StyleConstants.setBold(selfUsername, true);
		
	}
	
	/**
	 * Set the screen name right after negotiation.<br>
	 * This is the final screen name and cannot be changed twice.
	 * @param name the screen name.
	 */
	void setScreenName(String name) {
		super.setName(getName());
		this.name = name;
	}
	
	/**
	 * Change the bulletin content.
	 * @param msg the message displayed in the bulletin.
	 */
	public void setBulletin(String msg) {
		bulletinContent.setText(msg);
	}

	/**
	 * Display the received messages in the display box. <br>
	 * Some attributes will be added. 
	 * @param msg the message to be displayed.
	 */
	void display(String msg) {
		try {
			SimpleAttributeSet font = null;
			if (msg.startsWith("<sys>")) { 
				// messages starts with <sys> are system messages.
				msg = msg.substring("<sys> ".length());
				font = sysNotice;
				messageDisplay.getDocument().insertString(
						messageDisplay.getDocument().getLength(), msg, font);
				return;
			} else  {
				SimpleAttributeSet uname = username;
				if (msg.startsWith(name+":")) {
					font = selfMsg;
					uname = selfUsername;
				}
				int c = msg.indexOf(':');
				String un = msg.substring(0, c);
				messageDisplay.getDocument().insertString(
						messageDisplay.getDocument().getLength(), un, uname);
				msg = msg.substring(c);
			}
			msgProcess(msg, font);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the member list.
	 * @param ml the new member list.
	 */
	public void updateMemberList(String[] ml) {
		memberModel.clear();
		for (int i = 1; i < ml.length; i++) {
			memberModel.addElement(ml[i]);
		}
	}
	
	/**
	 * Process the messages received by inserting emotion icons at the specified place.
	 * @param msg the received message to be processed.
	 * @param font the designated font used to display the message.
	 * @throws BadLocationException if BadLocationException occurs when editing the document.
	 */
	private void msgProcess(String msg, SimpleAttributeSet font) throws BadLocationException {
		int pos = 0;
		while ((pos = msg.indexOf("[img ")) != -1) {
			String tmp = msg.substring(0, pos);
			messageDisplay.getDocument().insertString(
					messageDisplay.getDocument().getLength(), tmp, font);
			int p = msg.indexOf(']', pos) + 1;
			if (p == -1) break;
			String em = msg.substring(pos, p);
			if (em.matches("^\\[img [1-3]{0,1}[0-9]{0,1}\\]")) {
				msg = msg.substring(p);
				int num = Integer.parseInt(em.substring("[img ".length(), em.length()-1));
				messageDisplay.setCaretPosition(messageDisplay.getDocument().getLength());
				messageDisplay.insertIcon(new ImageIcon(getClass().getResource(num+".gif")));
			} else {
				// TODO: print one letter
				messageDisplay.getDocument().insertString(
						messageDisplay.getDocument().getLength(), msg.substring(pos, pos+1), font);
				msg = msg.substring(pos+1);
				
			}
			System.out.println("msg: " + msg);
		}
		messageDisplay.getDocument().insertString(
				messageDisplay.getDocument().getLength(), msg, font);
	}

	/**
	 * Central part of the UIRoomContent.
	 * This part can <strong>NOT</strong> be instantiated elsewhere other than UIRoomContent.
	 * @author ben
	 *
	 */
	private class centralPanel extends JPanel {
		UIEmotionPane ep = null;
		public centralPanel() {
			this.setLayout(new BorderLayout());
			JScrollPane sp = new JScrollPane(messageDisplay);
			sp.setBorder(BorderFactory.createEtchedBorder());
			messageDisplay.setEditable(false);
			this.add(sp, BorderLayout.CENTER);

			messageSenderPanel = new JPanel();
			messageSenderPanel.setLayout(new BorderLayout());
			JButton emotionButton = new JButton();
			JPanel tmp = new JPanel();
			tmp.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
			tmp.add(emotionButton);
			emotionButton.setPreferredSize(new Dimension(20, 20));
			messageSenderPanel.add(tmp, BorderLayout.NORTH);
			JPanel messagebox = new JPanel(new BorderLayout());
			JScrollPane sp2 = new JScrollPane(messageSender);
			sp2.setPreferredSize(new Dimension(350, 100));
			messagebox.add(sp2, BorderLayout.CENTER);
			JButton sendButton = new JButton("Send");
			sendButton.setBackground(Color.WHITE);
			messagebox.add(sendButton, BorderLayout.EAST);
			messagebox.setBorder(BorderFactory.createEtchedBorder());
			messageSenderPanel.add(messagebox, BorderLayout.SOUTH);
			this.add(messageSenderPanel, BorderLayout.SOUTH);
			ep = new UIEmotionPane(messageSender.getDocument());
			
			sendButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					send();
				}
			});
			messageSender.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyChar() == '\n')
						send();
				}
			});
			emotionButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Point p = messageSenderPanel.getLocationOnScreen();
					p.y -= 200;
					ep.show(p);
				}
			});
		}

		private void send() {
			String msg = messageSender.getText();
			int len = msg.length();
			if (msg.charAt(msg.length() - 1) != '\n')
				msg += "\n";
			System.out.println(msg);
			chatClient.send(msg);
			try {
				messageSender.getDocument().remove(0, len);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Right part of the UIRoomContent.
	 * This part can <strong>NOT</strong> be instantiated elsewhere other than UIRoomContent.
	 * @author ben
	 *
	 */
	private class rightPanel extends JPanel {
		private JList<String> memberList = null; 
		
		public rightPanel() {
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(200, 550));

			// Bulletin
			JPanel bulletin = new JPanel(new BorderLayout());
			bulletin.setBorder(BorderFactory.createTitledBorder("Bulletin"));
			bulletin.add(bulletinContent);
			bulletin.setPreferredSize(new Dimension(200, 150));
			bulletin.setBackground(Color.WHITE);
			this.add(bulletin, BorderLayout.NORTH);

			// Member List
			JPanel member = new JPanel();
			member.setLayout(new BorderLayout());
			memberList = new JList<>(memberModel);
			JScrollPane scrollPane = new JScrollPane(memberList);
			member.add(scrollPane);
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			member.setBorder(border);
			member.setBackground(Color.WHITE);
			this.add(member, BorderLayout.CENTER);
			
			memberList.addMouseListener(new MouseAdapter() {// TODO
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2)
					{
						String selectedMember = memberList.getSelectedValue();
						if (selectedMember.equals(name)) {
							JOptionPane.showMessageDialog(memberList, "You cannot chat with yourself.");
							return;
						}
						System.out.println("direct conn: " + selectedMember);
						chatClient.launchDirectConn(selectedMember);
					}
				}
			});
			
		}
	}

}