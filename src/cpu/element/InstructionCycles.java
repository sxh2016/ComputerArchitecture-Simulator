package cpu.element;

import java.util.ArrayList;
import java.util.List;

/**
 * every instruction is consisted of several cycles, 
 * this class define a sequence of cycles
 *
 */
public class InstructionCycles {

	/**
	 * every instruction is consisted of several cycles
	 */
	private List<Cycle> cycles;
	
	/**
	 * all instructions have some same cycles at beginning
	 */
	private static InstructionCycles commonBeginning;
	
	public InstructionCycles(){
		cycles = new ArrayList<Cycle>();		
	}
	
	
	public void addCycle(Cycle cycle){
		cycles.add(cycle);
	}
	
	public void addCycle(int type,String...args){
		cycles.add(new Cycle(type,args));
	}
	
	public void addCommonEndingCycles(){
		cycles.add(new Cycle(Cycle.CYCLE_PC_PLUS));
	}
	
	public List<Cycle> getCycles(){
		return this.cycles;
	}
	
	/**
	 * get the common beginning cycles of instructions 
	 * as all instructions have same beginning cycles
	 * 
	 * @return
	 */
	public static InstructionCycles getCommonBeginning(){
		if(null == commonBeginning){
			commonBeginning = new InstructionCycles();
			//insert common cycles at beginning
			commonBeginning.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"MAR","PC"));
			commonBeginning.addCycle(new Cycle(Cycle.CYCLE_MEM2REG,"MDR","MAR"));
			commonBeginning.addCycle(new Cycle(Cycle.CYCLE_REG2REG,"IR","MDR"));
			commonBeginning.addCycle(new Cycle(Cycle.CYCLE_DECODE));
			
		}
		return commonBeginning;
	}
}
