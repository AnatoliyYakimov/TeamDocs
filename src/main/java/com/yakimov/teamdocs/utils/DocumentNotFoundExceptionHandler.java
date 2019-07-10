package com.yakimov.teamdocs.utils;

import static com.yakimov.teamdocs.utils.ExceptionHandlerUtil.generateBodyWithTimestampAndStatus;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;

@ControllerAdvice
public class DocumentNotFoundExceptionHandler {
	private final static HttpStatus DEFAULT_STATUS = HttpStatus.NOT_FOUND;
	
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(DocumentNotFoundException.class)
	public ResponseEntity<Object> documentNotFoundHandler(DocumentNotFoundException ex) {
		Map<String, Object> body = generateBodyWithTimestampAndStatus(DEFAULT_STATUS);
		
		body.put("errors", ex.getMessage());
	
		return new ResponseEntity<>(body, DEFAULT_STATUS);
	}
}
