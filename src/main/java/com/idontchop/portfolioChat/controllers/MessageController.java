package com.idontchop.portfolioChat.controllers;


import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idontchop.portfolioChat.model.Message;
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
	
	@Autowired
	private final SimpUserRegistry simpUserRegistry;
	
    public MessageController(SimpUserRegistry simpUserRegistry) {
        this.simpUserRegistry = simpUserRegistry;
    }
	
	@PostMapping ("/newThread")
	public MessageThread addThread (
			@RequestParam long id,
			Principal principal ) throws IOException {
		
		return messageService.createThread(id, principal.getName());
		
	}
	
	@PostMapping ("/newThreadToName/{name}")
	public MessageThread addThreadToName (
			@PathVariable String name, Principal principal) {
		
		return messageService.createThread(name, principal.getName());
	}
			
	
	@PostMapping ("/newTextMessage/{messageThreadId}")
	public Message addTextMessage (
			 @PathVariable long messageThreadId, @RequestParam String content, Principal principal) throws IOException {
				
		return messageService.addMessage(content, messageThreadId, principal.getName());
	}
	
	/**
	 * Sets the seen field in messages entity 
	 * 
	 * called by frontend when there is a successful downlaod of a message thread.
	 * @return
	 */
	@PutMapping ("/seenThread/{messageThreadId}")
	public ResponseEntity<String> getThread (
			@PathVariable long messageThreadId, Principal principal) {
		
		messageService.setSeenMessages(messageThreadId, principal);
		
		return ResponseEntity.ok("{ \"message\": \"" + principal.getName() + "\" }");
	}
	
	@GetMapping ( "/unSeen/{messageThreadId}")
	public ResponseEntity<String> getUnSeen (
			@PathVariable long messageThreadId) {
		
		return ResponseEntity.ok("{ \"num\": \"" + messageService.getUnseenMessages(messageThreadId) + "\" }");
	}
	
}
