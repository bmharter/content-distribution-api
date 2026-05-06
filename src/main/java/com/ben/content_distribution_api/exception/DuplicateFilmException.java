package com.ben.content_distribution_api.exception;

@SuppressWarnings("serial")
public class DuplicateFilmException extends RuntimeException {

	public DuplicateFilmException(String message) {
        super(message);
    }
}
