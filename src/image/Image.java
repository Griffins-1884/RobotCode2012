package image;

import edu.wpi.first.wpilibj.image.*;

public class Image {
	public final MonoImage image;
	public Image(MonoImage image) {
		this.image = image;
	}
	public final RectangleMatch[] detectRectangles(RectangleDescriptor rectangleDescriptor, CurveOptions curveOptions, ShapeDetectionOptions shapeDetectionOptions, RegionOfInterest roi) throws NIVisionException {
		return NationalInstrumentsVision.detectRectangles(image, rectangleDescriptor, curveOptions, shapeDetectionOptions, roi);
	}
}