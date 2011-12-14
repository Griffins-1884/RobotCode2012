package actions;

import java.util.Vector;

/**
 * An action for the robot to take. It should be used in autonomous to speed development.
 * 
 * @author Colin Poler
 */
public abstract class Action {
	/**
	 * The parent of the action.
	 */
	public final MultiAction parent;
	private final Vector listeners;
	
	public Action(MultiAction parent) {
		this.parent = parent;
		this.listeners = new Vector();
	}
	
	/**
	 * Adds an ActionListener to this action.
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(ActionListener listener) {
		listeners.addElement(listener);
	}
	
	/**
	 * Called by actions to indicate that they are finished.
	 */
	public void finished() {
		for(int i = 0; i < listeners.size(); i++) {
			((ActionListener) listeners.elementAt(i)).actionCompleted(this);
		}
	}
	
	/**
	 * Returns the estimated time until this action completes.
	 * 
	 * @return An interval containing the estimated time until this action completes.
	 */
	public Interval ETA() {
		if(parent == null) {
			return duration();
		}
		return parent.ETD(this).add(duration());
	}
	
	/**
	 * The act method. This method should run all the code to complete the action.
	 */
	public abstract void act();
	
	/**
	 * Estimates the duration of the action (given distance to travel, time piston takes to fire, etc.)
	 * 
	 * @return The estimated duration of the action.
	 */
	public abstract Interval duration();
}