package com.yakimov.teamdocs.exceptions;


@SuppressWarnings("serial")
public class DocumentNotFoundException extends Exception{
	private static final String MESSAGE_TEMPLATE = "Document with %s '%s' not found";
	private String message;
	
	public DocumentNotFoundException(String hash) {
		message = String.format(MESSAGE_TEMPLATE, "hash", hash);
	}
	
	public DocumentNotFoundException(Long id) {
		message = String.format(MESSAGE_TEMPLATE, "ID", id.toString());
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}
