package cpu;

import java.util.HashMap;
import java.util.Map;

import cpu.element.Cycle;
import cpu.element.InstructionCycles;

/**
 * This class defines the instruction set in cpu
 *
 */
public class InstructionSet {
	/**
	 * Singleton
	 */
	private static InstructionSet instance = new InstructionSet();
	
	public static InstructionSet getInstance() {
		return instance;
	}
	
	/**
	 * opcode of different instructions
	 */
	private static final int OPCODE_LDR = 1;
	
	private static final int OPCODE_STR = 2;
	
	private static final int OPCODE_LDA = 3;
	
	private static final int OPCODE_LDX = 41;
	
	private static final int OPCODE_STX = 42;
	
	private static final int OPCODE_AMR = 4;
	
	private static final int OPCODE_SMR = 5;
	
	private static final int OPCODE_AIR = 6;
	
	private static final int OPCODE_SIR = 7;
	
	/**
	 * instruction format
	 * OPCODE(6),R(2),IX(2),I(1),ADD(5)
	 */
	//public static final int INSTR_OPCODE_R_IX_I_ADD = 1;
	
	
	/**
	 * map for finding code by name
	 */
	private Map<String,Integer> name2code;
	
	/**
	 * map for finding name by code
	 */
	private Map<Integer,String> code2name;
	
	/**
	 * map for finding InstructionCycles by code
	 */
	private Map<Integer,InstructionCycles> code2instrs;
	
	/**
	 * initial the cycles in different instructions
	 */
	private InstructionSet() {
		name2code = new HashMap<String,Integer>();
		code2name = new HashMap<Integer,String>();
		code2instrs = new HashMap<Integer,InstructionCycles>();
		
		setLDR();
		
		setSTR();
		
		setLDA();
		
		setLDX();
		
		setSTX();
		
		setAMR();
		
		setSMR();
		
		setAIR();
		
		setSIR();
		
		setJZ();
		setJNE();
		setJCC();
		setJMP();
		setJSR();
		
		setRFS();
		setSOB();
		setJGE();
		
		setMLT();
		setDVD();
		setTRR();
		setAND();
		setORR();
		setNOT();
		
		setSRC();
		setRRC();
		
		setFADD();
		setFSUB();
		setVADD();
		setVSUB();
		setCNVRT();
		setLDFR();
		setSTFR();
		
		setIN();
		setOUT();
		setCHK();
	}
	
	private void putCyclesIntoMaps(String name,int code,InstructionCycles cycles){
		name2code.put(name, code);
		code2name.put(code, name);
		code2instrs.put(code, cycles);
	}
	
	/**
	 *  LDR: Load Register From Memory
	 *  OPCODE = 1
	 */
	private void setLDR(){
		
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(Cycle.CYCLE_MEM2REG,"MDR","MAR");
		cycles.addCycle(Cycle.CYCLE_REG2REG,"RFI","MDR");
		cycles.addCommonEndingCycles();
		
		code2name.put(OPCODE_LDR, "LDR");
		name2code.put("LDR", OPCODE_LDR);
		code2instrs.put(OPCODE_LDR, cycles);
	}
	
	/**
	 * Store Register To Memory
	 * OPCODE = 2
	 */
	private void setSTR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"MDR","RFI"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2MEM,"MAR","MDR"));
		cycles.addCommonEndingCycles();
		
		code2name.put(OPCODE_STR, "STR");
		name2code.put("STR", OPCODE_STR);
		code2instrs.put(OPCODE_STR, cycles);
		
	}
	
	/**
	 * Load Register with Address
	 * OPCODE = 3
	 */
	private void setLDA(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(Cycle.CYCLE_REG2REG,"RFI","MAR");
		cycles.addCommonEndingCycles();
		
		code2name.put(OPCODE_LDA, "LDA");
		name2code.put("LDA", OPCODE_LDA);
		code2instrs.put(OPCODE_LDA, cycles);
	}
	
	/**
	 * Load index register from memory
	 * OPCODE = 41
	 */
	private void setLDX(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MDR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"IX","MDR"));
		cycles.addCommonEndingCycles();
		
		code2name.put(OPCODE_LDX, "LDX");
		name2code.put("LDX", OPCODE_LDX);
		code2instrs.put(OPCODE_LDX, cycles);
	}
	
	/**
	 * Store Index Register to Memory
	 * OPCODE = 42
	 */
	private void setSTX(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"MDR","IX"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2MEM,"MAR","MDR"));
		cycles.addCommonEndingCycles();
		
		code2name.put(OPCODE_STX, "STX");
		name2code.put("STX", OPCODE_STX);
		code2instrs.put(OPCODE_STX, cycles);
	}
	
	/**
	 * Add Memory To Register
	 * 4
	 */
	private void setAMR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MDR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RFI","MDR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RFI","RES"));
		cycles.addCommonEndingCycles();

		code2name.put(OPCODE_AMR, "AMR");
		name2code.put("AMR", OPCODE_AMR);
		code2instrs.put(OPCODE_AMR, cycles);
	}
	
	/**
	 * Subtract Memory From Register
	 * 5
	 */
	private void setSMR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MDR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RFI","MDR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RFI","RES"));
		cycles.addCommonEndingCycles();

		code2name.put(OPCODE_SMR, "SMR");
		name2code.put("SMR", OPCODE_SMR);
		code2instrs.put(OPCODE_SMR, cycles);
	}
	
	/**
	 * Add  Immediate to Register
	 * 6
	 */
	private void setAIR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RFI","ADDR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RFI","RES"));
		cycles.addCommonEndingCycles();

		code2name.put(OPCODE_AIR, "AIR");
		name2code.put("AIR", OPCODE_AIR);
		code2instrs.put(OPCODE_AIR, cycles);
	}
	
	/**
	 * Subtract  Immediate  from Register
	 * 7
	 */
	private void setSIR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RFI","ADDR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RFI","RES"));
		cycles.addCommonEndingCycles();

		code2name.put(OPCODE_SIR, "SIR");
		name2code.put("SIR", OPCODE_SIR);
		code2instrs.put(OPCODE_SIR, cycles);
	}
	
	/**
	 * Jump If Zero
	 * 010
	 */
	private static final int OPCODE_JZ = 10;
	
	private void setJZ(){
		InstructionCycles cycles = new InstructionCycles();		
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_JUMP_WITHCONDITION,"RFI"));			

		putCyclesIntoMaps("JZ",OPCODE_JZ,cycles);
	}	

	

	/**
	 * Jump If Not Equal
	 * 011
	 */
	private static final int OPCODE_JNE = 11;
	private void setJNE(){
		InstructionCycles cycles = new InstructionCycles();		
	    cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_JUMP_WITHCONDITION,"RFI"));

		putCyclesIntoMaps("JNE",OPCODE_JNE,cycles);
	}	

	/**
	 * Jump If Condition Code
	 * 012
	 */
	private static final int OPCODE_JCC = 12;
	private void setJCC(){
		InstructionCycles cycles = new InstructionCycles();		
	    cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));		
	    cycles.addCycle(new Cycle(Cycle.CYCLE_JUMP_WITHCONDITION,"CC"));			

		putCyclesIntoMaps("JCC",OPCODE_JCC,cycles);
	}

	/**
	 * Unconditional Jump To Address
	 * 013
	 */
	private static final int OPCODE_JMP = 13;
	private void setJMP(){
		InstructionCycles cycles = new InstructionCycles();		
	    cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_JUMP_WITHCONDITION,"RFI"));

		putCyclesIntoMaps("JMP",OPCODE_JMP,cycles);
	}
	
	/**
	 * Jump and Save Return Address
	 * 014
	 */
	private static final int OPCODE_JSR = 14;
	private void setJSR(){
		InstructionCycles cycles = new InstructionCycles();		
	    cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCommonEndingCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"R3","PC"));
	    cycles.addCycle(new Cycle(Cycle.CYCLE_JUMP_WITHCONDITION,"RFI"));

		putCyclesIntoMaps("JSR",OPCODE_JSR,cycles);
	}
	
	/**
	 * Return From Subroutine w/ return code as Immed portion (optional) stored in the instructions address field.
	 * 015
	 */
	private static final int OPCODE_RFS = 15;
	private void setRFS(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"R0","Immed"));	
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"PC","R3"));				

		putCyclesIntoMaps("RFS",OPCODE_RFS,cycles);
	}	
	
	/**
	 * Subtract One and Branch
	 * 016
	 */
	private static final int OPCODE_SOB = 16;
	private void setSOB(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(Cycle.CYCLE_MinusOne,"RFI");
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));		
	    cycles.addCycle(new Cycle(Cycle.CYCLE_JUMP_WITHCONDITION,"RFI","MAR"));			

		putCyclesIntoMaps("SOB",OPCODE_SOB,cycles);
	}	
	
	/**
	 * Jump Greater Than or Equal To
	 * 017
	 */
	private static final int OPCODE_JGE = 17;
	private void setJGE(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));		
	    cycles.addCycle(new Cycle(Cycle.CYCLE_JUMP_WITHCONDITION,"RFI","MAR"));			

		putCyclesIntoMaps("JGE",OPCODE_JGE,cycles);
	}
	
	/**
	 * Logical And of Register and Register
	 * 023
	 */
	private static final int OPCODE_AND = 23;
	private void setAND(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RX","RY"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RX","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("AND",OPCODE_AND,cycles);
	}	

	/**
	 * Logical Or of Register and Register
	 * 024
	 */
	private static final int OPCODE_ORR = 24;
	private void setORR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RX","RY"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RX","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("ORR",OPCODE_ORR,cycles);
	}

	/**
	 * Logical Not of Register To Register
	 * 025
	 */
	private static final int OPCODE_NOT = 25;
	private void setNOT(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RX","RY"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RX","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("NOT",OPCODE_NOT,cycles);
	}
	
	
	/**
	 * Input Character To Register from Device
	 * 061
	 */
	private static final int OPCODE_IN = 61;
	private void setIN(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_DEV2REG,"RFI","DEVID"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("IN",OPCODE_IN,cycles);
	}
	
	/**
	 * Output Character to Device from Register
	 * 062
	 */
	private static final int OPCODE_OUT = 62;
	private void setOUT(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2DEV,"RFI","DEVID"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("OUT",OPCODE_OUT,cycles);
	}	

	/**
	 * Check Device Status to Register
	 * 063
	 */
	private static final int OPCODE_CHK = 63;
	private void setCHK(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CHK_DEV,"RFI","DEVID"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("CHK",OPCODE_CHK,cycles);
	}

	/**
	 * Multiply Register by Register
	 * 020
	 */
	private static final int OPCODE_MLT = 20;
	private void setMLT(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RX","RY"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"RX","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("MLT",OPCODE_MLT,cycles);
	}
	
	/**
	 * Divide Register by Register
	 * 021
	 */
	private static final int OPCODE_DVD = 21;
	private void setDVD(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RX","RY"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("DVD",OPCODE_DVD,cycles);
	}
	
	
	/**
	 * Test the Equality of Register and Register
	 * 022
	 */
	private static final int OPCODE_TRR = 22;
	private void setTRR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RX","RY"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("TRR",OPCODE_TRR,cycles);
	}
	

	/**
	 * Shift Register by Count
	 * 031
	 */
	private static final int OPCODE_SRC = 31;
	private void setSRC(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1,"RFI"));		
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("SRC",OPCODE_SRC,cycles);
	}	
	
	/**
	 * Rotate Register by Count
	 * 032
	 */
	private static final int OPCODE_RRC = 32;
	private void setRRC(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1,"RFI"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("RRC",OPCODE_RRC,cycles);
	}
	
	
	/**
	 * Floating Add Memory To Register
	 * 033
	 */
	private static final int OPCODE_FADD = 33;
	private void setFADD(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MBR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"FR","MBR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"FR","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("FADD",OPCODE_FADD,cycles);
	}
	
	/**
	 * Floating Subtract Memory From Register
	 * 034
	 */
	private static final int OPCODE_FSUB = 34;
	private void setFSUB(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MBR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"FR","MBR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"FR","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("FSUB",OPCODE_FSUB,cycles);
	}	

	/**
	 * Vector Add
	 * 035
	 */
	private static final int OPCODE_VADD = 35;
	private void setVADD(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		//cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MBR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"FR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		//cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"FR","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("VADD",OPCODE_VADD,cycles);
	}

	/**
	 * Vector Subtract
	 * 036
	 */
	private static final int OPCODE_VSUB = 36;
	private void setVSUB(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		//cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MBR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"FR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_ALU_EXECUTE));
		//cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"FR","RES"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("VSUB",OPCODE_VSUB,cycles);
	}

	/**
	 * Convert to Fixed/FloatingPoint
	 * 037
	 */
	private static final int OPCODE_CNVRT = 37;
	private void setCNVRT(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MBR","MAR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_LOAD_OP1_AND_OP2,"RFI","MBR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_CONVERT));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("CNVRT",OPCODE_CNVRT,cycles);
	}
	
	/**
	 * Load Floating Register From Memory
	 * 050
	 */
	private static final int OPCODE_LDFR = 50;
	private void setLDFR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(Cycle.CYCLE_MEM2REG,"MBR","MAR");
		cycles.addCycle(Cycle.CYCLE_REG2REG,"FR","MBR");
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("LDFR",OPCODE_LDFR,cycles);
	}
	
	/**
	 * Store Floating Register To Memory
	 * 051
	 */
	private static final int OPCODE_STFR = 51;
	private void setSTFR(){
		InstructionCycles cycles = new InstructionCycles();
		cycles.addCycle(new Cycle(Cycle.CYCLE_CALCULATE_EA_TO_MAR));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"MBR","FR"));
		cycles.addCycle(new Cycle(Cycle.CYCLE_REG2MEM,"MAR","MBR"));
		cycles.addCommonEndingCycles();
		
		putCyclesIntoMaps("STFR",OPCODE_STFR,cycles);
	}

	
	
	
	/**
	 * get the instruction cycles by opcode
	 * @param code
	 * @return
	 */
	public static InstructionCycles getCycles(int code){
		return InstructionSet.getInstance().code2instrs.get(code);
	}
	
	/**
	 * get the instruction cycles by name
	 * @param name
	 * @return
	 */
	public static InstructionCycles getCycles(String name){
		return InstructionSet.getInstance().code2instrs.get(
				InstructionSet.getInstance().name2code.get(name));
	}

}
