package com.yakimov.teamdocs.filters;

import static com.yakimov.teamdocs.utils.SecurityConstraints.EXPIRATION_TIME;
import static com.yakimov.teamdocs.utils.SecurityConstraints.HEADER_STRING;
import static com.yakimov.teamdocs.utils.SecurityConstraints.SECRET;
import static com.yakimov.teamdocs.utils.SecurityConstraints.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yakimov.teamdocs.entities.ApplicationUser;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	
	@Bean
	public AuthenticationFilter authentificationFilter(AuthenticationManager authenticationManager) {
		return new AuthenticationFilter(authenticationManager);
	}
	
	AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			ApplicationUser user = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>()));
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String token = JWT.create()
				.withSubject(((User) authResult.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SECRET));
		response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
	}

}
