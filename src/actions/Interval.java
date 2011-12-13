package actions;

/**
 * An interval in time.
 * 
 * @author Colin Poler
 */
public class Interval {
	/**
	 * The number of milliseconds in this interval
	 */
	public final long milliseconds;
	
	/**
	 * Constructs an Interval with the specified minutes, seconds and milliseconds.
	 * 
	 * @param minutes The minutes in the interval.
	 * @param seconds The seconds in the interval.
	 * @param milliseconds The milliseconds in the interval.
	 */
	public Interval(int minutes, int seconds, int milliseconds) {
		this(minutes * 60 + seconds, milliseconds);
	}
	
	/**
	 * Constructs an Interval with the specified seconds and milliseconds.
	 * 
	 * @param seconds The seconds in the interval.
	 * @param milliseconds The milliseconds in the interval.
	 */
	public Interval(int seconds, int milliseconds) {
		this(seconds * 1000 + milliseconds);
	}
	
	/**
	 * Constructs an Interval with the specified milliseconds.
	 * 
	 * @param milliseconds The milliseconds in the interval.
	 */
	public Interval(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	
	/**
	 * Adds two Intervals (one after the other).
	 * 
	 * @param i The Interval to add.
	 * @return The resulting Interval.
	 */
	public Interval add(Interval i) {
		return new Interval(milliseconds + i.milliseconds);
	}
	
	/**
	 * Determines the minutes elapsing during the interval.
	 * 
	 * @return The interval in minutes (truncated).
	 */
	public long minutes() {
		return seconds() / 60;
	}
	
	/**
	 * Determines the seconds elapsing during the interval.
	 * 
	 * @return The interval in seconds (truncated).
	 */
	public long seconds() {
		return milliseconds / 1000;
	}
}