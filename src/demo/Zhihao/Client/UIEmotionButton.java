package demo.Zhihao.Client;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A label to display the emotion icon.<br>
 * This class should <strong>NOT</strong> be instantiated elsewhere other than {@link UIEmotionPane UIEmotionPane}.
 * @see UIEmotionPane
 * @author ben
 *
 */
class UIEmotionButton extends JLabel {
	private int num = 0;

	/**
	 * Creates a UIEmotionButton with specified image.
	 * @param i specifies the image to display.
	 */
	public UIEmotionButton(int i) {
		super();
		num = i;
		Icon image = new ImageIcon(getClass().getResource(i+".gif"));
		setIcon(image);
		setBackground(Color.WHITE);
	}
	
	/**
	 * Gets the icon number of this specific UIEmotionButton.
	 * @return the number of the icon.
	 */
	public int getNum() {
		return num;
	}
}
