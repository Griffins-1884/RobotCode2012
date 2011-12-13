package actions;

/**
 * An interface for Actions which contain actions themselves.
 * 
 * @author Colin Poler
 */
public interface MultiAction {
	/**
	 * Determines the time until the specified action will begin.
	 * 
	 * @param action The action to find.
	 * @return The time between this MultiAction beginning and the specified action beginning.
	 */
	// TODO should this include what time has already elapsed?
	public Interval ETD(Action action);
}