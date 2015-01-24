package stegan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ScaledImagePanel extends JPanel {
	
	private BufferedImage image;
	
	public ScaledImagePanel(BufferedImage image) {
		super();
		
		this.image = image;
	}
	
	@Override
	protected void paintComponent(Graphics g) {

	    super.paintComponent(g);

	    double scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()), getSize()));

	    int scaleWidth = (int) Math.round(image.getWidth() * scaleFactor);
	    int scaleHeight = (int) Math.round(image.getHeight() * scaleFactor);

	    Image scaled = resize(image, scaleWidth, scaleHeight);

	    int width = getWidth() - 1;
	    int height = getHeight() - 1;

	    int x = (width - scaled.getWidth(this)) / 2;
	    int y = (height - scaled.getHeight(this)) / 2;

	    g.drawImage(scaled, x, y, this);

	}

	private double getScaleFactor(int iMasterSize, int iTargetSize) {
	    double dScale = 1;
	    if (iMasterSize > iTargetSize) {
	        dScale = (double) iTargetSize / (double) iMasterSize;
	    } else {
	        dScale = (double) iTargetSize / (double) iMasterSize;
	    }

	    return dScale;
	}

	private double getScaleFactorToFit(Dimension original, Dimension toFit) {
	    double dScale = 1d;

	    if (original != null && toFit != null) {
	        double dScaleWidth = getScaleFactor(original.width, toFit.width);
	        double dScaleHeight = getScaleFactor(original.height, toFit.height);

	        dScale = Math.min(dScaleHeight, dScaleWidth);
	    }

	    return dScale;
	}
	
	private static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}
}