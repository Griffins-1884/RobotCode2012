/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author bhasinl
 */
public class OpenArm implements Action {

    Solenoid closingSolenoid;
    Solenoid openingSolenoid;
    boolean done = false;

    public OpenArm(Solenoid myClosingSolenoid, Solenoid myOpeningSolenoid)
    {
        closingSolenoid = myClosingSolenoid;
        openingSolenoid = myOpeningSolenoid;
    }

    public void act() {
        closingSolenoid.set(false);
        openingSolenoid.set(true);

        done = true;
    }

    public boolean isDone() {
        return done;
    }

}
