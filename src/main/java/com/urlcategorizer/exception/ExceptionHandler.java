package com.urlcategorizer.exception;

import com.urlcategorizer.dto.UrlContentResultDTO;
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
     * Handles all uncaught exceptions globally.
     *
     * @param ex The thrown exception.
     * @return A response entity containing a generic error message and HTTP status 500.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<UrlContentResultDTO> handleGeneralException(Exception ex) {
        UrlContentResultDTO errorResponse = new UrlContentResultDTO("Error", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
