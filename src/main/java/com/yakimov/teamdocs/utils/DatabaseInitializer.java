package com.yakimov.teamdocs.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.yakimov.teamdocs.entities.ApplicationUser;
import com.yakimov.teamdocs.entities.Document;
import com.yakimov.teamdocs.repositories.DocumentRepository;
import com.yakimov.teamdocs.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseInitializer {

	@Bean
	CommandLineRunner loadData(DocumentRepository documentRepository, UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			List<Document> docs = Arrays.asList(new Document("Doc1", "Sometext"), new Document("Doc2", "Sometext"),
					new Document("Doc3", "Sometext"), new Document("Doc4", "Sometext"));
			documentRepository.saveAll(docs);
			userRepository.save(new ApplicationUser("admin", encoder.encode("admin")));
			userRepository.save(new ApplicationUser("User1", encoder.encode("qwerty")));
			userRepository.save(new ApplicationUser("User2", encoder.encode("qwerty")));
			userRepository.save(new ApplicationUser("User3", encoder.encode("qwerty")));
			log.debug("Database initialized");
		};
	};

}
