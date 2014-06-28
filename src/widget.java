import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class widget implements Serializable {
	boolean isAnchored = false;
	Float truthval = (float) 0.50;
	float oldTruthVal = (float) 0;
	float percentChange = 0;
	float percentChangeNOT = 0; 
	String data;
	String name;
	boolean isConsequent;
	boolean isAntecedent;
	boolean isResultant;
	boolean isLogicalConnector;
	boolean isContextBubble;
	boolean notFlag = false;
	JLabel truthL;
	JSlider truth;
	
	/*
	 * 1 - AND 
	 * 2 - OR
	 * 
	 * 4 - IF THEN
	 * 5 - BICON
	 * 6 - STRONG AND
	 * 7 - STRONG OR
	 */
	int ConType;
	
	/*
	 * these two are a list of all the connections this object may
	 * have with anything else. USED BY (this).highlight_other_connectors
	 */
	ArrayList<Point> to_list  = new ArrayList<Point>();
	ArrayList<Point> from_list = new ArrayList<Point>();
	
	
	/*
	 * these are populated when the widget is a logical connector. it
	 * tells proVal_GUI.engage() what the antecedents, consequents, 
	 * and resultants are. 
	 */
	ArrayList<Point> ant_list = new ArrayList<Point>();
	ArrayList<Point> con_list = new ArrayList<Point>();
	ArrayList<Point> res_list = new ArrayList<Point>();
	
	int xPos;
	int yPos;
	
	
	/*
	 * TO DO - have this method drive a JDialog box to populate
	 * all the necessary fields 
	 */
	public void makeCB(int x, int y) {
		isContextBubble = true;
		isConsequent = false;
		isAntecedent = false;
		isLogicalConnector = false;
		setXYpos(x, y);
	}
	
	public void makeLogicalConnector(int x, int y, int conType) {
		isContextBubble = false;
		isConsequent = false;
		isAntecedent = false;
		isResultant = false;
		isLogicalConnector = true;
		setXYpos(x, y);
		setConType(conType);
	}
	
	public void makeConsequent(int x, int y, Point from, Point to) {
		isContextBubble = false;
		isConsequent = true;
		isAntecedent = false;
		isResultant = false;
		isLogicalConnector = false;
		setXYpos(x, y);
		from_list.add(from);
		to_list.add(to);
	}
	
	public void makeAntecedent(int x, int y, Point from, Point to) {
		isContextBubble = false;
		isConsequent = false;
		isAntecedent = true;
		isResultant  = false;
		isLogicalConnector = false;
		setXYpos(x, y);
		from_list.add(from);
		to_list.add(to);
	}
	
	
	public void makeResultant(int x, int y, Point from, Point to) {
		isContextBubble = false;
		isConsequent = false;
		isAntecedent = false;
		isResultant = true;
		isLogicalConnector = false;
		setXYpos(x, y);
		from_list.add(from);
		to_list.add(to);
	}
	
	
	public Color truth2color() {
		int redVal;
		int greenVal;
		float truthvalTemp = getTruthval();
					
		if (truthvalTemp >= 0.5) {
			greenVal = 255;
			redVal = (255 - (int)Math.abs((truthvalTemp * 510) - 255));
		}
		else {
			redVal = 255;
			greenVal = (255 - (int)Math.abs((truthvalTemp * 510) - 255));
		}
		
		return new Color(redVal, greenVal, 0);
	}
	
	
	public Color truth2color(float tVal) {
		int redVal;
		int greenVal;
		float truthvalTemp = tVal;
					
		if (truthvalTemp >= 0.5) {
			greenVal = 255;
			redVal = (255 - (int)Math.abs((truthvalTemp * 510) - 255));
		}
		else {
			redVal = 255;
			greenVal = (255 - (int)Math.abs((truthvalTemp * 510) - 255));
		}
		
		return new Color(redVal, greenVal, 0);
	}
	
	
	public int getConType() {
		return ConType;
	}
	
	public int getXpos() {
		return xPos;
	}
	
	public int getYpos() {
		return yPos;
	}
	
	public float getTruthval() {
		
		float returnVal = truthval;
		/*
		if (notFlag == true && ProVal_GUI.fromSD == true) {			
			returnVal = fwith2dp((1 - truthval));
		}
		*/
		return returnVal;
	}
	
	
	public float getOldTruthVal() {
		return oldTruthVal;
	}
	
	public float getPercentChange() {
		return percentChange;
	}
	
	public float getPercentChangeNOT() {
		return percentChangeNOT;
	}
	
	public String getData() {
		return data;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLName() {
		return truthL.getText();
	}
	
	public ArrayList<widget> getConsequentBubbles() {
		ArrayList<widget> conList = new ArrayList<widget>();
		
		for (int i = 0; i < con_list.size(); i ++) {
			widget w = new widget();
			w = ProVal_GUI.widgetArray[(int)con_list.get(i).getX()][(int)con_list.get(i).getY()];
			conList.add(w);			
		}
		
		return conList;
	}
	
	
	public ArrayList<widget> getConsequents() {
		ArrayList<widget> conList = new ArrayList<widget>();
		
		for (int i = 0; i < to_list.size(); i ++) {
			widget w = new widget();
			w = ProVal_GUI.widgetArray[(int)to_list.get(i).getX()][(int)to_list.get(i).getY()];
			conList.add(w);
		}
		
		return conList;
	}
	
	public ArrayList<widget> getAntecedantBubbles() {
		ArrayList<widget> antList = new ArrayList<widget>();
		
		for (int i = 0; i < ant_list.size(); i ++) {
			widget w = new widget();
			w = ProVal_GUI.widgetArray[(int)ant_list.get(i).getX()][(int)ant_list.get(i).getY()];
			antList.add(w);		
		}
		
		return antList;
	}
	
	
	public ArrayList<widget> getResultantBubbles() {
		ArrayList<widget> resList = new ArrayList<widget>();
		
		for (int i = 0; i < res_list.size(); i ++) {
			widget w = new widget();
			w = ProVal_GUI.widgetArray[(int)res_list.get(i).getX()][(int)res_list.get(i).getY()];
			resList.add(w);		
		}
		
		return resList;
	}
	
	
	
	public ArrayList<widget> getAntecedants() {
		ArrayList<widget> antList = new ArrayList<widget>();
		
		for (int i = 0; i < from_list.size(); i ++) {
			widget w = new widget();
			w = ProVal_GUI.widgetArray[(int)from_list.get(i).getX()][(int)from_list.get(i).getY()];
			antList.add(w);
		}
		
		return antList;
	}
	
	
	
	public boolean isContextBubble() {
		return isContextBubble;
	}
	
	public boolean isAntecedent() {
		return isAntecedent;
	}
	
	public boolean isLogicalConnector() {
		return isLogicalConnector;
	}
	
	public boolean isConsequent() {
		return isConsequent;
	}
	
	public Point getFrom(int i) {
		return from_list.get(i);
	}
	
	public Point getTo(int i) {
		return to_list.get(i);
	}
	
	public void setTruthval(float val) {
		val = fwith2dp(val);

		/*
		 * isAnchored tells us whether or not the user is 
		 * attempting to set the truth value for this 
		 * particular bubble. 
		 */
		if ((notFlag == true) && (isAnchored == false)) {			
			val = fwith2dp((1 - val));

			if (val > 1) {
				val = 1;
			}
			
			if (val < 0) {
				val = 0;
			}
			
		}

		oldTruthVal = truthval;
		truthval = val;
		calculatePercentChange(oldTruthVal, truthval);
		
		/*
		 * cause the associated graphic to update as well
		 */
        JPanel panel = ProVal_GUI.gbContentArray[getXpos()][getYpos()];
        panel.removeAll(); 
        setLName("TRUTH VALUE: " + truthval);
        Color c = truth2color();
        drawcircle dc = (new drawcircle(c));
        dc.setLabel("     " + truthval);      
        ProVal_GUI.gbContentArray[getXpos()][getYpos()].add(dc);          
        ProVal_GUI.gridScroll.revalidate();	
	}
	
	public void setTruthvalDOWN(float val) {
		truthval = val;
		
		/*
		 * cause the associated graphic to update as well
		 */
        JPanel panel = ProVal_GUI.gbContentArray[getXpos()][getYpos()];
        panel.removeAll(); 
        setLName("TRUTH VALUE: " + truthval);
        Color c = truth2color();
        drawcircle dc = (new drawcircle(c));
        dc.setLabel("     " + truthval);
        ProVal_GUI.gbContentArray[getXpos()][getYpos()].add(dc);  

        if (ProVal_GUI.fromSD == true) {
        	//ProVal_GUI.engageUP(this);
        	//ProVal_GUI.engageDOWN(this);
        }
        
        ProVal_GUI.gridScroll.revalidate();	
	}
	
	public void setData(String val) {
		data = val;
	}
	
	public void setName(String val) {
		name = val;
	}
	
	public void setLName(String val) {
		truthL.setText(val);
	}
	
	public void setIsContextBubble (boolean val) {
		isContextBubble = val;
	}
	
	public void setIsAntecedent(boolean val) {
		isAntecedent = val;
	}
	
	public void setIsConsequent (boolean val) {
		isConsequent = val;
	}
	
	public void setIsLogicalConnector (boolean val) {
		isLogicalConnector = val;
	}
	
	public void addFrom(Point val) {
		from_list.add(val);
	}
	
	public void addTo(Point val) {
		to_list.add(val);
	}
	
	public void removeFromAt(int i) {
		from_list.remove(i);
	}
	
	public void removeToAt(int i) {
		to_list.remove(i);
	}
	
	public void setXYpos (int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	public void setConType (int i) {
		ConType = i;
	}
	
	
	public void setNotFlag (boolean flag) {
		notFlag = flag;
		//setTruthval(truthval);
	}
	
	/*
	 * does the same work as the MListener's connection rendering method - essentially
	 * it calculates slope and distance between two context bubbles and renders the 
	 * connector icons. Here we are highlighting the entire connection on the fly, so
	 * this method will be called by MMListener whenever the mouse is atop anything
	 * that's connected
	 * 
	 * boolean undo tells us that we need to change everything back to it's original
	 * background color
	 */
	public void highlight_other_connectors(boolean undo) {
		
		
		for (int q = 0; q < to_list.size(); q ++) {
			boolean noX = false;
			boolean noY = false;
			int slope = 0;
			int slopeR = 0;
			Point fromP = from_list.get(q);
			Point toP = to_list.get(q);
			int deltaY = Math.abs((int)toP.getY() - (int)fromP.getY());
			int deltaX = Math.abs((int)toP.getX() - (int)fromP.getX());
			
			//calculate slope and error check for divide by zero (two cBubbles in the same row/col)
			//I think we can get rid of the slope vars... 
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
			int x = (int)fromP.getX();
			int y = (int)fromP.getY();	
			int xmod = 0;
			int ymod = 0; 
			
			if (noX != true) {
				
				if ((int)to_list.get(q).getX() - (int)from_list.get(q).getX() > 0) {
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
			
				if ((int)to_list.get(q).getY() - (int)from_list.get(q).getY() > 0) {
					ymod = 1;
				}
				else {
					ymod = -1;
				}
			
			}
			
			boolean firstrun = true;
			
			//update canvas with connectors 
			while (distance > 0) {

				//highlight first node
				if (firstrun == true) {
					firstrun = false; 
					JPanel p = ProVal_GUI.gbContentArray[x][y];
					
					if (undo == false) {
						p.setBackground(Color.green);
						ProVal_GUI.highlited = true;
					}
					else {
						//p.setBackground(new Color(238,238,238));
						
						//if (ProVal_GUI.engage == true) {
							p.setBackground(Color.white);
						//}
						
						ProVal_GUI.highlited = true;

						if ((p.getComponentCount() > 0) && (p.getComponent(0).toString().contains("drawcircle") == true)) {
							
							if (ProVal_GUI.widgetArray[x][y].notFlag == true) {
								p.setBackground(Color.red);	
							}
							
						}
						
					}
					
				}
				
				if (x != (int)to_list.get(q).getX()) {
					x = x + xmod;
				}
				
				if (y != (int)to_list.get(q).getY()) {
					y = y + ymod;
				}
	
				JPanel p = ProVal_GUI.gbContentArray[x][y];
				
				if (undo == false) {
					p.setBackground(Color.green);
				}
				else {
					//p.setBackground(new Color(238,238,238));
					
					//if (ProVal_GUI.engage == true) {
						p.setBackground(Color.white);
					//}
					
					if ((p.getComponentCount() > 0) && (p.getComponent(0).toString().contains("drawcircle") == true)) {
						
						if (ProVal_GUI.widgetArray[x][y].notFlag == true) {
							p.setBackground(Color.red);	
						}
						
					}
					
				}
				
				ProVal_GUI.gridScroll.revalidate();
				distance--;
				
			}
			
		}
		
		
	}
		
	
	public void showSD() {
		ProVal_GUI.fromSD = true;
		ProVal_GUI.wxPos = getXpos();
		ProVal_GUI.wyPos = getYpos();
		JTextField name = new JTextField(10);
		name.setText(getName());
		JTextArea detail = new JTextArea(15, 15);
		detail.setText(getData());
		detail.setWrapStyleWord(true);
		detail.setLineWrap(true);
		JScrollPane jsp = new JScrollPane(detail);
		float slider_init_posF = truthval * 100;
		int slider_init_posI = (int)slider_init_posF;
		truth = new JSlider(JSlider.HORIZONTAL, 0, 100, slider_init_posI);
		truth.addChangeListener(new slidelistener());
		truth.setMajorTickSpacing(10);
		truth.setMinorTickSpacing(1);
		truth.setPaintTicks(true);
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer(0), new JLabel("FALSE") );
		labelTable.put( new Integer(50), new JLabel("NEUTRAL") );
		labelTable.put( new Integer(100), new JLabel("TRUE") );
		truth.setLabelTable( labelTable );
		truth.setPaintLabels(true);
		truthL = new JLabel("TRUTH VALUE: " + getTruthval());
		
		//if ((notFlag == true) && (ProVal_GUI.fromSD == true)) {
		//	truthL.setText("TRUTH VALUE: " + fwith2dp(1 - getTruthval()));
		//}
		
		JComponent[] inputs = new JComponent[] {
		                new JLabel("NAME"),
		                name,
		                new JLabel("DETAIL"),
		                jsp,
		                truthL,
		                truth, 
					};

		JOptionPane.showMessageDialog(
						ProVal_GUI.frame,
						inputs);
	
		setName(name.getText());
		setData(detail.getText());
		ProVal_GUI.fromSD = false;
	}

	
	public float fwith2dp(float val) {
		Float vF = val;
		Double vD = vF.doubleValue();
		vD = Math.round(vD * 100.0) / 100.0;
		val = vD.floatValue();
		return val;
	}
	
	
	public void calculatePercentChange(float oldVal, float newVal) {
		newVal = fwith2dp(newVal);
		oldVal = fwith2dp(oldVal);
		percentChange = newVal - oldVal; 
		
		if (oldVal > 0) {
			percentChange = fwith2dp(percentChange / oldVal);	
		}
		else {
			percentChange = newVal;
		}

		
		/*
		 * now we populate the percent change as if this 
		 * bad boy has been NOTed 
		 */
		newVal = 1 - newVal;
		oldVal = 1 - oldVal; 
		percentChangeNOT = newVal - oldVal;
		
		if (oldVal > 0) {
			percentChangeNOT = fwith2dp(percentChangeNOT / oldVal);	
		}
		else {
			percentChangeNOT = newVal;
		}	

	}
	
	
}