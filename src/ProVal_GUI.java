import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.zip.GZIPOutputStream;

public class ProVal_GUI {

	static JFrame frame;
	static JMenuItem file_load_MI;
	static JMenuItem file_new_MI;
	static table_model tm;
	static table_renderer tr;
	static JTable table;
	static GridBagLayout GBL;
	static JPanel gbContentArray[][];
	static float truthScore = 0;
	
	/*
	 * because Images aren't serializable, we need 
	 * a way of figuring out where the components are
	 * during load/save operations. this array is what
	 * enables that to happen
	 */
	static boolean compHere[][];
	
	static widget widgetArray[][];
	static int cols;
	static int rows;
	static JPanel grid;
	static JScrollPane gridScroll;
	static WidgetToolBar wtoolbar;
	static InfoToolBar itoolbar;
	
	/*
	 * so we know if engage() has been called from
	 * the toolbar or the CB settings dialoge
	 */
	static boolean fromSD = false;
	
	/*
	 * so when we're in widget.setTruthVal() we 
	 * know when to switch from up to downward 
	 * tree traversing
	 */
	static boolean SD_up = true;
	
	/*
	 * so we know if the engage! button has been, 
	 * well, engaged. 
	 */
	static boolean engage = false;
	
	/*
	 * used by the widget truth selector listener
	 * to determine cbubble position
	 */
	static int wxPos;
	static int wyPos;
	
	/*
	 * so MListener can tell whether or not it's in a sequence in which
	 * the user is attempting to connect context bubbles 
	 */
	static boolean first_click = false;
	static Point first_point;
	static Point second_point;
	
	/*
	 * so MMListener knows whether or not it needs to unhighlight 
	 * a connected series
	 */
	static boolean highlited = false;
	static Point lastP;
	
	public static void ProVal_GUI_START() {
		//create the interface window
		frame = new JFrame("ProVal Prototype");
		frame.setSize(1150,768);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		//create the menu bar, populate it with items, add it to the frame
		JMenuBar menuBar = new JMenuBar();
		JMenu file_menu = new JMenu("FILE");
		JMenu help_menu = new JMenu("HELP");
		menuBar.add(file_menu);
		menuBar.add(help_menu);
		
		file_new_MI = new JMenuItem("NEW");
		file_new_MI.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gridScroll.removeAll();
						grid.removeAll();
						grid.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						
					    // Specify horizontal fill, with top-left corner anchoring
					    c.fill = GridBagConstraints.BOTH;
					    c.anchor = GridBagConstraints.NORTHWEST;

					    // Select x- and y-direction weight. Without a non-zero weight,
					    // the component will still be centered in the given direction.
					    c.weightx = 1;
					    c.weighty = 1;
						
						/*
						 * rebuild gbContentArray
						 */
						for (int i = 0; i < rows; i ++) {
							
							for (int k = 0; k < cols; k ++) {	
								c.gridx = k;
								c.gridy = i;
								JPanel panel = new JPanel();
								panel.setBackground(Color.white); 
					    		panel.setPreferredSize(new Dimension(64, 64));
					    		panel.setToolTipText(k + "," +  i); 
					    		panel.addMouseListener(new MListener());
					    		panel.addMouseMotionListener(new MMListener());
					 			panel.setOpaque(true);
								panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));								 
					    		gbContentArray[k][i] = panel;
								grid.add(panel, c);
								panel.revalidate();
								
							}
							
						}
						
						
						//frame.getContentPane().remove(0);
						
						for (int z = 0; z < frame.getContentPane().getComponentCount(); z ++) {
							
							if (frame.getContentPane().getComponent(z).toString().contains("JScroll") == true) {
								frame.getContentPane().remove(z);
								MMListener.noOldP = true;
								ProVal_GUI.highlited = false;
								itoolbar.tLabel.setText("\n\n\n\n\n\u221e");
								itoolbar.tLabel.setBackground(new Color(238, 238, 238));
							}
						
						}
						
						
						gridScroll = new JScrollPane(grid); 
						frame.getContentPane().add(gridScroll);
						//frame.revalidate();
						frame.validate();
						frame.repaint();	
					}
					
				}
			);
		
		file_menu.add(file_new_MI);
		
		file_load_MI = new JMenuItem("LOAD");
		file_load_MI.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						//resource anchor
						ClassLoader cl = ResourceAnchor.class.getClassLoader();
						
						
						JFileChooser fc = new JFileChooser();
						int returnVal = fc.showOpenDialog(frame);
						
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fc.getSelectedFile();
							
							try {
							    //gbContentArray = new JPanel[cols][rows];
							    //widgetArray = new widget[cols][rows];
								FileInputStream fis = new FileInputStream(file);
								ObjectInputStream ois = new ObjectInputStream(fis);
								//gbContentArray = (JPanel[][])ois.readObject();
								widgetArray = (widget[][])ois.readObject();
								compHere = (boolean[][])ois.readObject();
								//gridScroll = (JScrollPane)ois.readObject();
						
								ois.close();
							}
							catch (Exception e1) {
								System.out.println(e1);
							}
						
						}					
						
						gridScroll.removeAll();
						grid.removeAll();
						grid.setLayout(new GridBagLayout());
						GridBagConstraints c = new GridBagConstraints();
						
					    // Specify horizontal fill, with top-left corner anchoring
					    c.fill = GridBagConstraints.BOTH;
					    c.anchor = GridBagConstraints.NORTHWEST;

					    // Select x- and y-direction weight. Without a non-zero weight,
					    // the component will still be centered in the given direction.
					    c.weightx = 1;
					    c.weighty = 1;
						
						/*
						 * rebuild gbContentArray
						 */
						for (int i = 0; i < rows; i ++) {
							
							for (int k = 0; k < cols; k ++) {	
								c.gridx = k;
								c.gridy = i;
								JPanel panel = new JPanel();
								panel.setBackground(Color.white); 
					    		panel.setPreferredSize(new Dimension(64, 64));
					    		panel.setToolTipText(k + "," +  i); 
					    		panel.addMouseListener(new MListener());
					    		panel.addMouseMotionListener(new MMListener());
					 			gbContentArray[k][i] = panel;	    		
					    		
								if (compHere[k][i] == true) {
  						
									if (widgetArray[k][i].isLogicalConnector == true) {
										
										/*
										 * and
										 */
										if (widgetArray[k][i].getConType() == 1) {
											//Image image = Toolkit.getDefaultToolkit().getImage("images/and.jpg");
											Toolkit tk = Toolkit.getDefaultToolkit();
											Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/and.jpg"));
											drawconnector dc = new drawconnector();
											dc.image = image;
											panel.add(dc);
										}
										
										
										/*
										 * or
										 */
										if (widgetArray[k][i].getConType() == 2) {
											//Image image = Toolkit.getDefaultToolkit().getImage("images/or.jpg");
											Toolkit tk = Toolkit.getDefaultToolkit();
											Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/or.jpg"));
											drawconnector dc = new drawconnector();
											dc.image = image;
											panel.add(dc);
										}
										
										
										/*
										 * if then
										 */
										if (widgetArray[k][i].getConType() == 4) {
											//Image image = Toolkit.getDefaultToolkit().getImage("images/ifthen.jpg");
											Toolkit tk = Toolkit.getDefaultToolkit();
											Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/ifthen.jpg"));
											drawconnector dc = new drawconnector();
											dc.image = image;
											panel.add(dc);
										}
										
										
										/*
										 * bi con
										 */
										if (widgetArray[k][i].getConType() == 5) {
											//Image image = Toolkit.getDefaultToolkit().getImage("images/bicon.jpg");
											Toolkit tk = Toolkit.getDefaultToolkit();
											Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/bicon.jpg"));
											drawconnector dc = new drawconnector();
											dc.image = image;
											panel.add(dc);
										}
										
										
										/*
										 * strong and
										 */
										if (widgetArray[k][i].getConType() == 6) {
											//Image image = Toolkit.getDefaultToolkit().getImage("images/strongand.jpg");
											Toolkit tk = Toolkit.getDefaultToolkit();
											Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/strongand.jpg"));
											drawconnector dc = new drawconnector();
											dc.image = image;
											panel.add(dc);
										}
										
										
										/*
										 * strong or
										 */
										if (widgetArray[k][i].getConType() == 7) {
											//Image image = Toolkit.getDefaultToolkit().getImage("images/strongor.jpg");
											Toolkit tk = Toolkit.getDefaultToolkit();
											Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/strongor.jpg"));
											drawconnector dc = new drawconnector();
											dc.image = image;
											panel.add(dc);
										}
																				
										
									}
									
									if (widgetArray[k][i].isAntecedent == true) {
										//Image image = Toolkit.getDefaultToolkit().getImage("images/coupler.jpg");
										Toolkit tk = Toolkit.getDefaultToolkit();
										Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/coupler.jpg"));
										drawconnector dc = new drawconnector();
										dc.image = image;
										panel.add(dc);
									}
									
									if (widgetArray[k][i].isConsequent == true) {
										//Image image = Toolkit.getDefaultToolkit().getImage("images/consequent.jpg");
										Toolkit tk = Toolkit.getDefaultToolkit();
										Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/consequent.jpg"));
										drawconnector dc = new drawconnector();
										dc.image = image;
										panel.add(dc);
									}
																	
									if (widgetArray[k][i].isResultant == true) {
										//Image image = Toolkit.getDefaultToolkit().getImage("images/result.jpg");
										Toolkit tk = Toolkit.getDefaultToolkit();
										Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/result.jpg"));
										drawconnector dc = new drawconnector();
										dc.image = image;
										panel.add(dc);
									}
									
									if (widgetArray[k][i].isContextBubble == true) {
										drawcircle dc = new drawcircle(widgetArray[k][i].truth2color());
										dc.setLabel("     " + widgetArray[k][i].getTruthval());
										
										if (widgetArray[k][i].notFlag == true) {
											panel.setBackground(Color.red);
										}
						
										panel.add(dc);	
									}
									
								}
								

								panel.setOpaque(true);
								panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));								 
					    		gbContentArray[k][i] = panel;
								grid.add(panel, c);
								panel.revalidate();
								
							}
							
						}
						
						
						//frame.getContentPane().remove(0);
						
						for (int z = 0; z < frame.getContentPane().getComponentCount(); z ++) {
							
							if (frame.getContentPane().getComponent(z).toString().contains("JScroll") == true) {
								frame.getContentPane().remove(z);
								MMListener.noOldP = true;
								ProVal_GUI.highlited = false;
								itoolbar.tLabel.setText("\n\n\n\n\n\u221e");
								itoolbar.tLabel.setBackground(new Color(238, 238, 238));
							}
						
						}
						
						
						gridScroll = new JScrollPane(grid); 
						frame.getContentPane().add(gridScroll);
						frame.validate();
						//frame.revalidate();
						frame.repaint();						
					}
					
				}
				
		);
				
		file_menu.add(file_load_MI);

		
		
		JMenuItem file_save_MI = new JMenuItem("SAVE");
		file_save_MI.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser();
						int returnVal = fc.showSaveDialog(frame);
						
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fc.getSelectedFile();
							
							try {
								 					 					 
								// if file doesn't exists, then create it
								if (!file.exists()) {
									file.createNewFile();
								}
					 
						        FileOutputStream fos = new FileOutputStream(file);
						        ObjectOutputStream oos = new ObjectOutputStream(fos);					        
						        //JPanel[][] gbContentArrayOut = new JPanel[cols][rows];
						        widget[][] widgetArrayOut = new widget[cols][rows];
						        
						        
						        /*
						        for (int i = 0; i < rows; i ++) {
						        	
						        	for (int k = 0; k < cols; k ++) {
						        		JPanel p = new JPanel();
						        		p = gbContentArray[k][i];
						        		gbContentArrayOut[k][i] = p;
						        	}
						        	
						        }
								*/
						        
						        for (int i = 0; i < rows; i ++) {
						        	
						        	for (int k = 0; k < cols; k ++) {
						        		compHere[k][i] = false;
						        		
						        		if (gbContentArray[k][i].getComponentCount() > 0) {
						        			widget w = new widget();
						        			w = widgetArray[k][i];
						        			widgetArrayOut[k][i] = w;
						        			compHere[k][i] = true;					        			
						        		}
						        		
						        	}
						        	
						        }
						        
						        //oos.writeObject(gbContentArrayOut);
						        //oos.flush();
						        oos.writeObject(widgetArrayOut);
						        oos.writeObject(compHere);
						        //oos.writeObject(gridScroll);  
						        oos.close();
					 
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
						}					
						
					}
					
				}
				
		);
				
		file_menu.add(file_save_MI);
		
		
		
		JMenuItem help_about_MI = new JMenuItem("ABOUT");
		help_about_MI.addActionListener( 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(frame, 
								"ProVal Prototype \n" +
								"v1 28SEP12 by David Daedalus\n" +
								"theVagrantPhilosopher@yahoo.com \n" +
								"www.projectvalis.com");
					}
				}
				
		);
			
		help_menu.add(help_about_MI);	
		frame.setJMenuBar(menuBar);		
		
		
		//setup content pane
		GBL = new GridBagLayout();
		grid = new JPanel(GBL);
		grid.setBackground(Color.white);
	    GridBagConstraints c = new GridBagConstraints();
	    
	    // Specify horizontal fill, with top-left corner anchoring
	    c.fill = GridBagConstraints.BOTH;
	    c.anchor = GridBagConstraints.NORTHWEST;

	    // Select x- and y-direction weight. Without a non-zero weight,
	    // the component will still be centered in the given direction.
	    c.weightx = 1;
	    c.weighty = 1;

	    //grid.setBackground( Color.black);
	    //grid.setBorder( new MatteBorder(1, 1, 1, 1, Color.BLACK) );
	    cols = 25;
	    rows = 100;		
	    gbContentArray = new JPanel[cols][rows];
	    widgetArray = new widget[cols][rows];
	    compHere = new boolean[cols][rows];
	    
	    for (int i = 0; i < rows; i++) {
	    	
	    	for (int k = 0; k < cols; k ++) {
	    		JPanel panel = new JPanel();
	    		panel.setBackground(Color.white);
	    		panel.setPreferredSize(new Dimension(64, 64));
	    		panel.setToolTipText(k + "," +  i);    		
	    		c.gridx = k;
	    		c.gridy = i;	 
	    		panel.addMouseListener(new MListener());
	    		panel.addMouseMotionListener(new MMListener());
	    		//panel.setOpaque(true);
	    		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	    		//panel.setBackground(Color.white);
	        	grid.add(panel, c);
	        	gbContentArray[k][i] = panel;
	    	}
	        
	    }
		
	    //grid.addMouseListener(new MListener());
	    //grid.addMouseMotionListener(new MMListener());
	    //grid.setBackground(Color.white);
	    gridScroll = new JScrollPane(grid);
	    //gridScroll.setBackground(Color.white);

		
	    //toolbar whizbang foo!
	    wtoolbar = new WidgetToolBar();
	    wtoolbar.setFloatable(true);
	    itoolbar = new InfoToolBar();
	    itoolbar.setFloatable(true);	
	    
	    //populate frame
	    frame.add(gridScroll);
	    frame.add(wtoolbar, BorderLayout.NORTH);
	    frame.add(itoolbar, BorderLayout.EAST);
		frame.setVisible(true);
	}
	  
	public static void engage() {		
		
		/*
		 * found the first row of couplers? 
		 */
		boolean topCouplerRow = false;
		int topCouplerRowI = 0;	
		int topCouplerColI = 0; 
		float tScore;
		
		/*
		 * calculate truth score! 
		 */
		for (int j = 0; j < ProVal_GUI.rows; j ++) {
			
			for (int k = 0; k < ProVal_GUI.cols; k ++) {
				JPanel panel = ProVal_GUI.gbContentArray[k][j];
				
				if (panel.getComponentCount() > 0) {
					
					if (ProVal_GUI.widgetArray[k][j].isLogicalConnector() == true) {
						widget w = widgetArray[k][j];
					
						if (topCouplerRow == false) {		
							topCouplerRowI = j;
							topCouplerColI = k;
							topCouplerRow = true; 
						}
						
						/*
						 * check to see if we're still in the topmost row of couplers
						 */
						if ((j > topCouplerRowI) && (topCouplerRow == true)) {
							topCouplerRow = false;
						}
						
						
						/*AND
						 * 
						 * Weak Conjunction: F & (x,y) = min{x, y}
						 * 
						 */
						if (w.getConType() == 1) {
							
							if(topCouplerRow == true) {
								ArrayList<widget> antList = w.getAntecedantBubbles();
								ArrayList<widget> conList = w.getConsequentBubbles();
								
								float smallestF = 1;
								
								for (int z = 0; z < antList.size(); z ++) {
									
									if (antList.get(z).getTruthval() < smallestF) {
										smallestF = antList.get(z).getTruthval();
			
										if (antList.get(z).notFlag == true) {
											smallestF = w.fwith2dp(1 - smallestF);
										}
										
									}					

								}					
							
								for (int z = 0; z < conList.size(); z ++) {
									conList.get(z).setTruthval(smallestF);
								}
								
							}
							else {
								ArrayList<widget> antList = w.getAntecedantBubbles();
								ArrayList<widget> conList = w.getConsequentBubbles();
								
								float smallestF = 1;
								
								for (int z = 0; z < antList.size(); z ++) {
									
									if (antList.get(z).getTruthval() < smallestF) {
										smallestF = antList.get(z).getTruthval();
										
										if (antList.get(z).notFlag == true) {
											smallestF = 1 - smallestF;
										}
										
										//check for the antecedants of the antecedants
										engageUP(antList.get(z));
										
									}					

								}					
								
								for (int z = 0; z < conList.size(); z ++) {
									conList.get(z).setTruthval(smallestF);
								}
								
							}
							
						}
														
						
						
						/* OR
						 * 
						 * Weak Disjunction: F v (x,y) = max{x, y}
						 * 
						 */
						if (w.getConType() == 2) {
							
							if(topCouplerRow == true) {
								ArrayList<widget> antList = w.getAntecedantBubbles();
								ArrayList<widget> conList = w.getConsequentBubbles();
								
								antList = sortCBListH2L(antList);
								float biggestF = antList.get(0).getTruthval();
										
								if (antList.get(0).notFlag == true) {
									biggestF = w.fwith2dp(1 - biggestF);
								}
								
								for (int z = 0; z < conList.size(); z ++) {							
									conList.get(z).setTruthval(biggestF);
								}
								
							}
							else {
								ArrayList<widget> antList = w.getAntecedantBubbles();
								ArrayList<widget> conList = w.getConsequentBubbles();
								antList = sortCBListH2L(antList);
								float biggestF = antList.get(0).getTruthval();
										
								if (antList.get(0).notFlag == true) {
									biggestF = w.fwith2dp(1 - biggestF);
								}
								
								engageUP(antList.get(0));
													
								for (int z = 0; z < conList.size(); z ++) {
									conList.get(z).setTruthval(biggestF);
								}
								
							}
							
							
						}
											
						
						/* STRONG OR
						 * 
						 * Strong OR: min{1, x + y}
						 * 
						 */
						if (w.getConType() == 7) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							float totalT = 0;
								
							for (int z = 0; z < antList.size(); z ++) {		
								
								if (antList.get(z).notFlag == false) {
									totalT = w.fwith2dp(totalT + antList.get(z).truthval);
								}
								else {
									totalT = w.fwith2dp(totalT + (1 - antList.get(z).truthval));
								}
		
							}	
								
							if (totalT > 1) {
								totalT = 1;
							}
							
							for (int z = 0; z < conList.size(); z ++) {
								conList.get(z).setTruthval(totalT);
							}
							
							if(topCouplerRow == false) {
								
								for (int z = 0; z < antList.size(); z ++) {										
									//check for the antecedants of the antecedants
									engageUP(antList.get(z));
								}					

								
							}
							
							
						}
												
						
						/* STRONG AND
						 * 
						 * max{0, x + (y - 1)}
						 * 
						 */
						if (w.getConType() == 6) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							float totalT = 0;
									
							/*
							 * x val
							 */
							if (antList.get(0).notFlag == false) {
								totalT = w.fwith2dp(totalT + antList.get(0).truthval);
							}
							else {
								totalT = w.fwith2dp(totalT + (1 - antList.get(0).truthval));
							}
							
							/*
							 * y val
							 */
							if (antList.get(1).notFlag == false) {
								totalT = w.fwith2dp(totalT + (antList.get(1).truthval - 1));
							}
							else {
								totalT = w.fwith2dp(totalT + ((1 - antList.get(1).truthval) - 1));
							}
							
							
							if (totalT < 0) {
								totalT = 0;
							}
							
							
							for (int z = 0; z < conList.size(); z ++) {
								conList.get(z).setTruthval(totalT);
							}
							
							if(topCouplerRow == false) {
								
								for (int z = 0; z < antList.size(); z ++) {										
									//check for the antecedants of the antecedants
									engageUP(antList.get(z));
								}					

								
							}
							
							
						}
						
						
						/*
						 * IF THEN
						 * x->y
						 * min {1, ((1 - x) + y))}
						 */
						if (w.getConType() == 4) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							ArrayList<widget> resList = w.getResultantBubbles();
							float totalT = 0;
							float totalCon = 0; 

							for (int z = 0; z < antList.size(); z ++) {		

								if (antList.get(z).notFlag == false) {
									totalT = w.fwith2dp(totalT + antList.get(z).getTruthval());

								}
								else {
									totalT = w.fwith2dp(totalT + (1 - antList.get(z).getTruthval()));
								}

							}	


							for (int z = 0; z < conList.size(); z ++) {		

								if (conList.get(z).notFlag == false) {
									totalCon = w.fwith2dp(totalCon + conList.get(z).getTruthval());
								}
								else {
									totalCon = w.fwith2dp(totalCon + (1 - conList.get(z).getTruthval()));
								}


							}


							/*
							 * hope I'm doing the right thing by 
							 * taking the avg of the consequents and 
							 * antecedents. 
							 */
							totalT = w.fwith2dp(totalT / antList.size());
							totalCon = w.fwith2dp(totalCon / conList.size());
							totalT = 1 - totalT + totalCon;


							if (totalT > 1) {
								totalT = 1;
							}

							for (int z = 0; z < resList.size(); z ++) {
								resList.get(z).setTruthval(totalT);
							}

							if(topCouplerRow == false) {

								for (int z = 0; z < antList.size(); z ++) {										
									//check for the antecedants of the antecedants
									engageUP(antList.get(z));
								}					


							}


						}
						
						
						/*
						 * BICON
						 * x <-> y
						 * (1 - |x - y|)
						 */
						if (w.getConType() == 5) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							ArrayList<widget> resList = w.getResultantBubbles();
							float totalT = 0;
							float totalCon = 0; 

							for (int z = 0; z < antList.size(); z ++) {		

								if (antList.get(z).notFlag == false) {
									totalT = w.fwith2dp(totalT + antList.get(z).getTruthval());

								}
								else {
									totalT = w.fwith2dp(totalT + (1 - antList.get(z).getTruthval()));
								}

							}	


							for (int z = 0; z < conList.size(); z ++) {		

								if (conList.get(z).notFlag == false) {
									totalCon = w.fwith2dp(totalCon + conList.get(z).getTruthval());
								}
								else {
									totalCon = w.fwith2dp(totalCon + (1 - conList.get(z).getTruthval()));
								}


							}


							/*
							 * hope I'm doing the right thing by 
							 * taking the avg of the consequents and 
							 * antecedents. 
							 */
							totalT = w.fwith2dp(totalT / antList.size());
							totalCon = w.fwith2dp(totalCon / conList.size());
							totalT = 1 - Math.abs(totalT - totalCon);

							for (int z = 0; z < resList.size(); z ++) {
								resList.get(z).setTruthval(totalT);
							}

							if(topCouplerRow == false) {

								for (int z = 0; z < antList.size(); z ++) {										
									//check for the antecedants of the antecedants
									engageUP(antList.get(z));
								}					


							}


						}
						
						
					}
					
					
				}
								
				
			}
			
			
		}
		
		
	}
	
	/*
	 * this method is used to engage the calculation process 
	 * up through the chain of antecedents.
	 * 
	 * change process reduces the truth value of the antecedents
	 * proportionally to the change in the antecedent. in future, 
	 * other methods may be employed based upon the type of connector.
	 * 
	 *  for example, for AND, ther function is min{x, y}. if we know that
	 *  it's only the X value that's determining the consequent, it may
	 *  be possible to only change that value and leave the others alone. 
	 *  definitely worth exploring. 
	 *
	 */
	public static void engageUP(widget w) {
		float tScore;
		int xPos = w.getXpos();
		int yPos = w.getYpos();

		/*
		 * do all antecedents
		 */
		for (int q = 0; q < w.getAntecedants().size(); q ++) {
			
			widget wtemp = w.getAntecedants().get(q);
			
			if (wtemp.isLogicalConnector() == true) {

				/*AND
				 * 
				 * Weak Conjunction: F & (x,y) = min{x, y}
				 * 
				 */
				if (wtemp.getConType() == 1) {
					ArrayList<widget> antList = wtemp.getAntecedantBubbles();
					float percentChange;
					
					/*
					 * update antecedent bubbles as needed
					 */
					antList = sortCBListL2H(antList);				
					float smallestF = antList.get(0).getTruthval();
					
					if (antList.get(0).notFlag == true) {
						smallestF = 1 - antList.get(0).getTruthval();
					}
					
					float setDiff = 0; 
					float neighborDiff = 0; 
					boolean increased = true;
					
					if (w.percentChange < 0) {
						increased = false; 
					}
							
					if (w.notFlag == true) {
						
						if (increased == true) {
							increased = false;
						}
						else {
							increased = true;
						}
						
					}
					
					
					//Q: have we increased or decreased? 
					
					/*
					 * A: increased
					 */
					if (increased == true) {	
						setDiff = w.fwith2dp(w.getTruthval() - smallestF);
						
						if (w.notFlag == true) {
							setDiff = w.fwith2dp((1 - w.getTruthval()) - smallestF);
						}
						
						float prevT = smallestF;						
						float remainder = 0; 
						
						/*
						 * figure out which bubbles need to be updated
						 */
						for (int z = 0; z < (antList.size() - 1); z++ ) {	
							
							neighborDiff = w.fwith2dp((antList.get(z + 1).getTruthval()) - prevT);

							if (antList.get(z + 1).notFlag == true) {
								neighborDiff = w.fwith2dp(1 - (antList.get(z + 1).getTruthval()) - prevT);
							}
												
							if (neighborDiff >= setDiff) {
								float val = w.getTruthval();
								
								if (w.notFlag == true) {
									val = w.fwith2dp(1 - val);
								}
								
								antList.get(z).setTruthval(val);
								break;
							}
							else {
								prevT = antList.get(z).getTruthval();
								
								if (antList.get(z).notFlag == true) {
									prevT = 1 - (antList.get(z).getTruthval());
								}
								
								remainder = w.fwith2dp(setDiff - neighborDiff);					
								float zPlusOneNTV = antList.get(z + 1).getTruthval(); 
								
								if (antList.get(z + 1).notFlag == true) {
									zPlusOneNTV = 1 - zPlusOneNTV;
								}
								
								if ((remainder + (zPlusOneNTV)) > 1) {
									antList.get(z).setTruthval(1);
									antList.get(z + 1).setTruthval(1);
								}
								else {
									float val = w.getTruthval();
									
									if (w.notFlag == true) {
										val = w.fwith2dp(1 - val);
									}
									
									antList.get(z).setTruthval(val);
									antList.get(z + 1).setTruthval(w.fwith2dp(remainder + (zPlusOneNTV)));
								}
								
							}
							
						}				
						
					}
					/*
					 * A: DECREASED 
					 */
					else {
						float val = w.getTruthval();
						
						if (w.notFlag == true) {
							val = w.fwith2dp(1 - val);
						}
						
						antList.get(0).setTruthval(val);
					}
									
					
					/*
					 * update antecedents of antecedents
					 */
					for (int z = 0; z < antList.size(); z ++) {
						engageUP(antList.get(z));
					}
					
				}

				
				/* OR
				 * 
				 * Weak Disjunction: F v (x,y) = max{x, y}
				 * 
				 */
				if (wtemp.getConType() == 2) {
					ArrayList<widget> antList = wtemp.getAntecedantBubbles();

					/*
					 * update antecedent bubbles as needed
					 */
					antList = sortCBListH2L(antList);	
					float biggestF = antList.get(0).getTruthval();
					
					if (antList.get(0).notFlag == true) {
						biggestF = w.fwith2dp(1 - biggestF);
					}
					
					float setDiff = 0; 
					float neighborDiff = 0; 
					
					//Q: have we increased or decreased? 
					
					/*
					 * A: decreased
					 */
					if (w.getTruthval() < biggestF) {						
						setDiff = w.fwith2dp(w.getTruthval() - biggestF);
						float prevT = biggestF;
						float remainder = 0; 
						
						/*
						 * figure out which bubbles need to be updated
						 */
						for (int z = 0; z < (antList.size() - 1); z++ ) {	
							
							neighborDiff = w.fwith2dp((antList.get(z + 1).getTruthval()) - prevT);

							if (antList.get(z + 1).notFlag == true) {
								neighborDiff = w.fwith2dp((1 -(antList.get(z + 1).getTruthval())) - prevT);
							}
							
							if (neighborDiff <= setDiff) {
								float val = w.getTruthval();
								
								if (w.notFlag == true) {
									val = w.fwith2dp(1 - val);
								}
								
								antList.get(0).setTruthval(val);
								break;
							}
							else {
								prevT = antList.get(z).getTruthval();
								
								if (antList.get(z).notFlag == true) {
									prevT = w.fwith2dp(1 - prevT);
								}
								
								remainder = w.fwith2dp(setDiff - neighborDiff);
								float zPlusOneNTV = antList.get(z + 1).getTruthval(); 
								
								if (antList.get(z + 1).notFlag == true) {
									zPlusOneNTV = 1 - zPlusOneNTV;
								}
								
								if ((remainder + zPlusOneNTV) < 0) {
									antList.get(z).setTruthval(0);
									antList.get(z + 1).setTruthval(0);
								}
								else {
									float val = w.getTruthval();
									
									if (w.notFlag == true) {
										val = w.fwith2dp(1 - val);
									}
									
									antList.get(z).setTruthval(val);
									antList.get(z + 1).setTruthval(w.fwith2dp(remainder + zPlusOneNTV));
								}
								
							}
							
						}				
						
					}
					/*
					 * A: INCREASED 
					 */
					else {
						float val = w.getTruthval();
						
						if (w.notFlag == true) {
							val = w.fwith2dp(1 - val);
						}
						
						antList.get(0).setTruthval(val);
					}
										
					
					/*
					 * update antecedents of antecedents
					 */
					for (int z = 0; z < antList.size(); z ++) {
						engageUP(antList.get(z));
					}
					
					
				}
				
				
				/* STRONG OR
				 * 
				 * Strong OR: min{1, x + y}
				 * 
				 */
				if (wtemp.getConType() == 7) {
					ArrayList<widget> antList = wtemp.getAntecedantBubbles();
					antList = sortCBListL2H(antList);
					float percentChange;
					float totalT = 0;
					float totalTnot = 0;
					
					for (int z = 0; z < antList.size(); z ++) {		
						totalT = totalT + antList.get(z).getTruthval();
						totalTnot = totalTnot + (1 - antList.get(z).getTruthval());	
					}
					
					
					/*
					 * means we can use a simple ratio to figure out the 
					 * antecedent truth values should be
					 */
					if ((totalT < 1) && (w.getTruthval() < 1)) {
						percentChange = w.getPercentChange();
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							
							if (antList.get(g).getTruthval() == 0) {
								modF = Math.abs(percentChange) / antList.size();
							}
							
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if ((w.notFlag == true) && antList.get(g).getTruthval() != 0) {
								/*
								 * what percentage of the old CB truthVal was the 
								 * current antecedent? 
								 */
								float percentOldVal = antList.get(g).getTruthval() / (1 - w.oldTruthVal);
								
								/*
								 *  update the new val with the correct percentage of the 
								 *  change between the consequents old and new values		
								 */
								newtVal = w.fwith2dp((antList.get(g).getTruthval()) - 
										(percentOldVal * (w.getTruthval() - w.getOldTruthVal())));	
							}
							
				
							if (newtVal < 0) {
								newtVal = 0;
							}
							
							if (newtVal > 1) {
								newtVal = 1;
							}
							
							antList.get(g).setTruthval(w.fwith2dp(newtVal));
						}
						
					}
					
					
					if ((totalT > 1) && (w.getTruthval() < 1)) {
						percentChange = w.getPercentChange();
						float newtValTemp = 0;
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if (w.notFlag == true) {
								modF = 1 - newtVal;
								newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							}
							
				
							if ((newtVal < 0)) {
								newtVal = 0;
							}
													
							if (w.notFlag == false) {
								newtValTemp = newtValTemp + newtVal;
							}
							else {
								newtValTemp = newtValTemp + (1 - newtVal);
							}
														
							antList.get(g).setTruthval(newtVal);
						}
						
						/*
						 * check to see if we need to reduce the truth value
						 * of the antecedents further
						 */
						float difference = 0;
						float difference_div = 0;

						if (newtValTemp > w.getTruthval()) {
							difference = w.fwith2dp(newtValTemp - w.getTruthval());
							difference_div = difference = w.fwith2dp(difference / antList.size());	
						}
						
						while (difference > 0) {
							float ttemp = 0;
							
							for (int g = 0; g < antList.size(); g ++) {
								
								if (w.notFlag == false) {
									ttemp = w.fwith2dp(antList.get(g).getTruthval() - difference_div);
								}
								else {
									ttemp = w.fwith2dp(antList.get(g).getTruthval() + difference_div);
								}
								
								difference = w.fwith2dp(difference - difference_div);
								
								if (ttemp < 0) {
									difference = difference + Math.abs(ttemp);
									ttemp = 0;
								}
								
								antList.get(g).setTruthval(w.fwith2dp(ttemp));		
							}
							
						}
						
					}
					
					
					if ((totalT < 1) && (w.getTruthval() == 1)) {
						percentChange = w.getPercentChange();
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if (w.notFlag == true) {
								newtVal = 0;	
							}

							if ((newtVal > 1)) {
								newtVal = 1;
							}
							
							antList.get(g).setTruthval(newtVal);
						}
						
					}
					
				
					if ((totalT == 1) && (w.getTruthval() < 1)) {
						percentChange = w.getPercentChange();
						
						for (int g = 0; g < antList.size(); g ++) {
							float modF = w.fwith2dp(percentChange * antList.get(g).getTruthval());
							float newtVal = w.fwith2dp(antList.get(g).getTruthval() + modF);
							
							if (w.notFlag == true) {
								/*
								 * because totalT = 1, we know the percentage
								 * of 1 that each antecedent represents  
								 */
								newtVal = (1 - w.getTruthval()) * antList.get(g).getTruthval();
								
							}
							
							if ((newtVal > 1)) {
								newtVal = 1;
							}
							
							antList.get(g).setTruthval(newtVal);
						}
						
					}
					
					/*
					 * update antecedents of antecedents
					 */
					for (int z = 0; z < antList.size(); z ++) {
						engageUP(antList.get(z));
					}
										
				}				
				
				
				
				/* STRONG AND
				 * 
				 * max{0, x + y - 1)}
				 * 
				 */
				if (wtemp.getConType() == 6) {
					ArrayList<widget> antList = wtemp.getAntecedantBubbles();
					antList = sortCBListL2H(antList);
					float percentChange = w.getPercentChange();
					
					if (w.notFlag == true) {
						percentChange = w.getPercentChangeNOT();
					}
					
					float totalT = 0;
					float totalTnew = w.getTruthval();
					
					/*
					 * tells us whether or not zero was the max valus
					 */
					boolean maxFlag = false;
					
					for (int z = 0; z < antList.size(); z ++) {
						
						if (antList.get(z).notFlag == false) {
							totalT = w.fwith2dp(totalT + antList.get(z).truthval);
						}
						else {
							totalT = w.fwith2dp(totalT + (1 - antList.get(z).truthval));
						}
						
					}
					
					//if (totalT < 0) {
					//	totalT = 0;
					//	maxFlag = true;
					//}			
					
					
					/*
					 * now figure out by what value we need to change
					 * the antecedent values (by the power of 
					 * AL-GE-BRAAAA!!!
					 */
					float modVal = totalTnew - (totalT - 1);
					modVal = modVal / 2;
					float remainder = 0;
					boolean rFlag = false;
					float newTruthVal = 0;
					
					for (int z = 0; z < antList.size(); z ++) {						
						newTruthVal = w.fwith2dp(antList.get(z).getTruthval() + modVal);

						/*
						 * check for remainder
						 */
						if (rFlag == true) {
							newTruthVal = w.fwith2dp(newTruthVal + remainder);
							remainder = 0; 
							rFlag = false;
						}
						
						/*
						 * ensure we don't set anything beyond the range of zero to one
						 * and set the remainder flag if needed
						 */
						if (newTruthVal > 1) {
							remainder = newTruthVal - 1;
							newTruthVal = 1;
							rFlag = true;
						}
						
						if (newTruthVal < 0) {
							remainder = newTruthVal;
							newTruthVal = 0;
							rFlag = true;
						}
						
						antList.get(z).setTruthval(newTruthVal);				
					}
					
					
					/*
					 * incase the last antecedent in the list set the remainder flag
					 */
					if (rFlag == true) {
						boolean conMet = false;
						int z = 0;
						
						while (conMet == false) {
							newTruthVal = w.fwith2dp(antList.get(z).getTruthval() + remainder);

							if ((newTruthVal >= 0) && (newTruthVal <= 1)) {
								antList.get(z).setTruthval(newTruthVal);
								conMet = true;
							}
							else {
								z ++;								
							}
							
						}
						
					}

					
					
					/*
					 * update antecedents of antecedents
					 */
					for (int z = 0; z < antList.size(); z ++) {
						engageUP(antList.get(z));
					}
										
				}
				
				
				
				
				
				/* IF THEN
				 * 
				 * min{1, (1 - x) + y}
				 * 
				 */
				if (wtemp.getConType() == 4) {
					ArrayList<widget> antList = wtemp.getAntecedantBubbles();
					ArrayList<widget> conList = wtemp.getConsequentBubbles();
					ArrayList<widget> resList = wtemp.getResultantBubbles();
					antList = sortCBListL2H(antList);
					boolean ResAnchorFlag = false;
					float totalT = 0;
					float totalTant = 0;
					float totalCon = 0; 
					float totalRes = 0;
					
					for (int z = 0; z < antList.size(); z ++) {		
						
						if (antList.get(z).notFlag == false) {
							totalT = w.fwith2dp(totalT + antList.get(z).getTruthval());
						
						}
						else {
							totalT = w.fwith2dp(totalT + (1 - antList.get(z).getTruthval()));
						}

					}	
					
					
					for (int z = 0; z < conList.size(); z ++) {		
						
						if (conList.get(z).notFlag == false) {
							totalCon = w.fwith2dp(totalCon + conList.get(z).getTruthval());
						}
						else {
							totalCon = w.fwith2dp(totalCon + (1 - conList.get(z).getTruthval()));
						}

						
					}
					
					
					for (int z = 0; z < resList.size(); z ++) {
						
						if (resList.get(z).isAnchored == true) {
							ResAnchorFlag = true;
						}
						
						if (resList.get(z).notFlag == false) {
							totalRes = w.fwith2dp(totalRes + resList.get(z).getTruthval());
						}
						else {
							totalRes = w.fwith2dp(totalRes + (1 - resList.get(z).getTruthval()));
						}
						
					}
						
					
					/*
					 * hope I'm doing the right thing by 
					 * taking the avg of the consequents and 
					 * antecedents. 
					 */
					totalTant = totalT;
					totalT = w.fwith2dp(totalT / antList.size());
					totalCon = w.fwith2dp(totalCon / conList.size());
					totalT = 1 - totalT + totalCon;				
System.out.println("totalT is: " + totalT);	
System.out.println("totalTant is: " + totalTant);
					if (totalT > 1) {
						totalT = 1;
					}
					
					
					/*
					 * engageDown() will take care of the rest if the 
					 * user is altering one of the resultant bubbles 
					 */
					if (ResAnchorFlag == false) {						
						continue;				
					}

					
					/*
					 * calculate percent change for consequent and
					 * update accordingly 
					 */
					float newtotalCon = 0; 
					float percentChange = w.getPercentChange();
					
					
					/*
					 * this ensures we calculate all the truth
					 * scores correctly 
					 */
					if (w.notFlag == true) {
						percentChange = w.getPercentChangeNOT();
					}
					
System.out.println("w % change is: " + w.getPercentChange());					
					for (int z = 0; z < conList.size(); z ++) {
System.out.println("con truth val is: " + conList.get(z).getTruthval());	

						float newTval = w.fwith2dp(conList.get(z).getTruthval() + 
									(conList.get(z).getTruthval() * percentChange)); 
						
System.out.println("newTval is: " + newTval);
						
						if (conList.get(z).getTruthval() == 0) {
							float changeVal = Math.abs(w.getTruthval() - w.getOldTruthVal());
							conList.get(z).setTruthval(changeVal / conList.size());
						}
						else {
							conList.get(z).setTruthval(newTval);
						}
						
						newtotalCon = newtotalCon + conList.get(z).getTruthval();
					}
					
					
					/*
					 * now figure out by what value we need to change
					 * the antecedent values (by the power of 
					 * AL-GE-BRAAAA!!!
					 */
					float remainder = w.fwith2dp(w.getTruthval() - newtotalCon);
System.out.println("newtotalCon and remainder are: " + newtotalCon  + " " + remainder);
					remainder = (remainder - 1) * (-1); 					
					remainder = remainder * antList.size();				
					float antMod = w.fwith2dp(remainder / totalTant);
System.out.println("antMod is: " + antMod);		
					float newTval = 0; 

					for (int z = 0; z < antList.size(); z ++) {
						
						if (antList.get(z).getTruthval() == 0) {
							float changeVal = Math.abs(w.getTruthval() - w.getOldTruthVal());
							antList.get(z).setTruthval(changeVal / antList.size());
						}
						
						newTval = w.fwith2dp(antList.get(z).getTruthval() * antMod);
System.out.println("newTval is: " + newTval);
	
						if (newTval > 1) {
							newTval = 1;
						}
						
						if (newTval < 0) {
							newTval = 0;
						}
						
						antList.get(z).setTruthval(newTval);
					}
			

					
					
					
					/*
					 * update antecedents of antecedents
					 */
					for (int z = 0; z < antList.size(); z ++) {
						engageUP(antList.get(z));
					}
										
				}
				
				
				
				
				/* BICON
				 * 
				 * x <-> y
				 * 1 - |x-y|
				 * 
				 */
				if (wtemp.getConType() == 5) {
					ArrayList<widget> antList = wtemp.getAntecedantBubbles();
					ArrayList<widget> conList = wtemp.getConsequentBubbles();
					ArrayList<widget> resList = wtemp.getResultantBubbles();
					antList = sortCBListL2H(antList);
					boolean ResAnchorFlag = false;
					float totalT = 0;
					float totalTant = 0;
					float totalCon = 0; 
					float totalRes = 0;
					
					for (int z = 0; z < antList.size(); z ++) {		
						
						if (antList.get(z).notFlag == false) {
							totalT = w.fwith2dp(totalT + antList.get(z).getTruthval());
						
						}
						else {
							totalT = w.fwith2dp(totalT + (1 - antList.get(z).getTruthval()));
						}

					}	
					
					
					for (int z = 0; z < conList.size(); z ++) {		
						
						if (conList.get(z).notFlag == false) {
							totalCon = w.fwith2dp(totalCon + conList.get(z).getTruthval());
						}
						else {
							totalCon = w.fwith2dp(totalCon + (1 - conList.get(z).getTruthval()));
						}

						
					}
					
					
					for (int z = 0; z < resList.size(); z ++) {
						
						if (resList.get(z).isAnchored == true) {
							ResAnchorFlag = true;
						}
						
						if (resList.get(z).notFlag == false) {
							totalRes = w.fwith2dp(totalRes + resList.get(z).getTruthval());
						}
						else {
							totalRes = w.fwith2dp(totalRes + (1 - resList.get(z).getTruthval()));
						}
						
					}
						
					
					/*
					 * hope I'm doing the right thing by 
					 * taking the avg of the consequents and 
					 * antecedents. 
					 */
					totalTant = totalT;
					totalT = w.fwith2dp(totalT / antList.size());
					totalCon = w.fwith2dp(totalCon / conList.size());
					totalT = 1 - Math.abs(totalT - totalCon);				

					
					/*
					 * engageDown() will take care of the rest if the 
					 * user is altering one of the resultant bubbles 
					 */
					if (ResAnchorFlag == false) {						
						continue;				
					}

					
					/*
					 * calculate percent change for consequent and
					 * update accordingly 
					 */
					float newtotalCon = 0; 
					float percentChange = w.getPercentChange();
					
					
					/*
					 * this ensures we calculate all the truth
					 * scores correctly 
					 */
					if (w.notFlag == true) {
						percentChange = w.getPercentChangeNOT();
					}
					
					for (int z = 0; z < conList.size(); z ++) {
						float newTval = w.fwith2dp(conList.get(z).getTruthval() + 
									(conList.get(z).getTruthval() * percentChange)); 
						
						
						if (conList.get(z).getTruthval() == 0) {
							float changeVal = Math.abs(w.getTruthval() - w.getOldTruthVal());
							conList.get(z).setTruthval(changeVal / conList.size());
						}
						else {
							conList.get(z).setTruthval(newTval);
						}
						
						newtotalCon = newtotalCon + conList.get(z).getTruthval();
					}
					
					
					/*
					 * now figure out by what value we need to change
					 * the antecedent values (by the power of 
					 * AL-GE-BRAAAA!!!
					 */
					float remainder = w.fwith2dp(w.getTruthval() - newtotalCon);
					remainder = (remainder - 1) * (-1); 					
					remainder = remainder * antList.size();				
					float antMod = w.fwith2dp(remainder / totalTant);
					float newTval = 0; 

					for (int z = 0; z < antList.size(); z ++) {
						
						if (antList.get(z).getTruthval() == 0) {
							float changeVal = Math.abs(w.getTruthval() - w.getOldTruthVal());
							antList.get(z).setTruthval(changeVal / antList.size());
						}
						
						newTval = w.fwith2dp(antList.get(z).getTruthval() * antMod);
	
						if (newTval > 1) {
							newTval = 1;
						}
						
						if (newTval < 0) {
							newTval = 0;
						}
						
						antList.get(z).setTruthval(newTval);
					}
								
					
					/*
					 * update antecedents of antecedents
					 */
					for (int z = 0; z < antList.size(); z ++) {
						engageUP(antList.get(z));
					}
										
				}
				
				
				
			}

		}
		
	}
	
	
	/*
	 * this method only calculates truth score from the
	 * axiom row down to the consequent row. it's called 
	 * after engage() as a means of ensuring any change made
	 * somewhere between the axiom and consequent rows are 
	 * propagated throughout the entire stream. 
	 */
	public static void engageDOWN() {		

		float tScore = 0;
		int NumTScoreBubbles = 0;
		
		/*
		 * calculate truth score! 
		 */
		for (int j = 0; j < ProVal_GUI.rows; j ++) {
			
			for (int k = 0; k < ProVal_GUI.cols; k ++) {
				JPanel panel = ProVal_GUI.gbContentArray[k][j];
				
				if (panel.getComponentCount() > 0) {
					
					if (ProVal_GUI.widgetArray[k][j].isLogicalConnector() == true) {
						widget w = widgetArray[k][j];
						
						/*AND
						 * 
						 * Weak Conjunction: F & (x,y) = min{x, y}
						 * 
						 */
						if (w.getConType() == 1) {												
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							antList = sortCBListL2H(antList);							
							float smallestF = antList.get(0).getTruthval();
							
							if (antList.get(0).notFlag == true) {
								smallestF = 1 - antList.get(0).getTruthval();
							}

							for (int z = 0; z < conList.size(); z ++) {	
								
								//incase the consequent has the NOT flag set
								if ((conList.get(z).notFlag == true)){
									
									/*
									 * this is the one case where we want to override the
									 * ANCHOR restriction on NOTing a truthValue. We're
									 * double negating the value so that it ends up being 
									 * the same (ie - truth preserving)
									 */
									if (conList.get(z).isAnchored == true) {
										smallestF = w.fwith2dp(1 - smallestF);
										conList.get(z).setTruthval(smallestF);
									}
									else {								 
										conList.get(z).setTruthval(smallestF);
									}
									
								}
								else {
									conList.get(z).setTruthval(smallestF);
								}
								

							}
							
						}
														
						
						
						/* OR
						 * 
						 * Weak Disjunction: F v (x,y) = max{x, y}
						 * 
						 */
						if (w.getConType() == 2) {							
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							antList = sortCBListH2L(antList);
							float biggestF = antList.get(0).getTruthval();
		
							if (antList.get(0).notFlag == true) {
								biggestF = 1 - antList.get(0).getTruthval();
							}

							for (int z = 0; z < conList.size(); z ++) {	
								
								//incase the consequent has the NOT flag set
								if ((conList.get(z).notFlag == true)){
									
									/*
									 * this is the one case where we want to override the
									 * ANCHOR restriction on NOTing a truthValue. We're
									 * double negating the value so that it ends up being 
									 * the same (ie - truth preserving)
									 */
									if (conList.get(z).isAnchored == true) {
										biggestF = w.fwith2dp(1 - biggestF);
										conList.get(z).setTruthval(biggestF);
									}
									else {								 
										conList.get(z).setTruthval(biggestF);
									}
									
								}
								else {
									conList.get(z).setTruthval(biggestF);
								}
								
								
							}
								
							
						}
											
						
						
						/* STRONG OR
						 * 
						 * Strong OR: min{1, x + y}
						 * 
						 */
						if (w.getConType() == 7) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							float totalT = 0;
							
							for (int z = 0; z < antList.size(); z ++) {		
								
								if (antList.get(z).notFlag == false) {
									totalT = w.fwith2dp(totalT + antList.get(z).truthval);
								}
								else {
									totalT = w.fwith2dp(totalT + (1 - antList.get(z).truthval));
								}
		
							}	
					
							if (totalT > 1) {
								totalT = 1;
							}
							
							for (int z = 0; z < conList.size(); z ++) {
								
								//incase the consequent has the NOT flag set
								if ((conList.get(z).notFlag == true)){
									
									/*
									 * this is the one case where we want to override the
									 * ANCHOR restriction on NOTing a truthValue. We're
									 * double negating the value so that it ends up being 
									 * the same (ie - truth preserving)
									 */
									if (conList.get(z).isAnchored == true) {
										totalT = 1 - totalT;
										conList.get(z).setTruthval(totalT);
									}
									else {								 
										conList.get(z).setTruthval(totalT);
									}
									
								}
								else {
									conList.get(z).setTruthval(totalT);
								}
								
							}
						
						}					
						
						
						/*
						 * IF THEN
						 * 
						 * min {1, (1 - x) + y)}
						 */
						if (w.getConType() == 4) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							ArrayList<widget> resList = w.getResultantBubbles();
							float totalT = 0;
							float totalCon = 0; 

							for (int z = 0; z < antList.size(); z ++) {		

								if (antList.get(z).notFlag == false) {
									totalT = w.fwith2dp(totalT + antList.get(z).getTruthval());

								}
								else {
									totalT = w.fwith2dp(totalT + (1 - antList.get(z).getTruthval()));
								}

							}	


							for (int z = 0; z < conList.size(); z ++) {		

								if (conList.get(z).notFlag == false) {
									totalCon = w.fwith2dp(totalCon + conList.get(z).getTruthval());
								}
								else {
									totalCon = w.fwith2dp(totalCon + (1 - conList.get(z).getTruthval()));
								}


							}


							/*
							 * hope I'm doing the right thing by 
							 * taking the avg of the consequents and 
							 * antecedents. 
							 */
							totalT = w.fwith2dp(totalT / antList.size());
							totalCon = w.fwith2dp(totalCon / conList.size());
							totalT = 1 - totalT + totalCon;


							if (totalT > 1) {
								totalT = 1;
							}

							for (int z = 0; z < resList.size(); z ++) {							
							
								//incase the consequent has the NOT flag set
								if ((resList.get(z).notFlag == true)) {

									/*
									 * this is the one case where we want to override the
									 * ANCHOR restriction on NOTing a truthValue. We're
									 * double negating the value so that it ends up being 
									 * the same (ie - truth preserving)
									 */
									if (resList.get(z).isAnchored == true) {
										//totalT = w.fwith2dp(1 - totalT);
										resList.get(z).setTruthval(totalT);
									}
									else {								 
										resList.get(z).setTruthval(totalT);
									}

								}
								else {
									resList.get(z).setTruthval(totalT);
								}
							
							}


						}
						
						
						/*
						 * BICON
						 * 
						 * x <-> y
						 * 1 - |x - y|
						 */
						if (w.getConType() == 5) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							ArrayList<widget> resList = w.getResultantBubbles();
							float totalT = 0;
							float totalCon = 0; 

							for (int z = 0; z < antList.size(); z ++) {		

								if (antList.get(z).notFlag == false) {
									totalT = w.fwith2dp(totalT + antList.get(z).getTruthval());

								}
								else {
									totalT = w.fwith2dp(totalT + (1 - antList.get(z).getTruthval()));
								}

							}	


							for (int z = 0; z < conList.size(); z ++) {		

								if (conList.get(z).notFlag == false) {
									totalCon = w.fwith2dp(totalCon + conList.get(z).getTruthval());
								}
								else {
									totalCon = w.fwith2dp(totalCon + (1 - conList.get(z).getTruthval()));
								}


							}


							/*
							 * hope I'm doing the right thing by 
							 * taking the avg of the consequents and 
							 * antecedents. 
							 */
							totalT = w.fwith2dp(totalT / antList.size());
							totalCon = w.fwith2dp(totalCon / conList.size());
							totalT = 1 - Math.abs(totalT - totalCon);

							for (int z = 0; z < resList.size(); z ++) {							
							
								//incase the consequent has the NOT flag set
								if ((resList.get(z).notFlag == true)) {

									/*
									 * this is the one case where we want to override the
									 * ANCHOR restriction on NOTing a truthValue. We're
									 * double negating the value so that it ends up being 
									 * the same (ie - truth preserving)
									 */
									if (resList.get(z).isAnchored == true) {
										//totalT = w.fwith2dp(1 - totalT);
										resList.get(z).setTruthval(totalT);
									}
									else {								 
										resList.get(z).setTruthval(totalT);
									}

								}
								else {
									resList.get(z).setTruthval(totalT);
								}
							
							}


						}
						
						
						
						/* STRONG AND
						 * 
						 * Strong OR: max{0, x + (y - 1)}
						 * 
						 */
						if (w.getConType() == 6) {
							ArrayList<widget> antList = w.getAntecedantBubbles();
							ArrayList<widget> conList = w.getConsequentBubbles();
							float totalT = 0;
									
							/*
							 * antecedent	
							 */
							if (antList.get(0).notFlag == false) {
								totalT = w.fwith2dp(totalT + antList.get(0).truthval);
							}
							else {
								totalT = w.fwith2dp(totalT + (1 - antList.get(0).truthval));
							}
							
							/*
							 * consequent
							 */
							if (antList.get(1).notFlag == false) {
								totalT = w.fwith2dp(totalT + (antList.get(1).truthval - 1));
							}
							else {
								totalT = w.fwith2dp(totalT + ((1 - antList.get(1).truthval) - 1));
							}
							
							
							if (totalT < 0) {
								totalT = 0;
							}
							
							
							for (int z = 0; z < conList.size(); z ++) {
								conList.get(z).setTruthval(totalT);
							}
													
							
						}
						
						
						
						
						
						
						
						//calculate and display truth score!
						if (w.getResultantBubbles().size() > 0) {
							
							for (int z = 0; z < w.getResultantBubbles().size(); z ++) {
								
								if (w.getResultantBubbles().get(z).to_list.size() < 2) {
									tScore = tScore + w.getResultantBubbles().get(z).getTruthval();
									NumTScoreBubbles ++;							
								}
								
							}
							
						}
						else {
							
							for (int z = 0; z < w.getConsequentBubbles().size(); z ++) {

								if (w.getConsequentBubbles().get(z).to_list.size() < 2) {
									tScore = tScore + w.getConsequentBubbles().get(z).getTruthval();
									NumTScoreBubbles ++;							
								}
								
							}
							
						}
						
					}
					
								
				}
								
			
			}
		


		}
		
			
		
		/*
		 * calculate and display stream truth score
		 */
		widget w = new widget();
		truthScore = w.fwith2dp(tScore / NumTScoreBubbles);
		itoolbar.tLabel.setOpaque(true);
		itoolbar.tLabel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		itoolbar.tLabel.setBackground(w.truth2color(truthScore));
		itoolbar.tLabel.setForeground(Color.black);
		itoolbar.tLabel.setText("" + truthScore);	
		
		
		
		
	}
	
	
	
	public static ArrayList<widget> sortCBListL2H (ArrayList<widget> list) {	
		boolean swapped = true;
	
		while (swapped == true) {
			swapped = false;
			
			for (int i = 1; i < list.size(); i ++) {
				
				if ((list.get(i).getTruthval() < list.get(i - 1).getTruthval())) {
					widget w = new widget();
					w = list.get((i - 1));
					list.set((i - 1), list.get(i));
					list.set(i, w);
					swapped = true;	
				}
				
			}
			
			
			/*
			 * check for NOT flags
			 */
			boolean notHere = false; 
				
			for (int i = 0; i < list.size(); i ++) {
				
				if (list.get(i).notFlag == true) {
					notHere = true;
				}
				
			}
				
			swapped = notHere;
			
			while (swapped == true) {

				for (int i = 1; i < list.size(); i ++) {						
					float tempF = list.get(i).truthval; 
					float tempF2 = list.get(i - 1).truthval;
					swapped = false;
				
					if (list.get(i).notFlag == true) {
						tempF = (1 - list.get(i).truthval);
					}

					if (list.get(i - 1).notFlag == true) {
						tempF2 = (1 - list.get(i - 1).truthval);
					}
			
					if (tempF2 > tempF) {
						widget w = new widget();
						w = list.get((i - 1));
						list.set((i - 1), list.get(i));
						list.set(i, w);
						swapped = true;	
					}
					
				}
				
			}
			
			
		}
	
		return list;
		
	}
	
	
	public static ArrayList<widget> sortCBListH2L (ArrayList<widget> list) {	
		boolean swapped = true;
		
		while (swapped == true) {
			swapped = false;
			
			for (int i = 1; i < list.size(); i ++) {
				
				if ((list.get(i).getTruthval() > list.get(i - 1).getTruthval())) {
					widget w = new widget();
					w = list.get((i - 1));				
					list.set((i - 1), list.get(i));
					list.set(i, w);
					swapped = true;
				}
				
			}
			
			
			/*
			 * check for NOT flags
			 */
			boolean notHere = false; 
				
			for (int i = 0; i < list.size(); i ++) {
				
				if (list.get(i).notFlag == true) {
					notHere = true;
				}
				
			}
				
			swapped = notHere;
		
			while (swapped == true) {

				for (int i = 1; i < list.size(); i ++) {						
					float tempF = list.get(i).truthval; 
					float tempF2 = list.get(i - 1).truthval;
					swapped = false;
				
					if (list.get(i).notFlag == true) {
						tempF = (1 - list.get(i).truthval);
					}

					if (list.get(i - 1).notFlag == true) {
						tempF2 = (1 - list.get(i - 1).truthval);
					}
					
					if (tempF2 < tempF) {
						widget w = new widget();
						w = list.get((i - 1));
						list.set((i - 1), list.get(i));
						list.set(i, w);
						swapped = true;	
					}
					
				}
				
			}
			
		}
		
		return list;
		
	}
	
	
}




