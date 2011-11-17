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
public class LineTrackingFromForkCenter implements Action
{

   private boolean done;
   private DigitalInput centerlightSensor;
   private DigitalInput leftlightSensor;
   private DigitalInput rightlightSensor;
   private Gyro g;

   public LineTrackingFromForkCenter(DigitalInput left, DigitalInput center, DigitalInput right, Gyro g)
   {
       leftlightSensor = left;
       centerlightSensor = center;
       rightlightSensor = right;
       done = false;
       this.g = g;
   }

   public void act()
   {
       //sensors return true when hitting the line
        //each boolean represents each sensor - names are self-explanatory
        boolean center = centerlightSensor.get();
        boolean left = leftlightSensor.get();
        boolean right = rightlightSensor.get();

       if(left && right && !center)
       {
           MecanumDrive.drive(.3, 0, 0);
       }
       else if(!right && !left && !center)
       {
          MecanumDrive.drive(.3, 0, 0);
       }
       else if(left && right && center)
       {
           if(g.getAngle()%360 > 1 || g.getAngle()%360 > 1)
           {
                MecanumDrive.drive(0, 0, .3);
           }
           else
           {
               MecanumDrive.drive(0,-.3,0);
           }

       }
       else if(left && !right && !center)
       {
         done = true;
       }

   }

   public boolean isDone()
   {
    return done;
   }
}
