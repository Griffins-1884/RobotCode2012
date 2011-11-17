/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author michaelcw
 */

public class ArmAction implements Action {

    Jaguar motor;
    double direction;
    boolean hasAlreadyActed = false;
    boolean hasFinished = false;

    // Direction constants
    public static final int UP = RobotTemplate.UP;
    public static final int NOTHING = RobotTemplate.NOTHING;
    public static final int DOWN = RobotTemplate.DOWN;

    long timeTarget;
    int duration;

    // Testing purposes


    public ArmAction(Jaguar myMotor, int dir, int milliseconds)
    {
        motor = myMotor;
        direction = dir; // assume still initially
        duration = milliseconds;
    }


    public void act()
    {
        if(!hasAlreadyActed)
        {
            timeTarget = System.currentTimeMillis() + duration;
            hasAlreadyActed = true;
        }

        if(direction <= 0.0) // move down
        {
            motor.set(-0.2); // gravity to factor in
        }
        else if(direction >= 0.0) // move up
        {
            motor.set(0.78);
        }

    }

    public boolean isDone()
    {
        if(System.currentTimeMillis() >= timeTarget && !hasFinished && hasAlreadyActed)
        {
            motor.set(0.0);
            hasFinished = true;
        }

        return hasFinished;
    }


}
