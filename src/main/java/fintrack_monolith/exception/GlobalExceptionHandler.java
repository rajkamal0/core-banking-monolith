package fintrack_monolith.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<ErrorDetails> handleInsufficientFundsException(InsufficientFundsException ex){
		log.info("Inside handleInsufficientFundsException");
		log.warn("InsufficientFundsException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
													 HttpStatus.BAD_REQUEST.value(),
													 "INCORRECT_REQUEST",
													 ex.getMessage());
		log.warn("returning from handleInsufficientFundsException");
		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex) {
		log.info("Inside handleResourceNotFoundException");
		log.warn("ResourceNotFoundException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
													 HttpStatus.NOT_FOUND.value(),
													 "NOT_FOUND",
													 ex.getMessage());
		log.warn("returning from handleResourceNotFoundException");
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(KycNotVerifiedException.class)
	public ResponseEntity<ErrorDetails> handleKycNotVerifiedException(KycNotVerifiedException ex){
		log.info("Inside handleKycNotVerifiedException");
		log.warn("KycNotVerifiedException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
													 HttpStatus.FORBIDDEN.value(),
													 "UNVERIFIED_KYC",
													 ex.getMessage());
		log.warn("returning from handleKycNotVerifiedException");
		return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(CannotCloseAccountException.class)
	public ResponseEntity<ErrorDetails> handleCannotCloseAccountException(CannotCloseAccountException ex){
		log.info("Inside handleCannotCloseAccountException");
		log.warn("CannotCloseAccountException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
													 HttpStatus.CONFLICT.value(),
													 "ACCOUNT_CLOSURE_FAILED",
													 ex.getMessage());
		log.warn("returning from handleCannotCloseAccountException");
		return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({AccountInactiveException.class, GlInactiveException.class})
	public ResponseEntity<ErrorDetails> handleAccountInactiveException(RuntimeException ex){
		log.info("Inside handleAccountInactiveException");
		log.warn("AccountInactiveException or GlInactiveException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
													 HttpStatus.UNPROCESSABLE_CONTENT.value(),
													 "ACCOUNT_IS_NOT_ACTIVE",
													 ex.getMessage());
		log.warn("returning from handleAccountInactiveException");
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNPROCESSABLE_CONTENT);
	}
	
	@ExceptionHandler(CurrencyMismatchException.class)
	public ResponseEntity<ErrorDetails> handleCurrencyMismatchException(CurrencyMismatchException ex){
		log.info("Inside handleCurrencyMismatchException");
		log.warn("CurrencyMismatchException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
													 HttpStatus.BAD_REQUEST.value(),
													 "CURRENCY_MISMATCH",
													 ex.getMessage());
		log.warn("returning from handleCurrencyMismatchException");
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CannotReverseTransactionException.class)
	public ResponseEntity<ErrorDetails> handleCannotReverseTransactionException(CannotReverseTransactionException ex){
		log.info("Inside handleCannotReverseTransactionException");
		log.warn("CannotReverseTransactionException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
													 HttpStatus.UNPROCESSABLE_CONTENT.value(),
													 "CANNOT_PROCESS_TRANSACTION",
													 ex.getMessage());
		log.warn("returning from handleCannotReverseTransactionException");
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNPROCESSABLE_CONTENT);
	}
	
	@ExceptionHandler(IncorrectTransactionTypeException.class)
	public ResponseEntity<ErrorDetails> handleIncorrectTransactionTypeException(IncorrectTransactionTypeException ex){
		log.info("Inside handleCurrencyMismatchException");
		log.warn("IncorrectTransactionTypeException: {}", ex.getMessage());
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), 
													 HttpStatus.BAD_REQUEST.value(),
													 "INCORRECT_TXN_TYPE",
													 ex.getMessage());
		log.warn("returning from handleIncorrectTransactionTypeException");
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
