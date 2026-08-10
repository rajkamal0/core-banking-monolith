package fintrack_monolith.exception;

public class AccountInactiveException extends RuntimeException {

	public AccountInactiveException(String message) {
		super(message);
	}
	
}
