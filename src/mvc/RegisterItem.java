package mvc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cpu.CPU;
import cpu.element.Register;

/**
 * this class defines registers in RegisterItemViewPanel
 *
 */
public class RegisterItem extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2214055590555657308L;
	
	private String name;
	private JLabel labelName;
	private Register reg;
	private JLabel labelValue;
	
	private int intValue;
	private boolean modifiedFlag = false;
	
	
	private Color selected = new Color(51,153,255);
	private Color unselected = new Color(224,224,224);
	private Color modified = Color.yellow;
	
	private Color bgColor = unselected;
	
	public String getRegisterName(){
		return this.name;
	}
	
	public RegisterItem(String name){
		//super(name);
		this.name = name;
		this.reg = CPU.getInstance().getRegisters().get(name);
		
		intValue = reg.getIntValue();
		labelName = new JLabel(name+" ("+reg.getIntValue()+")");
		this.add(labelName);
		resetToolTipText();
		
		labelValue = new JLabel(reg.getBinaryString());
		labelValue.setVisible(false);
		
		this.setLayout(new GridLayout(1,1,4,1));
		
		this.setSize(150, 30);
		//this.setBackground(Color.CYAN);
		
		
		this.addMouseListener(
				new MouseListener(){

					@Override
					public void mouseClicked(MouseEvent e) {
						
					}

					@Override
					public void mousePressed(MouseEvent e) {
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						highlight();
					}

					@Override
					public void mouseExited(MouseEvent e) {
						deHighlight();
					}
					
				});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		Dimension size = this.getSize();
		
		g.setColor(bgColor);
		g.fillRoundRect(0, 0, size.width, size.height, 4, 4);
		g.setColor(Color.gray);
		g.drawRoundRect(0, 0, size.width, size.height, 6, 6);
	}



	private void highlight(){
		bgColor = selected;
		repaint();
	}
	
	private void deHighlight(){
		bgColor = unselected;
		if(modifiedFlag)
			bgColor = modified;
		repaint();
	}
	
	public void update(){
		
		labelName.setText(name+" ("+reg.getIntValue()+")");
		resetToolTipText();
		
		super.repaint();
	}



	public void updateState() {

		int value = reg.getIntValue();
		
		if(intValue != value){
			modifiedFlag = true;
			intValue = value;
			bgColor = modified;
		}
		else {
			modifiedFlag = false;
			bgColor = unselected;
		}
			
		labelName.setText(name+" ("+intValue+")");
		resetToolTipText();
		
		super.repaint();
	}
	
	private void resetToolTipText(){
		this.setToolTipText(labelName.getText()+"["+reg.getBinaryString()+"]");
	}

}
