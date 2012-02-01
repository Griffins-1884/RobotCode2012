package actions;

/**
 * Represents actions occurring simultaneously.
 * 
 * @author Colin Poler
 */
public class ConcurrentActions extends Action implements ActionListener, MultiAction {
	/**
	 * The actions that will take place.
	 */
	public final Action[] actions;
	private int actionsCompleted;
	
	/**
	 * Constructs a ConcurrentActions from the array of actions, and the parent.
	 * 
	 * @param actions The actions to be called simultaneously.
	 * @param parent The parent of the action. null if it has no parent.
	 */
	public ConcurrentActions(Action[] actions, MultiAction parent) {
		super(parent);
		this.actions = actions;
	}
	
	/**
	 * Starts calling Actions.
	 */
	public void act() {
		for(int i = 0; i < actions.length; i++) {
			ActionThread thread = new ActionThread(actions[i]);
			actions[i].addListener(this);
			thread.start();
		}
	}
	
	/**
	 * Determines the duration of the ConcurrentActions, by finding the maximum duration.
	 * 
	 * @return The duration of the ConcurrentActions.
	 */
	public Interval duration() {
		long longestDuration = 0;
		for(int i = 0; i < actions.length; i++) {
			longestDuration = Math.max(longestDuration, actions[i].duration().milliseconds);
		}
		return new Interval(longestDuration);
	}
	
	/**
	 * Determines how long after beginning the specified action will begin.
	 */
	public Interval ETD(Action action) {
		return new Interval(0);
	}
	
	/**
	 * Listens to children actions and finishes the event when all actions are completed.
	 */
	public void actionCompleted(Action source) {
		actionsCompleted++;
		if(actionsCompleted >= actions.length) {
			finished();
		}
	}
	
	/**
	 * An ActionThread for ConcurrentActions.
	 * 
	 * @author Colin Poler
	 */
	protected class ActionThread extends Thread {
		/**
		 * The action the thread is executing.
		 */
		public final Action action;
		
		/**
		 * Constructs an ActionThread with the specified action.
		 * 
		 * @param action The action to call;
		 */
		public ActionThread(Action action) {
			this.action = action;
		}
		
		/**
		 * Calls the action.
		 */
		public void run() {
			action.start();
		}
	}
}