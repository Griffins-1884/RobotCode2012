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
	private final Vector<ActionListener> listeners;
	
	public Action(MultiAction parent) {
		this.parent = parent;
		this.listeners = new Vector<ActionListener>();
	}
	
	/**
	 * Adds an ActionListener to this action.
	 * 
	 * @param listener The listener to add.
	 */
	public void addListener(ActionListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Called by actions to indicate that they are finished.
	 */
	public void finished() {
		for(ActionListener l : listeners) {
			l.actionCompleted(this);
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