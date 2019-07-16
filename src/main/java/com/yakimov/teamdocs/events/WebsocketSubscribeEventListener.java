package com.yakimov.teamdocs.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.yakimov.teamdocs.services.StompSubscriptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebsocketSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {
	private StompSubscriptionService stompSubscriptionService;
	
	@Autowired
	public WebsocketSubscribeEventListener(StompSubscriptionService stompSubscriptionService) {
		this.stompSubscriptionService = stompSubscriptionService;
	}

	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		var message = event.getMessage();
		var accessor = StompHeaderAccessor.wrap(message);
		if(accessor.getDestination().matches("/topic/document.*")) {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			stompSubscriptionService.subscribeUser(username, accessor.getDestination(), accessor.getSubscriptionId());
			log.debug(username + " subscribed to the channel");
		}
		
	}

}
