package com.tsp.new_tsp_admin.exception;

import lombok.Getter;

public class TspException extends RuntimeException {
	@Getter
	private final BaseExceptionType baseExceptionType;

	public TspException(BaseExceptionType baseExceptionType, Throwable cause) {
		super(baseExceptionType.getErrorMessage(), cause);
		this.baseExceptionType = baseExceptionType;
	}
}
