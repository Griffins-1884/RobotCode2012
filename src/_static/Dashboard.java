package _static;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Dashboard {
	private static SmartDashboard d = new SmartDashboard();
	public static SmartDashboard d() {
		return d;
	}
}