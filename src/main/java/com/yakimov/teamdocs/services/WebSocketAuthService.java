package com.yakimov.teamdocs.services;


import static com.yakimov.teamdocs.utils.SecurityConstraints.SECRET;

import java.util.Collections;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yakimov.teamdocs.utils.SecurityConstraints;

@Component
public class WebSocketAuthService {
	
	public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String token) throws AuthenticationException {
		String normalizedToken = token.replace(SecurityConstraints.TOKEN_PREFIX, "");
		String user = JWT.require(Algorithm.HMAC512(SECRET))
				.build()
				.verify(normalizedToken)
				.getSubject();
		 if(user != null) {
			return new UsernamePasswordAuthenticationToken(user, null, Collections.singleton((GrantedAuthority) () -> "USER"));
		 }
		 else {
			 throw new BadCredentialsException("Bad token in STOMP CONNECT message");
		 }
    }
}
