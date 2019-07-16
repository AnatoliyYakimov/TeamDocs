package com.yakimov.teamdocs.exceptions;

import java.lang.Thread.UncaughtExceptionHandler;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class CustomGlobalExceptionHandler implements UncaughtExceptionHandler {

	@Override
	@ExceptionHandler(value = Throwable.class)
	public void uncaughtException(Thread t, Throwable e) {
		log.error("Caught exception in thread '{}':{}" +
	"\n Message:{}\n Caused by:{}", t.getName(),e.toString(), e.getMessage(), e.getCause().toString());
	}

}
