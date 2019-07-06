package com.yakimov.teamdocs.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DocumentStompController {
	
	@SubscribeMapping("/topic/document.*")
	public void subscribeToDocumentChanges(@PathVariable String hash) {
		log.debug("Stomp controller got subscribe with hash: " + hash);
	}
		
}
