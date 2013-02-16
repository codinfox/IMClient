package demo.Zhihao.Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * A pane showing all the emotion icons and used to insert emotion icons into the document.
 * This class should not be instantiated elsewhere other than {@link UIChatRoom UIChatRoom}.
 * @author ben
 * @see UIChatRoom
 *
 */
class UIEmotionPane extends JFrame {
	private UIEmotionPane ep = null;
	private Document doc = null;
	private MouseAdapter ma = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int num = ((UIEmotionButton)e.getSource()).getNum();
			System.out.println(num);
			ep.setVisible(false);
			try {
				doc.insertString(doc.getLength(), "[img "+num+"]", null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	};
	
	/**
	 * Creates an EmotionPane.<br>
	 * This class should <strong>NOT</strong> be created more than once.<br>
	 * This pane contains 39 icons.
	 * @param cc the document to which to insert emotion icons.
	 */
	public UIEmotionPane(Document cc) {
		setLayout(new GridLayout(5, 8));
		setBackground(Color.WHITE);
		for (int i = 1; i < 40; i++) {
			UIEmotionButton eb = new UIEmotionButton(i);
			add(eb);
			eb.addMouseListener(ma);
		}
		setSize(320, 200);
		setResizable(false);
		setUndecorated(true);
		setVisible(false);
		
		ep = this; // because this cannot be used in a inner class below.
		doc = cc;
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				ep.setVisible(false);
			}
		});
	}
	
	/**
	 * Show emotion pane at a specific position.
	 * @param p the position at which to show the emotion pane.<br>
	 * The point is the left-top point of the pane. 
	 */
	public void show(Point p) {
		this.setLocation(p);
		this.setVisible(true);
	}
}
