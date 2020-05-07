package exception;

public class IncorrectQuantityException extends Exception{
	public IncorrectQuantityException() {
		super("Quantity must be higher than 0");
	}
}
