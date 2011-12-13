package _static;

import driveSystems.DriveSystem;

public abstract class AbstractRobot {
	public final DriveSystem driveSystem;
	public final Apparatus[] apparatuses;
	public AbstractRobot(DriveSystem driveSystem, Apparatus[] apparatuses) {
		this.driveSystem = driveSystem;
		this.apparatuses = apparatuses;
	}
}