package common;

/**
 * this class defines the message between cpu thread and mainframe
 *
 */
public class Message {
	
	public static final int MSG_CPU_STEP_WAITING = 0;
	
	public static final int MSG_CPU_STEP_CONTINUE = 1;
	
	public static final int MSG_CPU_CIRCLE_FINISH = 2;
	
	/**
	 * waiting for keyboard
	 */
	public static final int MSG_KEYBOARD_WAITING = 3;
	
	/**
	 * keyboard pressed event
	 */
	public static final int MSG_KEYBOARD_PRESSED = 4;
}
