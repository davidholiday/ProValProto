import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class drawcircle extends JPanel implements Serializable  {	
	Shape circle;
	//preset to Java Component-grey
	//Color c = new Color (238, 238, 238);
	Color c;
	JLabel label = new JLabel();
	
	public drawcircle (Color colorIn) {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		c = colorIn;
		setPreferredSize(new Dimension (64, 64));
		add(label, BorderLayout.CENTER);
	}
	
	public void setLabel(String text) {
		label.setText(text);
	}
	
	public String getText(String test) {
		return label.getText();
	}
	
	public void paintComponent(Graphics g) {
		circle = new Ellipse2D.Double(1, 0, 55, 55);
		Graphics2D ga = (Graphics2D)g;
		ga.setColor(Color.BLACK);
		ga.setStroke(new BasicStroke(3.5f));
		ga.draw(circle);
		ga.setColor(c);	
		ga.fill(circle);
	}
	
	  
}