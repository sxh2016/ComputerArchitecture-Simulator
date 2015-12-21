package exception;

public class RegisterNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7641204005241724905L;
	
	public RegisterNotFoundException(String name){
		super("Register Not Found Exception: try to use register:"+name);
	}

}
