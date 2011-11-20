package _static;

public abstract class AbstractController {
	private final AbstractRobot robot;
	public AbstractController(AbstractRobot robot) {
		this.robot = robot;
	}
	public abstract void initialize();
	public abstract void periodic();
	public abstract void continuous();
}