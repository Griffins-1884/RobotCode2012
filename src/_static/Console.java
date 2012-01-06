package _static;

import java.util.Vector;

/**
 * Represents the console. Allows for easy printing of messages to the console, and in the future might have a scanner for text input.
 * 
 * @author Colin Poler
 */
public class Console {
	private static Console console = new Console();
	
	/**
	 * Gets the console.
	 * 
	 * @return The console.
	 */
	public static Console console() {
		return console;
	}
	
	private Vector messages;
	private PrintThread thread;
	private Console() {
		messages = new Vector();
		thread = new PrintThread();
		thread.start();
	}
	
	/**
	 * Logs a String to the console.
	 * 
	 * @param key The key (or description) of the value.
	 * @param value The value to print.
	 */
	public void log(String key, String value) {
		Message m = new Message(key, value);
		messages.removeElement(m);
		messages.addElement(m);
	}
	
	/**
	 * Logs an object to the console.
	 * 
	 * @param key The key (or description) of the value.
	 * @param value The value to print.
	 */
	public void log(String key, Object value) {
		log(key, value.toString());
	}
	
	/**
	 * Logs an int, short, or long to the console.
	 * 
	 * @param key The key (or description) of the value.
	 * @param value The value to print.
	 */
	public void log(String key, long value) {
		log(key, "" + value);
	}
	
	/**
	 * Logs a boolean to the console.
	 * 
	 * @param key The key (or description) of the value.
	 * @param value The value to print.
	 */
	public void log(String key, boolean value) {
		log(key, "" + value);
	}
	
	/**
	 * Logs a double or a float to the console.
	 * 
	 * @param key The key (or description) of the value.
	 * @param value The value to print.
	 */
	public void log(String key, double value) {
		log(key, "" + value);
	}
	
	/**
	 * Sets the interval between which the Console prints its messages.
	 * 
	 * @param delay The delay in milliseconds.
	 */
	public void setDelay(long delay) {
		thread.running = delay > 0;
		thread.printDelay = delay;
	}
	
	/**
	 * Gets the first message in the list.
	 * 
	 * @return The first message in the list.
	 */
	protected Message poll() {
		if(messages.size() > 0) {
			Message m = (Message) messages.elementAt(0);
			messages.removeElementAt(0);
			return m;
		} else {
			return null;
		}
	}
	
	/**
	 * Represents a message in the console.
	 * 
	 * @author Colin Poler
	 */
	public class Message {
		private final String key, value;
		
		/**
		 * Creates a Message with the specified key and value.
		 * 
		 * @param key The key (or description).
		 * @param value The value of the message.
		 */
		public Message(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Note: This doesn't really test for equality, instead it is here to so that messages.removeElement will work correctly.
		 */
		public boolean equals(Object o) {
			return o instanceof Message && ((Message) o).key.equals(key);
		}
		
		/**
		 * Prints "key: value".
		 */
		public String toString() {
			return key + ": " + value;
		}
	}
	
	/**
	 * A class printing the messages periodically.
	 * 
	 * @author Colin Poler
	 */
	public class PrintThread extends Thread {
		private long printDelay;
		private boolean running;
		private PrintThread() {
			printDelay = 20;
			running = true;
		}
		
		/**
		 * Goes through all messages and prints them all.
		 */
		public void run() {
			while(running) {
				for(Message m = poll(); m != null; m = poll()) {
					System.out.println(m);
				}
				try {
					Thread.sleep(printDelay);
				} catch(InterruptedException e) {}
			}
		}
	}
}