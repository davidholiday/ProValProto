import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class slidelistener implements ChangeListener {
	
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        
        if (!source.getValueIsAdjusting()) {   	
        	ProVal_GUI.widgetArray[ProVal_GUI.wxPos][ProVal_GUI.wyPos].isAnchored = true;
            float tVal = (float)source.getValue() / 100; 
            ProVal_GUI.widgetArray[ProVal_GUI.wxPos][ProVal_GUI.wyPos].setTruthval(tVal);
            
            if (ProVal_GUI.engage == true) {
            	ProVal_GUI.engageUP(ProVal_GUI.widgetArray[ProVal_GUI.wxPos][ProVal_GUI.wyPos]);
            	ProVal_GUI.engageDOWN();
            }
                
            ProVal_GUI.widgetArray[ProVal_GUI.wxPos][ProVal_GUI.wyPos].isAnchored = false;
        }   
        
    }
    
}