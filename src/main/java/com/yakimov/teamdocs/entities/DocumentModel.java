package com.yakimov.teamdocs.entities;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentModel extends RepresentationModel<DocumentModel>{
	
	public DocumentModel(Document document) {
		super();
		this.hash = document.getHash();
		this.createdAt = document.getCreatedAt();
		this.updatedAt = document.getUpdatedAt();
		this.name = document.getName();
		this.text = document.getText();
	}
	private String hash;
	
	private Date createdAt;
	private Date updatedAt;
	
	private String name;
	private String text;
	
}
