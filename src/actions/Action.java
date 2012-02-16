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
	
	/**
	 * The time at which this action started.
	 */
	protected Long start = null;
	
	/**
	 * If true, this signals that the thread should exit. Please check it often, and before any time consuming code.
	 */
	protected boolean quit = false;
	private final Vector listeners;
	private SeparateActionThread thread;
	
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
	 * Removes an ActionListener from this action.
	 * 
	 * @param listener The listener to remove.
	 */
	public void removeListener(ActionListener listener) {
		listeners.removeElement(listener);
	}
	
	/**
	 * Called by actions to indicate that they are finished.
	 */
	public void finished() {
		this.start = null;
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
		if(start != null) {
			return new Interval(duration().milliseconds + start.longValue() - System.currentTimeMillis());
		} else if(parent == null) {
			return duration();
		}
		return parent.ETD(this).add(duration());
	}
	
	/**
	 * Starts the action
	 */
	public void start() {
		this.start = new Long(System.currentTimeMillis());
		act();
		stop();
	}
	
	/**
	 * Cleans up the thread, and calls destroy
	 */
	public void stop() {
		if(thread != null) {
			this.quit = true;
			thread.interrupt();
		}
		destroy();
		finished();
	}
	
	/**
	 * Cleans up the action
	 */
	protected abstract void destroy();
	
	/**
	 * An ActionThread for Actions in separate threads.
	 * 
	 * @author Colin Poler
	 */
	protected static class SeparateActionThread extends Thread {
		/**
		 * The action the thread is executing.
		 */
		public final Action action;
		
		/**
		 * Constructs an SeparateActionThread with the specified action.
		 * 
		 * @param action The action to call;
		 */
		public SeparateActionThread(Action action) {
			this.action = action;
		}
		
		/**
		 * Calls the action.
		 */
		public void run() {
			action.start();
		}
	}
	
	/**
	 * Starts the action in a separate thread
	 */
	public void startSeparate() {
		thread = new SeparateActionThread(this);
		thread.start();
	}
	
	/**
	 * The act method. This method should run all the code to complete the action.
	 */
	protected abstract void act();
	
	/**
	 * Estimates the duration of the action (given distance to travel, time piston takes to fire, etc.)
	 * 
	 * @return The estimated duration of the action.
	 */
	public abstract Interval duration();
}