import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;



public class MMListener implements MouseMotionListener {

	static Point oldP = new Point();
	static boolean noOldP = true;
	
	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		JPanel panel = (JPanel)e.getSource();
		InfoToolBar.cLabel.setText("LOCATION: " + panel.getToolTipText());
		int index = panel.getToolTipText().indexOf(",");
		String xS = panel.getToolTipText().substring(0, (index));
		String yS = panel.getToolTipText().substring(index + 1);	
		Point p = new Point(Integer.parseInt(xS), Integer.parseInt(yS));
		JPanel newPanel;
		JPanel oldPanel;
		
		
		/*
		 * highlight active cell, unhighlight former active cell
		 */
		if ((p.equals(oldP) == false) && 
				((int)p.getX() < ProVal_GUI.cols) && ((int)p.getY() < ProVal_GUI.rows)) {
			newPanel = panel;				
			
			if (noOldP == false) {	
				newPanel.setBackground(Color.cyan);
				oldPanel = ProVal_GUI.gbContentArray[(int) oldP.getX()][(int) oldP.getY()];
				//oldPanel.setBackground(new Color(238,238,238));	
				
				//if (ProVal_GUI.engage == true) {
					oldPanel.setBackground(Color.white);
				//}
	
				if ((oldPanel.getComponentCount() > 0) && 
						(oldPanel.getComponent(0).toString().contains("drawcircle") == true)) {
				
					if (ProVal_GUI.widgetArray[(int)oldP.getX()][(int)oldP.getY()].notFlag == true) {
						oldPanel.setBackground(Color.RED);
					}
			
				}
				
				//if (ProVal_GUI.engage == true) {
					oldPanel.setBackground(Color.white);
				//}
				
				//check for connector/context bubble and highlight as needed
				if (panel.getComponentCount() > 0) {
					
					if (panel.getComponent(0).toString().contains("drawconnector") == true) {						
						widget w = ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()];
						ProVal_GUI.lastP = p;
						w.highlight_other_connectors(false);
						
						/*
						 * update component info pane to reflect new mouse position
						 */
						InfoToolBar.cTLabel.setText("TRUTH SCORE: ");
						InfoToolBar.cNLabel.setText("NAME: ");
						InfoToolBar.cDLabel.setText("DETAIL: ");
						InfoToolBar.cDtextarea.setText("");
					}
					else if (panel.getComponent(0).toString().contains("drawcircle") == true) {
						
						/*
						 * update component info field if mouse is on a cbubble
						 */
						InfoToolBar.cTLabel.setText("TRUTH SCORE: " +
								ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].getTruthval());

						InfoToolBar.cNLabel.setText("NAME: " +
								ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].getName());

						InfoToolBar.cDtextarea.setText(
								ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].getData());				
					}	

					
					widget w = ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()];
					ProVal_GUI.lastP = p;
						
					if (w.from_list.isEmpty() == false) {
						w.highlight_other_connectors(false);
					}					
					
				}
				else {
					
					/*
					 * update component info pane to reflect new mouse position
					 */
					InfoToolBar.cTLabel.setText("TRUTH SCORE: ");
					InfoToolBar.cNLabel.setText("NAME: ");
					InfoToolBar.cDLabel.setText("DETAIL: ");
					InfoToolBar.cDtextarea.setText("");
					
					if (ProVal_GUI.highlited == true) {
						widget w = ProVal_GUI.widgetArray[(int)ProVal_GUI.lastP.getX()][(int)ProVal_GUI.lastP.getY()];
						w.highlight_other_connectors(true);
					}
						
				}
				
				
				oldP = p;
			}
			else {
				noOldP = false;
			}
			
		}
		
	}	
	
}