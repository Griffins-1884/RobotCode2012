package exceptions;

public class InvalidArrayException extends RuntimeException {
	private static final long serialVersionUID = 614481235538458946L;
	public InvalidArrayException(String message) {
		super(message);
	}
}