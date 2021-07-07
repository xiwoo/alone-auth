package com.inminhouse.alone.auth.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.inminhouse.alone.auth.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AuthExceptionHandler {

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        ErrorCode eCode = ErrorCode.INVALID_INPUT_VALUE;
        return new ResponseEntity<>(ErrorResponse.of(eCode, e.getBindingResult()), HttpStatus.valueOf(eCode.getStatus()));
    }
    
	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
		log.error("handleResourceNotFoundException", e);
		ErrorCode eCode = ErrorCode.INVALID_INPUT_VALUE;
		return new ResponseEntity<>(ErrorResponse.of(eCode), HttpStatus.valueOf(eCode.getStatus()));
	}
	
	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<String> handleRuntimeException(RuntimeException e) {
		log.error("handleRuntimeException", e);
		return new ResponseEntity<>("??", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> handleException(Exception e) {
		log.error("handleException", e);
		return new ResponseEntity<>("??", HttpStatus.BAD_REQUEST);
	}
}
