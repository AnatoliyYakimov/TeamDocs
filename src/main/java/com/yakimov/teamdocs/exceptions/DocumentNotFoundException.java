package com.yakimov.teamdocs.exceptions;

public class DocumentNotFoundException extends Exception{
	private final String MESSAGE = "Document not found";
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return MESSAGE;
	}
	
}
