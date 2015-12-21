package mvc;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observer;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import common.Util;

import io.IODevice;
import io.IODeviceController;
import cpu.element.Word;

/**
 * This class builds a panel for printer
 *
 */

public class PrinterPanel extends JPanel implements Observer{
	
	private static final long serialVersionUID = -6614008801609108682L;
	JTextArea text = new JTextArea();
	private IODevice printer;

	public PrinterPanel(){
	TitledBorder nameTitle = new TitledBorder("Printer");
	printer = IODeviceController.getInstance().getPrinter();
	this.setBorder(nameTitle);
	
	this.setLayout(new GridLayout());
	
	text = new JTextArea();
	text.setBounds(650, 205, 211, 155);
	text.setBackground(Color.black);
	text.setForeground(Color.white);
	text.setEditable(false);
	text.setLineWrap(true);
	
	printer.addObserver(this);
	
	JScrollPane js = new JScrollPane(text);
	js.setBackground(Color.black);
	this.add(js);
	
	}
	
	private void append(int value) {
		text.append(String.valueOf((char)(value)));
		text.setSelectionStart(text.getText().length());
	}
	
	
	
	@Override
	public void update(Observable o, Object msg) {
		//System.out.println("UPDATE:"+msg);
		
		Word word = (Word)msg;
		
		append(Util.getIntValueFromBinaryArray(word.getContent()));
	}
	
}
