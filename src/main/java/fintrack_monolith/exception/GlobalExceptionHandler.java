package fintrack_monolith.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<ErrorDetails> handleInsufficientFundsException(InsufficientFundsException ex){
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
													 HttpStatus.BAD_REQUEST.value(),
													 "Incorrect Request",
													 ex.getMessage());
		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
													 HttpStatus.NOT_FOUND.value(),
													 "Not found",
													 ex.getMessage());
		
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
}
