package spatial;

import spatial.LocationTracker.LocationTrackerEvent;
import spatial.LocationTracker.LocationTrackerListener;
import actions.Interval;
import actions.MultiAction;
import driveSystems.DriveSystem;
import driveSystems.Movement;

public class Goto extends DriveSystem.DrivingAction implements LocationTrackerListener {
	public static final double DISTANCE_TOLERANCE = 0.1, RADIAL_TOLERANCE = Math.PI / 24;
	
	private final Location destination;
	private final double targetAngle;
	private final LocationTracker tracker;
	private final boolean holomonic; // If true, we move and rotate simultaneously, if false, we turn, drive, then turn again
	public Goto(Location destination, LocationTracker tracker, DriveSystem driveSystem, MultiAction parent) { // If no angle specified, then just go to the final location
		super(driveSystem, parent);
		this.destination = destination;
		this.tracker = tracker;
		this.holomonic = (driveSystem.capabilities() & DriveSystem.LEFT_RIGHT_MOTION) > 0;
		if(holomonic) {
			this.targetAngle = tracker.rotation();
		} else {
			this.targetAngle = tracker.location().directionTo(destination);
		}
	}
	public Goto(Location destination, double targetAngle, LocationTracker tracker, DriveSystem driveSystem, MultiAction parent) {
		super(driveSystem, parent);
		this.destination = destination;
		this.targetAngle = targetAngle;
		this.tracker = tracker;
		this.holomonic = (driveSystem.capabilities() & DriveSystem.LEFT_RIGHT_MOTION) > 0;
	}
	protected void act() {
		tracker.addListener(this);
	}
	protected void _destroy() {
		tracker.removeListener(this);
	}
	public Interval duration() {
		return new Interval(10000); // TODO I have no idea
	}
	public void locationTracker(LocationTrackerEvent ev) {
		if(ev.currentLocation.distanceTo(destination) < DISTANCE_TOLERANCE && Math.abs(shortestRotation(ev.currentRotation, targetAngle)) < RADIAL_TOLERANCE) {
			stop();
		} else if(holomonic) {
			double rotation = shortestRotation(ev.currentRotation, targetAngle);
			Vector movement = ev.currentLocation.vectorTo(destination).rotateHorizontal(-ev.currentRotation);
			driveSystem.move(new Movement(movement, rotation / Math.PI)); // Move to destination and turn towards target angle
		} else {
			if(ev.currentLocation.distanceTo(destination) < DISTANCE_TOLERANCE) {
				double rotation = shortestRotation(ev.currentRotation, targetAngle);
				driveSystem.move(new Movement(new Vector(0, 0, 0), rotation / Math.PI)); // Turn towards target rotation
			} else {
				double driveAngle = ev.currentLocation.directionTo(destination), shortestAngle = shortestRotation(ev.currentRotation, driveAngle);
				if(Math.abs(shortestAngle) < RADIAL_TOLERANCE) {
					driveSystem.move(new Movement(new Vector(0, 0, 0), shortestAngle / Math.PI)); // Turn towards destination
				} else {
					driveSystem.move(new Movement(new Vector(1, 0, 0), 0)); // Go forward
				}
			}
		}
	}
	protected static double shortestRotation(double currentAngle, double targetAngle) {
		while(currentAngle <= 0) {
			currentAngle += 2 * Math.PI;
		}
		while(currentAngle > 2 * Math.PI) {
			currentAngle -= 2 * Math.PI;
		}
		while(targetAngle <= 0) {
			targetAngle += 2 * Math.PI;
		}
		while(targetAngle > 2 * Math.PI) {
			targetAngle -= 2 * Math.PI;
		}
		double firstAngle = targetAngle - currentAngle, secondAngle = firstAngle + 2 * Math.PI, thirdAngle = firstAngle - 2 * Math.PI;
		if(Math.abs(firstAngle) < Math.min(Math.abs(secondAngle), Math.abs(thirdAngle))) {
			return firstAngle;
		} else if(Math.abs(secondAngle) < Math.abs(thirdAngle)) {
			return secondAngle;
		} else {
			return thirdAngle;
		}
	}
}