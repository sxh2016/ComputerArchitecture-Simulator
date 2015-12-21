package mvc;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import memory.MainMemory;
import common.Configuration;
import common.NumOnlyDocument;
import common.Util;
import exception.IllegalMemoryAddressException;

public class LoadMemoryPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2238940591490400715L;
	
	private MainMemory memory = MainMemory.getInstance();
	
	private JLabel addressLabel;
	private JTextField addressText;
	private JLabel valueLabel;
	private JTextField valueText;
	
	private JButton loadButton;
	
	public LoadMemoryPanel() {
		TitledBorder nameTitle =new TitledBorder("Load Memory"); 	
		this.setBorder(nameTitle);
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0, 0));
		
		addressLabel = new JLabel("Address(Binary)");
		addressLabel.setPreferredSize(new Dimension(100,15));
		
		valueLabel = new JLabel("Value(binary)");
		valueLabel.setPreferredSize(new Dimension(100,15));
		
		addressText = new JTextField(11);
		addressText.setDocument(new NumOnlyDocument());
		
		valueText = new JTextField(11);
		valueText.setDocument(new NumOnlyDocument());
		
		loadButton = new JButton("Load");
		loadButton.setPreferredSize(new Dimension(80,25));
		
		this.add(addressLabel);
		this.add(addressText);
		
		this.add(valueLabel);
		this.add(valueText);
		
		this.add(loadButton);
		
		/**
		 * add click event for loadButton
		 */
		loadButton.addActionListener(
				new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						
						
						try {
							int address = Util.getIntValueFromBinaryString(addressText.getText());
							int value = Util.getIntValueFromBinaryString(valueText.getText());
							
							memory.write(address, Util.getBinaryArrayFromIntValue(value, Configuration.WORD_LENGTH));
							MemoryViewPanel.getInstance().update();
						} catch (NumberFormatException e1) {
							infoDialog("Address or value is illegal!");
						} catch (IllegalMemoryAddressException e1) {
							infoDialog("Address should be a binary value between 0 and 2047!");
						}
						
					}
				});
		
	}
	
	/*** 
     * information dialog
     *
     * @param mesg
     */
    public static void infoDialog(String mesg)
    {
        JOptionPane.showMessageDialog(null, mesg, "Info",
            JOptionPane.INFORMATION_MESSAGE);
    }

}
