/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author bhasinl
 */
public class ForwardsMovementTimeWise implements Action {

    boolean hasAlreadyActed = false;
    boolean hasFinished = false;
    long timeTarget;
    int duration;


    public ForwardsMovementTimeWise(int milliseconds)
    {
        duration = milliseconds;
    }

    public void act() {
        if(!hasAlreadyActed)
        {
            timeTarget = System.currentTimeMillis() + duration;
            hasAlreadyActed = true;
        }

        MecanumDrive.drive(0.3, 0, 0);
    }

    public boolean isDone() {
        if(System.currentTimeMillis() >= timeTarget && !hasFinished && hasAlreadyActed)
        {
            MecanumDrive.drive(0, 0, 0);
            hasFinished = true;
        }

        return hasFinished;
    }

}
