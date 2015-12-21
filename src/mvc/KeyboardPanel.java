package mvc;
import io.IODeviceController;
import io.KeyboardDevice;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import common.Message;

public class KeyboardPanel extends JPanel implements Observer{
	
	private static final long serialVersionUID = 1L;
	
	private JCheckBox linkToKeyboard = new JCheckBox("Link to keyboard");
	//private JLabel linkToKeyboard;
	private KeyboardDevice keyboard = (KeyboardDevice)IODeviceController.getInstance().getKeyboard();
	
	private boolean flashing = false;
	private int degree = 0;
	private int increase = 1;
	private int MAX = 20;
	
	private Thread runningThread;
	
	public KeyboardPanel(){
		
		keyboard.addObserver(this);
		
		//TitledBorder nameTitle =new TitledBorder("");
		TitledBorder nameTitle =new TitledBorder("Keyboard (DEVID:0)");
		this.setBorder(nameTitle);
		
        this.setLayout(null);
        this.setOpaque(false);
        
        //linkToKeyboard=new JLabel("Input from keyboard");
        this.add(linkToKeyboard);
        linkToKeyboard.setBounds(10, 10, 250, 35);
        linkToKeyboard.setOpaque(false);
        linkToKeyboard.addKeyListener(
        		new KeyListener(){

					@Override
					public void keyTyped(KeyEvent e) {
						//System.out.println("Keyboard typed: "+e.getKeyCode());
					}

					@Override
					public void keyPressed(KeyEvent e) {
						//System.out.println("Keyboard pressed: "+e.getKeyCode());
						
						
						int value = (int)e.getKeyChar();
						if(value>0&&value<128)
							keyboard.activeEvent(value);
					}

					@Override
					public void keyReleased(KeyEvent e) {
						//System.out.println("Keyboard released: "+e.getKeyCode());
					}
        			
        		});
        linkToKeyboard.addFocusListener(
        		new FocusListener(){

					@Override
					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void focusLost(FocusEvent e) {
						linkToKeyboard.setSelected(false);
					}
        			
        		});
        
        
	}
	
	public void startFlash(){
		flashing = true;
		
		if(runningThread==null||!runningThread.isAlive()){
			runningThread = new Thread(){
				@Override
				public void run(){
					while(flashing){
						repaint();
						try {
							Thread.sleep(50);
							degree+=increase;
							if(degree>=MAX)
								increase = -1;
							if(degree<=0)
								increase = 1;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					runningThread = null;
				}
			};
			runningThread.start();
		}
	}
	
	public void stopFlash(){
		flashing = false;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		
		//Dimension size = this.getSize();
		if(flashing){
			g.setColor(new Color(20+8*degree,150+5*degree,20+5*degree));
			g.fillRoundRect(5, 17, 200, 25, 4, 4);
			g.setColor(Color.gray);
			g.drawRoundRect(5, 17, 200, 25, 6, 6);
		}
		
		super.paintComponent(g);
		this.paintComponents(g);
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
       if(arg==null){
			
		}else{
			int msg = (Integer)arg;
			switch(msg){
			case(Message.MSG_KEYBOARD_WAITING):
				this.startFlash();
			
			break;
			case(Message.MSG_KEYBOARD_PRESSED):
				this.stopFlash();
			break;
			}
		}
		
	}

}
