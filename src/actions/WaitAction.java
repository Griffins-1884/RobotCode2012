package actions;

/**
 * An action that waits a specified duration;
 * 
 * @author Colin Poler
 */
public class WaitAction extends Action {
	private final Interval duration;
	
	/**
	 * Constructs a WaitAction with the specified duration and parent.
	 * 
	 * @param duration The duration to wait.
	 * @param parent The parent of the action. null if it has no parent.
	 */
	public WaitAction(Interval duration, MultiAction parent) {
		super(parent);
		this.duration = duration;
	}
	
	/**
	 * Pauses the thread for the duration of the action.
	 */
	public void act() {
		try {
			wait(duration.milliseconds);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		finished();
	}
	
	/**
	 * Returns the length that the action was to wait for.
	 */
	public Interval duration() {
		return duration;
	}
}