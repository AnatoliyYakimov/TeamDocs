package com.yakimov.teamdocs.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yakimov.teamdocs.entities.WsMessage;
import com.yakimov.teamdocs.entities.StompEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StompSubscriptionService {
	
	private MessageSendingOperations<String> messageSender;
	
	public StompSubscriptionService(MessageSendingOperations<String> messageSender) {
		this.messageSender = messageSender;
	}

	private final Map<String, List<String>> subscriptions = new ConcurrentHashMap<String, List<String>>();
	
	public void subscribeUser(String username, String destination) {
		
		if(subscriptions.containsKey(destination)) {
			subscriptions.get(destination).add(username);
		}else {
			var subcribedUsers = new CopyOnWriteArrayList<String>();
			subcribedUsers.add(username);
			subscriptions.put(destination, subcribedUsers);
		}
		log.debug(username + " subscribed to " + destination); 
		String userPath = String.format("/user/%s/%s", username, destination);
		log.debug(userPath);
		messageSender.convertAndSend(userPath, new WsMessage<List<String>>(StompEvent.ALL_SUBS, getSubscribedUsers(destination)));
		messageSender.convertAndSend(destination, new WsMessage<String>(StompEvent.USER_SUB, username));
	}
	
	public void unsubscribeUser(String username, String destination) {
		if(subscriptions.containsKey(destination)) {
			log.debug(username + " unsubscribed from " + destination); 
			subscriptions.get(destination).remove(username);
			messageSender.convertAndSend(destination, new WsMessage<String>(StompEvent.USER_UNSUB, username));
		}
	}
	
	public List<String> getSubscribedUsers(String destination){
		if(subscriptions.containsKey(destination)) {
			return new ArrayList<String>(subscriptions.get(destination));
		}else {
			return Collections.emptyList();
		}
	}
}
