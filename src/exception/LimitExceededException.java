package exception;

public class LimitExceededException extends Exception{
	public LimitExceededException() {
		super("The amount entered exceeds the allowed limit");
	}
}
