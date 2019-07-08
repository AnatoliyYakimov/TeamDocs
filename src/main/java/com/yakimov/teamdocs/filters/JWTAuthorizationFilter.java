package com.yakimov.teamdocs.filters;

import static com.yakimov.teamdocs.utils.SecurityConstraints.HEADER_STRING;
import static com.yakimov.teamdocs.utils.SecurityConstraints.SECRET;
import static com.yakimov.teamdocs.utils.SecurityConstraints.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(HEADER_STRING);
		if(header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken authentification = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentification);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
		String user = JWT.require(Algorithm.HMAC512(SECRET))
				.build()
				.verify(token)
				.getSubject();
		if(user != null) {
			return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<GrantedAuthority>());
		}else {
			return null;
		}
	}

}
