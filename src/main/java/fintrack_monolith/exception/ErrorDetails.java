package fintrack_monolith.exception;

import java.time.LocalDateTime;

public class ErrorDetails {
	
	private LocalDateTime timestamp;
	private int errorcode;
	private String errordetails;
	private String message;
	
	public ErrorDetails(LocalDateTime timestamp, int errorcode, String errordetails, String message) {
		super();
		this.timestamp = timestamp;
		this.errorcode = errorcode;
		this.errordetails = errordetails;
		this.message = message;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrordetails() {
		return errordetails;
	}
	public void setErrordetails(String errordetails) {
		this.errordetails = errordetails;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
