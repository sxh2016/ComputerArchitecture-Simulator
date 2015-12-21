package cpu;

import io.IODeviceController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import memory.Cache;
import memory.MainMemory;
import memory.Memory;
import common.Configuration;
import common.Message;
import common.Util;
import cpu.element.Cycle;
import cpu.element.InstructionCycles;
import cpu.element.Register;
import cpu.element.Word;
import exception.IllegalDeviceStatusException;
import exception.IllegalInstructionCodeException;
import exception.IllegalMemoryAddressException;
import exception.RegisterNotFoundException;

/**
 * This class simulates real CPU
 *
 */
public class CPU extends Observable {
	
	/**
	 * Singleton
	 */
	private static CPU instance = new CPU();
	
	public static CPU getInstance(){
		return instance;
	}
	
	/**
	 * there are three modes to run this CPU
	 * <br><b>MODE_RUN</b>: run till error or end
	 * <br><b>MODE_INSTRUCTION</b>: run an instruction then pause (waiting)
	 * <br><b>MODE_CYCLE</b>: run a cycle of an instruction then pause (waiting)
	 */
	public static final int MODE_RUN = 0;
	
	public static final int MODE_INSTRUCTION = 1;
	
	public static final int MODE_CYCLE = 2;
	
	/**
	 * record current mode of this cpu
	 */
	private int currentMode = MODE_RUN;
	
	public void setMode(int mode)
	{
		this.currentMode = mode;
	}
	
	/**
	 * 
	 */
	private IODeviceController ioDevices = IODeviceController.getInstance();
	
	/**
	 * current state of this thread
	 */
	private int state;
	
	/**
	 * three states of thread
	 */
	public static final int STATE_IDLE = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_FINISHED = 2;
	
	/**
	 * the thread that simulating the cpu will be held for single step
	 */
	private int waitingSignal;
	
	/**
	 * when simulator runs a single step, the thread will be held
	 */
	public static final int SIGNAL_STEP = 0;
	
	/**
	 * 
	 */
	public static Boolean POWER_STATE = false;
	
	/**
	 * access to memory
	 */
	//private Memory memory = MainMemory.getInstance();
	private Memory memory = Cache.getInstance();
	
	public Memory getMemory(){
		return this.memory;
	}
	
	private Map<String, Register> registers;
	
	/**
	 * get the map of registers
	 * @return
	 */
	public Map<String,Register> getRegisters() {
		return registers;
	}

	/**
	 * get the amount of registers
	 * @return
	 */
	public int getRegisterSize(){
		return registers.size();
	}
	
	/**
	 * the list of register names in cpu
	 */
	private List<String> registerList;
	
	/**
	 * get the name list (keep the order in which registers are added)
	 * @return
	 */
	public List<String> getRegisterNames(){
		return this.registerList;
	}
	
	private void addRegister(String name,int size){
		registers.put(name, new Register(name,size));
		registerList.add(name);
	}
	
	/**
	 * construction, initial registers in cpu
	 */
	private CPU(){
		registers = new HashMap<String, Register>();
		registerList = new ArrayList<String>();
		
		/*
		 * General Purpose Registers
		 * <br>16-bit
		 */
		addRegister("R0", Configuration.REG_GPR_LENGTH);
		addRegister("R1", Configuration.REG_GPR_LENGTH);
		addRegister("R2", Configuration.REG_GPR_LENGTH);
		addRegister("R3", Configuration.REG_GPR_LENGTH);
		
		/*
		 * index registers: contains a 12-bit base address that supports base register addressing of memory.
		 * <br>12-bit
		 */
		addRegister("X1", Configuration.REG_IX_LENGTH);
		addRegister("X2", Configuration.REG_IX_LENGTH);
		addRegister("X3", Configuration.REG_IX_LENGTH);
		
		/*
		 * Program Counter: address of next instruction to be executed
		 * <br>12-bit
		 */
		addRegister("PC", Configuration.REG_IX_LENGTH);
		
		/*
		 * Condition Code: set when arithmetic/logical operations are executed; 
		 * it has four 1-bit elements: overflow, underflow, division by zero, equal-or-not. 
		 * <br>4-bit
		 */	
		addRegister("CC", Configuration.REG_CC_LENGTH);
		
		/*
		 * Instruction Register: holds the instruction to be executed
		 * <br>16-bit
		 */
		addRegister("IR", Configuration.REG_GPR_LENGTH);
		
		/*
		 * Memory Address Register: holds the address of the word to be fetched from memory
		 * <br>12-bit
		 */
		addRegister("MAR", Configuration.REG_IX_LENGTH);
		
		/*
		 * Memory Data Register: holds the word just fetched from or stored into memory
		 * <br>16-bit
		 */
		addRegister("MDR", Configuration.REG_GPR_LENGTH);
		
		/*
		 * Machine Status Register: certain bits record the status of the health of the machine
		 * <br>16-bit
		 */
		addRegister("MSR",Configuration.REG_GPR_LENGTH);
		
		/*
		 * Machine Fault Register: contains the ID code if a machine fault after it occurs
		 * <br>4-bit
		 */
		addRegister("MFR",4);
		
		/*
		 * the following registers store the values derived from instructions
		 */
		addRegister("OPCODE", 6);
		addRegister("RFI", 2);
		addRegister("IX", 2);
		addRegister("IND", 1);
		addRegister("ADDR", 5);
		addRegister("EA", Configuration.REG_IX_LENGTH);
		
		addRegister("OP1",16);
		addRegister("OP2",16);
		addRegister("RES",16);
		
		addRegister("RX",2);
		addRegister("RY",2);
		
		addRegister("DEVID",5);
		
		addRegister("FR0", 16);
		addRegister("FR1", 16);
		addRegister("FR", 2);
	}
	
	/**
	 * this method return the register by specific name
	 * <br>if name="R", Rn will be returned (n is decided by R-register's value)
	 * <br>so does "IX"
	 * 
	 * @param name
	 * @return Register with the name
	 * @throws RegisterNotFoundException if no register is found
	 */
	private Register getRegister(String name) throws RegisterNotFoundException{
		//find the real name for register
		String registerRealName;
		int val = Util.getIntValueFromBinaryArray(registers.get(name).read());
		if("RFI".equals(name)){
			registerRealName = ("R"+val);
		}
		else if("IX".equals(name)){
			registerRealName = ("X"+val);
		}
		else if("RX".equals(name)){
			registerRealName = ("R"+val);
		}
		else if("RY".equals(name)){
			registerRealName = ("R"+val);
		}
		else if("FR".equals(name)){
			registerRealName = ("FR"+val);
		}
		else{
			registerRealName = name;
		}
		
		return getRegisterByRealName(registerRealName);
	}
	
	/**
	 * this method return the specific register with the name
	 * @param name
	 * @return  Register
	 * @throws RegisterNotFoundException if no register is found
	 */
	public Register getRegisterByRealName(String name) throws RegisterNotFoundException{
		if(registers.keySet().contains(name)) {
			return registers.get(name);
		}
		else {
			throw new RegisterNotFoundException(name);
		}
	}
	
	/**
	 * thread for running cpu
	 */
	private Thread thread;
	
	/**
	 * begin to execute instructions
	 */
	public void execute(){
		
		/**
		 * unable to run when cpu not idle 
		 */
		if(state != STATE_IDLE){
			return;
		}
		
		thread = new Thread(){
			@Override
			public void run(){
				try {
					while(true){
						
						state = STATE_RUNNING;
						
						executeNextInstruction();
						
						if(currentMode == MODE_INSTRUCTION){
							waitForSignal(SIGNAL_STEP);
						}
						
					}
				} catch (Exception e) {
					//log.console(ConsoleMessage.TYPE_ERROR, e.getMessage());
					e.printStackTrace();
				} finally{
					//powerOff();
					state = STATE_IDLE;
					//log.changeState();
				}
			}
		};
		
		thread.start();
		
	}
	
	/**
	 * this function defines how to act in different instructions
	 * @throws IllegalInstructionCodeException
	 * @throws RegisterNotFoundException
	 * @throws IllegalMemoryAddressException
	 * @throws IllegalDeviceStatusException 
	 */
	private void executeNextInstruction() throws IllegalInstructionCodeException, RegisterNotFoundException, IllegalMemoryAddressException, IllegalDeviceStatusException {
		
		System.out.println("Begin to execute instruction: PC = " + Util.getIntValueFromBinaryArray(getRegisterByRealName("PC").read()));
		
		/*
		 * execute first 4 steps
		 */
		InstructionCycles beginning = InstructionCycles.getCommonBeginning();
		for(Cycle cycle : beginning.getCycles()){
			executeOneCycle(cycle);
		}
		
		/*
		 * execute the different circles for specific instruction 
		 * (after last 4 steps, we can know which instruction is executing)
		 */
		InstructionCycles content = InstructionSet.getCycles(Util.getIntValueFromBinaryArray(getRegisterByRealName("OPCODE").read()));
		if(null == content){
				throw new IllegalInstructionCodeException("CODE:"+Util.getIntValueFromBinaryArray(getRegisterByRealName("OPCODE").read()));
		}
		for(Cycle cycle:content.getCycles()){
			executeOneCycle(cycle);
		}
		System.out.println("IR: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("IR").read()));
		System.out.println("R0: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("R0").read()));
		System.out.println("R1: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("R1").read()));
		System.out.println("R2: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("R2").read()));
		System.out.println("R3: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("R3").read()));
		System.out.println("X1: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("X1").read()));
		System.out.println("X2: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("X2").read()));
		System.out.println("X3: " + Util.getBinaryStringFromBinaryArray(getRegisterByRealName("X3").read()));
		
	}
	/**
	 * this function defines how to act in different cpu cycles
	 * @param cycle
	 * @throws RegisterNotFoundException
	 * @throws IllegalMemoryAddressException
	 * @throws IllegalDeviceStatusException 
	 */
	private void executeOneCycle(Cycle cycle) throws RegisterNotFoundException, IllegalMemoryAddressException, IllegalDeviceStatusException {
		
		Register dest, source;
		
		switch(cycle.type) {
			case(Cycle.CYCLE_REG2REG):
				
				//if opcode is LDX, dest is IX but index is in RFI
				if(Util.getIntValueFromBinaryArray(getRegister("OPCODE").read()) == 41
						&& "MDR".equalsIgnoreCase(cycle.args[1]) && "IX".equalsIgnoreCase(cycle.args[0])) {
					dest = getRegisterByRealName("X" + Util.getIntValueFromBinaryArray(getRegisterByRealName("RFI").read()));
					//getRegister("OPCODE").write(0);
				}
				else
				{
					dest = getRegister(cycle.args[0]);
				}
				
				
				//if opcode is STX, source is IX but index is in RFI
				if(Util.getIntValueFromBinaryArray(getRegister("OPCODE").read()) == 42
						&& "MDR".equalsIgnoreCase(cycle.args[0]) && "IX".equalsIgnoreCase(cycle.args[1])) {
					source = getRegisterByRealName("X" + Util.getIntValueFromBinaryArray(getRegisterByRealName("RFI").read()));
				}
				else
				{
					source = getRegister(cycle.args[1]);
				}
				
				dest.write(source.read());
				//msg.append(dest.name).append(" = (").append(source.name).append(")");
				break;
				
				
			case(Cycle.CYCLE_REG2MEM):
				dest = getRegister(cycle.args[0]);
				source = getRegister(cycle.args[1]);
				
				memory.write(Util.getIntValueFromBinaryArray(dest.read()), new Word(source.read()));
				//msg.append("MEM(").append(dest.name).append(") = ").append(source.name);
				break;
				
				
			case(Cycle.CYCLE_MEM2REG):
				source = getRegister(cycle.args[1]);
				dest = getRegister(cycle.args[0]);
				dest.write(memory.read(Util.getIntValueFromBinaryArray(source.read())).getContent());
				//msg.append(dest.name).append(" = MEM(").append(source.name).append(")");
				break;
				
				
			case(Cycle.CYCLE_DECODE):
				doCPUDecode();
				break;
			/*
			 * calculate EA by XI,i
			 */
			case(Cycle.CYCLE_CALCULATE_EA_TO_MAR):
				int ix = getRegisterByRealName("IX").getIntValue();
				//get address from ADDR
				int address = getRegisterByRealName("ADDR").getIntValue();
				//add address by Xn if n(IX)>0
				if(ix != 0){
					address += getRegisterByRealName("X"+ix).getIntValue();
				}
				//get value of IND-register
				int i = getRegisterByRealName("IND").getIntValue();
				//change address to the value of memory at $address$ if i==1
				if(i==1) {
					address = Util.getIntValueFromBinaryArray(memory.read(address).getContent());
				}
				//save address into EA-register
				getRegisterByRealName("EA").write(address);
				getRegisterByRealName("MAR").write(address);
				System.out.println("EA:" + address);
				//msg.append("MAR = (ADDR,IX,I)");
				break;
			case(Cycle.CYCLE_ALU_EXECUTE):
				int op1 = getRegisterByRealName("OP1").getIntValue();
				int op2 = getRegisterByRealName("OP2").getIntValue();
				int opcode = getRegisterByRealName("OPCODE").getIntValue();
				int value = 0;
				
				//msg.append("RES = OP1 ");
				getRegister("CC").clear();
				Register cc = this.getRegister("CC");
				
				switch(opcode){
				
					//AMR,AIR
					case(4):
					case(6):
						value = op1 + op2;
					break;
					
					//SMR,SIR
					case(5):
					case(7):
						value = op1-op2;
						if(value<0){
							cc.setBitAt(1, 1);
							value = 0 -value;	
						}
						System.out.println("op1 - op2:" + op1 + "-" + op2);
					break;
					//MLT
					case(20):
						value = op1*op2;
					
					break;
					
					//DVD
					case(21):
						if(op2==0){
							//DIVZERO
							getRegister("CC").setBitAt(1, 2);
							break;
						}
						value = op1/op2;
						int left = op1%op2;
						int rx = getRegisterByRealName("RX").getIntValue();
						getRegisterByRealName("R"+rx).write(value);
						rx++;
						getRegisterByRealName("R"+rx).write(left);
					break;
					
					//TRR
					case(22):
						
						if(op1==op2)
							cc.setBitAt(1, 3);
						else
							cc.setBitAt(0, 3);
						value = 0;
					break;
					default:
						value = 0;
				}
				
				getRegisterByRealName("RES").write(value);
				break;
			case(Cycle.CYCLE_LOAD_OP1_AND_OP2):
				dest = getRegister(cycle.args[0]);
				source = getRegister(cycle.args[1]);
				getRegister("OP1").write(dest.read());
				getRegister("OP2").write(source.read());
				break;
			case(Cycle.CYCLE_PC_PLUS):
				dest = getRegisterByRealName("PC");
				dest.write(dest.getIntValue() + 1);
				//msg.append("PC = (PC) + 1");
				break;
			case(Cycle.CYCLE_JUMP_WITHCONDITION):
				//MAR
				source = getRegister("MAR");
				dest = getRegisterByRealName("PC");
				//get the condition value
				int conValue = getRegister(cycle.args[0]).getIntValue();
				opcode = getRegister("OPCODE").getIntValue();
				cc = this.getRegister("CC");
				boolean jump = false;
				
				//check if jump
				//JZ && c(r)=0
				if(opcode == 10 && conValue == 0)
					jump = true;
				
				//JNE && c(r)!=0
				else if(opcode == 11 && conValue != 0)
					jump = true;
				
				//SOB && c(r)>0
				else if(opcode == 16 && conValue>0)
					jump = true;
				
				//JMP
				else if(opcode == 13)
					jump = true;
				
				//JSR
				else if(opcode == 14)
					jump = true;
				
				else if(opcode == 12){
					conValue = registers.get("RFI").getIntValue();
					if(cc.getBitAt(conValue)==1)
						jump = true;
				}
				
				if(jump){
					//PC <- MAR
					dest.write(source.read());
				}else{
					//PC = PC + 1
					
					dest.write(dest.getIntValue() + 1);;
				}
				
//				msg.append("PC = (MAR)");
				
				break;
				
			case(Cycle.CYCLE_DEV2REG):
				//save devid
				//waitingDevice = getRegister(circle.args[1]).intValue();
				
				//waiting for device input
				//waitForSignal(SIGNAL_IN);
				
				int inDevice = getRegister("DEVID").getIntValue();
				Word word = ioDevices.input(inDevice);
			
				
				//signal in, save value into R
				dest = getRegister(cycle.args[0]);
				dest.write(word.getContent());
				//msg.append(dest.name+" = IN_VALUE("+word.intValue()+")");
				
				
			break;
			
			case(Cycle.CYCLE_REG2DEV):
				//get output value and device id
				source = getRegister(cycle.args[0]);
				int devid = getRegister("DEVID").getIntValue();
				//send signal
				ioDevices.output(devid, new Word(source.getIntValue()));
				//outputBuffer.outputSignal(devid, source.intValue());
				//log.changeState();
				//msg.append("DEV("+devid+") = "+source.name);
			break;
			
			case(Cycle.CYCLE_MinusOne):
				dest = getRegister(cycle.args[0]);
				dest.write(dest.getIntValue()-1);
				//msg.append(dest.name+" = "+dest.name+" - 1");
			break;
			default:
				break;
		}
		
		this.setChanged();
		this.notifyObservers(Message.MSG_CPU_CIRCLE_FINISH);
		
		if(currentMode == MODE_CYCLE){
			waitForSignal(SIGNAL_STEP);
		}
		
	}
	
	
	/**
	 * decompose instruction into different registers
	 * @throws RegisterNotFoundException
	 */
	private void doCPUDecode() throws RegisterNotFoundException {
		
		
		/*
		 * get the instruction register
		 */
		Register source = getRegister("IR");
		
		/*
		 * get the opcode in the instruction
		 */
		getRegisterByRealName("OPCODE").write(source.subValue(0, 6));
		
		int opcode = Util.getIntValueFromBinaryArray(source.subValue(0, 6));
		
		switch(opcode){
		case(31):
		case(32):
			System.out.println("decode pattern: OPCODE(6),R(2),A/L(1),L/R(1),(1),Count(5)");
			//OPCODE(6),R(2),A/L(1),L/R(1),(1),Count(5)
			getRegisterByRealName("RFI").write(source.subValue(6, 2));
			getRegisterByRealName("AL").write(source.subValue(8, 1));
			getRegisterByRealName("LR").write(source.subValue(9, 1));
			getRegisterByRealName("COUNT").write(source.subValue(11, 5));
			break;
		
		case(61):
		case(62):
		case(63):
			System.out.println("decode pattern: OPCODE(6),(2),R(2),(1),DevId(5)");
			//OPCODE(6),(2),R(2),(1),DevId(5)
			getRegisterByRealName("RFI").write(source.subValue(6, 2));
			getRegisterByRealName("DEVID").write(source.subValue(11, 5));
			break;
		
		case(20):
		case(21):
		case(22):
		case(23):
		case(24):
		case(25):
			System.out.println("decode pattern: OPCODE(6),Rx(2),Ry(2),(6)");
			//OPCODE(6),Rx(2),Ry(2),(8)
			getRegisterByRealName("RX").write(source.subValue(6, 2));
			getRegisterByRealName("RY").write(source.subValue(8, 2));
			break;
		
		default:
			System.out.println("decode pattern: default pattern");
			/*
			 * load/store instruction style: 
			 * OPCODE(6),RFI(2),IX(2),IND(1),ADDR(5)
			 */
			getRegisterByRealName("RFI").write(source.subValue(6, 2));
			getRegisterByRealName("IX").write(source.subValue(8, 2));
			getRegisterByRealName("IND").write(source.subValue(10, 1));
			getRegisterByRealName("ADDR").write(source.subValue(11, 5));
			break;
		}
	}
		
	/**
	 * if cpu is in debugMode: instruction or cycle, cpu thread will wait for the right signal to wake up
	 * @param signal
	 */
	public void waitForSignal(int signal){
		
		if(signal == SIGNAL_STEP){
			this.setChanged();
			this.notifyObservers(Message.MSG_CPU_STEP_WAITING);
		}
		
		try {
			synchronized(thread){
				waitingSignal = signal;
				thread.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * notify thread to run next instruction or cycle
	 */
	public boolean continueRun(int signal){
		
		if(signal != waitingSignal)
			return false;
		
		if(thread==null)
			return false;
		synchronized(thread){
			thread.notify();
		}
		this.setChanged();
		this.notifyObservers(Message.MSG_CPU_STEP_CONTINUE);
		return true;
	}

}
