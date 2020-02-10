package com.idontchop.portfolioChat.events;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.idontchop.portfolioChat.model.Message;

@Component
public class MessageEventHandler {
	Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);
	
	private final SimpMessagingTemplate websocket;
	
	public MessageEventHandler ( SimpMessagingTemplate websocket ) {
		logger.info("Event Handler on User.class initialized");
		this.websocket = websocket;
	}
	
	public void newMessage ( Message message ) {
		
		// send notification of new message to every member of thread
		message.getMessageThread().getMembers().forEach( m -> {
			logger.info("websocket send to " + m.getId());
			websocket.convertAndSendToUser(m.getName(),
					"/secured/user/queue/specific-user", message);
		});
	}
	

}
