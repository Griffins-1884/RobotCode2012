package image;

import com.sun.cldc.jna.*;

import edu.wpi.first.wpilibj.image.*;

/**
 * All of the below code was borrowed from WPILibJ/src/edu/wpi/first/wpilibj/image/NIVision.java. As per the license, the FIRST BSD license is in this package folder.
 */
public class NationalInstrumentsVision {
	private static final TaskExecutor taskExecutor = new TaskExecutor("nivision task");
    private static final BlockingFunction imaqGetLastErrorFn = NativeLibrary.getDefaultInstance().getBlockingFunction("imaqGetLastError");
    static { imaqGetLastErrorFn.setTaskExecutor(taskExecutor);  }
	protected static void assertCleanStatus (int code) throws NIVisionException {
        if (code == 0) {
            throw new NIVisionException(imaqGetLastErrorFn.call0());
        }
    }
	
    private static final BlockingFunction imaqDetectRectanglesFn =
            NativeLibrary.getDefaultInstance().getBlockingFunction("imaqDetectRectangles");
    static { imaqDetectRectanglesFn.setTaskExecutor(NationalInstrumentsVision.taskExecutor); }
    private static Pointer numberOfRectanglesDetected = new Pointer(4);


    public static RectangleMatch[] detectRectangles(MonoImage image, RectangleDescriptor rectangleDescriptor,
            CurveOptions curveOptions, ShapeDetectionOptions shapeDetectionOptions,
            RegionOfInterest roi) throws NIVisionException {

        int curveOptionsPointer = 0;
        if (curveOptions != null)
            curveOptionsPointer = curveOptions.getPointer().address().toUWord().toPrimitive();
        int shapeDetectionOptionsPointer = 0;
        if (shapeDetectionOptions != null)
            shapeDetectionOptionsPointer = shapeDetectionOptions.getPointer().address().toUWord().toPrimitive();
        int roiPointer = 0;
        if (roi != null)
            roiPointer = roi.getPointer().address().toUWord().toPrimitive();

        int returnedAddress =
                imaqDetectRectanglesFn.call6(
                image.image.address().toUWord().toPrimitive(),
                rectangleDescriptor.getPointer().address().toUWord().toPrimitive(),
                curveOptionsPointer, shapeDetectionOptionsPointer,
                roiPointer,
                numberOfRectanglesDetected.address().toUWord().toPrimitive());

        try {
        	NationalInstrumentsVision.assertCleanStatus(returnedAddress);
        } catch (NIVisionException ex) {
            if (!ex.getMessage().equals("No error."))
                throw ex;
        }

        RectangleMatch[] matches = RectangleMatch.getMatchesFromMemory(returnedAddress, numberOfRectanglesDetected.getInt(0));
        NIVision.dispose(new Pointer(returnedAddress,0));
        return matches;
    }
}