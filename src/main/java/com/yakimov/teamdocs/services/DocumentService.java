package com.yakimov.teamdocs.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
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

	private MessageSendingOperations<String> messageSender;
	
	public DocumentService(DocumentRepository documentRepository,
			MessageSendingOperations<String> messageSender) {
		this.documentRepository = documentRepository;
		this.messageSender = messageSender;
	}
	
	/*
	 * @Scheduled(fixedDelay = 10000) public void test() {
	 * log.debug("Scheduled test"); messageSender.convertAndSend("/topic/test", new
	 * StompMessagePayload<String>(StompEvent.USER_SUB, "Payload")); }
	 */
	
	public Document saveDocument(Document document) {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String hash = document.getHash();
		document.setId(null);
		document.setCreatedAt(documentRepository.getDocumentCreationTime(hash));
		document.setAuthor(documentRepository.getAuthorOfDocument(hash));
		document.setModifiedBy(username);
		document = documentRepository.save(document);
		String destination = "/topic/document." + document.getHash();
		messageSender.convertAndSend("/topic/test", document);
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
