package com.yakimov.teamdocs.events;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.yakimov.teamdocs.services.StompSubscriptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketDisconnetEventListener implements ApplicationListener<SessionDisconnectEvent> {
	
	private StompSubscriptionService service;
	
	

	public WebSocketDisconnetEventListener(StompSubscriptionService service) {
		this.service = service;
	}



	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		var user = SecurityContextHolder.getContext().getAuthentication();
		if(user != null) {
			log.debug("Unsubscribing user " + user.getName());
			service.unsubscribeUser(user.getName());
		}
	}

}
