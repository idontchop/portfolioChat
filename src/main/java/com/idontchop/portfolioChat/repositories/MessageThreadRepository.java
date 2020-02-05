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
	
	@Query ( value = "FROM MessageThread t join fetch t.members m WHERE t.id = :id AND m.name = ?#{principal.username}")	
	MessageThread findOne(long id);

	@Override
	@Query ( value = "FROM MessageThread t join fetch t.members m WHERE t.id = :id AND m.name = ?#{principal.username}")
	Optional<MessageThread> findById(Long id);
	
	//@Query ( value = "FROM MessageThread t join fetch t.members m WHERE id1 IN ")
	//Optional<MessageThread> findByMembers( long id1, long id2 );
	/* Native query: (test more)
	 * select * from message_thread mt where exists 
	 * (select mtm.message_threads_id as mtm_id from message_thread_members mtm 
	 * join chat_user u on mtm.members_id = u.id 
	 * where mt.id = mtm.message_threads_id AND (u.id = 14 OR u.id = 15) 
	 * group by mtm_id having count(*) = 2);
	 */
	/* didn't work
	 * @Query ( value = "FROM MessageThread mt where exists ("
			+ "select u from mt.members u where (u.id = :id1 OR u.id = :id2) "
			+ "group by u.id having count(u.id) = 2 )" )
	*/
	/**
	 * This retrives a thread based on a group of 2 members. Note that this
	 * can be used to find any thread id. A member could get the id if he
	 * wasnt' in the thread. But shouldn't be able to see any messages.
	 * @param id1
	 * @param id2
	 * @return
	 */
	@Query ( value ="FROM MessageThread mt where exists ("
	+ "select mtt.id from User u join u.messageThreads mtt where "
	+ "mt.id = mtt.id AND "
	+ "(u.name = :id1 OR u.name = :id2) "
	+ "group by mtt.id having count(mtt.id) = 2)")
	Optional<MessageThread> findByMembers ( String id1, String id2 );
		
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
