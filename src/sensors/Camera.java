package sensors;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 * A camera (no shape tracking).
 * 
 * @author Colin Poler
 */
public class Camera extends Sensor {
	/**
	 * An interface that should be implemented by all listeners.
	 * 
	 * @author Colin Poler
	 */
	public static interface CameraListener extends SensorListener {
		public void camera(CameraEvent ev);
	}
	
	/**
	 * A SensorEvent for Cameras.
	 * 
	 * @author Colin Poler
	 */
	public static class CameraEvent extends SensorEvent {
		/**
		 * The image of the camera.
		 */
		public final ColorImage image;
		
		/**
		 * The sensor firing this event.
		 */
		public final Camera source;
		
		/**
		 * Constructs an CameraEvent from the specified source, value, and delta value.
		 * 
		 * @param source The source of the event.
		 * @param image The most recent image taken by the camera.
		 */
		public CameraEvent(Camera source, ColorImage image) {
			this.source = source;
			this.image = image;
		}
	}
	
	private final AxisCamera camera;
	
	/**
	 * Constructs a Camera with the specified ID and address.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public Camera(long sensorId, String address) {
		super(sensorId);
		camera = AxisCamera.getInstance(address);
	}
	
	/**
	 * Checks if the sensor should notify its listeners.
	 */
	protected void checkForEvents() {
		if(camera.freshImage()) {
			fireEvent(new CameraEvent(this, image()));
		}
	}
	
	/**
	 * Determines the type of the sensor. It should just return a value from Sensor.Types, no funny business.
	 * 
	 * @return The type of the sensor.
	 */
	public short type() {
		return Sensor.Types.CAMERA;
	}
	
	/**
	 * Gets the value of the sensor.
	 * 
	 * @return The value of the sensor.
	 */
	public ColorImage image() {
		try {
			return camera.getImage();
		} catch(AxisCameraException e) {
			e.printStackTrace();
		} catch(NIVisionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Fires a CameraEvent to all of the listeners.
	 * 
	 * @param ev The CameraEvent to be fired.
	 */
	protected void fireEvent(CameraEvent ev) {
		for(int i = 0; i < listeners.size(); i++) {
			((CameraListener) listeners.elementAt(i)).camera(ev);
		}
	}
	
	/**
	 * Checks if the SensorListener is a listener for the actual sensor.
	 * 
	 * @param listener The SensorListener to check
	 * @return True if it is valid, false if not.
	 */
	protected boolean isValidListener(SensorListener listener) {
		return listener instanceof CameraListener;
	}
}