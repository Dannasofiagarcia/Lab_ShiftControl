package exception;

public class UserAlreadyExistsException extends Exception {
	public UserAlreadyExistsException() {
		super("User you are trying to add already exists");
	}
}
