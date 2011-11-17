/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author michaelcw
 */
public class RightFavoringTrackToForkAndTowerUp implements Action {

    boolean hasReachedFork = false;
    boolean hasFinishedLineTracking = false;
    boolean hasHitTopOnce = false;
    DigitalInput centerlightSensor;
    DigitalInput leftlightSensor;
    DigitalInput rightlightSensor;
    AnalogChannel topTowerSwitch;
    Jaguar towerJaguar;
    boolean hasAlreadyHitLimitSwitch = false;

    boolean hasActedBefore = false;

    int iterationCount;


    public RightFavoringTrackToForkAndTowerUp(DigitalInput left, DigitalInput center, DigitalInput right, Jaguar myTowerJaguar, AnalogChannel topLimitSwitch)
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
        if(!hasActedBefore)
        {
            iterationCount = 0;
            hasActedBefore = true;
        }

        //sensors return true when hitting the line
        //each boolean represents each sensor - names are self-explanatory
        boolean center = centerlightSensor.get();
        boolean left = leftlightSensor.get();
        boolean right = rightlightSensor.get();

        System.out.println("\nLeft: " + left + "\nCenter: " + center + "\nRight: " + right);

        // If it reaches a fork, take the right route
        if (right&&left && !center)
        {
            iterationCount = 0;
            hasReachedFork = true;
            MecanumDrive.drive(0, 0,.3);
        }
        //if it reaches a T (all sensors returning true) stop AS A FAILSAFE
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
            MecanumDrive.drive(.23, 0, .3);
        }
        //If it's the left, turn counterclockwise and forward to readjust
        else if(left)
        {
            iterationCount = 0;
            MecanumDrive.drive(.23, 0, -.3);
        }
        //If it's only the center, just move forward.
        else if(center)
        {
            iterationCount = 0;
            MecanumDrive.drive(0.62, 0, 0);

            if(hasReachedFork)
            {
                hasFinishedLineTracking = true; // finish when we have reached the fork and gotten on one of the fork's legs
                MecanumDrive.drive(0, 0, 0);
            }
        }
        //If it loses all sensors, circle RIGHT until you find one
        else
        {
            iterationCount ++;
            MecanumDrive.drive(.3, 0, .27);
        }

        if(iterationCount >= 250)
        { // if it's been turning around for too long, disable autonomous
            hasFinishedLineTracking = true;
            hasHitTopOnce = true;
        }

        if(!hasHitTopOnce())
        {
            towerJaguar.set(-0.75);
        }
    }

    // Returns true when the action hasFinishedLineTracking
    public boolean isDone()
    {
        return hasFinishedLineTracking;
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
