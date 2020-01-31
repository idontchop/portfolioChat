package com.idontchop.portfolioChat.repositories;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.idontchop.portfolioChat.model.MessageThread;

public interface MessageThreadRepository extends CrudRepository<com.idontchop.portfolioChat.model.MessageThread, Long> {

	@Override
	@RestResource (exported = false)
	 <S extends MessageThread> S save(S entity);

	@Override
	@Query ( value = "FROM MessageThread t join fetch t.members m WHERE t.id = :id AND m.name = ?#{principal}")
	Optional<MessageThread> findById(Long id);

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
	@Query ( value = "FROM MessageThread t join fetch t.members m WHERE m.name = ?#{principal} ")
	Iterable<MessageThread> findAll() ;
	
}
