package actions;

/**
 * An interface for classes that listen to actions.
 * 
 * @author Colin Poler
 */
public interface ActionListener {
	/**
	 * Called when an action is completed
	 * 
	 * @param source The action that has completed.
	 */
	public void actionCompleted(Action source);
}