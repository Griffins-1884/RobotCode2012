package _static;

import actions.Action;
import actions.MultiAction;

public abstract class Apparatus {
	private boolean lock = false;
	private boolean locked() {
		return lock;
	}
	private synchronized void lock() {
		lock = true;
	}
	private synchronized void unlock() {
		lock = false;
	}
	public static abstract class ApparatusAction extends Action {
		public final Apparatus apparatus;
		public ApparatusAction(Apparatus apparatus, MultiAction parent) {
			super(parent);
			this.apparatus = apparatus;
		}
		public void start() {
			while(apparatus.locked()) {} // Idle until the shooter is not busy
			apparatus.lock();
			super.start();
			apparatus.unlock();
		}
		public final void destroy() {
			apparatus.unlock();
			_destroy();
		}
		public abstract void _destroy();
	}
}