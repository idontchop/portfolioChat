package com.idontchop.portfolioChat.repositories;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import com.idontchop.portfolioChat.model.Message;
import com.idontchop.portfolioChat.model.MessageThread;

/**
 * Repositories override Spring Data Rest's default endpoints by adding
 * queries that return results based on the principal's name which is
 * supplied via JWT.
 * 
 * Save and delete endpoints are disabled and will be managed by custom
 * controller endpoints.
 * 
 * Security: All additions to this repository must check for permissions
 * from the authorized user. Message in thread that contains the user.
 * 
 * @author nathan
 *
 */
public interface MessageRepository extends PagingAndSortingRepository<Message, Long > {
	

	@PreAuthorize ( "@messageThreadRepository.findOne(#mtId) != null")
	@Query ( value = "SELECT COUNT(*) FROM Message m WHERE m.messageThread.id = :mtId  AND m.sender.name != ?#{principal}")
	int unSeen(long mtId);
	
	@PreAuthorize ( "@messageThreadRepository.findOne(#mtId) != null")
	@Transactional
	@Modifying
	@Query ( value = "update message m JOIN chat_user u on u.id = m.message_thread_id "
			+ "set seen=current_timestamp() where m.message_thread_id = :mtId AND u.name !=:principal",
			nativeQuery = true )
	void setSeen(long mtId, String principal);
	
	@Override
	@Query ( value = "FROM Message m WHERE m.sender.name = ?#{principal}")
	Page<Message> findAll(Pageable pageable);

	@PreAuthorize( "@messageThreadRepository.findOne(#id) != null")
	@Query ( value = "FROM Message m WHERE m.messageThread.id = :id ORDER BY m.created desc")
	@RestResource ( path = "/findByMessageThread", rel = "findByMessageThread" )
	Page<Message> findAllByMessageThread_id( @Param("id") long mt, Pageable p );

	/**
	 * Primary call used by Message Thread Window in front end
	 * 
	 * @param date
	 * @param id
	 * @param p
	 * @return
	 */
	@PreAuthorize( "@messageThreadRepository.findOne(#id) != null")
	@Query ( value = "FROM Message m WHERE m.messageThread.id = :id AND m.created > :date ORDER BY m.created desc")
	@RestResource (path = "/findAllSince", rel = "findAllSince")
	Page<Message> findAllByCreatedAfter(Date date, long id, Pageable p);
	
	@Override
	@RestResource (exported = false)
	<S extends Message> S save(S msg) ;

	@Override
	@Query ( value = "FROM Message m  WHERE m.id = :id and m.sender.name = ?#{principal}")
	Optional<Message> findById(Long id) ;

	@Query ( value = "FROM Message m  WHERE m.id = :id and m.sender.name = ?#{principal}")
	@RestResource (exported = false)
	Message findOne(Long id);
	
	//@Override
	//@Query ( value = "FROM Message m WHERE m.sender.name = ?#{principal}")
	//Iterable<Message> findAll();

	@Override
	@Query ( value = "FROM Message m WHERE m.id IN :ids")
	@RestResource ( exported = false )
	Iterable<Message> findAllById(Iterable<Long> ids);

	@Override
	/**
	 * Can only delete your own message
	 */
	@PreAuthorize ( "@messageRepository.findOne(#id)?.sender?.name == principal" )
	void deleteById(Long id);
	
	@RestResource ( exported = false )
	void deleteByIdAndSender_Name ( long id, String name );

	@Override
	@RestResource ( exported = false )
	void delete(Message entity);
	
}
