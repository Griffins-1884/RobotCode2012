/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author bhasinl
 */
public interface Action {

    // Act method carries out the process of this action
    // And returns when the action has been carried out
    public void act();

    // Returns true when the action is done
    public boolean isDone();

}
