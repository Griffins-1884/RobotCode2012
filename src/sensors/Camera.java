package sensors;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCamera.ResolutionT;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;

import image.RectangleMatch;

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
	private CriteriaCollection cc;      // the criteria for doing the particle filter operation
	
	/**
	 * Constructs a Camera with the specified ID and address.
	 * 
	 * @param sensorId The ID of the sensor.
	 */
	public Camera(long sensorId, String address) {
		super(sensorId);
		if(address != null) {
			camera = AxisCamera.getInstance(address);
		} else {
			camera = AxisCamera.getInstance();
		}
		camera.writeResolution(ResolutionT.k320x240);
		camera.writeCompression(30);
		
		Timer.delay(3); // Sometimes, the cRIO starts before the camera so we have to put in a wait
		
		cc = new CriteriaCollection();      // create the criteria for the particle filter
		cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
		cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
	}
	
	/**
	 * Checks if the sensor should notify its listeners.
	 */
	protected void checkForEvents() {
		if(camera.freshImage()) {
			try {
				fireEvent(new CameraEvent(this, image()));
			} catch(AxisCameraException e) {} catch(NIVisionException e) {}
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
	public ColorImage image() throws AxisCameraException, NIVisionException {
		return camera.getImage();
	}
	
	/**
	 * Gets the CriteriaCollection for vision tracking.
	 * 
	 * @return The CriteriaCollection object.
	 */
	public CriteriaCollection cc() {
		return cc;
	}
	
	public RectangleMatch[] trackRectangles() throws AxisCameraException, NIVisionException {
		ColorImage colorImage = image();
		BinaryImage thresholdImage = colorImage.thresholdHSL(0, 255, 0, 63, 175, 255);	// Get only areas of a certain brightness
		BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);			// Remove smaller objects
		BinaryImage convexHullImage = bigObjectsImage.convexHull(false);					// Fill in damaged rectangles
		BinaryImage filteredImage = convexHullImage.particleFilter(cc());					// Find rectangles
		
		RectangleMatch[] result = new RectangleMatch[NIVision.countParticles(filteredImage.image)];
		for(int i = 0; i < result.length; i++) {
			result[i] = new RectangleMatch(filteredImage, i);
		}
		
		colorImage.free();
		thresholdImage.free();
		bigObjectsImage.free();
		convexHullImage.free();
		filteredImage.free();
		
		return result;
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