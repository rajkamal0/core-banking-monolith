package fintrack_monolith.exception;

public class CannotReverseTransactionException extends RuntimeException {

	public CannotReverseTransactionException(String message) {
		super(message);
	}

}
