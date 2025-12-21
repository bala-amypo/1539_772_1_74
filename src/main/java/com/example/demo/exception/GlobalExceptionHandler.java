package com.example.demo.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
@RestControllerAdvice
public class GlobalExceptionHandler {
    //404 - Resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        return new ResponseEntity<>(
                new ApiErrorResponse(ex.getMessage(), request.getDescription(false)),
                HttpStatus.NOT_FOUND
        );
    }

    //400 - Validation / business errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        return new ResponseEntity<>(
                new ApiErrorResponse(ex.getMessage(), request.getDescription(false)),
                HttpStatus.BAD_REQUEST
        );
    }

    //400 - Bean validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return new ResponseEntity<>(
                new ApiErrorResponse(errorMessage, request.getDescription(false)),
                HttpStatus.BAD_REQUEST
        );
    }

    // ⚫ 500 - Any other error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobal(
            Exception ex, WebRequest request) {

        return new ResponseEntity<>(
                new ApiErrorResponse("Internal Server Error", request.getDescription(false)),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // INNER RESPONSE CLASS (NO EXTRA FILE)
    static class ApiErrorResponse {

        private LocalDateTime timestamp;
        private String message;
        private String details;

        public ApiErrorResponse(String message, String details) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
            this.details = details;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getMessage() {
            return message;
        }

        public String getDetails() {
            return details;
        }
    }
}
