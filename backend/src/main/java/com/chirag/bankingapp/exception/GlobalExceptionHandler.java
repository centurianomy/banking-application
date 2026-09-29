package com.chirag.bankingapp.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//for handling Validation Exception
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

	//for handling CustomerNotFoundException
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
	    ErrorResponse errorResponse = ErrorResponse.builder()
	            .status(HttpStatus.NOT_FOUND.value())
	            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
	            .message(ex.getMessage())
	            .timestamp(LocalDateTime.now())
	            .build();

	    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	//for handling Generic Exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
	    ErrorResponse errorResponse = ErrorResponse.builder()
	            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
	            .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
	            .message("An unexpected error occurred. Please try again later.")
	            .timestamp(LocalDateTime.now())
	            .build();

	    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//for handling invalid account exception
	@ExceptionHandler(InvalidAccountBalanceException.class)
	public ResponseEntity<ErrorResponse> handleInvalidAccountBalanceException(InvalidAccountBalanceException ex) {
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
	            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
	            .message(ex.getMessage())
	            .timestamp(LocalDateTime.now())
	            .build();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	//for handling AccountNotFoundException
		@ExceptionHandler(AccountNotFoundException.class)
		public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
		    ErrorResponse errorResponse = ErrorResponse.builder()
		            .status(HttpStatus.NOT_FOUND.value())
		            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
		            .message(ex.getMessage())
		            .timestamp(LocalDateTime.now())
		            .build();

		    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
		}
		// for handling insufficient accnt balance exception
		@ExceptionHandler(InsufficientAccountBalanceException.class)
		public ResponseEntity<ErrorResponse> handleInsufficientAccountBalanceException(InsufficientAccountBalanceException ex) {
		    ErrorResponse errorResponse = ErrorResponse.builder()
					.status(HttpStatus.BAD_REQUEST.value())
		            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
		            .message(ex.getMessage())
		            .timestamp(LocalDateTime.now())
		            .build();
			
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		// handle conflict (between two transactions at same time) [409 conflict]
		@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
		public ResponseEntity<ErrorResponse> handleOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
		    ErrorResponse errorResponse = ErrorResponse.builder()
		            .status(HttpStatus.CONFLICT.value())
		            .error(HttpStatus.CONFLICT.getReasonPhrase())
		            .message("This account was updated by another request. Please retry.")
		            .timestamp(LocalDateTime.now())
		            .build();

		    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
		}
		
		//for handling Invalid credential exception 
		@ExceptionHandler(InvalidCredentialsException.class)
		public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		    ErrorResponse errorResponse = ErrorResponse.builder()
		            .status(HttpStatus.UNAUTHORIZED.value())
		            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
		            .message(ex.getMessage())
		            .timestamp(LocalDateTime.now())
		            .build();

		    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
		
		//for handling AccessDenied exception
		@ExceptionHandler(AccessDeniedException.class)
		public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
		    // fill this in — status 403, safe to use ex.getMessage() since you authored that text
			ErrorResponse errorResponse =  ErrorResponse.builder()
					.status(HttpStatus.FORBIDDEN.value())
					.error(HttpStatus.FORBIDDEN.getReasonPhrase())
					.message(ex.getMessage())
					.timestamp(LocalDateTime.now())
					.build();
					
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);			
		}
}
