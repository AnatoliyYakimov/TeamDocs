package com.yakimov.teamdocs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Service;

import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;
import com.yakimov.teamdocs.repositories.DocumentRepository;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private MessageSendingOperations<String> messageSender;
	
	public Document saveDocument(Document document) {
		document.setId(null);
		document = documentRepository.save(document);
		String destination = "/topic/document." + document.getHash();
		messageSender.convertAndSend(destination, document);
		return document;
	}
	
	public Document getDocumentById(Long id) throws DocumentNotFoundException {
		Optional<Document> document = documentRepository.findById(id);
		return document.orElseThrow(() -> new DocumentNotFoundException(id));
	}
	public Document getLastVersionOfDocumentWith(String hash) throws DocumentNotFoundException {
		Optional<Document> document = documentRepository.findLastVersionOfDocumentWith(hash);
		return document.orElseThrow(() -> new DocumentNotFoundException(hash));
	}
	
	public Page<Document> getDocumentHistory(String hash, Pageable page){
		return documentRepository.findAllByHash(hash, page);
	}
}
