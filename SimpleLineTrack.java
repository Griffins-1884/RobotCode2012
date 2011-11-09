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
public class SimpleLineTrack implements Action {

    boolean done;
    int state;
    // 0 forward until find end line
    // 1 back for a little bit
    // 2 strafe until center on a line
    // 3 forward until on end line
    double direction;
    DigitalInput centerlightSensor;
    DigitalInput leftlightSensor;
    DigitalInput rightlightSensor;

    public SimpleLineTrack(DigitalInput left, DigitalInput center, DigitalInput right) {
        leftlightSensor = left;   // TRUE IS ON LINE
        centerlightSensor = center;
        rightlightSensor = right;
        direction = 0.5; //determines whether robot goes left or right, and how quickly
        done = false;
        state = 0;
    }

    public SimpleLineTrack(DigitalInput left, DigitalInput center, DigitalInput right, double dir) {
        leftlightSensor = left;   // TRUE IS ON LINE
        centerlightSensor = center;
        rightlightSensor = right;
        direction = dir; //determines whether robot goes left or right, and how quickly
        done = false;
        state = 0;
    }

    public void act() {
        //sensors return true when hitting the line
        //each boolean represents each sensor - names are self-explanatory
        boolean center = centerlightSensor.get();
        boolean left = leftlightSensor.get();
        boolean right = rightlightSensor.get();

        switch (state) {
            case 0:   // While not on the end line
                if (!(right && center && left)) {
                    MecanumDrive.drive(0.6, 0.0, 0.0);
                } else {
                    MecanumDrive.drive(-0.3, 0.0, 0.0);
                    state=1;
                }
                break;

            case 1:
                if (right || left || center) { // while still on the line
                    MecanumDrive.drive(-0.6, 0.0, 0.0);
                } else {
                    MecanumDrive.drive(-0.6, 0.0, 0.0);
                    state=2;
                }
                break;

            case 2:
                if (!(center&&right&&left)) {// pass line
                    MecanumDrive.drive(-0.6, 0.0, 0.0);
                } else {  
                    MecanumDrive.drive(-0.3, 0.0, 0.0);
                    state=3;
                }
                break;

            case 3:
                if (right || center || left) { // on line, slow down
                    MecanumDrive.drive(-0.3, 0.0, 0.0);
                } else {
                    MecanumDrive.drive(0.0, 0.4, 0.0);
                    state=4;
                }
                break;

             case 4:
                if (!(!center&&right&&!left)) {// slow down
                    MecanumDrive.drive(0.0, 0.4, 0.0);
                } else {
                    MecanumDrive.drive(0.0, 0.1, 0.0);
                    state=5;
                }
                break;

             case 5:
                if (!(center&&!right&&!left)) {// on line
                    MecanumDrive.drive(0.0, 0.2, 0.0);
                } else {
                    MecanumDrive.drive(0.3, 0.0, 0.0);
                    state=6;
                }
                break;

             case 6:
                if (!(center&&right&&left)) {// go forward
                    MecanumDrive.drive(0.3, 0.0, 0.0);
                } else {
                    MecanumDrive.drive(0.0, 0.0, 0.0);
                    state=7;
                    done=true;
                }
                break;
        }
    }

    public boolean isDone() {
        if (done)state=0;
        return done;
    }
}
