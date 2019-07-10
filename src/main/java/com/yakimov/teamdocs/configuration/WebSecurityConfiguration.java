package com.yakimov.teamdocs.configuration;

import static com.yakimov.teamdocs.utils.SecurityConstraints.SIGN_UP_URL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.yakimov.teamdocs.filters.AuthenticationFilter;
import com.yakimov.teamdocs.filters.JWTAuthorizationFilter;
import com.yakimov.teamdocs.services.UserDetailsServiceImpl;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
	@Qualifier(value = "userDetailsServiceImpl")
	private UserDetailsServiceImpl userDetailsService;
	private BCryptPasswordEncoder encoder;
	
	public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder encoder) {
		this.userDetailsService = userDetailsService;
		this.encoder = encoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
			.antMatchers("/", "/javascript/**", "/stylesheets/**", "/fonts/**", "/favicon.ico").permitAll()
			.antMatchers(HttpMethod.POST, SIGN_UP_URL, "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(new AuthenticationFilter(authenticationManager()))
			.addFilter(new JWTAuthorizationFilter(authenticationManager()))
			//Disable session creation
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(){
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
}
