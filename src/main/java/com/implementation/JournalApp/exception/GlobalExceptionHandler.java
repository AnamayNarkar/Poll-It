package com.implementation.JournalApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.implementation.JournalApp.exception.custom.InternalServerErrorException;
import com.implementation.JournalApp.exception.custom.ResourceNotFoundException;
import com.implementation.JournalApp.util.ApiResponse;
import com.implementation.JournalApp.exception.custom.ValidationException;
import com.implementation.JournalApp.exception.custom.InvalidSessionException;
import com.implementation.JournalApp.exception.custom.UnauthorizedRequestException;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler({ InternalServerErrorException.class, Exception.class })
        public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiResponse<Object>> handleValidationException(ValidationException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler({ InvalidSessionException.class })
        public ResponseEntity<ApiResponse<Object>> handleInvalidSessionException(InvalidSessionException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(UnauthorizedRequestException.class)
        public ResponseEntity<ApiResponse<Object>> handleUnauthorizedFilterException(UnauthorizedRequestException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

}