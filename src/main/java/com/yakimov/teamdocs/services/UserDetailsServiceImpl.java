package com.yakimov.teamdocs.services;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yakimov.teamdocs.entities.ApplicationUser;
import com.yakimov.teamdocs.repositories.UserRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private UserRepository repository;

	public UserDetailsServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser user = repository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(username, user.getPassword(), Collections.singleton((GrantedAuthority) () -> "USER"));
	}

}
