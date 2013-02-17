package demo.Zhihao.Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicEditorPaneUI;

/**
 * <p>Output jtextpane to jpeg.</p>
 * <p>Created on May 14, 2011, 7:53:17 PM</p>
 * <p>Copyright (c) 2007-2011. The CUCKOO Workgroup, P.R.China</p>
 * @author Ren Jian, ben
 * @version 4.1
 */
class ImageOutput {
    private JTextPane panel;
    
    ImageOutput(JTextPane panel) {
        this.panel = panel;
    }
    
    void output() {
        int width = panel.getWidth();
        int height = panel.getHeight();
        int pageIndex = 1;
        boolean isContinue = true;
        String outputFile = choose();
        if (outputFile == null)
        	return;
        while (isContinue) {
            try {
                BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
                Graphics g = image.getGraphics();
                g.setClip(0, 0, width, height);
                isContinue = paint(g, height, pageIndex);
                g.dispose();
                ImageIO.write(image, "JPG", new File(outputFile));
                pageIndex++;
            } catch (IOException ex) {
                Logger.getLogger(ImageOutput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private String choose() {
    	String rtrn = null;
		JFileChooser jfc = new JFileChooser();
		int i = jfc.showSaveDialog(panel);
		if (i == JFileChooser.APPROVE_OPTION) {
			rtrn = jfc.getSelectedFile().getPath() + ".jpg";
		}
		return rtrn;
    }
    
    private boolean paint(Graphics g, int height, int pageIndex) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = ((BasicEditorPaneUI) panel.getUI()).getPreferredSize(panel);
        double panelHeight = d.height;
        double pageHeight = height;
        int totalNumPages = (int) Math.ceil(panelHeight / pageHeight);
        g2d.translate(0f, -(pageIndex - 1) * pageHeight);
        panel.paint(g2d);
        boolean ret = true;
        if (pageIndex >= totalNumPages) {
            ret = false;
            return ret;
        }
        return ret;
    }
}