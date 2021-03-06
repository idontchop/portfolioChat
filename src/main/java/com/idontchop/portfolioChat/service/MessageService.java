package com.idontchop.portfolioChat.service;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idontchop.portfolioChat.events.MessageEventHandler;
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
	
	Logger logger = LoggerFactory.getLogger(MessageService.class);
	
	@Autowired
	private MessageEventHandler messageEventHandler;
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private MessageRepository mRepo;
	
	@Autowired
	private MessageThreadRepository mtRepo;
	
	/*
	 * TODO: move this variable to external config 
	 */
	
	private final String NEWUSERMESSAGE = "Hello! \n\nWelcome to Nate's Portfolio!\n\n"
			+ "Check out my JWT enabled chat microservice code at my github, after dropping me a quick message.\n\n"
			+ "Thank you for stopping by!";

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
	 * Creates a thread from the authenticated user to the supplied target
	 * id. If the auth user does not have a saved user entity, it is
	 * created.
	 * 
	 * When an id is sent, the record must exist already (because we don't know name)
	 * 
	 * See overloaded methods in other cases.
	 * 
	 * @param id target user
	 * @param name the authenticated user
	 * @return
	 */
	public MessageThread createThread ( long id, String name ) throws IOException {
		
		User authUser = uRepo.findByName(name).orElseGet( () -> {
			return addUser (name);
		});
		
		MessageThread newMessageThread = 
				getThreadByTarget ( authUser.getId(), id);
		
		// If we are creating a new thread to the user with id of 1, create a welcome
		// message
		// Check also if message thread had problem being created
		
		if ( id == 1 && newMessageThread.getId() > 0 ) {
			addMessage ( NEWUSERMESSAGE, newMessageThread, uRepo.findById(id).orElseThrow() );
		}
		
		return newMessageThread;
		
	}
	
	public MessageThread createThread ( String name, String principalName)  {
		
		return getThreadByTarget ( principalName, name);
	}
	
	/**
	 * Creates a new message thread with the supplied member ids
	 * 
	 * Stays private for now, not sure we'd ever want a controller to call this
	 * 
	 * @param members Minimum of 2 ids corresponding to users
	 * @return the new message thread entity
	 */
	private MessageThread addThread ( List<Long> ids) throws IOException {
		
		// Find members and check we have them all
		List<User> members = (List<User>) uRepo.findAllById(ids);
		if ( members.size() != ids.size() )
			throw new IOException ("ids of all members not found in DB. Found: " + members.size() + ", Supplied: " + ids.size());
		
		MessageThread mt = new MessageThread();
		mt.setMembers(members);

		return mtRepo.save(mt);
	}
	
	public byte[] getImage ( long messageId, long messageThreadId) {
		Optional<Message> message = mRepo.findById(messageId);
		if ( message.isEmpty() ) {
			return null;
		} else {
			return message.get().getImage();
		}
		
	}
	
	/**
	 * No Message may be added without an existing message thread and sender id.
	 * This method adds a new message to the supplied messagethread id and sender
	 * id.
	 * 
	 * This method is intended to be called with messagethread and sender lookups already
	 * known. If a new entity is passed, likely result is an error as checks will fail
	 * null values will return null
	 * 
	 * @param content
	 * @param mtId id of thread
	 * @return new message entity or null
	 */
	public Message addMessage ( String content, MessageThread mt, User sender ) throws IOException {

		if ( mt == null || sender == null ) return null;
		
		// Check sender is in message thread
		// necessary for bad actors
		if ( ! mt.getMemberIds().contains(sender.getId()) )
			throw new IOException ("Sender not in thread.");
		
		// done with checks, construct message.
		
		Message newMessage = new Message();
		newMessage.setContent(content);
		newMessage.setSender(sender);
		newMessage.setMessageThread(mt);
				
		return mRepo.save(newMessage);
		
	}
	
	public Message addImageMessage ( byte[] imageBytes, String content, MessageThread mt, User sender ) throws IOException {
		
		if ( mt == null || sender == null ) return null;
		
		// Check sender is in message thread
		// necessary for bad actors
		if ( ! mt.getMemberIds().contains(sender.getId()) )
			throw new IOException ("Sender not in thread.");
		
		// done with checks, construct message.
		
		Message newMessage = new Message();
		newMessage.setContent(content);
		newMessage.setImage(imageBytes); // only difference from addMessage (need to DRY)
		newMessage.setSender(sender);
		newMessage.setMessageThread(mt);
				
		return mRepo.save(newMessage);
		
	}
	
	public Message addImageMessage ( byte[] imageBytes, String content, long mtId, String sender) throws IOException {
		
		User newSender = uRepo.findByName(sender).orElseThrow(IOException::new);
		MessageThread mt = mtRepo.findById(mtId).orElseThrow(IOException::new);
		
		return addImageMessage (imageBytes, content, mt, newSender);
		
	}
	
	/**
	 * Overloaded for ids only, will make another database call.
	 */
	public Message addMessage ( String content, long mtId, long sender ) throws IOException {
		
		// Get sender and mt by parameters
		User newSender = uRepo.findById(sender).orElseThrow(IOException::new);		
		MessageThread mt = mtRepo.findById(mtId).orElseThrow(IOException::new);
		
		return addMessage ( content, mt, newSender );
	}
	
	/**
	 * Overloaded for ids only, will make another database call.
	 */
	public Message addMessage ( String content, long mtId, String sender ) throws IOException {
		
		// Get sender and mt by parameters
		User newSender = uRepo.findByName(sender).orElseThrow(IOException::new);		
		MessageThread mt = mtRepo.findById(mtId).orElseThrow(IOException::new);
		
		return addMessage ( content, mt, newSender );
	}	
	
	/**
	 * Finds a message thread based on sender and target. This is the main method
	 * used when a user clicks on a message button. If it doesn't find an existing
	 * thread, it creates a new one.
	 * 
	 * This method checks for null, but does not check if the passed entity is
	 * in the database. This method shouldn't be called if the entity isn't
	 * created by a database call.
	 *  
	 * @param sender
	 * @param target
	 * @return
	 * @throws IOException 
	 */
	public MessageThread getThreadByTarget ( User sender, User target ) {
		
		// send me nulls, I return nulls
		if ( sender == null || target == null ) {
			logger.debug("getThreadByTarget received null: " + sender.getClass().toString() + ":" + target.getClass().toString());
			return null;
		}
		
		MessageThread mt = mtRepo.findByMembers(sender.getName(), target.getName()).orElseGet( () -> {
			
			// Couldn't find a thread so creating a new one
			logger.info("New Thread: " + sender.getId() + " " + target.getId() );
			MessageThread newMt = new MessageThread();
			newMt.addMember(sender);
			newMt.addMember(target);
			return mtRepo.save(newMt);
		});
		
		return mt;
	}
	
	/**
	 * Overloaded to find the members by their JWT name
	 * 
	 * In this case, we can add the user if it doesn't exist
	 * */
	public MessageThread getThreadByTarget ( String sender, String target ) {
		
		User senderId = uRepo.findByName(sender).orElseGet( () -> {
			return addUser (sender);
		});
		
		User targetId = uRepo.findByName(target).orElseGet( () -> {
			return addUser (target);
		});
		
		return getThreadByTarget ( senderId, targetId );
	}
	
	/**
	 * Overloaded to find the members by their id
	 * */
	public MessageThread getThreadByTarget ( long sender, long target ) {
		
		User senderId = uRepo.findById(sender).orElse(null);
		User targetId = uRepo.findById(target).orElse(null);
		
		return getThreadByTarget ( senderId, targetId );
	}
	
	/**
	 * Finds the number of unseen messages in a thread for the current user.
	 * This is used in the message thread model to support field in rest data.
	 * @param messageThreadId
	 * @return int # of unseen messages
	 */
	public int getUnseenMessages(long messageThreadId) {
		return mRepo.unSeen(messageThreadId);
	}
	
	/**
	 * Sets all messages seen on the supplied thread. Will only set seen on messages
	 * not sent by user.
	 * 
	 * @param messageThreadId
	 * @param principal 
	 */
	public void setSeenMessages(long messageThreadId, Principal principal) {
		
		mRepo.setSeen(messageThreadId, principal.getName());
	}
}
