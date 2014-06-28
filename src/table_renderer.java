import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class table_renderer extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
			boolean hasFocus,int row,int col) {
	
	    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);	
		int selCol = table.getSelectedColumn();
		int selRow = table.getSelectedRow();

		if ( row == selRow){
			c.setForeground(null);			
		}
	    
		if (ProVal_GUI.table.getModel().getValueAt(selRow, selCol) != null) {	
			table.setRowHeight(selRow, getPreferredSize().height + selRow * 10);
		}
		else {
			
			InfoToolBar.cLabel.setText("\n\n\n\n\nCELL: (" +row + "," + col + ") IS EMPTY!");
		}	
		
		
		if (ProVal_GUI.frame.getCursor().getName().contentEquals("cbubble")) {

			//if ((row == selRow) && (col == selCol) == true) {
				//Toolkit tk = Toolkit.getDefaultToolkit();
				//Image image = tk.getImage("images/cbubble.jpg");
				ProVal_GUI.table.getModel().setValueAt(new ImageIcon("images/and.jpg"), selRow, selCol);
				
				
			//}
			
		}
		
		
		
		
	    return c;
	}
	
}
