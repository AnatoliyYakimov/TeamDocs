package com.yakimov.teamdocs;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.repositories.DocumentRepository;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Slf4j
public class DataTest {
	@Autowired
	private DocumentRepository documentRepository;
	
	private final static List<Document> docs = Arrays.asList(
			new Document("Doc1", "Sometext"),
			new Document("Doc2", "Sometext"),
			new Document("Doc3", "Sometext"),
			new Document("Doc4", "Sometext")
	);
	
	@BeforeEach
	public void initDatabase() {
		documentRepository.deleteAll();
		documentRepository.saveAll(docs);
		log.debug("Database initialized successefully");
	}
	
	@Test
	public void testSaveDocument() {
		Document doc = new Document("Doc", "Empty");
		doc = documentRepository.save(doc);
		
		var loaded = documentRepository.findById(doc.getId());
		
		assertNotNull(loaded);
		assertTrue(loaded.isPresent());
		assertNotNull(loaded.get().getCreatedAt());
		assertNotNull(loaded.get().getUpdatedAt());
		
		log.debug("Loaded document: " + loaded.get().toString());
	}
	
	@Test
	public void testFindLastUpdated() {
		Document doc = new Document("Doc", "SomeText");
		doc = documentRepository.save(doc);
		log.debug("Doc1  " + doc.toString());
		Document doc2 = new Document("NewDoc", "SomeText");
		doc2.setHash(doc.getHash());
		doc2 = documentRepository.save(doc2);
		log.debug("Doc2  " + doc2.toString());
		var founded = documentRepository.findLastVersionOfDocumentWith(doc2.getHash());
		log.debug(founded.get().toString());
		assertNotNull(founded);
		assertTrue(founded.isPresent());
		assertTrue(founded.get().getName().equals("NewDoc"));
		log.debug("Founded: " + founded.get().toString());
	}
	
}
