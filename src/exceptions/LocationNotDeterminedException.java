package exceptions;

/**
 * An exception thrown when a camera cannot determine the location of the robot.
 * 
 * @author Colin Poler
 */
public class LocationNotDeterminedException extends Exception {
	private static final long serialVersionUID = 4397739318602910596L;

	/**
	 * Constructs a LocationNotDeterminedException with the specified message.
	 * 
	 * @param message The message of the exception.
	 */
	public LocationNotDeterminedException(String message) {
		super(message);
	}
}