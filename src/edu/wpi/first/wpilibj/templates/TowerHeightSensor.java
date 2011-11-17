/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author bhasinl
 */
public class TowerHeightSensor {

    Encoder enc;
    public static final double INCHES_PER_ENCODER_TICK = TowerMovementForDistanceChange.INCHES_PER_ENCODER_TICK;
    public static final double START_OFF_VALUE =  TowerMovementForDistanceChange.START_OFF_VALUE; // this is the y-intercept in inches
    public static final double INCHES_Y_INTERCEPT = TowerMovementForDistanceChange.INCHES_Y_INTERCEPT;

    public TowerHeightSensor(Encoder myEnc)
    {
        enc = myEnc;
    }


    // Returns height of tower in inches
    public double getHeight()
    {
        return convertEncoderToAbsoluteHeight(enc.get());
    }


    public double convertEncoderToAbsoluteHeight(int encoderCount)
    {
        // For experimental data

        /*
         *
         * (CHANGE THIS IN TOWERMOVEMENTFORENCODERCHANGE AS WELL)
         *
         */


        // Inches = encoder value * constant + y-intercept
        // Encoder value = (Inches - y-intercept)/constant

        return encoderCount * INCHES_PER_ENCODER_TICK + START_OFF_VALUE + INCHES_Y_INTERCEPT;
    }
}
