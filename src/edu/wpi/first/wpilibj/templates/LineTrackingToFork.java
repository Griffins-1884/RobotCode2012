/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class LineTrackingToFork implements Action
{

   private boolean done;
   private DigitalInput centerlightSensor;
   private DigitalInput leftlightSensor;
   private DigitalInput rightlightSensor;

   public LineTrackingToFork(DigitalInput left, DigitalInput center, DigitalInput right)
   {
       leftlightSensor = left;
       centerlightSensor = center;
       rightlightSensor = right;
       done = false;
   }

   public void act()
   {
       //sensors return true when hitting the line
        //each boolean represents each sensor - names are self-explanatory
        boolean center = centerlightSensor.get();
        boolean left = leftlightSensor.get();
        boolean right = rightlightSensor.get();

       if(left && !right)
       {
           MecanumDrive.drive(0, .3, 0);
       }
       else if(right && !left)
       {
           MecanumDrive.drive(0,-.3,0);
       }
       else if(center)
       {
           MecanumDrive.drive(.3, 0, 0);
       }
       else if(right && left)
       {
          done = true;
       }
   }
   
   public boolean isDone()
   {
    return done;
   }
}
