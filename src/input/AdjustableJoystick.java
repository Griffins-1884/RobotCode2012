package input;

/**
 * Represents a joystick that can adjust its sensitivity with the throttle.
 * 
 * @author Colin Poler
 */
public class AdjustableJoystick extends Joystick {
	/**
	 * Constructs an AdjustableJoystick.
	 * 
	 * @param port The port to use.
	 */
	public AdjustableJoystick(int port) {
		super(port);
	}
	
	/**
	 * Gets the forward position of the joystick.
	 * 
	 * @return The forward position of the joystick.
	 */
	public double getForward() {
		return joystick.getY() * (((-(super.getThrottle())+1)/2)+0.4);
	}
	
	/**
	 * Gets the backward position of the joystick.
	 * 
	 * @return The backward position of the joystick.
	 */
	public double getBackward() {
		return -getForward();
	}
	
	/**
	 * Gets the right position of the joystick.
	 * 
	 * @return The right position of the joystick.
	 */
	public double getRight() {
		return joystick.getX() * (((-(super.getThrottle())+1)/2)+0.4);
	}
	
	/**
	 * Gets the left position of the joystick.
	 * 
	 * @return The left position of the joystick.
	 */
	public double getLeft() {
		return -getRight();
	}
	
	/**
	 * Gets the counter-clockwise twist of the joystick.
	 * 
	 * @return The counter-clockwise twist of the joystick.
	 */
	public double getCounterClockwise() {
		return joystick.getTwist() * (((-(super.getThrottle())+1)/2)+0.4);
	}
	
	/**
	 * Gets the clockwise twist of the joystick.
	 * 
	 * @return The clockwise twist of the joystick.
	 */
	public double getClockwise() {
		return -getCounterClockwise();
	}
}