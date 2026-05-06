package com.ben.content_distribution_api.exception;

@SuppressWarnings("serial")
public class InvalidSortFieldException extends RuntimeException {

	public InvalidSortFieldException(String message) {
        super(message);
    }
}
