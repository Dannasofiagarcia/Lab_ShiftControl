package exception;

public class NotFoundException extends Exception {

	public NotFoundException(int idNumber) {
		super("The user with the id number " + idNumber + " was not found");
	}
}
