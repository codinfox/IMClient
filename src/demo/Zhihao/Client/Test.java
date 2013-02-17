package demo.Zhihao.Client;

import javax.swing.JFileChooser;
import javax.swing.JFrame;



public class Test extends JFrame{
	
	public Test() {
		JFileChooser jfc = new JFileChooser();
		int i = jfc.showSaveDialog(this);
		if (i == JFileChooser.APPROVE_OPTION)
			System.out.println(jfc.getSelectedFile().getPath());
			System.out.println(jfc.getSelectedFile().getName());
	}
	
	public static void main(String[] args) {
		new Test();
	}
}
