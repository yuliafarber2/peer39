package com.urlcategorizer.exception;

public class UrlProcessingException extends RuntimeException {
    public UrlProcessingException() {
        super();
    }

    public UrlProcessingException(String message) {
        super(message);
    }
}
