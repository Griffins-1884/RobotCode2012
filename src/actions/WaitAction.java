package actions;

/**
 * An action that waits a specified duration;
 * 
 * @author Colin Poler
 */
public class WaitAction extends Action {
	private final Interval duration;
	private Thread waitThread;
	
	/**
	 * Constructs a WaitAction with the specified duration and parent.
	 * 
	 * @param duration The duration to wait.
	 * @param parent The parent of the action. null if it has no parent.
	 */
	public WaitAction(Interval duration) {
		this.duration = duration;
	}
	
	/**
	 * Pauses the thread for the duration of the action.
	 */
	protected void act() {
		waitThread = Thread.currentThread();
		try {
			Thread.sleep(duration.milliseconds);
		} catch(InterruptedException e) {} // If interrupted, just continue
		stop();
	}
	
	/**
	 * Cleans up the action
	 */
	public void destroy() {
		if(waitThread != null) {
			waitThread.interrupt();
		}
	}
	
	/**
	 * Returns the length that the action was to wait for.
	 */
	public Interval duration() {
		return duration;
	}
}