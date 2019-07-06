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
		document.setId(null);
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
	public Document getLastVersionOfDocumentWith(String hash) throws DocumentNotFoundException {
		Optional<Document> document = documentRepository.findLastVersionOfDocumentWith(hash);
		return document.orElseThrow(() -> new DocumentNotFoundException());
	}
}
