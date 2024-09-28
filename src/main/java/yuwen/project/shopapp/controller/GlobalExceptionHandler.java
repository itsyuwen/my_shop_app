package yuwen.project.shopapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import yuwen.project.shopapp.exceptions.EntityNotFoundException;
import yuwen.project.shopapp.exceptions.InvalidCredentialsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handling specific exception
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
/*
    // Handling specific exception
    @ExceptionHandler(NotEnoughInventoryException.class)
    public ResponseEntity<?> handleNotEnoughInventoryException(NotEnoughInventoryException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
*/
    // Handling specific exception
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    // Handling general exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        // You can create a custom body if you want to return more details
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    // Define an ErrorDetails class to hold error information
    static class ErrorDetails {
        private final int status;
        private final String message;
        private final String details;

        public ErrorDetails(int status, String message, String details) {
            this.status = status;
            this.message = message;
            this.details = details;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getDetails() {
            return details;
        }
    }
}
