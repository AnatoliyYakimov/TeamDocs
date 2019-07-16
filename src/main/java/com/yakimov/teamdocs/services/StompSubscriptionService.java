package com.yakimov.teamdocs.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.yakimov.teamdocs.entities.StompEvent;
import com.yakimov.teamdocs.entities.WsMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StompSubscriptionService {

	private SimpMessagingTemplate messageSender;

	public StompSubscriptionService(SimpMessagingTemplate messageSender) {
		this.messageSender = messageSender;
	}

	private final Map<String, Set<String>> subscriptions = new ConcurrentHashMap<String, Set<String>>();
	private final Map<String, String> subId_dest = new ConcurrentHashMap<>();

	public void subscribeUser(String username, String destination, String subId) {

		if (subscriptions.containsKey(destination)) {
			subscriptions.get(destination).add(username);
		} else {
			var subcribedUsers = new CopyOnWriteArraySet<String>();
			subcribedUsers.add(username);
			subscriptions.put(destination, subcribedUsers);
		}
		subId_dest.put(subId, destination);
		log.debug(username + " subscribed to " + destination);
		messageSender.convertAndSend("/queue/reply-" + username,
				new WsMessage<Set<String>>(StompEvent.ALL_SUBS, getSubscribedUsers(destination)));
		messageSender.convertAndSend(destination, new WsMessage<String>(StompEvent.USER_SUB, username));
	}

	public void unsubscribeUser(String username, String subId) {
		if (subId_dest.containsKey(subId)) {
			String destination = subId_dest.get(subId);
			if (subscriptions.containsKey(destination)) {
				log.debug(username + " unsubscribed from " + destination);
				Set<String> users = subscriptions.get(destination);
				if (users != null) {
					users.remove(username);
				}
				messageSender.convertAndSend(destination, new WsMessage<String>(StompEvent.USER_UNSUB, username));
			}
		}

	}

	public void unsubscribeUser(String username) {
		this.subscriptions.entrySet().forEach((e) -> {
			e.getValue().remove(username);
			messageSender.convertAndSend(e.getKey(), new WsMessage<String>(StompEvent.USER_UNSUB, username));
		});

	}

	public Set<String> getSubscribedUsers(String destination) {
		if (subscriptions.containsKey(destination)) {
			return new HashSet<String>(subscriptions.get(destination));
		} else {
			return Collections.emptySet();
		}
	}

}
