package exception;

public class AllTurnsPassedException extends Exception {
	public AllTurnsPassedException() {
		super("All currently registered shifts have passed, it is not possible to advance");
	}
}
