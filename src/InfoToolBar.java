/*
 * not all mine - from:
 * 
 * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-JToolBar.html
 * 
 * 
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.Border;

public class InfoToolBar extends JToolBar {

	JPanel cInfoPanel;
	JScrollPane jscrollP;
	JPanel tScorePanel;
	static JLabel cLabel = new JLabel("LOCATION: ");
	static JLabel cTLabel = new JLabel("TRUTH SCORE: ");
	static JLabel cNLabel = new JLabel("NAME: ");
	static JLabel cDLabel = new JLabel("DETAIL: ");
	static JLabel cWLabel = new JLabel("WEIGHT:  ");
	static JTextArea cDtextarea = new JTextArea(500,15);
	static JLabel tLabel = new JLabel("\n\n\n\n\n\u221e");
	
	public InfoToolBar() { 
		Font font = new Font("Arial", Font.BOLD, 24);
		cInfoPanel = new JPanel();
		cInfoPanel.setLayout(new BoxLayout(cInfoPanel, BoxLayout.PAGE_AXIS));
		//cInfoPanel.setMinimumSize(new Dimension(400,400));
		cInfoPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createBevelBorder(0), "COMPONENT INFO"));
		cLabel.setFont(font);		
		cTLabel.setFont(font);
		cNLabel.setFont(font);
		cDLabel.setFont(font);
		cWLabel.setFont(font);
		cInfoPanel.add(cLabel);		
		cInfoPanel.add(cTLabel);
		cInfoPanel.add(cNLabel);
		cInfoPanel.add(cWLabel);
		cInfoPanel.add(cDLabel);	
		cDtextarea.setEditable(false);
		cDtextarea.setWrapStyleWord(true);
		cDtextarea.setLineWrap(true);
		jscrollP = new JScrollPane(cDtextarea);
		cInfoPanel.add(jscrollP);
		
		tScorePanel = new JPanel();
		tScorePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createBevelBorder(0), "STREAM COHERENCY SCORE"));
		//tScorePanel.setMinimumSize(new Dimension(400,400));
		Font f2 = new Font("Arial", Font.BOLD, 64);
		tLabel.setFont(f2);
		tLabel.setOpaque(true);
		tLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		tLabel.setForeground(Color.black);
		//tLabel.setBackground(Color.orange);
		tScorePanel.add(tLabel);
		
		
		
		
		JSplitPane jsplitP = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				cInfoPanel, tScorePanel);
		jsplitP.setOneTouchExpandable(true);
		jsplitP.setDividerLocation(425);
		add(jsplitP);

		
	}



	/*
		  public void setTextLabels(boolean labelsAreEnabled) {

		    Component c;
		    int i = 0;
		    while((c = getComponentAtIndex(i++)) != null) {
		      ToolBarButton button = (ToolBarButton)c;
		      if (labelsAreEnabled)
		        button.setText(button.getToolTipText());
		      else
		        button.setText(null);
		    }

		  }
	 */

	//my crappy half cocked code 
	/*
	public void setAL (ToolBarButton button) {
							
			  if (button.getText().contentEquals("AND")) {
				  
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {

								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								Image image = tk.getImage("images/andpointer.jpg");
								c = tk.createCustomCursor(image, new Point(0,0),"cursorName");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);				
				  
			  }
			  
		  }
		*/
		  
}