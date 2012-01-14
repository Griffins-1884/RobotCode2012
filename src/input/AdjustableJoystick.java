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
	public double forward() {
		return super.forward() * (((-(throttle())+1)/2)+0.4);
	}
	
	/**
	 * Gets the right position of the joystick.
	 * 
	 * @return The right position of the joystick.
	 */
	public double right() {
		return super.right() * (((-(throttle())+1)/2)+0.4);
	}
	
	/**
	 * Gets the counter-clockwise twist of the joystick.
	 * 
	 * @return The counter-clockwise twist of the joystick.
	 */
	public double counterClockwise() {
		return super.clockwise() * (((-(throttle())+1)/2)+0.4);
	}
}