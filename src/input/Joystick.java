package input;

/**
 * Represents a joystick.
 * 
 * This class should remove all confusion from Joysticks and be very straightforward to use.
 * 
 * @author Colin Poler
 */
public class Joystick {
	/**
	 * The joystick behind this facade.
	 */
	protected edu.wpi.first.wpilibj.Joystick joystick;
	
	/**
	 * Constructs a Joystick using the specified port.
	 * 
	 * @param port The port to use.
	 */
	public Joystick(int port) {
		joystick = new edu.wpi.first.wpilibj.Joystick(port);
	}
	
	/**
	 * Gets the forward position of the joystick.
	 * 
	 * @return The forward position of the joystick.
	 */
	public double getForward() {
		return joystick.getY();
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
		return joystick.getX();
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
		return joystick.getTwist();
	}
	
	/**
	 * Gets the clockwise twist of the joystick.
	 * 
	 * @return The clockwise twist of the joystick.
	 */
	public double getClockwise() {
		return -getCounterClockwise();
	}
	
	/**
	 * Gets the forward position of the throttle.
	 * 
	 * @return The forward position of the throttle.
	 */
	public double getThrottle() {
		return joystick.getZ();
	}
	
	/**
	 * Gets the state of a button on the joystick.
	 * 
	 * @return The state of a button on the joystick.
	 */
	public boolean getButton(int number) {
		return joystick.getRawButton(number);
	}
}