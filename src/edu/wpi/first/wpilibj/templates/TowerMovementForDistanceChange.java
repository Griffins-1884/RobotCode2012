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
public class TowerMovementForDistanceChange implements Action {

    Encoder enc;
    Jaguar towerJaguar;
    int targetEncoder;
    boolean done = false;
    boolean hasMadeItBefore = false;
    boolean hasActedBefore = false;
    double inchesToTravelUp;
    public static final double INCHES_PER_ENCODER_TICK = 0.04333676;
    public static final double INCHES_Y_INTERCEPT = 2.93307; // this is the y-intercept in inches
    public static final double START_OFF_VALUE = 39.37; 
    

    // inchesToTravel can be negative (go down) or positive (go up)
    public TowerMovementForDistanceChange(Jaguar myTowerJaguar, Encoder myEnc, double inchesToTravel)
    {
        towerJaguar = myTowerJaguar;
        enc = myEnc;
        inchesToTravelUp = inchesToTravel;
    }


    // Assume more positive is upwards
    // Pre: isDone() has been called
    public void act() {
        if(!hasActedBefore)
        {
            targetEncoder = enc.get() + (int)(convertDistanceToEncoderDelta(inchesToTravelUp));
            hasActedBefore = true;
        }

        if(enc.get() < targetEncoder)
            towerJaguar.set(-0.6); // NEGATIVE IS UP
        else if(enc.get() > targetEncoder)
            towerJaguar.set(0.6); // POSITIVE IS DOWN

        if(Math.abs(targetEncoder - enc.get()) <= 15 && !hasMadeItBefore) // assume 15 is within range
        {
            towerJaguar.set(0.0); // this only happens once at the end
        }
    }


    // isDone is called before act()
    public boolean isDone() {
        return hasMadeItOnce();
    }
    

    public boolean hasMadeItOnce()
    {
        if(Math.abs(targetEncoder - enc.get()) <= 15 && !hasMadeItBefore) // assume 15 is within range
        {
            towerJaguar.set(0.0); // this only happens once at the end
            done = true;
            hasMadeItBefore = true;
        }

        return done;
    }


    public double convertDistanceToEncoderDelta(double inches)
    {
        // For experimental data

        /* 
         *
         * (CHANGE THIS IN TOWERHEIGHTSENSOR AS WELL)
         *
         */

        // Inches = encoder value * constant + y-intercept
        // Encoder value = (Inches - y-intercept)/constant
        return (inches)/INCHES_PER_ENCODER_TICK;
    }

}
