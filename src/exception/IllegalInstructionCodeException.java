package exception;

public class IllegalInstructionCodeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8121560176849660740L;
	
	public IllegalInstructionCodeException(String code){
		super("Illegal Instrucation Code: "+code);
	}

}
