package cpu.element;

/**
 * This class defines the different actions during a cpu cycle
 *
 */
public class Cycle {
	
	/**
	 * load register to register
	 */
	public static final int CYCLE_REG2REG = 0;
	
	/**
	 * load register to memory
	 */
	public static final int CYCLE_MEM2REG = 1;
	
	/**
	 * load memory to register
	 */
	public static final int CYCLE_REG2MEM = 2;
	
	/**
	 * analysis of opcode and other params
	 */
	public static final int CYCLE_DECODE = 3;
	
	/**
	 * calculate EA and assign the value to MAR
	 */
	public static final int CYCLE_CALCULATE_EA_TO_MAR = 4;
	
	/**
	 * PC++
	 */
	public static final int CYCLE_PC_PLUS = 6;
	
	/**
	 * assign values to operand1 and operand2
	 */
	public static final int CYCLE_LOAD_OP1_AND_OP2 = 7;
	
	/**
	 * execute operations
	 */
	public static final int CYCLE_ALU_EXECUTE = 8;
	
	//part 2 begin
	
	public static final int CYCLE_MinusOne= 10;
	
	public static final int CYCLE_JUMP_WITHCONDITION = 11;

	public static final int CYCLE_DEV2REG = 12;
	public static final int CYCLE_REG2DEV = 13;
	public static final int CYCLE_CHK_DEV = 14;
	public static final int CYCLE_LOAD_OP1 = 15;
	
	//part 2 end
	
	/** used for CNVRT */
	public static final int CYCLE_CONVERT = 16;
	
	public int type;
	public String[] args;
	
	public Cycle(int type,String...args){
		this.type = type;
		this.args = args.clone();
	}

}
