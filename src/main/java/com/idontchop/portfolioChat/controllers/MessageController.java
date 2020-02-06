package com.idontchop.portfolioChat.controllers;


import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idontchop.portfolioChat.model.MessageThread;
import com.idontchop.portfolioChat.service.MessageService;

/**
 * Exposes endpoints for service requests when a new message / thread / user
 * is needed.
 * 
 * @author nathan
 *
 */
@RestController
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@PostMapping ("/newThread")
	public MessageThread addThread (
			@RequestParam long id,
			Principal principal ) throws IOException {
		
		return messageService.createThread(id, principal.getName());
		
	}
	
}
