package Y2012.shooting;

import sensors.Camera;
import sensors.Sensor;

/**
 * A camera that tracks rectangles behind basketball hoops.
 * 
 * @author Colin Poler
 */
public class RectangleCamera extends Sensor {
	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 * @author Colin Poler
	 */
	public static interface RectangleCameraListener extends SensorListener {
		public void rectangleCamera(RectangleCameraEvent ev);
	}
	
	/**
	 * A SensorEvent for RectangleCameras.
	 * 
	 * @author Colin Poler
	 */
	public static class RectangleCameraEvent extends SensorEvent {
		
		/**
		 * The sensor firing this event.
		 */
		public final RectangleCamera source;
		
		/**
		 * Constructs an RectangleCamera from the specified source, value, and delta value.
		 * 
		 * @param source The source of the event.
		 */
		public RectangleCameraEvent(RectangleCamera source) {
			this.source = source;
		}
	}
	
	public final Camera camera;
	
	/**
	 * Constructs a Camera with the specified ID and address.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public RectangleCamera(long sensorId, Camera camera) {
		super(sensorId);
		this.camera = camera;
	}
	
	/**
	 * Checks if the sensor should notify its listeners.
	 */
	protected void checkForEvents() {
		// TODO implement
	}
	
	/**
	 * Determines the type of the sensor. It should just return a value from Sensor.Types, no funny business.
	 * 
	 * @return The type of the sensor.
	 */
	public short type() {
		return Sensor.Types.SHAPE_TRACKING_CAMERA;
	}
	
	/**
	 * Fires a RectangleCameraEvent to all of the listeners.
	 * 
	 * @param ev The CameraEvent to be fired.
	 */
	protected void fireEvent(RectangleCameraEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((RectangleCameraListener) listeners.elementAt(i)).rectangleCamera(ev);
		}
	}
	
	/**
	 * Checks if the SensorListener is a listener for the actual sensor.
	 * 
	 * @param listener The SensorListener to check
	 * @return True if it is valid, false if not.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return listener instanceof RectangleCameraListener;
	}
}