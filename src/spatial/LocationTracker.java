package spatial;

import sensors.*;

public class LocationTracker {
	private final Accelerometer accelerometer;
	private final Gyro gyro;
	public LocationTracker(Accelerometer accelerometer, Gyro gyro) {
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
	}
	
	public Location location() {
		return location;
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
}