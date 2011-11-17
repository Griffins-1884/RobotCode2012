/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import java.util.Vector;
/**
 *
 * @author bhasinl
 */
public class PlayList {

    private int index;
    private Vector list;

    public PlayList()
    {
        index = 0;
        list = new Vector();
    }

    public void add(Action a)
    {
        list.addElement(a);
    }

    public void update()
    {
        Action a = ((Action)list.elementAt(index));

        if(a.isDone())
        {
            index++;
        }
        else
        {
            a.act();
        }
        if(index >= list.size())
        {
           System.out.println("end");
           //index = 0;
        }
    }

    public void restart()
    {
        index = 0;
    }

    public void playNext()
    {
        index++;
    }

    public void playLast()
    {
        index--;
    }

    public Action getCurrentAction()
    {
        return (Action)(list.elementAt(index));
    }

}
