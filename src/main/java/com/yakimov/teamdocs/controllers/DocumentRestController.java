package com.yakimov.teamdocs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.exceptions.DocumentNotFoundException;
import com.yakimov.teamdocs.repositories.DocumentRepository;
import com.yakimov.teamdocs.services.DocumentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/documents", produces = "application/json")
public class DocumentRestController {

	@Autowired
	private DocumentService documentService;

	@GetMapping("/{id}")
	public Document getDocumentById(@PathVariable Long id) {
		Document document = null;
		try {
			document = documentService.getDocumentById(id);
		} catch (DocumentNotFoundException e) {
			// TODO Auto-generated catch block
			log.debug(e.getMessage());
		}
		return document;
	}
	
	@GetMapping("/last/{identifier}")
	public Document getLastVarsionOfDocumentWithHash(@PathVariable String hash) {
		Document document = null;
		try {
			document = documentService.getLastVersionOfDocumentWith(hash);
		} catch (DocumentNotFoundException e) {
			// TODO Auto-generated catch block
			log.debug(e.getMessage());
		}
		return document;
	}

	@PostMapping("/")
	public Document saveDocument(@RequestBody Document document) {
		log.debug("Got request POST: " + document.toString());
		return documentService.saveDocument(document);
	}

	@PutMapping("/")
	public Document updateDocument(@RequestBody Document document, @PathVariable String hash) {
		return documentService.updateDocument(document);
	}
}
