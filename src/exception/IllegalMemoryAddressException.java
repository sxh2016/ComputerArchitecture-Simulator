package exception;

public class IllegalMemoryAddressException extends Exception {

	/**
	 * generated UID
	 */
	private static final long serialVersionUID = -3038534425429591523L;
	
	public IllegalMemoryAddressException(int index){
		super("Illegal Memory Address:"+index);
	}

}
