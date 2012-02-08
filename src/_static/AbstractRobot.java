package _static;

import driveSystems.DriveSystem;

/**
 * A class representing a robot.
 * 
 * Every Robot has a drive system, and a collection of apparatuses.
 * 
 * @author Colin Poler=
 */
public abstract class AbstractRobot {
	/**
	 * The robot's drive system.
	 */
	public final DriveSystem driveSystem;
	
	/**
	 * Constructs a robot with the specified drive system, and apparatuses.
	 * 
	 * @param driveSystem The drive system to use.
	 * @param apparatuses The apparatuses to use.
	 */
	public AbstractRobot(DriveSystem driveSystem) {
		this.driveSystem = driveSystem;
	}
}