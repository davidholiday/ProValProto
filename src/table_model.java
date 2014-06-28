import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;



public class table_model extends AbstractTableModel {
	
	int rowLength = 10;
	int colLength = 10;
	
	ImageIcon[][] rowData = new ImageIcon[rowLength][colLength];
	

	public void seedRowData() {

		//Toolkit tk = Toolkit.getDefaultToolkit();
		//Image Image = tk.get("images/cbubble.jpg");
		
		
		for (int i = 0; i < rowLength; i ++) {
			
			for (int k = 0; k < colLength; k ++) {
				//rowData[i][k] = i + "," + k;
				rowData[i][k] = new ImageIcon("images/cbubble.jpg"); 
			}
			
		}
		
	}
  
    public Class<Icon> getColumnClass(int col) {  
       return Icon.class; 
    }  
    
    public int getRowCount() { 
    	return rowLength;
    }
    
    public int getColumnCount() { 
    	return colLength; 
    }
    
    public ImageIcon getValueAt(int row, int col) {
        return rowData[row][col];
    }
    
    public boolean isCellEditable(int row, int col) { 
    	return false; 
    }
    
    public void setValueAt(ImageIcon value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
	
}