package com.implementation.PollingApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.implementation.PollingApp.exception.custom.InternalServerErrorException;
import com.implementation.PollingApp.exception.custom.ResourceNotFoundException;
import com.implementation.PollingApp.util.ApiResponse;
import com.implementation.PollingApp.exception.custom.ValidationException;
import com.implementation.PollingApp.exception.custom.InvalidSessionException;
import com.implementation.PollingApp.exception.custom.PollExpirationException;
import com.implementation.PollingApp.exception.custom.UnauthorizedRequestException;
import com.implementation.PollingApp.exception.custom.CannotVoteException;
import com.implementation.PollingApp.exception.custom.ForbiddenRequestException;

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

        @ExceptionHandler(InvalidSessionException.class)
        public ResponseEntity<ApiResponse<Object>> handleInvalidSessionException(InvalidSessionException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        @ExceptionHandler(UnauthorizedRequestException.class)
        public ResponseEntity<ApiResponse<Object>> handleUnauthorizedFilterException(UnauthorizedRequestException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        @ExceptionHandler(PollExpirationException.class)
        public ResponseEntity<ApiResponse<Object>> handlePollExpirationException(PollExpirationException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(CannotVoteException.class)
        public ResponseEntity<ApiResponse<Object>> handleAlreadyVotedException(CannotVoteException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(ForbiddenRequestException.class)
        public ResponseEntity<ApiResponse<Object>> handleForbiddenRequestException(ForbiddenRequestException ex) {
                ApiResponse<Object> response = new ApiResponse<>(null, ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

}