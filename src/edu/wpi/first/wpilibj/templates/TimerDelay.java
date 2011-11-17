/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author bhasinl
 */
public class TimerDelay implements Action {

    boolean hasNotActedBefore = true;
    long timerTargetMilliseconds;
    int durationOfTimer;

    // DURATION IS IN MILLISECONDS
    public TimerDelay(int duration)
    {
        durationOfTimer = duration;
    }

    public void act() {
        if(hasNotActedBefore)
        {
            timerTargetMilliseconds = System.currentTimeMillis() + durationOfTimer;
            hasNotActedBefore = false;
        }
    }

    public boolean isDone() {
        if(hasNotActedBefore)
            return false;
        
        return System.currentTimeMillis() >= timerTargetMilliseconds;
    }

    
}
