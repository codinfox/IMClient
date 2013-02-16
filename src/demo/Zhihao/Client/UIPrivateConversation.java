package demo.Zhihao.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * This is the view part of the Private Conversation Module. <br>
 * @author ben
 *
 */
public class UIPrivateConversation extends JFrame implements Runnable{
	private JTextPane displayPane = new JTextPane();
	private JTextPane messagePane = new JTextPane();
	private PrivateConv pc = null;
	private String otherClient = null;
	private SimpleAttributeSet selfMsg = new SimpleAttributeSet();
	private boolean active = true;
	
	/**
	 * Creates a new UIPrivateConversation instance.<br>
	 * @param otherClient the name of the other client.
	 * @param pConv the model of this view.
	 * @see PrivateConv
	 */
	public UIPrivateConversation(String otherClient, PrivateConv pConv) {
		super("Conversation with "+otherClient);
		this.pc = pConv;
		this.otherClient = otherClient;
		setSize(500, 600);
		setResizable(false);
		JPanel content = new JPanel();
		content.setLayout(null);
		this.add(content);
		JScrollPane sp1 = new JScrollPane(displayPane);
		JScrollPane sp2 = new JScrollPane(messagePane);
		displayPane.setEditable(false);
		
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(sp2, BorderLayout.CENTER);
		JButton sendButton = new JButton("Send");
		messagePanel.add(sendButton, BorderLayout.EAST);
		sp1.setBounds(5, 5, 490, 400);
		content.add(sp1);
		messagePanel.setBounds(5, 435, 490, 140);
		content.add(messagePanel);
//		JButton emotionButton = new JButton();
//		emotionButton.setBounds(5, 407, 25, 25);
//		content.add(emotionButton);
		
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
		
		messagePane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					send();
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!pc.isSocketClosed())
					pc.close();
				active = false;
			}
		});
		
		StyleConstants.setForeground(selfMsg, Color.BLUE);
		setVisible(true);
	}

	@Override
	public void run() {
		while (true) {
			try {
				String msg = pc.receive();
				Document doc = displayPane.getDocument();
				doc.insertString(doc.getLength(), otherClient + ": " + msg, null);
			} catch (IOException e) {
				if (!active) break;
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sends messages.
	 */
	private void send() {
		try {
			String msg = messagePane.getText();
			messagePane.getDocument().remove(0, msg.length());
			if (!msg.endsWith("\n"))
				msg += "\n";
			pc.send(msg);
			Document doc = displayPane.getDocument();
			doc.insertString(doc.getLength(), "Me: " + msg, selfMsg);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes conversation.
	 */
	public void close() {
		this.dispose();
		active = false;
	}
}
