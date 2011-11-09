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

public class ArmActor {

    Jaguar motor;
    AnalogChannel bottomSwitch;
    AnalogChannel topSwitch;
    boolean usingLimitSwitches = true;
    double direction;

    // Direction constants
    public static final int UP = RobotTemplate.UP;
    public static final int NOTHING = RobotTemplate.NOTHING;
    public static final int DOWN = RobotTemplate.DOWN;


    // Testing purposes
    int iterationCount = 0;


    public ArmActor(Jaguar myMotor, AnalogChannel aBottom, AnalogChannel aTop)
    {
        motor = myMotor;
        bottomSwitch = aBottom;
        topSwitch = aTop;
        direction = 0; // assume still initially
    }


    // 0 means do nothing, -1 means down, 1 means up
    public void setDirectionTo(double up)
    {
        direction = up;
    }


    public void act()
    {
        iterationCount ++;
        /*if(iterationCount%50 == 0)
           {
            System.out.println("Has hit bottom: " + hasHitBottom());
            System.out.println("Has hit top: " + hasHitTop());
            System.out.println("Direction: " + direction);
        }*/



        if(direction <= 0.0 && (!usingLimitSwitches || !hasHitBottom())) // move down
        {
            motor.set(direction); // gravity to factor in
        }
        else if(direction >= 0.0 && (!usingLimitSwitches || !hasHitTop())) // move up
        {
            motor.set(direction);
        }
        else if(hasHitTop() || hasHitBottom())
        {
            motor.set(0.0);
        }
            

    }


    public boolean hasHitTop()
    {
        return topSwitch.getVoltage() >= 4.5;
    }

    public boolean hasHitBottom()
    {
        return bottomSwitch.getVoltage() >= 4.5;
    }


}
