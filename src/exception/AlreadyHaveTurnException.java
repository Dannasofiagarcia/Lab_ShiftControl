package exception;

public class AlreadyHaveTurnException extends Exception {
	public AlreadyHaveTurnException() {
		super("User already have a shift active");
	}
}
