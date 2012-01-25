package actions;

import java.util.Vector;

public class MutableActionList extends SequentialActions {
	private final Vector actionList;
	
	/**
	 * Constructs an empty MutableActionList.
	 */
	public MutableActionList() {
		this(null, null);
	}
	
	/**
	 * Constructs a MutableActionList from the array of actions, and the parent.
	 * 
	 * @param actions The actions to be called sequentially.
	 * @param parent The parent of the action. null if it has no parent.
	 */
	public MutableActionList(Action[] actions, MultiAction parent) {
		super(null, parent);
		actionList = new Vector();
		for(int i = 0; i < actions.length; i++) {
			actionList.addElement(actions[i]);
		}
	}
	
	/**
	 * Adds the specified action to the queue.
	 * @param a The action to add.
	 */
	public void add(Action a) {
		actionList.addElement(a);
	}
	
	/**
	 * Gets (and removes) the first action in the list.
	 * 
	 * @return The first action in the list.
	 */
	protected Action poll() {
		if(actionList.size() > 0) {
			Action a = (Action) actionList.elementAt(0);
			actionList.removeElementAt(0);
			return a;
		} else {
			return null;
		}
	}
	
	/**
	 * Starts calling Actions.
	 */
	public void act() {
		Action a = poll();
		if(a == null) {
			finished();
			return;
		}
		thread = new ActionThread(a);
		a.addListener(this);
		thread.start();
	}
	
	/**
	 * Determines the duration of the SequentialActions, by adding all the actions.
	 * 
	 * @return The duration of the SequentialActions.
	 */
	public Interval duration() {
		long duration = 0;
		for(int i = 0; i < actionList.size(); i++) {
			duration += ((Action) actionList.elementAt(i)).duration().milliseconds;
		}
		return new Interval(duration);
	}
	
	/**
	 * Determines how long after beginning the specified action will begin.
	 */
	public Interval ETD(Action action) {
		Interval etd = new Interval(0);
		for(int i = 0; !((Action) actionList.elementAt(i)).equals(action); i++) {
			etd = etd.add(((Action) actionList.elementAt(i)).duration());
		}
		if(parent != null) {
			etd = etd.add(parent.ETD(this));
		}
		if(start != null) {
			return new Interval(etd.milliseconds + start.longValue() - System.currentTimeMillis());
		}
		return etd;
	}
}