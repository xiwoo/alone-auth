package com.inminhouse.alone.auth.exception.handler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

	private int status;
	private String code;
    private String message;
    private List<FieldError> errors;
    
    public static ErrorResponse of(ErrorCode errorCode) {
    	return new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage(), Arrays.asList());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
    	return new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), message, Arrays.asList());
    }
    
    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
    	return new ErrorResponse(
			errorCode.getStatus(), 
			errorCode.getCode(), 
			errorCode.getMessage(),
			bindingResult.getAllErrors().stream()
				.filter(x -> (x instanceof FieldError))
				.map(x -> (FieldError) x)
				.collect(Collectors.toList())
		);
    }
}