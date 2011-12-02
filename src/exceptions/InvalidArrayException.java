package exceptions;

/**
 * An exception (don't bother catching, you should just ensure it doesn't happen) thrown when an invalid array is passed to a method or constructor.
 * 
 * @author Colin Poler
 */
public class InvalidArrayException extends RuntimeException {
	private static final long serialVersionUID = 614481235538458946L;
	
	/**
	 * Constructs an InvalidArrayException with the specified message.
	 * 
	 * @param message The message of the exception.
	 */
	public InvalidArrayException(String message) {
		super(message);
	}
}