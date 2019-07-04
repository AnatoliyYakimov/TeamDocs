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
import com.yakimov.teamdocs.repositories.DocumentRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/documents", produces = "application/json")
public class DocumentRestController {

	@Autowired
	private DocumentRepository documentRepository;

	@GetMapping("/{id}")
	public Document getDocumentById(@PathVariable Long id) {
		var document = documentRepository.findById(id);
		return document.get();
	}

	@PostMapping("/")
	public Document saveDocument(@RequestBody Document document) {
		log.debug("Got request POST: " + document.toString());
		document = documentRepository.save(document);
		document.setOriginalDocumentId(document.getId());
		return documentRepository.save(document);
	}

	@PutMapping("/{id}")
	public void updateDocument(Document document, @PathVariable Long id) {
		document.setOriginalDocumentId(id);
		document.setId(null);
		documentRepository.save(document);
	}
}
