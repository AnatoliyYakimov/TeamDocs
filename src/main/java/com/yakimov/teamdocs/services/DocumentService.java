package com.yakimov.teamdocs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;
import com.yakimov.teamdocs.repositories.DocumentRepository;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepository documentRepository;
	
	public Document saveDocument(Document document) {
		return document = documentRepository.save(document);
	}
	
	public Document updateDocument(Document document) {
		document.setId(null);
		return documentRepository.save(document);
	}
	
	public Document getDocumentById(Long id) throws DocumentNotFoundException {
		Optional<Document> document = documentRepository.findById(id);
		return document.orElseThrow(() -> new DocumentNotFoundException());
	}
	Document getLastVersionOfDocumentWith(String identifier) throws DocumentNotFoundException {
		Optional<Document> document = documentRepository.findLastVersionOfDocumentWith(identifier);
		return document.orElseThrow(() -> new DocumentNotFoundException());
	}
}
