import java.awt.Color;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;



public class MListener implements MouseListener {
	
	public void mousePressed(MouseEvent e) {
	       
	}

	public void mouseReleased(MouseEvent e) {
     
	}

    public void mouseEntered(MouseEvent e) {
      
     }

	public void mouseExited(MouseEvent e) {
        
	}

	public void mouseClicked(MouseEvent e) {
		ClassLoader cl = ResourceAnchor.class.getClassLoader();
		JPanel panel = (JPanel)e.getSource();
		int index = panel.getToolTipText().indexOf(",");
		String xS = panel.getToolTipText().substring(0, (index));
		String yS = panel.getToolTipText().substring(index + 1);	
		Point p = new Point(Integer.parseInt(xS), Integer.parseInt(yS));
		
		/*
		 * TRASH handler
		 */
		if (ProVal_GUI.grid.getCursor().getName() == "trash") {
			
			//make sure the user clicked on something with content 
			if (panel.getComponentCount() == 0) {		
				return;
			}
			
			//make sure to unhighlight everything first
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].highlight_other_connectors(true);
			ProVal_GUI.highlited = false;
			
			//erase content from content arrays	
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = null;
			ProVal_GUI.compHere[(int)p.getX()][(int)p.getY()] = false;
			
			//erase content from JPanel
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].removeAll();
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].repaint();
			//ProVal_GUI.gridScroll.revalidate();
			
		}
		
		
		/*
		 * SELECTION handler
		 */
		if (ProVal_GUI.grid.getCursor().getName() == "Default Cursor") {
			
			//make sure the user clicked on a context bubble
			if (panel.getComponentCount() == 0) {		
				return;
			}
			else if (panel.getComponent(0).toString().contains("drawcircle") == false) {
				return;
			}
			
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].showSD();
			
		}
		
		
		
		/*
		 * NOT handler
		 */
		if (ProVal_GUI.grid.getCursor().getName() == "not") {
			
			//make sure the user clicked on a context bubble
			if ((panel.getComponentCount() == 0) && 
					(panel.getComponent(0).toString().contains("drawcircle") == false)){		
				return;
			}
			
			if (ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].notFlag == true) {
				ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].setNotFlag(false);
				ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].setBackground(Color.white);
				//ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].setBackground(new Color (238, 238, 238));
			}
			else {
				ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].setNotFlag(true);
				ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].setBackground(Color.RED);
			}
			
		}
		
		
		/*
		 * OR coupler handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("or") == true) {
			//Image image = Toolkit.getDefaultToolkit().getImage("images/or.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/or.jpg"));
			drawconnector dc = new drawconnector();
			dc.image = image;
			widget w = new widget();
			w.makeLogicalConnector((int)p.getX(), (int)p.getY(), 2);
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = w;
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].add(dc);
			ProVal_GUI.gridScroll.revalidate();			
		}
		
		
		/*
		 * Strong OR coupler handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("strongor") == true) {
			//Image image = Toolkit.getDefaultToolkit().getImage("images/strongor.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/strongor.jpg"));
			drawconnector dc = new drawconnector();
			dc.image = image;
			widget w = new widget();
			w.makeLogicalConnector((int)p.getX(), (int)p.getY(), 7);
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = w;
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].add(dc);
			ProVal_GUI.gridScroll.revalidate();			
		}
		
		
		/*
		 * IF THEN coupler handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("ifthen") == true) {
			//Image image = Toolkit.getDefaultToolkit().getImage("images/ifthen.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/ifthen.jpg"));
			drawconnector dc = new drawconnector();
			dc.image = image;
			widget w = new widget();
			w.makeLogicalConnector((int)p.getX(), (int)p.getY(), 4);
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = w;
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].add(dc);
			ProVal_GUI.gridScroll.revalidate();			
		}
		
		
		/*
		 * BI CON coupler handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("bicon") == true) {
			//Image image = Toolkit.getDefaultToolkit().getImage("images/bicon.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/bicon.jpg"));
			drawconnector dc = new drawconnector();
			dc.image = image;
			widget w = new widget();
			w.makeLogicalConnector((int)p.getX(), (int)p.getY(), 5);
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = w;
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].add(dc);
			ProVal_GUI.gridScroll.revalidate();			
		}
		
		
		/*
		 * AND coupler handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("and") == true) {
			//Image image = Toolkit.getDefaultToolkit().getImage("images/and.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/and.jpg"));
			drawconnector dc = new drawconnector();
			dc.image = image;
			widget w = new widget();
			w.makeLogicalConnector((int)p.getX(), (int)p.getY(), 1);
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = w;
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].add(dc);
			ProVal_GUI.gridScroll.revalidate();			
		}
		
		
		/*
		 * Strong AND coupler handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("strongand") == true) {
			//Image image = Toolkit.getDefaultToolkit().getImage("images/strongand.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/strongand.jpg"));
			drawconnector dc = new drawconnector();
			dc.image = image;
			widget w = new widget();
			w.makeLogicalConnector((int)p.getX(), (int)p.getY(), 6);
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = w;
			ProVal_GUI.gbContentArray[(int)p.getX()][(int)p.getY()].add(dc);
			ProVal_GUI.gridScroll.revalidate();			
		}
		
		
		
		/*
		 * context bubble handler 
		 */ 	 
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("cbubble") == true) {
 			//ImageIcon icon = new ImageIcon("images/cbubbleorange.jpg");
			//ProVal_GUI.gbContentArray[(int) p.getX()][(int) p.getY()].add(new JLabel(icon));
			//panel.add(new JLabel(icon));						
			widget w = new widget();
			w.makeCB((int)p.getX(), (int)p.getY());
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()] = w;
			w.showSD();
			
			
			drawcircle dc = new drawcircle(w.truth2color());
			dc.setLabel("       " + w.getTruthval());
			//dc.setLabel("     " + w.getName());
			panel.add(dc);			
			panel.revalidate();
			//ProVal_GUI.wtoolbar.butt_list.get(10).doClick();
		}
		
		
		/*
		 * antecedent handler
		 */	 
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("antecedent") == true) {

			if (ProVal_GUI.first_click == false) {
	
				//make sure the user clicked on a context bubble
				if (panel.getComponentCount() == 0) {		
					return;
				}
				else if (panel.getComponent(0).toString().contains("drawcircle") == false) {
					return;
				}
				
				ProVal_GUI.first_click = true;
				ProVal_GUI.first_point = p;
				
				//update widget object with connection data
				ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addFrom(new Point(p));
			}
			//if we're here, it means we're on the second click
			else {
				boolean noX = false;
				boolean noY = false;
				int slope = 0;
				int slopeR = 0;
				int deltaY = Math.abs((int)p.getY() - (int)ProVal_GUI.first_point.getY());
				int deltaX = Math.abs((int)p.getX() - (int)ProVal_GUI.first_point.getX());
				
				//make sure the user clicked on a context bubble
				if (panel.getComponentCount() == 0) {
					return;
				}
				/*
				else if (panel.getComponent(0).toString().contains("drawcircle") == false) {
					return;
				}
				*/
				else if ((deltaY < 2) && (deltaX < 2)) {
					return;
				}
				
				ProVal_GUI.second_point = p;
				
				//update widget from and to data 
				ProVal_GUI.widgetArray[(int)ProVal_GUI.first_point.getX()][(int)ProVal_GUI.first_point.getY()].addTo(new Point(p));
				ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addFrom(new Point(ProVal_GUI.first_point));
				ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addTo(new Point(p));
				
				/*
				 * figure out which is the logical connector and which is the 
				 * context bubble so we can properly update the connector's 
				 * antecedent list
				 */
				if (ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].isLogicalConnector == true) {
					ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].ant_list.add(new Point(ProVal_GUI.first_point));
				}
				else {
					ProVal_GUI.widgetArray[(int)ProVal_GUI.first_point.getX()][(int)ProVal_GUI.first_point.getY()].ant_list.add(new Point(ProVal_GUI.second_point));
				}
				
				
				ProVal_GUI.first_click = false;
				
				//calculate slope and error check for divide by zero (two cBubbles in the same row/col)
				if (deltaX != 0) {
					slope = deltaY/deltaX;
					slopeR = deltaY % deltaX;
				
					if (slopeR > 0.5) {
						slope = slope + 1;
					}		
	
				}
				else {
					noX = true;
				}
				
				if (deltaY == 0) {
					noY = true;
				}
						
				int distance = (int)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
				//Image image = Toolkit.getDefaultToolkit().getImage("images/coupler.jpg");
				Toolkit tk = Toolkit.getDefaultToolkit();
				Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/coupler.jpg"));

				//ProVal_GUI.gbContentArray[(int) p.getX()][(int) p.getY()].add(new JLabel(icon));
				
				int x = ProVal_GUI.first_point.x;
				int y = ProVal_GUI.first_point.y;	
				int xmod = 0;
				int ymod = 0; 
				
				if (noX != true) {
					
					if ((int)ProVal_GUI.second_point.getX() - (int)ProVal_GUI.first_point.getX() > 0) {
						xmod = 1;
					}
					else {
						xmod = -1;
					}
					
				}
				else {
					slope = 1;
				}
				
				if (noY != true) {
				
					if ((int)ProVal_GUI.second_point.getY() - (int)ProVal_GUI.first_point.getY() > 0) {
						//ymod = slope;
						ymod = 1;
					}
					else {
						//ymod = (0 - slope);
						ymod = -1;
					}
				
				}
				
				//update canvas with connectors and create widget objects
				while (distance > 0) {				
					drawconnector dc = new drawconnector();
					dc.image = image;
					
					if (x != (int)ProVal_GUI.second_point.getX()) {
						x = x + xmod;
					}
					
					if (y != (int)ProVal_GUI.second_point.getY()) {
						y = y + ymod;
					}

					//prevent drawing in the same cell as a cbubble
					if ((((int)ProVal_GUI.second_point.getX() == x) && 
							((int)ProVal_GUI.second_point.getY() == y)) == false) {
						widget w = new widget();
						w.makeAntecedent(x, y, new Point(ProVal_GUI.first_point), new Point(ProVal_GUI.second_point));
						ProVal_GUI.widgetArray[x][y] = w;
						ProVal_GUI.gbContentArray[x][y].add(dc);
						ProVal_GUI.gridScroll.revalidate();
						ProVal_GUI.gbContentArray[x][y].repaint();
						//distance = distance - (slope + 1);
						distance--;
					}
					else {
						break;
					}
					
				}
				
				//enable pointer toolbar button
				ProVal_GUI.wtoolbar.butt_list.get(11).doClick();
				
			}
			
		}
		
		
		
		/*
		 * consequent handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("consequent") == true)	
		
		if (ProVal_GUI.first_click == false) {

			//make sure the user clicked on a context bubble
			if (panel.getComponentCount() == 0) {		
				return;
			}
			else if (panel.getComponent(0).toString().contains("drawcircle") == true) {
				return;
			}

			ProVal_GUI.first_click = true;
			ProVal_GUI.first_point = p;

			//update widget object with connection data
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addFrom(new Point(p));
		}
		//if we're here, it means we're on the second click
		else {
			boolean noX = false;
			boolean noY = false;
			int slope = 0;
			int slopeR = 0;
			int deltaY = Math.abs((int)p.getY() - (int)ProVal_GUI.first_point.getY());
			int deltaX = Math.abs((int)p.getX() - (int)ProVal_GUI.first_point.getX());

			//make sure the user clicked on a context bubble
			if (panel.getComponentCount() == 0) {
				return;
			}
			else if (panel.getComponent(0).toString().contains("drawcircle") == false) {
				return;
			}
			else if ((deltaY < 2) && (deltaX < 2)) {
				return;
			}

			ProVal_GUI.second_point = p;

			//update widget from and to data 
			ProVal_GUI.widgetArray[(int)ProVal_GUI.first_point.getX()][(int)ProVal_GUI.first_point.getY()].addTo(new Point(p));
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addFrom(new Point(ProVal_GUI.first_point));
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addTo(new Point(p));
			
			
			/*
			 * figure out which is the logical connector and which is the 
			 * context bubble so we can properly update the connector's 
			 * consequent list
			 */
			if (ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].isLogicalConnector == true) {
				ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].con_list.add(new Point(ProVal_GUI.first_point));
			}
			else {
				ProVal_GUI.widgetArray[(int)ProVal_GUI.first_point.getX()][(int)ProVal_GUI.first_point.getY()].con_list.add(new Point(ProVal_GUI.second_point));
			}
			
			
			ProVal_GUI.first_click = false;

			//calculate slope and error check for divide by zero (two cBubbles in the same row/col)
			if (deltaX != 0) {
				slope = deltaY/deltaX;
				slopeR = deltaY % deltaX;

				if (slopeR > 0.5) {
					slope = slope + 1;
				}		

			}
			else {
				noX = true;
			}

			if (deltaY == 0) {
				noY = true;
			}

			int distance = (int)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
			//Image image = Toolkit.getDefaultToolkit().getImage("images/consequent.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/consequent.jpg"));

			//ProVal_GUI.gbContentArray[(int) p.getX()][(int) p.getY()].add(new JLabel(icon));

			int x = ProVal_GUI.first_point.x;
			int y = ProVal_GUI.first_point.y;	
			int xmod = 0;
			int ymod = 0; 

			if (noX != true) {

				if ((int)ProVal_GUI.second_point.getX() - (int)ProVal_GUI.first_point.getX() > 0) {
					xmod = 1;
				}
				else {
					xmod = -1;
				}

			}
			else {
				slope = 1;
			}

			if (noY != true) {

				if ((int)ProVal_GUI.second_point.getY() - (int)ProVal_GUI.first_point.getY() > 0) {
					//ymod = slope;
					ymod = 1;
				}
				else {
					//ymod = (0 - slope);
					ymod = -1;
				}

			}

			//update canvas with connectors and create widget objects
			while (distance > 0) {				
				drawconnector dc = new drawconnector();
				dc.image = image;

				if (x != (int)ProVal_GUI.second_point.getX()) {
					x = x + xmod;
				}

				if (y != (int)ProVal_GUI.second_point.getY()) {
					y = y + ymod;
				}

				//prevent drawing in the same cell as a cbubble
				if ((((int)ProVal_GUI.second_point.getX() == x) && 
						((int)ProVal_GUI.second_point.getY() == y)) == false) {
					widget w = new widget();
					w.makeConsequent(x, y, new Point(ProVal_GUI.first_point), new Point(ProVal_GUI.second_point));
					ProVal_GUI.widgetArray[x][y] = w;
					ProVal_GUI.gbContentArray[x][y].add(dc);
					ProVal_GUI.gridScroll.revalidate();
					//ProVal_GUI.gbContentArray[x][y].repaint();
					//distance = distance - (slope + 1);
					distance--;
				}
				else {
					break;
				}

			}

			//enable pointer toolbar button
			ProVal_GUI.wtoolbar.butt_list.get(11).doClick();

		}
			

		
		/*
		 * result handler
		 */
		if (ProVal_GUI.grid.getCursor().getName().contentEquals("result") == true)	
	
		if (ProVal_GUI.first_click == false) {

			//make sure the user clicked on either if/then or bi-con
			if (panel.getComponentCount() == 0) {				
				return;
			}
			else if (panel.getComponent(0).toString().contains("drawconnector") == true) {
			
				if ((ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].ConType < 7) &&
						(ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].ConType > 8)) {
					return;
				}
				
			}

			ProVal_GUI.first_click = true;
			ProVal_GUI.first_point = p;

			//update widget object with connection data
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addFrom(new Point(p));
		}
		//if we're here, it means we're on the second click
		else {
			boolean noX = false;
			boolean noY = false;
			int slope = 0;
			int slopeR = 0;
			int deltaY = Math.abs((int)p.getY() - (int)ProVal_GUI.first_point.getY());
			int deltaX = Math.abs((int)p.getX() - (int)ProVal_GUI.first_point.getX());

			//make sure the user clicked on a context bubble
			if (panel.getComponentCount() == 0) {
				return;
			}
			else if (panel.getComponent(0).toString().contains("drawcircle") == false) {
				return;
			}
			else if ((deltaY < 2) && (deltaX < 2)) {
				return;
			}

			ProVal_GUI.second_point = p;

			//update widget from and to data 
			ProVal_GUI.widgetArray[(int)ProVal_GUI.first_point.getX()][(int)ProVal_GUI.first_point.getY()].addTo(new Point(p));
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addFrom(new Point(ProVal_GUI.first_point));
			ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].addTo(new Point(p));
			
			
			/*
			 * figure out which is the logical connector and which is the 
			 * context bubble so we can properly update the connector's 
			 * consequent list
			 */
			if (ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].isLogicalConnector == true) {
				ProVal_GUI.widgetArray[(int)p.getX()][(int)p.getY()].res_list.add(new Point(ProVal_GUI.first_point));
			}
			else {
				ProVal_GUI.widgetArray[(int)ProVal_GUI.first_point.getX()][(int)ProVal_GUI.first_point.getY()].res_list.add(new Point(ProVal_GUI.second_point));
			}
			
			
			ProVal_GUI.first_click = false;

			//calculate slope and error check for divide by zero (two cBubbles in the same row/col)
			if (deltaX != 0) {
				slope = deltaY/deltaX;
				slopeR = deltaY % deltaX;

				if (slopeR > 0.5) {
					slope = slope + 1;
				}		

			}
			else {
				noX = true;
			}

			if (deltaY == 0) {
				noY = true;
			}

			int distance = (int)Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
			//Image image = Toolkit.getDefaultToolkit().getImage("images/result.jpg");
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/result.jpg"));

			//ProVal_GUI.gbContentArray[(int) p.getX()][(int) p.getY()].add(new JLabel(icon));

			int x = ProVal_GUI.first_point.x;
			int y = ProVal_GUI.first_point.y;	
			int xmod = 0;
			int ymod = 0; 

			if (noX != true) {

				if ((int)ProVal_GUI.second_point.getX() - (int)ProVal_GUI.first_point.getX() > 0) {
					xmod = 1;
				}
				else {
					xmod = -1;
				}

			}
			else {
				slope = 1;
			}

			if (noY != true) {

				if ((int)ProVal_GUI.second_point.getY() - (int)ProVal_GUI.first_point.getY() > 0) {
					//ymod = slope;
					ymod = 1;
				}
				else {
					//ymod = (0 - slope);
					ymod = -1;
				}

			}

			//update canvas with connectors and create widget objects
			while (distance > 0) {				
				drawconnector dc = new drawconnector();
				dc.image = image;

				if (x != (int)ProVal_GUI.second_point.getX()) {
					x = x + xmod;
				}

				if (y != (int)ProVal_GUI.second_point.getY()) {
					y = y + ymod;
				}

				//prevent drawing in the same cell as a cbubble
				if ((((int)ProVal_GUI.second_point.getX() == x) && 
						((int)ProVal_GUI.second_point.getY() == y)) == false) {
					widget w = new widget();
					w.makeResultant(x, y, new Point(ProVal_GUI.first_point), new Point(ProVal_GUI.second_point));
					ProVal_GUI.widgetArray[x][y] = w;
					ProVal_GUI.gbContentArray[x][y].add(dc);
					ProVal_GUI.gridScroll.revalidate();
					//ProVal_GUI.gbContentArray[x][y].repaint();
					//distance = distance - (slope + 1);
					distance--;
				}
				else {
					break;
				}

			}

			//enable pointer toolbar button
			ProVal_GUI.wtoolbar.butt_list.get(11).doClick();

		}
		
		
	}
	
}