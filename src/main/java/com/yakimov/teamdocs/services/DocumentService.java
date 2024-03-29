package com.yakimov.teamdocs.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.entities.StompEvent;
import com.yakimov.teamdocs.entities.WsMessage;
import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;
import com.yakimov.teamdocs.repositories.DocumentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentService {
	private DocumentRepository documentRepository;

	private SimpMessagingTemplate messageSender;
	
	public DocumentService(DocumentRepository documentRepository,
			SimpMessagingTemplate messageSender) {
		this.documentRepository = documentRepository;
		this.messageSender = messageSender;
	}
	

	public Document saveDocument(Document document) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String hash = document.getHash();
		document.setId(null);
		document.setCreatedAt(documentRepository.getDocumentCreationTime(hash));
		document.setAuthor(documentRepository.getAuthorOfDocument(hash));
		document.setModifiedBy(username);
		document = documentRepository.save(document);
		String destination = "/topic/document." + document.getHash();
		messageSender.convertAndSend(destination, new WsMessage<Document>(StompEvent.NEW_DOCUMENT, document) );
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
