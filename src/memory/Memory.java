package memory;

import cpu.element.Word;
import exception.IllegalMemoryAddressException;

public interface Memory {

	public abstract Word read(int index) throws IllegalMemoryAddressException;
	
	public abstract void write(int index,Word word) throws IllegalMemoryAddressException;
}
