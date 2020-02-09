package com.idontchop.portfolioChat.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idontchop.portfolioChat.events.MessageEventHandler;
import com.idontchop.portfolioChat.model.Message;

@Component
@Aspect
public class MessageNotifications {
	Logger logger = LoggerFactory.getLogger(MessageNotifications.class);
	
	@Autowired
	private MessageEventHandler messageEventHandler;
	
	public MessageNotifications () {
		logger.info ("Message Notifications initialized.");
	}

	@Pointcut ( "execution ( * com.idontchop.portfolioChat.repositories.MessageRepository.save(..))")
	public void saveMessage() {}
	
	@After ( "saveMessage () &&" + "args(msg,..)")
	public void notifyNewMessage ( JoinPoint jp, Message msg) {
		messageEventHandler.newMessage(msg);
	}
	
}
