/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
// This code is teh rockzorz
/**
 *
 * @author michaelcw
 */
public class ZackNathan implements Action {

    // Act method carries out the process of this action
    // And returns when the action has been carried out
    // Or, in this case, the method times out or quits
    public void act()
    {
        System.out.println("I totally do work, you just didn't use my code.");

    }


    public boolean isDone()
    {
        return false; // Yeah, like he's ever going to return at all
    }
}
