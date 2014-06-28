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
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

	public class WidgetToolBar extends JToolBar {
		ClassLoader cl = ResourceAnchor.class.getClassLoader();
		static ArrayList<JButton> butt_list = new ArrayList();
		
		  public WidgetToolBar() { 
		    String[] imageFiles =
		      {"and.jpg", "or.jpg", "not.jpg", "ifthen.jpg", "bicon.jpg", 
		    		"strongand.jpg", "strongor.jpg", "consequent.jpg", "coupler.jpg", "result.jpg",
		    		"cbubble.jpg", "select.jpg", "OscarGrouch.jpg", "eye.jpg"};
		    String[] toolbarLabels =
		      { "AND", "OR", "NOT", "IF/THEN", "BI-CON", "STRONG AND", "STRONG OR", 
		    		"CONSEQUENT", "ANTECEDENT", "RESULT", "CONTEXT BUBBLE", "SELECT", "TRASH", ""};
		    Insets margins = new Insets(0, 0, 0, 0);
		    
		    for(int i=0; i<toolbarLabels.length; i++) {
		      //ToolBarButton button =
		      //  new ToolBarButton("images/" + imageFiles[i]);
		    	ToolBarButton button =
		  		       new ToolBarButton(new ImageIcon (cl.getResource("images/" + imageFiles[i])));
		    	
		    	
		      //button.setToolTipText(toolbarLabels[i]);
		      button.setText(toolbarLabels[i]);
		      button.setMargin(margins);
		      setAL(button);
		      add(button);
		      butt_list.add(button);
		    }
		    
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
		  
		  //button action handlers 
		  public void setAL (ToolBarButton button) {

			  
			  if (button.getText().contentEquals("")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								
								/*
								 * set engage boolean 
								 */
								if (ProVal_GUI.engage == false) {
									ProVal_GUI.engage = true;
								}
								else {
									ProVal_GUI.engage = false;
								}
								
								/*
								 * disable/enable buttons as required
								 */
								if (ProVal_GUI.engage == true) {
									ProVal_GUI.file_new_MI.setEnabled(false);
									ProVal_GUI.file_load_MI.setEnabled(false);
									
									for (int i = 0; i < 11; i ++) {
										ToolBarButton tbb = (ToolBarButton)ProVal_GUI.wtoolbar.getComponent(i);
										tbb.setEnabled(false);										
									}
									
									ProVal_GUI.wtoolbar.getComponent(12).setEnabled(false);
									
									//get rid of gridlines
								    for (int i = 0; i < ProVal_GUI.rows; i++) {
								    	
								    	for (int k = 0; k < ProVal_GUI.cols; k ++) {
								    		ProVal_GUI.gbContentArray[k][i].setBorder(null);								    		
								    		ProVal_GUI.gbContentArray[k][i].setBackground(Color.white);
								    	}
								        
								    }
								    
									/*
									 * calculate the truth score! 
									 */
									ProVal_GUI.engage();
									ProVal_GUI.engageDOWN();
								
								}
								else {
									ProVal_GUI.file_new_MI.setEnabled(true);
									ProVal_GUI.file_load_MI.setEnabled(true);
									
									for (int i = 0; i < 11; i ++) {
										ToolBarButton tbb = (ToolBarButton)ProVal_GUI.wtoolbar.getComponent(i);
										tbb.setEnabled(true);
									}
																	
									ProVal_GUI.wtoolbar.getComponent(12).setEnabled(true);
									
									//replace grid lines
								    for (int i = 0; i < ProVal_GUI.rows; i++) {
								    	
								    	for (int k = 0; k < ProVal_GUI.cols; k ++) {
								    		ProVal_GUI.gbContentArray[k][i].setBorder(
								    				BorderFactory.createLineBorder(Color.LIGHT_GRAY));
								    		//ProVal_GUI.gbContentArray[k][i].setBackground(new Color(238,238,238));
								    	}
								        
								    }
									
								}
								
								

								ProVal_GUI.wtoolbar.butt_list.get(11).doClick();
								
							}
						
					});	
				  
			  }
			  
			  
			  if (button.getText().contentEquals("TRASH")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								//Image image = tk.getImage("images/OscarGrouch.jpg");
								Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/OscarGrouch.jpg"));
								c = tk.createCustomCursor(image, new Point(0,0),"trash");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);	
				  
			  }
			  
			  
			  
			  if (button.getText().contentEquals("STRONG OR")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								// image = tk.getImage("images/strongor.jpg");
								Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/strongor.jpg"));
								c = tk.createCustomCursor(image, new Point(0,0),"strongor");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);	
				  
			  }
			  
			  
			  if (button.getText().contentEquals("STRONG AND")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/strongand.jpg"));
								//Image image = tk.getImage("images/strongand.jpg");
								c = tk.createCustomCursor(image, new Point(0,0),"strongand");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);	
				  
			  }
			  
			  
			  if (button.getText().contentEquals("BI-CON")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								//Image image = tk.getImage("images/bicon.jpg");
								Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/bicon.jpg"));
								c = tk.createCustomCursor(image, new Point(0,0),"bicon");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);	
				  
			  }
			  
			  
			  if (button.getText().contentEquals("IF/THEN")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								//Image image = tk.getImage("images/ifthen.jpg");
								Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/ifthen.jpg"));
								c = tk.createCustomCursor(image, new Point(0,0),"ifthen");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);	
				  
			  }
			  
			  
			  if (button.getText().contentEquals("NOT")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								//Image image = tk.getImage("images/not.jpg");
								Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/not.jpg"));
								c = tk.createCustomCursor(image, new Point(0,0),"not");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);	
				  
			  }
			  
			  
			  if (button.getText().contentEquals("OR")) {
				  button.addActionListener( 
						new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Cursor c;
								Toolkit tk = Toolkit.getDefaultToolkit();
								//Image image = tk.getImage("images/or.jpg");
								Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/or.jpg"));
								c = tk.createCustomCursor(image, new Point(0,0),"or");
								ProVal_GUI.frame.setCursor(c);
							}
						}
						
					);	
				  
			  }
			  
			  
			  if (button.getText().contentEquals("ANTECEDENT")) {
					  button.addActionListener( 
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									Cursor c;
									Toolkit tk = Toolkit.getDefaultToolkit();
									//Image image = tk.getImage("images/coupler.jpg");
									Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/coupler.jpg"));
									c = tk.createCustomCursor(image, new Point(0,0),"antecedent");
									ProVal_GUI.frame.setCursor(c);
								}
							}
							
						);	
					  
				  }
			  
			  
				 if (button.getText().contentEquals("CONSEQUENT")) {
					  button.addActionListener( 
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									Cursor c;
									Toolkit tk = Toolkit.getDefaultToolkit();
									//Image image = tk.getImage("images/consequentpointer.jpg");
									Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/consequent.jpg"));
									c = tk.createCustomCursor(image, new Point(0,0),"consequent");
									ProVal_GUI.frame.setCursor(c);
								}
							}
							
						);	
					  
				  }
			  
				 
				 if (button.getText().contentEquals("RESULT")) {
					  button.addActionListener( 
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									Cursor c;
									Toolkit tk = Toolkit.getDefaultToolkit();
									//Image image = tk.getImage("images/result.jpg");
									Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/result.jpg"));
									c = tk.createCustomCursor(image, new Point(0,0),"result");
									ProVal_GUI.frame.setCursor(c);
								}
							}
							
						);	
					  
				  }
				 
				 
				 if (button.getText().contentEquals("AND")) {
					  button.addActionListener( 
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									Cursor c;
									Toolkit tk = Toolkit.getDefaultToolkit();
									//Image image = tk.getImage("images/andpointer.jpg");
									Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/and.jpg"));
									c = tk.createCustomCursor(image, new Point(0,0),"and");
									ProVal_GUI.frame.setCursor(c);
								}
							}
							
						);	
					  
				  }
				  
				  
				 if (button.getText().contentEquals("CONTEXT BUBBLE")) {				  
					 button.addActionListener(new ActionListener() {
						 
						 public void actionPerformed(ActionEvent e) {
							 Cursor c;
							 Toolkit tk = Toolkit.getDefaultToolkit();
							// Image image = tk.getImage("images/cbubblepointer.jpg");
							 Image image = tk.getDefaultToolkit().getImage(cl.getResource("images/cbubble.jpg"));
							 c = tk.createCustomCursor(image, new Point(0,0),"cbubble");
							 ProVal_GUI.frame.setCursor(c);
						 }
						 
					 });	
	
				 }
			  
			 if (button.getText().contentEquals("SELECT")) {				  
				 button.addActionListener(new ActionListener() {
					 
					 public void actionPerformed(ActionEvent e) {
						 Cursor c;
						 //Toolkit tk = Toolkit.getDefaultToolkit();
						 //Image image = tk.getImage("images/cbubblepointer.jpg");
						 //c = tk.createCustomCursor(image, new Point(0,0),"select");
						 ProVal_GUI.frame.setCursor(Cursor.DEFAULT_CURSOR);
					 }
					 
				 });	

			 }
			 
			  
			  
		  }
		
		  
	}