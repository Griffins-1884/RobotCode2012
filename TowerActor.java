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

public class TowerActor {

    Jaguar motor;
    AnalogChannel bottomSwitch;
    AnalogChannel topSwitch;
    int direction;
    boolean usingLimitSwitches = true;
    double towerHeight;

    // Direction constants
    public static final int UP = RobotTemplate.UP;
    public static final int NOTHING = RobotTemplate.NOTHING;
    public static final int DOWN = RobotTemplate.DOWN;


    public TowerActor(Jaguar myMotor, AnalogChannel aBottom, AnalogChannel aTop, Encoder myEnc)
    {
        motor = myMotor;
        bottomSwitch = aBottom;
        topSwitch = aTop;
        direction = 0; // assume still initially
    }


    // 0 means do nothing, -1 means down, 1 means up
    public void setDirectionTo(int up)
    {
        direction = up;
    }


    // Used in both teleop and autonomous (through PlayList in autonomous)
    // Precondition in autonomous: we're not done yet
    public void act()
    {
        // Negative is UP!!!!!!!!

        if(direction == UP && (!usingLimitSwitches || !hasHitTop()))
        {
            motor.set(-0.75);
        }

        if(direction == DOWN && (!usingLimitSwitches || !hasHitBottom()))
        {
            motor.set(0.75);
        }

        if(direction == NOTHING)
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
