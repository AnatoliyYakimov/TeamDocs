package com.yakimov.teamdocs.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yakimov.teamdocs.entities.ApplicationUser;
import com.yakimov.teamdocs.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserRestController {
	
	private UserRepository repository;
	private BCryptPasswordEncoder encoder;

	public UserRestController(UserRepository repository, BCryptPasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	@PostMapping("/sign-up")
	public void signup(@RequestBody ApplicationUser user) {
		user.setPassword(encoder.encode(user.getPassword()));
		repository.save(user);
	}
	
	
}
