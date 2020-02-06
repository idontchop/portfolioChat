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
	

	@Override
	@Query ( value = "FROM Message m WHERE m.sender.name = ?#{principal}")
	Page<Message> findAll(Pageable pageable);

	@PreAuthorize( "@messageThreadRepository.findOne(#MessageThread) != null")
	@Query ( value = "FROM Message m WHERE m.messageThread.id = :MessageThread ORDER BY m.created desc")
	Page<Message> findAllByMessageThread_id( @Param("MessageThread") long mt, Pageable p );

	@PreAuthorize( "@messageThreadRepository.findOne(#mtId) != null")
	@Query ( value = "FROM Message m WHERE m.messageThread.id = :mtId AND m.created > :date ORDER BY m.created desc")
	Page<Message> findAllByCreatedAfter(Date date, long mtId, Pageable p);
	
	@Override
	@RestResource (exported = false)
	<S extends Message> S save(S entity) ;

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
