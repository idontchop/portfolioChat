package com.idontchop.portfolioChat.repositories;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.idontchop.portfolioChat.model.MessageThread;

/**
 * See MessageRepository.
 * 
 * @author nathan
 *
 */
public interface MessageThreadRepository extends CrudRepository<com.idontchop.portfolioChat.model.MessageThread, Long> {

	@Override
	@RestResource (exported = false)
	 <S extends MessageThread> S save(S entity);

	@Override
	@Query ( value = "FROM MessageThread t join fetch t.members m WHERE t.id = :id AND m.name = ?#{principal.username}")
	Optional<MessageThread> findById(Long id);
	
	//@Query ( value = "FROM MessageThread t join fetch t.members m WHERE id1 IN ")
	//Optional<MessageThread> findByMembers( long id1, long id2 );
	/* Native query: (test more)
	 * select * from message_thread mt where exists (select mtm.message_threads_id as mtm_id from message_thread_members mtm join chat_user u on mtm.members_id
	= u.id where mt.id = mtm.message_threads_id AND (u.id = 14 OR u.id = 15) group b
	y mtm_id having count(*) = 2);
	 */
	
	@Override
	@RestResource ( exported = false )
	Iterable<MessageThread> findAllById(Iterable<Long> ids);

	@Override
	@RestResource (exported = false)
	void deleteById(Long id);

	@Override
	@RestResource (exported = false)
	void delete(MessageThread entity);

	@Override
	@Query ( value = "FROM MessageThread t join fetch t.members m WHERE m.name = ?#{principal.username} ")
	Iterable<MessageThread> findAll() ;
	
}
