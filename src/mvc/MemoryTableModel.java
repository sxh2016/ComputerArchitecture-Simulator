package mvc;

import javax.swing.table.AbstractTableModel;

import common.Configuration;
import common.Util;
import memory.MainMemory;
import cpu.element.Word;

/**
 * this class defines the table to show contents in MainMemory
 *
 */
public class MemoryTableModel extends AbstractTableModel{

	/**
	 * generated UID
	 */
	private static final long serialVersionUID = -5502091771384085783L;

	Word[] data = MainMemory.getInstance().getAllData();
	String[] title ={ "Addr", "Binary Value"};

	@Override
	public String getColumnName(int column) {
		return title[column];
	}

	@Override
	public int getRowCount() {
		return Configuration.MEMORY_SIZE;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Word word = data[rowIndex];
		switch(columnIndex){
		case(0):
			return rowIndex;
		case(1):
			return Util.getBinaryStringFromBinaryArray(word.getContent());
		default:
			return "";
		}
	}
	
}
