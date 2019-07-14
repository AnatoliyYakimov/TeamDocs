package com.yakimov.teamdocs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.yakimov.teamdocs.services.StompSubscriptionService;

@Component
public class WebSocketUnsubEventListener implements ApplicationListener<SessionUnsubscribeEvent> {
	
	@Autowired
	public WebSocketUnsubEventListener(StompSubscriptionService stompSubscriptionService) {
		this.stompSubscriptionService = stompSubscriptionService;
	}

	private StompSubscriptionService stompSubscriptionService;

	@Override
	public void onApplicationEvent(SessionUnsubscribeEvent event) {
		var message = event.getMessage();
		String username = event.getUser() != null ? event.getUser().getName() : "anonymous";
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		stompSubscriptionService.unsubscribeUser(username, accessor.getDestination());
	}

}
