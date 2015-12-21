package cpu.element;

import common.Util;

/**
 * This class simulates the physical register in CPU
 *
 */
public class Register {
	
	/**
	 * the name of this register
	 */
	private String name;
	
	/**
	 * the size of this register
	 */
	private int size;
	
	/**
	 * the content in this register, in int[], each integer can be 0 or 1
	 */
	private int[] content;
	
	/**
	 * default construction
	 * @param name
	 * @param size
	 */
	public Register(String name, int size){
		this.name = name;
		this.size = size;
		this.write(new int[size]);
	}
	
	/**
	 * get the content of this register
	 * @return
	 */
	public int[] read() {
		return content.clone();
	}

	/**
	 * set the content of this register
	 * @param content
	 */
	public void write(int[] content) {
		this.content = Util.getBinaryArrayFromIntValue(Util.getIntValueFromBinaryArray(content), size);
	}
	
	/**
	 * set the content of this register
	 * @param content
	 */
	public void write(int value) {
		this.content =  Util.getBinaryArrayFromIntValue(value, this.size);
	}

	/**
	 * get the name of this register
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * get the size of this register
	 * @return
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * clear the content in this register
	 */
	public void clear() {
		for (int i = 0; i < size; i++){
			content[i] = 0;
		}
	}
	
	/**
	 * get sub array of content
	 * @param index
	 * @param size
	 * @return
	 */
	public int[] subValue(int index,int size){
		int[] sub = new int[size];
		for(int i = 0; i < size; i++) {
			sub[i] = content[index + i];
		}
		return sub;
	}
	
	/**
	 * get the int value of content
	 * 
	 */
	public int getIntValue() {
		return Util.getIntValueFromBinaryArray(content);
	}
	
	/**
	 * convert int[] content to binary string
	 * @return
	 */
	public String getBinaryString(){
		StringBuffer buf = new StringBuffer();
		try{
			
			for(int i=0;i<size;i++)
				buf.append(content[i]);
			
		}catch(ArrayIndexOutOfBoundsException e)
		{
			buf.append("0");
			System.out.println(name);
			System.out.println(size);
			System.out.println(content.length);
			for(int i = 0; i < content.length; i++)
				System.out.println(content[i]);
		}
		return buf.toString();
	}
	
	public void setBitAt(int bit,int index){
		content[index] = bit;
	}
	public int getBitAt(int index){
		return content[index];
	}
	public void addValueBy(int bit,int index){
		content[index] = bit;
	}
	

}
