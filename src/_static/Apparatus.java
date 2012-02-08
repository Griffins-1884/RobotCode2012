package _static;

import actions.Action;
import actions.MultiAction;

public abstract class Apparatus {
	private boolean lock = false;
	public static abstract class ApparatusAction extends Action {
		public final Apparatus apparatus;
		public ApparatusAction(Apparatus apparatus, MultiAction parent) {
			super(parent);
			this.apparatus = apparatus;
		}
		public void start() {
			while(apparatus.lock) {} // Idle until the shooter is not busy
			apparatus.lock = true;
			super.start();
			apparatus.lock = false;
		}
		public void destroy() {
			apparatus.lock = false;
			_destroy();
		}
		public abstract void _destroy();
	}
}