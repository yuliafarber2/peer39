package com.urlcategorizer.exception;

import com.urlcategorizer.dto.UrlTextResultDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global exception handler for the application.
 * Handles specific and general exceptions thrown in controllers.
 */
@ControllerAdvice
public class ExceptionHandler {

    /**
     * Handles UrlProcessingException thrown during URL text extraction.
     *
     * @param ex The exception instance.
     * @return A response entity containing an error message and HTTP status 500.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UrlProcessingException.class)
    public ResponseEntity<UrlTextResultDTO> handleUrlProcessingException(UrlProcessingException ex) {
        UrlTextResultDTO errorResponse = new UrlTextResultDTO("Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handles all other uncaught exceptions globally.
     *
     * @param ex The thrown exception.
     * @return A response entity containing a generic error message and HTTP status 500.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<UrlTextResultDTO> handleGeneralException(Exception ex) {
        UrlTextResultDTO errorResponse = new UrlTextResultDTO("Error", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
