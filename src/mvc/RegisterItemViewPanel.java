package mvc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import common.NumOnlyDocument;
import common.Util;
import cpu.CPU;
import cpu.element.Register;

public class RegisterItemViewPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2621743937134005402L;
	
	private CPU cpu = CPU.getInstance();
	
	private JPanel editor;
	private JLabel labelText;
	private JTextField binaryText;
	private JButton loadButton;
	private JPanel items;
	private List<RegisterItem> panelItems;
	
	public RegisterItemViewPanel(){
		TitledBorder nameTitle =new TitledBorder("Registers"); 	
		this.setBorder(nameTitle);
		this.setLayout(new BorderLayout());
		List<String> regs = cpu.getRegisterNames();
		
		editor = new JPanel();
		editor.setLayout(new FlowLayout(FlowLayout.LEFT,0, 0));
		//editor.setBackground(Color.red);
		this.add(editor,BorderLayout.NORTH);
		labelText = new JLabel("<REG>");
		labelText.setPreferredSize(new Dimension(60,40));
		binaryText = new JTextField(11);
		binaryText.setDocument(new NumOnlyDocument());
		loadButton = new JButton("Load");
		loadButton.setPreferredSize(new Dimension(55,30));
		editor.add(labelText);
		editor.add(binaryText);
		editor.add(loadButton);
		
		items = new JPanel();
		items.setLayout(new GridLayout(8,4,3,1));
		this.add(items, BorderLayout.CENTER);
		
		
		panelItems = new ArrayList<RegisterItem>();
		for(String name:regs){
			RegisterItem item = new RegisterItem(name);
			item.addMouseListener(
					new MouseListener(){

						@Override
						public void mouseClicked(MouseEvent e) {
							RegisterItem item = (RegisterItem)e.getSource();
							modifyingRegister(item.getRegisterName());
						}

						@Override
						public void mousePressed(MouseEvent e) {
							
						}

						@Override
						public void mouseReleased(MouseEvent e) {
							
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							
						}

						@Override
						public void mouseExited(MouseEvent e) {
							
						}
						
					});
			items.add(item);
			panelItems.add(item);
		}
		
		loadButton.addActionListener(
				new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						Register reg = CPU.getInstance().getRegisters().get(labelText.getText());
						if(reg==null)
							return;
						try {
							int value = Util.getIntValueFromBinaryString(binaryText.getText());
							reg.write(value);
							update();
							//Log.getInstance().console(ConsoleMessage.TYPE_LOG, "set "+labelText.getText()+" to "+value);
						} catch (NumberFormatException e1) {
							e1.printStackTrace();
						}
					}
				});
		//this.setLayout(new BorderLayout());
	}

	private void modifyingRegister(String name){
		labelText.setText(name);
		Register reg = CPU.getInstance().getRegisters().get(name);
		binaryText.setText(reg.getBinaryString());
	}

	public void update() {
		for(RegisterItem item : panelItems)
			item.update();
	}


	public void updateState() {
		for(RegisterItem item : panelItems)
			item.updateState();
	}

}
