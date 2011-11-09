/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.templates.Action;

/**
 *
 * @author michaelcw
 */
public class LeftFavoringLineTrackingAndTowerUntilLimitSwitch implements Action {

    boolean hasFinishedLineTracking = false;
    DigitalInput centerlightSensor;
    DigitalInput leftlightSensor;
    DigitalInput rightlightSensor;

    boolean hasHitTopOnce = false;
    AnalogChannel topTowerSwitch;
    Jaguar towerJaguar;
    boolean hasAlreadyHitLimitSwitch = false;
    int iterationCount = 0;


    public LeftFavoringLineTrackingAndTowerUntilLimitSwitch(DigitalInput left, DigitalInput center, DigitalInput right, Jaguar myTowerJaguar, AnalogChannel topLimitSwitch)
    {
        leftlightSensor = left;
        centerlightSensor = center;
        rightlightSensor = right;
        
        towerJaguar = myTowerJaguar;
        topTowerSwitch = topLimitSwitch;
    }

    // Act method carries out the process of this action
    // And returns when the action has been carried out
    public void act()
    {
        //sensors return true when hitting the line
        //each boolean represents each sensor - names are self-explanatory
        boolean center = centerlightSensor.get();
        boolean left = leftlightSensor.get();
        boolean right = rightlightSensor.get();

        // If it reaches a fork, take the left route
        if (right&&left && !center)
        {
            iterationCount = 0;
            MecanumDrive.drive(0, 0, -.3);
        }
        //if it reaches a T(all sensors returning true) stop
        else if(right && left && center)
        {
            iterationCount = 0;
            MecanumDrive.drive(0, 0, 0);
            hasFinishedLineTracking = true;
        }
        //if it's the right, turn clockwise and forward to readjust
        else if (right)
        {
            iterationCount = 0;
            MecanumDrive.drive(.3, 0, .3);
        }
        //If it's the left, turn counterclockwise and forward to readjust
        else if(left)
        {
            iterationCount = 0;
            MecanumDrive.drive(.3, 0, -.3);
        }
        //If it's only the center, just move forward.
        else if(center)
        {
            iterationCount = 0;
            MecanumDrive.drive(0.65, 0, 0);
        }
        //If it loses all sensors, circle LEFT until you find one
        else
        {
            iterationCount ++;
            MecanumDrive.drive(.3, 0, -.27);
        }
        
        if(iterationCount >= 250)
        { // if it's been turning around for too long, disable this step of autonomous
            hasFinishedLineTracking = true;
            hasHitTopOnce = true;
        }

        if(!hasHitTopOnce())
        {
            towerJaguar.set(-0.75);
        }
    }

    // Returns true when the action is hasFinishedLineTracking
    public boolean isDone()
    {
        return hasFinishedLineTracking && hasHitTopOnce();
    }

    public boolean hasHitTopOnce()
    {
        if(topTowerSwitch.getVoltage() >= 4.5 && !hasAlreadyHitLimitSwitch)
        {
            hasHitTopOnce = true;
            towerJaguar.set(0.0);
            hasAlreadyHitLimitSwitch = true;
        }
        return hasHitTopOnce;
    }
}
