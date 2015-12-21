package mvc;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class MemoryViewPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3839198252853259361L;
	
	/**
	 * Singleton
	 */
	private static MemoryViewPanel instance = new MemoryViewPanel();
	
	public static MemoryViewPanel getInstance(){
		return instance;
	}

	private JScrollPane js;
	private JTable table;
	
	private MemoryTableModel model;
	
	private MemoryViewPanel(){
		TitledBorder nameTitle =new TitledBorder("Memory"); 	
		this.setBorder(nameTitle);
		
		this.setLayout(new BorderLayout());
		
		model = new MemoryTableModel();
		table = new JTable(model);

		DefaultTableCellRenderer d = new DefaultTableCellRenderer();
		d.setHorizontalAlignment(JLabel.CENTER);
		TableColumn col = table.getColumnModel().getColumn(0);
		table.getTableHeader().setResizingColumn(col);
		col.setWidth(35);
		col.setCellRenderer(d);
		
		
		col = table.getColumnModel().getColumn(1);
		table.getTableHeader().setResizingColumn(col);
		col.setCellRenderer(d);
		col.setWidth(150);
		

		table.setEnabled(false);
		
		
		js = new JScrollPane(table);
				
		this.add(js,BorderLayout.CENTER);
	}
	
	/**
	 * help to update the memory table immediately
	 */
	public void update()
	{
		model.fireTableDataChanged();
	}

}
