package com.yakimov.teamdocs.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.yakimov.teamdocs.services.StompSubscriptionService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class WebSocketUnsubEventListener implements ApplicationListener<SessionUnsubscribeEvent> {
	
	@Autowired
	public WebSocketUnsubEventListener(StompSubscriptionService stompSubscriptionService) {
		this.stompSubscriptionService = stompSubscriptionService;
	}

	private StompSubscriptionService stompSubscriptionService;

	@Override
	public void onApplicationEvent(SessionUnsubscribeEvent event) {
		log.debug("User unsub");
		var message = event.getMessage();
		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		if (user != null) {
			log.debug(user.getName());
			StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
			stompSubscriptionService.unsubscribeUser(user.getName(), accessor.getSubscriptionId());
		}
		
	}

}
