package mvc;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import common.Message;
import common.Util;
import cpu.CPU;
import cpu.element.Word;
import exception.IllegalMemoryAddressException;
import exception.RegisterNotFoundException;

public class MainFrame extends JFrame implements Observer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5470389048053832497L;
	
	/**
	 * display registers in cpu
	 */
	private RegisterItemViewPanel registerItemPanel;
	
	/**
	 * display memory in cpu
	 */
	private MemoryViewPanel memoryViewPanel;
	
	/**
	 * display function to change value in memory
	 */
	private LoadMemoryPanel loadMemoryPanel;

	/**
	 * display different running mode to run cpu
	 */
	private JPanel actionPanel;
	private FlashButton runSingleStepButton;
	private JButton presetButton;
	private JRadioButton radioModeRun;
    private JRadioButton radioModeDebugInstr;
    private JRadioButton radioModeDebugCycle;
    
    private CachePanel cachePanel;
    private PrinterPanel printerPanel;
    private KeyboardPanel keyboardPanel;
    
    private CPU cpu = CPU.getInstance();
	
	public MainFrame()
	{
		super("Computer Simulator");
		//this.setSize(730, 305);
		this.setSize(730, 650);
		this.setLocation(200, 120);
		this.setLayout(null);
		
		actionPanel = new JPanel();
		actionPanel.setBorder(new TitledBorder("Action"));
		runSingleStepButton = new FlashButton("RunSingleStep");
		runSingleStepButton.setPreferredSize(new Dimension(120,30));
		presetButton = new JButton("PreSetProgram1");
		presetButton.setPreferredSize(new Dimension(140,30));
		Box box = Box.createHorizontalBox();
		radioModeRun = new JRadioButton("Run");
		radioModeRun.setSelected(true); 
		radioModeDebugInstr = new JRadioButton("Debug(SingleInstruction)");
		radioModeDebugCycle = new JRadioButton("Debug(SingleCycle)");
		
		ButtonGroup group = new ButtonGroup();
		group.add(radioModeRun);
		group.add(radioModeDebugInstr);
		group.add(radioModeDebugCycle);
		box.add(radioModeRun);
		box.add(radioModeDebugInstr);
		box.add(radioModeDebugCycle);
		
		actionPanel.add(box);
		actionPanel.add(presetButton);
		actionPanel.add(runSingleStepButton);
		
		
		registerItemPanel = new RegisterItemViewPanel();
		memoryViewPanel = MemoryViewPanel.getInstance();
		loadMemoryPanel = new LoadMemoryPanel();
		
		actionPanel.setBounds(5, 220, 725, 65);
		registerItemPanel.setBounds(445, 5, 275, 210);
		memoryViewPanel.setBounds(5, 5, 270, 210);
		loadMemoryPanel.setBounds(275, 5, 170, 140);
		
		cachePanel=new CachePanel();
		cachePanel.setBounds(5, 285, 260, 275);
		
		
		printerPanel = new PrinterPanel();
    	printerPanel.setBounds(270, 285, 260, 220);
    	
		
    	keyboardPanel = new KeyboardPanel();
		keyboardPanel.setBounds(270, 510, 260, 50);
		keyboardPanel.setPreferredSize(new Dimension(211,200));
    	

		
		this.add(actionPanel);
		this.add(registerItemPanel);
		this.add(memoryViewPanel);
		this.add(loadMemoryPanel);
		this.add(cachePanel);
		this.add(printerPanel);
		this.add(keyboardPanel);
		
		this.setVisible(true);
		this.setResizable(false);
		this.addListener();
		
		//observe CPU
		cpu.addObserver(this);
	}
	
	/**
	 * add listeners to buttons and radios
	 */
	private void addListener(){
		runSingleStepButton.addMouseListener(
				new MouseListener(){

					@Override
					public void mouseClicked(MouseEvent e) {
						
						if(!CPU.POWER_STATE)
						{
							cpu.execute();
							CPU.POWER_STATE = true;
						}
						cpu.continueRun(CPU.SIGNAL_STEP);
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
		presetButton.addMouseListener(
				new MouseListener(){

					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							/*
							cpu.getRegisterByRealName("PC").write(30);
							
							cpu.getRegisterByRealName("X1").write(50);
							MainMemory.getInstance().write(51, Util.getBinaryArrayFromIntValue(51, 16));
							MainMemory.getInstance().write(54, Util.getBinaryArrayFromIntValue(55, 16));
							MainMemory.getInstance().write(55, Util.getBinaryArrayFromIntValue(15, 16));
							
							MainMemory.getInstance().write(30, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("10101000001"), 16));
							MainMemory.getInstance().write(31, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("100101000010"), 16));
							MainMemory.getInstance().write(32, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("111101000001"), 16));
							MainMemory.getInstance().write(33, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011001000001"), 16));
							MainMemory.getInstance().write(34, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010101001000011"), 16));
							MainMemory.getInstance().write(35, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1000101000001"), 16));
							MainMemory.getInstance().write(36, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010101000001"), 16));
							MainMemory.getInstance().write(37, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1100101000001"), 16));
							MainMemory.getInstance().write(38, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1110101000001"), 16));
							MainMemory.getInstance().write(39, Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("10101100100"), 16));
							*/
							
							cpu.getRegisterByRealName("PC").write(55);
							

							
							cpu.getMemory().write(55, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(56, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000010111"), 16)));
							cpu.getMemory().write(57, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100000011000"), 16)));
							cpu.getMemory().write(58, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(59, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(60, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100000011001"), 16)));
							
							cpu.getMemory().write(61, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(62, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(63, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100000011010"), 16)));
							cpu.getMemory().write(64, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(65, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							
							cpu.getMemory().write(66, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100000011011"), 16)));
							cpu.getMemory().write(67, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(68, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(69, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100000011100"), 16)));
							cpu.getMemory().write(70, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							
							cpu.getMemory().write(71, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(72, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100000011101"), 16)));
							cpu.getMemory().write(73, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(74, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000000011100"), 16)));
							cpu.getMemory().write(75, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000000011100"), 16)));
							
							cpu.getMemory().write(76, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							cpu.getMemory().write(77, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011010001"), 16)));
							cpu.getMemory().write(78, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010010111010001"), 16)));
							cpu.getMemory().write(79, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011100001000"), 16)));
							
							cpu.getMemory().write(80, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001101100010101"), 16)));
							//cpu.getMemory().write(80, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001101100010001"), 16)));
							
							cpu.getMemory().write(81, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000101111010010"), 16)));
							cpu.getMemory().write(82, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011000001000"), 16)));
							cpu.getMemory().write(83, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111010000000000"), 16)));
							cpu.getMemory().write(84, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(85, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010100001000"), 16)));
							
							cpu.getMemory().write(86, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100100001010"), 16)));
							cpu.getMemory().write(87, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0101100001000000"), 16)));
							cpu.getMemory().write(88, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011010"), 16)));
							cpu.getMemory().write(89, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0011001111001101"), 16)));
							cpu.getMemory().write(90, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001110000011000"), 16)));
							
							cpu.getMemory().write(91, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001110000011000"), 16)));
							
							//add line
							cpu.getMemory().write(92, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							
							cpu.getMemory().write(93, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011011011"), 16)));
							cpu.getMemory().write(94, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(95, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000001010"), 16)));
							cpu.getMemory().write(96, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0101001000000000"), 16)));
							
							cpu.getMemory().write(97, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001001011011011"), 16)));
							cpu.getMemory().write(98, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011001"), 16)));
							cpu.getMemory().write(99, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0011010011011100"), 16)));
							cpu.getMemory().write(100, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000101001000000"), 16)));
							
							cpu.getMemory().write(101, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							cpu.getMemory().write(102, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010100111011011"), 16)));
							cpu.getMemory().write(103, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010011011011"), 16)));
							cpu.getMemory().write(104, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(105, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011011011"), 16)));
							cpu.getMemory().write(106, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							cpu.getMemory().write(107, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010010111011011"), 16)));
							cpu.getMemory().write(108, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011001"), 16)));
							cpu.getMemory().write(109, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0100001111011011"), 16)));
							cpu.getMemory().write(110, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							cpu.getMemory().write(111, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010011010001"), 16)));
							cpu.getMemory().write(112, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011011011"), 16)));
							cpu.getMemory().write(113, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000011010010"), 16)));
							cpu.getMemory().write(114, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011011100"), 16)));
							cpu.getMemory().write(115, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011111010010"), 16)));
							cpu.getMemory().write(116, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001111100000001"), 16)));
							cpu.getMemory().write(117, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000101011011101"), 16)));
							
							cpu.getMemory().write(118, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							
							
							cpu.getMemory().write(119, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010011111011"), 16)));
							cpu.getMemory().write(120, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001010011011101"), 16)));
							cpu.getMemory().write(121, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011111100"), 16)));
							cpu.getMemory().write(122, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(123, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(124, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011000011000"), 16)));
							cpu.getMemory().write(125, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000010011011"), 16)));
							cpu.getMemory().write(126, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100010011011"), 16)));
							cpu.getMemory().write(127, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(128, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(129, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000010011100"), 16)));
							cpu.getMemory().write(130, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100010011100"), 16)));
							cpu.getMemory().write(131, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011010"), 16)));
							cpu.getMemory().write(132, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0100001111011111"), 16)));
							cpu.getMemory().write(133, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011000011000"), 16)));
							cpu.getMemory().write(134, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010100110011011"), 16)));
							cpu.getMemory().write(135, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010110011011"), 16)));
							cpu.getMemory().write(136, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010010111011"), 16)));
							cpu.getMemory().write(137, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100010010011"), 16)));
							cpu.getMemory().write(138, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011110010010"), 16)));
							cpu.getMemory().write(139, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001111100000010"), 16)));
							cpu.getMemory().write(140, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011000001000"), 16)));
							cpu.getMemory().write(141, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001101000000001"), 16)));
							cpu.getMemory().write(142, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011000011000"), 16)));
							cpu.getMemory().write(143, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001001010011011"), 16)));
							cpu.getMemory().write(144, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000101010011011"), 16)));
							cpu.getMemory().write(145, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010010010011"), 16)));
							cpu.getMemory().write(146, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001010010111011"), 16)));
							cpu.getMemory().write(147, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011100"), 16)));
							cpu.getMemory().write(148, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0011000111000001"), 16)));
							cpu.getMemory().write(149, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010110011011"), 16)));
							cpu.getMemory().write(150, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010010111011"), 16)));
							cpu.getMemory().write(151, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100010010011"), 16)));
							cpu.getMemory().write(152, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011011"), 16)));
							cpu.getMemory().write(153, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0100001111010101"), 16)));
							cpu.getMemory().write(154, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011000011000"), 16)));
							cpu.getMemory().write(155, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001010110010010"), 16)));
							cpu.getMemory().write(156, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100110011011"), 16)));
							cpu.getMemory().write(157, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011010111011"), 16)));
							cpu.getMemory().write(158, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(159, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(160, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(161, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000010000"), 16)));
							cpu.getMemory().write(162, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(163, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000010111"), 16)));
							cpu.getMemory().write(164, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(165, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001110000000100"), 16)));
							cpu.getMemory().write(166, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(167, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000010001"), 16)));
							cpu.getMemory().write(168, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(169, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001110000001101"), 16)));
							cpu.getMemory().write(170, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(171, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000001110"), 16)));
							cpu.getMemory().write(172, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(173, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011000011000"), 16)));
							cpu.getMemory().write(174, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011000011000"), 16)));
							cpu.getMemory().write(175, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(176, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(177, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001110000011111"), 16)));
							cpu.getMemory().write(178, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001110000011011"), 16)));
							cpu.getMemory().write(179, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111100000000001"), 16)));
							cpu.getMemory().write(180, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(181, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(182, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(183, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000011111"), 16)));
							cpu.getMemory().write(184, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000111"), 16)));
							cpu.getMemory().write(185, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011000011000"), 16)));
							cpu.getMemory().write(186, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100010011011"), 16)));
							cpu.getMemory().write(187, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011010011011"), 16)));
							
							cpu.getMemory().write(188, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							
							cpu.getMemory().write(189, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010011010001"), 16)));
							cpu.getMemory().write(190, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							cpu.getMemory().write(191, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000011010010"), 16)));
							cpu.getMemory().write(192, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000011010010"), 16)));
							cpu.getMemory().write(193, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011010100"), 16)));
							cpu.getMemory().write(194, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011010101"), 16)));
							cpu.getMemory().write(195, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010100010010"), 16)));
							
							cpu.getMemory().write(196, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100100001010"), 16)));
							
							
							cpu.getMemory().write(197, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0101011001000000"), 16)));
							
							
							cpu.getMemory().write(198, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							
							
							cpu.getMemory().write(199, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000101111110100"), 16)));
							cpu.getMemory().write(200, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000010000001000"), 16)));
							cpu.getMemory().write(201, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001100000000001"), 16)));
							cpu.getMemory().write(202, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001000011010100"), 16)));
							cpu.getMemory().write(203, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000100011010100"), 16)));
							cpu.getMemory().write(204, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011101"), 16)));
							cpu.getMemory().write(205, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0010111011001110"), 16)));
							
							cpu.getMemory().write(206, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							
							cpu.getMemory().write(207, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001010011010101"), 16)));
							cpu.getMemory().write(208, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							cpu.getMemory().write(209, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011011010100"), 16)));
							cpu.getMemory().write(210, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001111000000001"), 16)));
							cpu.getMemory().write(211, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011000"), 16)));
							cpu.getMemory().write(212, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000101011010100"), 16)));
							cpu.getMemory().write(213, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0000011011110100"), 16)));
							cpu.getMemory().write(214, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001101000011111"), 16)));
							cpu.getMemory().write(215, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0001101000010001"), 16)));
							cpu.getMemory().write(216, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1111101000000001"), 16)));
							cpu.getMemory().write(217, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("1010011100011101"), 16)));
							cpu.getMemory().write(218, new Word(Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryString("0100000011011001"), 16)));

							
							registerItemPanel.update();
							memoryViewPanel.update();
							cachePanel.update();
						} catch (NumberFormatException
								| IllegalMemoryAddressException | RegisterNotFoundException e1) {
							e1.printStackTrace();
						}
						
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
		radioModeRun.addActionListener(
				new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(radioModeRun.isSelected())
							cpu.setMode(CPU.MODE_RUN);
					}
				});
		radioModeDebugInstr.addActionListener(
				new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(radioModeDebugInstr.isSelected())
							cpu.setMode(CPU.MODE_INSTRUCTION);
					}
				});
		radioModeDebugCycle.addActionListener(
				new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(radioModeDebugCycle.isSelected())
							cpu.setMode(CPU.MODE_CYCLE);
					}
				});
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg == null){
			registerItemPanel.update();
			memoryViewPanel.update();
			cachePanel.update();
		}else{
			int msg = (Integer)arg;
			
			switch(msg){
			
				case(Message.MSG_CPU_STEP_WAITING):
					runSingleStepButton.setVisible(true);
					runSingleStepButton.startFlash();
					registerItemPanel.update();
					memoryViewPanel.update();
					cachePanel.update();
					break;
				
				case(Message.MSG_CPU_STEP_CONTINUE):
					runSingleStepButton.setVisible(false);
					runSingleStepButton.stopFlash();
					registerItemPanel.update();
					memoryViewPanel.update();
					cachePanel.update();
					break;
				
				case(Message.MSG_CPU_CIRCLE_FINISH):
					registerItemPanel.updateState();
					memoryViewPanel.update();
					cachePanel.update();
					break;
				default:
					
					break;
			}
		}
	}
	
	/**
	 * main function of this simulator
	 * @param args
	 */
	public static void main(String[] args){
		try {
			
			if(System.getProperty("os.name").contains("Windows")){
				String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				
				UIManager.setLookAndFeel(lookAndFeel);
			}
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
