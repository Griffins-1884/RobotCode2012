package Y2012;

import image.NationalInstrumentsVision;
import image.RectangleDescriptor;
import image.RectangleMatch;
import edu.wpi.first.wpilibj.image.*;

public class RectangleTrackingImage {
	private MonoImage image;
	private RectangleTrackingImage derivative;
	public RectangleTrackingImage(ColorImage image) throws NIVisionException {
		this.image = image.getLuminancePlane();
	}
	public RectangleMatch[] detectRectangles(RectangleDescriptor rectangleDescriptor, CurveOptions curveOptions, ShapeDetectionOptions shapeDetectionOptions, RegionOfInterest roi) throws NIVisionException {
		return NationalInstrumentsVision.detectRectangles(this.threshold(239, 255).convexHull().image, rectangleDescriptor, curveOptions, shapeDetectionOptions, roi);
	}
	public RectangleTrackingImage threshold(float min, float max) throws NIVisionException {
		if(derivative != null) {
			derivative.free();
		}
	}
	public RectangleTrackingImage convexHull() throws NIVisionException {
		if(derivative != null) {
			derivative.free();
		}
	}
	public void free() throws NIVisionException {
		derivative.free();
		image.free();
	}
}