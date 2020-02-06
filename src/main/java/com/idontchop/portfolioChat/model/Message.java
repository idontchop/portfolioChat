package com.idontchop.portfolioChat.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Message {
	
	public enum MesType {
		MESSAGE,
		IMAGE,
		JOIN,
		LEAVE
	}

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne
	private MessageThread messageThread;
	
	@ManyToOne
	private User sender;
	
	private MesType type = MesType.MESSAGE;
	
	private String content;
	
	private Date created = new Date();
	
	/*
	 * TODO: For the portfolio which is always two-way chats, having a 
	 * seen variable works. Later if I want to develop this into a flexible
	 * chat mod with multiple chat members, this will need to be updated.
	 * 
	 * My idea:
	 * Seen table
	 *  message_id composite key
	 *  user_id    composite key
	 *  created
	 *  
	 * Then to reduce cycles and manage db size, add a variable seenByAll
	 * here which would allow deletion of above entries, or leave the entries
	 * so members could see when which member saw the message (when requested
	 * only, leave the seenByAll variable for faster loading of chat)
	 */
	private Date seen = null;

	public Message () {
		
	}
	
	public void seen() {
		seen = new Date();
	}
	
	public Date getSeen() {
		return seen;
	}

	public void setSeen(Date seen) {
		this.seen = seen;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public MesType getType() {
		return type;
	}

	public void setType(MesType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public MessageThread getMessageThread() {
		return messageThread;
	}

	public void setMessageThread(MessageThread messageThread) {
		this.messageThread = messageThread;
	}
	
	
}
