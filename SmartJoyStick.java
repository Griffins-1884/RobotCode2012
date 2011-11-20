/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Suryansh and Alonso AKA The Drive Team
 */

package edu.wpi.first.wpilibj.templates;
import  edu.wpi.first.wpilibj.Joystick;

public class SmartJoyStick extends Joystick
{
    public SmartJoyStick(int pwm)
    {
       super(pwm);

    }
    public double getSmartY()
    {
        return super.getY() * getSmartZ();
    }

    public double getSmartX()
    {
        return super.getX() * getSmartZ();
    }
    public double getSmartZ()
    {
        return ((-(super.getZ())+1)/2)+0.4;
    }
}
