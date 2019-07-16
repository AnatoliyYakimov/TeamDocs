package com.yakimov.teamdocs.exceptions;

import static com.yakimov.teamdocs.utils.ExceptionHandlerUtil.generateBodyWithTimestampAndStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class MethodArgumentNotValidExceptionsHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = generateBodyWithTimestampAndStatus(status);
		
		List<String> errors = ex.getBindingResult()
				.getFieldErrors().stream()
				.map((er) -> er.getDefaultMessage())
				.collect(Collectors.toList());
		body.put("errors", errors);
		
		return new ResponseEntity<>(body, headers, status);
	}
	
	
	
}
