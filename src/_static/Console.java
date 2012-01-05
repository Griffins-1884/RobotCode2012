package _static;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import sensors.Sensor;
import sensors.SensorThread;

public class Console {
	private static Console console = new Console();
	public static Console console() {
		return console;
	}
	
	private ConcurrentLinkedQueue messages;
	private PrintThread thread;
	private Console() {
		messages = new ConcurrentLinkedQueue();
		thread = new PrintThread();
	}
	public void log(String key, String value) {
		Message m = new Message(key, value);
		messages.remove(m);
		messages.add(m);
	}
	public void log(String key, Object value) {
		log(key, value.toString());
	}
	public void setDelay(long delay) {
		thread.running = delay > 0;
		thread.printDelay = delay;
		thread.start();
	}
	public class Message {
		private final String key, value;
		public Message(String key, String value) {
			this.key = key;
			this.value = value;
		}
		public boolean equals(Object o) {
			return o instanceof Message && ((Message) o).key.equals(key);
		}
		public String toString() {
			return key + ": " + value;
		}
	}
	public class PrintThread extends Thread {
		private long printDelay;
		private boolean running;
		private PrintThread() {
			printDelay = 20;
		}
		public void run() {
			while(running) {
				for(Message m = (Message) messages.poll(); m != null; m = (Message) messages.poll()) {
					System.out.println(m);
					m = (Message) messages.poll();
				}
				try {
					Thread.sleep(printDelay);
				} catch(InterruptedException e) {}
			}
		}
	}
}