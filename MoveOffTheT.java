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
public class MoveOffTheT implements Action {

    boolean done = false;
    DigitalInput leftLightSensor;
    DigitalInput centerLightSensor;
    DigitalInput rightLightSensor;
    boolean hasMovedOff = false;


    public MoveOffTheT(DigitalInput left, DigitalInput center, DigitalInput right)
    {
        leftLightSensor = left;
        centerLightSensor = center;
        rightLightSensor = right;
    }
    

    // Pre: isDone() has been called beforehand
    public void act() {
        MecanumDrive.drive(.5, 0, 0);
    }

    public boolean isDone() {
        return hasGoneOffT();
    }


    public boolean hasGoneOffT()
    {
        //sensors return true when hitting the line
        //each boolean represents each sensor - names are self-explanatory
        boolean center = centerLightSensor.get();
        boolean left = leftLightSensor.get();
        boolean right = rightLightSensor.get();

        if(!left && !right && !center && !hasMovedOff)
        {
            MecanumDrive.drive(0, 0, 0);
            done = true;
            hasMovedOff = true;
        }

        return done;
    }

}
