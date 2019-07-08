package com.yakimov.teamdocs.repositories;

import org.springframework.data.repository.CrudRepository;

import com.yakimov.teamdocs.entities.ApplicationUser;

public interface UserRepository extends CrudRepository<ApplicationUser, Long> {
	public ApplicationUser findByUsername(String username);
}
