package com.yakimov.teamdocs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.entities.DocumentModel;
import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;
import com.yakimov.teamdocs.services.DocumentService;
import com.yakimov.teamdocs.utils.DocumentResourceAssembler;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/documents", produces = "application/json")
public class DocumentRestController {

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private DocumentResourceAssembler documentAssembler;

	@GetMapping("/{id}")
	public HttpEntity<DocumentModel> getDocumentById(@PathVariable Long id) throws DocumentNotFoundException {
		Document document = documentService.getDocumentById(id);
		return ResponseEntity.ok(documentAssembler.toModel(document));
	}

	@GetMapping("/last/{hash}")
	public HttpEntity<Document> getLastVarsionOfDocumentByHash(@PathVariable String hash) throws DocumentNotFoundException {
		Document document = documentService.getLastVersionOfDocumentWith(hash);
		return ResponseEntity.ok(document);
	}

	@GetMapping("/history/{hash}")
	public HttpEntity<?> getHistoryOfDocumentByHash(@PathVariable String hash, Pageable page){
		Page<Document> documents = documentService.getDocumentHistory(hash, page);
		var resp = ResponseEntity.ok(documentAssembler.toPagedModel(documents));
		return resp;
	}
	
	@PostMapping("/")
	public Document saveDocument(@RequestBody Document document) {
		log.debug("Got request POST: " + document.toString());
		return documentService.saveDocument(document);
	}
	
	

}
