package spatial;

import sensors.*;

public class LocationTracker extends Sensor {
	private final Accelerometer accelerometer;
	private final Gyro gyro;
	public LocationTracker(Accelerometer accelerometer, Gyro gyro) {
		super("__location-tracker".hashCode());
		this.accelerometer = accelerometer;
		this.gyro = gyro;
		thread = new LocationTrackingThread(this);
		start();
	}
	
	private static final double deltaTime = 0.050;
	private Vector acceleration = new Vector(0, 0, 0);
	private Location location = new Location(0, 0, 0), previousLocation = new Location(0, 0, 0);
	public void update() {
		// Find absolute acceleration
		double rotation = gyro.value(); // I'll assume we start at 0 // TODO don't assume that
		Vector relativeAcceleration = new Vector(accelerometer.xValue(), accelerometer.yValue(), 0.0);
		
		Vector absoluteAcceleration = relativeAcceleration.rotateHorizontal(rotation); // TODO check sign here, I believe it is positive
		
		// Figure out where we are
		Location newLocation = new Location(2 * location.x - previousLocation.x + acceleration.x * deltaTime * deltaTime,
											2 * location.y - previousLocation.y + acceleration.y * deltaTime * deltaTime,
											0);
		this.acceleration = absoluteAcceleration;
		this.previousLocation = this.location;
		this.location = newLocation;
		changed = true;
	}
	
	public Location location() {
		return location;
	}
	public double rotation() {
		return gyro.value();
	}
	
	public void correct(Location correctionLocation, Vector correctionVelocity) {
		this.location = correctionLocation;
		this.previousLocation = new Location(this.location.x - deltaTime * correctionVelocity.x, this.location.y - deltaTime * correctionVelocity.y, 0);
	}
	public void correctLocation(Location correction) {
		this.location = correction;
		this.previousLocation = new Location(this.location.x - deltaTime * (this.location.x - this.previousLocation.x), this.location.y - deltaTime *  (this.location.y - this.previousLocation.y), 0);
	}
	
	private final LocationTrackingThread thread;
	protected static class LocationTrackingThread extends Thread {
		private final LocationTracker locationTracker;
		private boolean stop = false;
		public LocationTrackingThread(LocationTracker locationTracker) {
			this.locationTracker = locationTracker;
		}
		private long delay = (long) (deltaTime * 1000.0);
		public void run() {
			while(!stop) {
				locationTracker.update();
				try {
					Thread.sleep(delay);
				} catch(InterruptedException e) {
					return;
				}
			}
		}
	}
	public void start() {
		thread.stop = false;
		thread.start();
	}
	public void stop() {
		thread.stop = true;
		thread.interrupt();
	}
	
	public short type() {
		return Sensor.Types.COMPOUND;
	}
	
	private boolean changed;
	protected void checkForEvents() {
		if(changed) {
			for(int i = 0; i < listeners.size(); i++) {
				((LocationTrackerListener) listeners.elementAt(i)).locationTracker(new LocationTrackerEvent(this, location, gyro.value()));
			}
		}
		changed = false;
	}
	public static interface LocationTrackerListener extends SensorListener {
		public void locationTracker(LocationTrackerEvent ev);
	}
	public static class LocationTrackerEvent extends SensorEvent {
		public final Location currentLocation;
		public final double currentRotation;
		public final LocationTracker source;
		public LocationTrackerEvent(LocationTracker source, Location currentLocation, double currentRotation) {
			this.source = source;
			this.currentLocation = currentLocation;
			this.currentRotation = currentRotation;
		}
	}
	protected boolean isValidListener(SensorListener listener) {
		return listener instanceof LocationTrackerListener;
	}
}