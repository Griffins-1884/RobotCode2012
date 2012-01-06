package input;

public class AdjustableJoystick extends Joystick {
	public AdjustableJoystick(int port) {
		super(port);
	}
	public double getForward() {
		return joystick.getY() * (((-(super.getThrottle())+1)/2)+0.4);
	}
	public double getBackward() {
		return -getForward();
	}
	public double getRight() {
		return joystick.getX() * (((-(super.getThrottle())+1)/2)+0.4);
	}
	public double getLeft() {
		return -getRight();
	}
	public double getCounterClockwise() {
		return joystick.getTwist() * (((-(super.getThrottle())+1)/2)+0.4);
	}
	public double getClockwise() {
		return -getCounterClockwise();
	}
}