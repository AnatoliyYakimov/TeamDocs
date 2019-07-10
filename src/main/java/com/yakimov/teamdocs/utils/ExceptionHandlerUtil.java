package com.yakimov.teamdocs.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ExceptionHandlerUtil {
	public static Map<String, Object> generateBodyWithTimestampAndStatus(HttpStatus status){
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());
		
		return body;
	}
}
