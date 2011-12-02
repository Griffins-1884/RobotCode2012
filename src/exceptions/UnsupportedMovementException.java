package exceptions;

/**
 * An exception (don't bother catching, you should just ensure it doesn't happen) thrown when an invalid movement is passed to a DriveSystem.
 * 
 * @author Colin Poler
 */
public class UnsupportedMovementException extends RuntimeException {
	private static final long serialVersionUID = 614481235538458946L;
	
	/**
	 * Constructs an UnsupportedMovementException with the specified message.
	 * 
	 * @param message The message of the exception.
	 */
	public UnsupportedMovementException(String message) {
		super(message);
	}
}