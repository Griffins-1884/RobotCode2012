package actions;

/**
 * Represents actions occurring sequentially
 * 
 * @author Colin Poler
 */
public class SequentialActions extends Action implements ActionListener, MultiAction {
	/**
	 * The actions that will take place.
	 */
	public final Action[] actions;
	private int actionsCompleted;
	
	/**
	 * The thread executing the actions.
	 */
	protected ActionThread thread;
	
	/**
	 * Constructs a SequentialActions from the array of actions, and the parent.
	 * 
	 * @param actions The actions to be called sequentially.
	 * @param parent The parent of the action. null if it has no parent.
	 */
	public SequentialActions(Action[] actions, MultiAction parent) {
		super(parent);
		this.actions = actions;
	}
	
	/**
	 * Starts calling Actions.
	 */
	public void act() {
		thread = new ActionThread(actions[actionsCompleted]);
		actions[actionsCompleted].addListener(this);
		thread.start();
	}
	
	/**
	 * Determines the duration of the SequentialActions, by adding all the actions.
	 * 
	 * @return The duration of the SequentialActions.
	 */
	public Interval duration() {
		long duration = 0;
		for(int i = 0; i < actions.length; i++) {
			duration += actions[i].duration().milliseconds;
		}
		return new Interval(duration);
	}
	
	/**
	 * Listens to children actions and finishes the event when all actions are completed.
	 */
	public void actionCompleted(Action source) {
		actionsCompleted++;
		if(actionsCompleted >= actions.length) {
			finished();
			return;
		}
		thread = new ActionThread(actions[actionsCompleted]);
		actions[actionsCompleted].addListener(this);
		thread.start();
	}
	
	/**
	 * Determines how long after beginning the specified action will begin.
	 */
	public Interval ETD(Action action) {
		Interval etd = new Interval(0);
		for(int i = 0; !actions[i].equals(action); i++) {
			etd = etd.add(actions[i].duration());
		}
		return etd;
	}
	
	/**
	 * An ActionThread for SequentialActions.
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
		 * Goes through actions and calls them.
		 */
		public void run() {
			action.act();
		}
	}
}