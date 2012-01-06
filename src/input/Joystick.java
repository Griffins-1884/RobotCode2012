package input;

public class Joystick {
	protected edu.wpi.first.wpilibj.Joystick joystick;
	public Joystick(int port) {
		joystick = new edu.wpi.first.wpilibj.Joystick(port);
	}
	public double getForward() {
		return joystick.getY();
	}
	public double getBackward() {
		return -getForward();
	}
	public double getRight() {
		return joystick.getX();
	}
	public double getLeft() {
		return -getRight();
	}
	public double getCounterClockwise() {
		return joystick.getTwist();
	}
	public double getClockwise() {
		return -getCounterClockwise();
	}
	public double getThrottle() {
		return joystick.getZ();
	}
	public boolean getButton(int number) {
		return joystick.getRawButton(number);
	}
}