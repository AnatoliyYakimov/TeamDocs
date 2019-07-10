package com.yakimov.teamdocs.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;

@ControllerAdvice
public class DocumentNotFoundAdvice {
	
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(DocumentNotFoundException.class)
	public String documentNotFoundHandler(DocumentNotFoundException ex) {
		return ex.getMessage();
	}
}
