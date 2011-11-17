/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

/**
 * A color image represented in HSL color space at 3 bytes per pixel.
 * @author dtjones
 */
public class HSLImage extends ColorImage {

    /**
     * Create a new 0x0 image.
     */
    public HSLImage() throws NIVisionException{
        super(NIVision.ImageType.imaqImageHSL);
    }

    HSLImage(HSLImage sourceImage) {
        super(sourceImage);
    }

    /**
     * Create a new image by loading a file.
     * @param fileName The path of the file to load.
     */
    public HSLImage(String fileName) throws NIVisionException {
        super(NIVision.ImageType.imaqImageHSL);
        NIVision.readFile(image, fileName);
    }
}
