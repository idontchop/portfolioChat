package com.idontchop.portfolioChat.model;

import javax.persistence.Entity;

@Entity
public class Message {
	
	public enum MessageType {
		MESSAGE,
		IMAGE,
		JOIN,
		LEAVE
	}

	private MessageType type;
	
	private String 
}
