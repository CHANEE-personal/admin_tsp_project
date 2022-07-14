package com.tsp.new_tsp_admin.exception;

public interface BaseExceptionType {
	// errorCode
	String getErrorCode();
	// HttpStatus
	int getHttpStatus();
	// errorMessage
	String getErrorMessage();
}
