package com.idontchop.portfolioChat.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.portfolioChat.model.Message;
import com.idontchop.portfolioChat.model.MessageThread;
import com.idontchop.portfolioChat.model.User;
import com.idontchop.portfolioChat.repositories.MessageRepository;
import com.idontchop.portfolioChat.repositories.MessageThreadRepository;
import com.idontchop.portfolioChat.repositories.UserRepository;

/**
 * Handles business logic when we need more than what the exposed
 * data rest endpoints provide.
 * 
 * 1 ) 	Creates a new message and checks that the user is registered, message thread
 * 		exists. Creates users and threads where necessary.
 * 
 *
 * @author nathan
 *
 */
@Service
public class MessageService {
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private MessageRepository mRepo;
	
	@Autowired
	private MessageThreadRepository mtRepo;

	/**
	 * Adds a user to the database
	 * 
	 * @param name of user supplied by spring security
	 * @return The full entity of new user or null
	 */
	public User addUser ( String name ) {
		
		User newUser = new User ();
		newUser.setName(name);
		
		return uRepo.save(newUser);
	}
	
	/**
	 * Creates a new message thread with the supplied member ids
	 * 
	 * @param members Minimum of 2 ids corresponding to users
	 * @return the new message thread entity
	 */
	public MessageThread addThread ( List<Long> ids) throws IOException {
		
		// Find members and check we have them all
		List<User> members = (List<User>) uRepo.findAllById(ids);
		if ( members.size() != ids.size() )
			throw new IOException ("ids of all members not found in DB. Found: " + members.size() + ", Supplied: " + ids.size());
		
		MessageThread mt = new MessageThread();
		mt.setMembers(members);

		return mtRepo.save(mt);
	}
	
	/**
	 * No Message may be added without an existing message thread and sender id.
	 * This method adds a new message to the supplied messagethread id and sender
	 * id.
	 * 
	 * @param content
	 * @param mtId id of thread
	 * @return new message entity or null
	 */
	public Message addMessage ( String content, long mtId, long sender ) throws IOException {
				
		// Get sender and mt by parameters
		User newSender = uRepo.findById(sender).orElseThrow(IOException::new);		
		MessageThread mt = mtRepo.findById(mtId).orElseThrow(IOException::new);
		
		// Check sender is in message thread
		// necessary for bad actors
		if ( ! mt.getMemberIds().contains(sender) )
			throw new IOException ("Sender not in thread.");
		
		// done with checks, construct message.
		
		Message newMessage = new Message();
		newMessage.setContent(content);
		newMessage.setSender(newSender);
		
		return mRepo.save(newMessage);
		
	}
	
	/**
	 * Finds a message thread based on sender and target. This is the main method
	 * used when a user clicks on a message button. If it doesn't find an existing
	 * thread, it creates a new one.
	 *  
	 * @param sender
	 * @param target
	 * @return
	 */
	public MessageThread getThreadByTarget ( long sender, long target ) {
		
		return null;
	}
}