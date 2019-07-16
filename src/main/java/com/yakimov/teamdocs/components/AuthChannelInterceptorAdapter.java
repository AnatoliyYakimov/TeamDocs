package com.yakimov.teamdocs.components;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.yakimov.teamdocs.services.WebSocketAuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

	private static final Map<String, Authentication> users = new ConcurrentHashMap<>();

	private WebSocketAuthService auth;

	public AuthChannelInterceptorAdapter(WebSocketAuthService auth) {
		this.auth = auth;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) throws AuthenticationException {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		Message<?> msgToReturn = message;
		switch (accessor.getCommand()) {
		case CONNECT:
			String token = accessor.getFirstNativeHeader("Authorization");
			final var credentials = auth.getAuthenticatedOrFail(token);

			SecurityContextHolder.getContext().setAuthentication(credentials);
			users.put(accessor.getSessionId(), credentials);
			accessor.setUser(credentials);

			msgToReturn = MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
			break;
		case DISCONNECT:
			users.remove(accessor.getSessionId());
			break;
		default:
			Authentication user = users.get(accessor.getSessionId());
			SecurityContextHolder.getContext().setAuthentication(user);
			accessor.setUser(user);
			msgToReturn = MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
		}
		return msgToReturn;
	}


}
