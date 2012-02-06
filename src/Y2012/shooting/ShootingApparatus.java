package Y2012.shooting;

import edu.wpi.first.wpilibj.Jaguar;
import _static.Apparatus;

public class ShootingApparatus extends Apparatus {
	public static final int CONVEYOR = 0, SHOOTING = 2, LOWER = 0, UPPER = 1, POWER = 0, ANGLE = 1;
	public final Jaguar[] motors;
	public final double[] motorCoefficients;
	public ShootingApparatus(Jaguar[] motors, double[] motorCoefficients) {
		this.motors = motors;
		this.motorCoefficients = motorCoefficients;
	}
}