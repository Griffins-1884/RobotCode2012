package spatial;

import spatial.LocationTracker.LocationTrackerEvent;
import spatial.LocationTracker.LocationTrackerListener;
import actions.Interval;
import actions.MultiAction;
import driveSystems.DriveSystem;

public class Goto extends DriveSystem.DrivingAction implements LocationTrackerListener {
	public static final double DISTANCE_TOLERANCE = 0.1, RADIAL_TOLERANCE = Math.PI / 24;
	
	private final Location destination;
	private final double targetAngle;
	private final LocationTracker tracker;
	private final boolean holomonic; // If true, we move and rotate simultaneously, if false, we turn, drive, then turn again
	public Goto(Location destination, LocationTracker tracker, DriveSystem driveSystem, MultiAction parent) { // If no angle specified, then just go to the final location
		super(driveSystem, parent);
		this.destination = destination;
		if((driveSystem.capabilities() & DriveSystem.LEFT_RIGHT_MOTION) > 0) {
			this.targetAngle = tracker.rotation();
		} else {
			this.targetAngle = tracker.location().directionTo(destination);
		}
		this.tracker = tracker;
		this.holomonic = (driveSystem.capabilities() & DriveSystem.LEFT_RIGHT_MOTION) > 0;
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
		if(ev.currentLocation.distanceTo(destination) < DISTANCE_TOLERANCE && Math.abs(ev.currentRotation - targetAngle) < RADIAL_TOLERANCE) {
			stop();
		} else if(holomonic) {
			// TODO figure out rotation and vector to move towards target
		} else {
			// TODO figure out rotation towards target, then moving, then turning
		}
	}
}