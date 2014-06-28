import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class drawconnector extends JPanel  {	
	Image image;
	//preset to Java Component-grey
	Color c = new Color (238, 238, 238);
	boolean useColor = false;
	
	public drawconnector() {
		setPreferredSize(new Dimension (32, 32));
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, 32, 32, null);
		repaint();
	}
	
	  
}