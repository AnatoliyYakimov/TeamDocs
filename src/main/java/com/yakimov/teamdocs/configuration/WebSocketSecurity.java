package com.yakimov.teamdocs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;

@Configuration
@EnableWebSecurity
public class WebSocketSecurity extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages.simpTypeMatchers(SimpMessageType.CONNECT).authenticated();
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}


	@Override
	public SecurityContextChannelInterceptor securityContextChannelInterceptor() {
		// TODO Auto-generated method stub
		return new SecurityContextChannelInterceptor();
	}


}
