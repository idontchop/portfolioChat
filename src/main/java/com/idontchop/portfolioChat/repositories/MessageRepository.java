package com.idontchop.portfolioChat.repositories;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Query ( value = "FROM Message m join fetch m.messageThread.members mem WHERE m.messageThread.id = :MessageThread and mem.name = ?#{principal.username}")
	Iterable<Message> findAllByMessageThread_id( @Param("MessageThread") long mt );

	@PreAuthorize( "@messageThreadRepository.findOne(#mtId) != null")
	@Query ( value = "FROM Message m WHERE m.messageThread.id = :mtId AND m.created > :date")
	Page<Message> findAllByCreatedAfter(Date date, long mtId, Pageable p);
	
	@Override
	@RestResource (exported = false)
	<S extends Message> S save(S entity) ;

	@Override
	@Query ( value = "FROM Message m  WHERE m.id = :id and m.sender.name = ?#{principal.username}")
	Optional<Message> findById(Long id) ;

	@Query ( value = "FROM Message m  WHERE m.id = :id and m.sender.name = ?#{principal.username}")
	Message findOne(Long id);
	
	@Override
	@Query ( value = "FROM Message m WHERE m.sender.name = ?#{principal.username}")
	Iterable<Message> findAll();

	@Override
	@Query ( value = "FROM Message m WHERE m.id IN :ids")
	Iterable<Message> findAllById(Iterable<Long> ids);

	/*
	 * Seemed to be some limitations in writing the hql query.
	 * Use deleteByIdAndMember_Name instead
	 */
	@Override
	/*@Modifying
	@Query ( nativeQuery = true,
	value = "DELETE m FROM message m CROSS JOIN chat_user u ON u.id = m.sender_id "
			+ "WHERE m.id=:id and name=?#{principal.name}"
			)*/
	@PreAuthorize ( "@messageRepository.findOne(#id)?.sender?.name == principal" )
	void deleteById(Long id);
	
	@Transactional
	void deleteByIdAndSender_Name ( long id, String name );

	@Override
	@RestResource ( exported = false )
	void delete(Message entity);
	
}
