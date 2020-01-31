package com.idontchop.portfolioChat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.idontchop.portfolioChat.model.Message;
import com.idontchop.portfolioChat.model.MessageThread;

public interface MessageRepository extends CrudRepository<Message, Long > {
	
	@Query ( value = "FROM Message m join fetch m.messageThread.members mem WHERE m.messageThread.id = :MessageThread and mem.name = ?#{principal.username}")
	Iterable<Message> findAllByMessageThread_id( @Param("MessageThread") long mt );
}
