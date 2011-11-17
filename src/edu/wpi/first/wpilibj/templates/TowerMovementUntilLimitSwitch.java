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
public class TowerMovementUntilLimitSwitch implements Action {

    AnalogChannel topTowerSwitch;
    Jaguar towerJaguar;
    boolean done = false;
    boolean hasAlreadyHitLimitSwitch = false;

    public TowerMovementUntilLimitSwitch(Jaguar myTowerJaguar, AnalogChannel topLimitSwitch)
    {
        towerJaguar = myTowerJaguar;
        topTowerSwitch = topLimitSwitch;
    }

    // Pre: isDone is called beforehand
    public void act() {
        towerJaguar.set(-0.86);
    }

    public boolean isDone() {
        return hasHitTopOnce();
    }

    public boolean hasHitTopOnce()
    {
        if(topTowerSwitch.getVoltage() >= 4.5 && !hasAlreadyHitLimitSwitch)
        {
            done = true;
            towerJaguar.set(0.0);
            hasAlreadyHitLimitSwitch = true;
        }
        return done;
    }


}
