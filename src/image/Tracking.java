package image;

import _static.Location;
import _static.Vector;

import com.sun.squawk.util.MathUtils;


public class Tracking {
	public static final double targetHeight = 0.4572; // target height in meters
	public static final double targetWidth = 0.6096; // target width in meters
	public static final double focalLength = 0.0004; // Axis 206 camera focal length in meters
	public static final double cameraFOV = 48.0*Math.PI/180.0; // 54 degrees in radians. A doc suggested 48 degrees is better
	public static final double imageHeightPixels = 240.0;
	public static final double imageWidthPixels = 320.0;
	
	// Heights of centers of rectangle (in meters) relative to the camera
	// Black rectangle has 22 inch height
	public static final double kInch = 0.0254; // an inch in meters
	public static final double blackRectangleHeight = 22*kInch;

	public static final double CAMERA_HEIGHT = 18*kInch;
	public static final double BOTTOM_ELEVATION = 28*kInch + blackRectangleHeight/2.0 - CAMERA_HEIGHT;
	public static final double MIDDLE_ELEVATION = 61*kInch + blackRectangleHeight/2.0 - CAMERA_HEIGHT;
	public static final double TOP_ELEVATION = 98*kInch + blackRectangleHeight/2.0 - CAMERA_HEIGHT;
	
	
	public static final Vector getVectorToTarget(RectangleMatch rect, Location rectangleLocation) {
		double imageWidth = Math.tan(cameraFOV / 2.0) * focalLength * 2.0, imageHeight = imageWidth * imageHeightPixels / imageWidthPixels;
		
		double rectangleWidthOnSensor = rect.boundingRectWidth * imageWidth / imageWidthPixels, rectangleHeightOnSensor = rect.boundingRectHeight * imageHeight / imageHeightPixels;
		
		double angleOfElevation = MathUtils.asin(2.0 * (rectangleLocation.z - CAMERA_HEIGHT) * rectangleHeightOnSensor / (targetHeight * focalLength)) / 2.0;
		
		double distanceToTarget = (rectangleLocation.z - CAMERA_HEIGHT) / Math.sin(angleOfElevation);
		
		double angleOfRectangle = MathUtils.acos(distanceToTarget) * rectangleWidthOnSensor / (focalLength * targetWidth);
		if(rect.center_mass_x_normalized < 0) { // TODO fix this bug
			angleOfRectangle *= -1;
		}
		
		double horizontalDistance = distanceToTarget * Math.cos(angleOfElevation);
		
		double yDistance = distanceToTarget * Math.sin(angleOfRectangle);
		double horizontalAngleToTarget = MathUtils.acos(yDistance / horizontalDistance);
		double xDistance = horizontalDistance * Math.sin(horizontalAngleToTarget);
		
		System.out.println("X distance: " + xDistance + " meters");
		System.out.println("Y distance: " + yDistance + " meters");
		
		// Don't subtract camera height for this location vector. Keep height off floor
		return new Vector(horizontalDistance, 0, rectangleLocation.z);
	}
	public static final Location getRobotLocation(RectangleMatch rect, Location rectangleLocation) {
		Vector v = getVectorToTarget(rect, rectangleLocation);
		
		if(rectangleLocation.x < 0) {
			return new Location(rectangleLocation.x + v.x, rectangleLocation.y - v.y, CAMERA_HEIGHT);
		} else {
			return new Location(rectangleLocation.x - v.x, rectangleLocation.y + v.y, CAMERA_HEIGHT);
		}
	}
	public static final double getHorizontalAngleToRectangle(RectangleMatch rect) {
		return MathUtils.atan2(Math.tan(cameraFOV / 2.0) * focalLength * rect.center_mass_x, focalLength);
	}
	public static final double getVerticalAngleToRectangle(RectangleMatch rect, double cameraAngle) {
		return MathUtils.atan2(Math.tan(cameraFOV / 2.0) * focalLength * imageHeightPixels / imageWidthPixels * rect.center_mass_y, focalLength) + cameraAngle;
	}
}