package memory;

import common.Configuration;
import cpu.element.Word;
import exception.IllegalMemoryAddressException;

/**
 * This class simulates the physical memory of a computer
 * 
 *
 */
public class MainMemory implements Memory{

	/**
	 * This simulates the data stored in MainMemory
	 */
	private Word data[];

	/**
	 * Initial MainMemory
	 */
	private MainMemory() {
		this.data = new Word[Configuration.MEMORY_SIZE];
		for (int i = 0; i < Configuration.MEMORY_SIZE; i++)
			data[i] = new Word();
	}

	private static MainMemory instance = new MainMemory();

	/**
	 * Use Singleton. User can only get the instance of this Class
	 * 
	 * @return
	 */
	public static MainMemory getInstance() {
		return instance;
	}

	/**
	 * According to the address index, get the content of that word
	 * 
	 * @param index
	 * @return Word
	 * @throws IllegalMemoryAddressException
	 */
	public Word read(int index) throws IllegalMemoryAddressException {
		if (index < 0 || index > Configuration.MEMORY_SIZE) {
			throw new IllegalMemoryAddressException(index);
		}
		return this.data[index].clone();
	}

	/**
	 * 
	 * @param index
	 * @param data
	 * @throws IllegalMemoryAddressException
	 */
	public void write(int index, int[] data)
			throws IllegalMemoryAddressException {
		if (index < 0 || index > Configuration.MEMORY_SIZE) {
			throw new IllegalMemoryAddressException(index);
		}

		this.data[index].setContent(data);
	}
	
	/**
	 * get the whole data in memory
	 * @return
	 */
	public Word[] getAllData() {
		return data;
	}

	@Override
	public void write(int index, Word word)
			throws IllegalMemoryAddressException {
		if (index < 0 || index > Configuration.MEMORY_SIZE) {
			throw new IllegalMemoryAddressException(index);
		}
		
		this.data[index].setContent(word.getContent());
	}

}
