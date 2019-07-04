package com.yakimov.teamdocs.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.repositories.DocumentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseInitializer {

	@Bean
	CommandLineRunner loadData(DocumentRepository documentRepository) {
		return args -> {
			List<Document> docs = Arrays.asList(new Document("Doc1", "Sometext"), new Document("Doc2", "Sometext"),
					new Document("Doc3", "Sometext"), new Document("Doc4", "Sometext"));
			documentRepository.saveAll(docs);
			log.debug("Database initialized");
		};
	};

}
