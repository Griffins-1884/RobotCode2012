package image;

import edu.wpi.first.wpilibj.image.*;

public class RectangleTrackingImage {
	public static RectangleMatch[] track(ColorImage image) throws NIVisionException {
		return track(image.getLuminancePlane());
	}
	public static RectangleMatch[] track(MonoImage image) throws NIVisionException {
		return (new RectangleTrackingImage(image)).detectRectangles(new RectangleDescriptor(15, 320, 15, 240), new CurveOptions(0,40,1,25,15,15,10,1,0), new ShapeDetectionOptions(ShapeDetectionOptions.IMAQ_GEOMETRIC_MATCH_SHIFT_INVARIANT,0,0,75,125,500), null);
	}
	private MonoImage image;
	private RectangleTrackingImage derivative;
	public RectangleTrackingImage(ColorImage image) throws NIVisionException {
		this.image = image.getLuminancePlane();
	}
	public RectangleTrackingImage(MonoImage image) throws NIVisionException {
		this.image = image;
	}
	public RectangleMatch[] detectRectangles(RectangleDescriptor rectangleDescriptor, CurveOptions curveOptions, ShapeDetectionOptions shapeDetectionOptions, RegionOfInterest roi) throws NIVisionException {
		return NationalInstrumentsVision.detectRectangles(this.threshold(168, 255).convexHull().image, rectangleDescriptor, curveOptions, shapeDetectionOptions, roi);
	}
	public RectangleTrackingImage threshold(float min, float max) throws NIVisionException {
		if(derivative != null) {
			derivative.free();
		}
		derivative = new RectangleTrackingImage(new MonoImage());
		NationalInstrumentsVision.threshold(derivative.image.image, image.image, min, max, 1, Float.floatToIntBits((float) 255.0));
		return derivative;
	}
	public RectangleTrackingImage convexHull() throws NIVisionException {
		if(derivative != null) {
			derivative.free();
		}
		derivative = new RectangleTrackingImage(new MonoImage());
		NationalInstrumentsVision.convexHull(derivative.image.image, image.image, 8);
		return derivative;
	}
	public void free() throws NIVisionException {
		derivative.free();
		image.free();
	}
}