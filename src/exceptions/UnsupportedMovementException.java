package exceptions;

public class UnsupportedMovementException extends RuntimeException {
	private static final long serialVersionUID = 614481235538458946L;
	public UnsupportedMovementException(String message) {
		super(message);
	}
}