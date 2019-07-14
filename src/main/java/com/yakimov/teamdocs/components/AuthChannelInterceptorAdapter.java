package com.yakimov.teamdocs.components;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession;

import com.yakimov.teamdocs.services.WebSocketAuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
	private final String USERNAME_HEADER = "login";
	private final String PASSWORD_HEADER = "passcode";
	
	private WebSocketAuthService auth;

	public AuthChannelInterceptorAdapter(WebSocketAuthService auth) {
		this.auth = auth;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) throws AuthenticationException{
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		log.debug(message.getHeaders().toString());
		if(StompCommand.CONNECT.equals(accessor.getCommand())) {
			String token = accessor.getFirstNativeHeader("Authorization");
			log.debug(token);
			final var credentials = auth.getAuthenticatedOrFail(token);
			
			SecurityContextHolder.getContext().setAuthentication(credentials);
			accessor.setUser(credentials);
		}
		return message;
	}

	@Override
	public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
		log.debug("Message " + message.toString() + "were sent");
	}

	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
		log.debug("Message " + message.toString() + "were recieved");
	}

}
