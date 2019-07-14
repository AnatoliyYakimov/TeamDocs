package com.yakimov.teamdocs.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WsMessage<T> {
	
	private StompEvent event;
	private T data;
	
}
