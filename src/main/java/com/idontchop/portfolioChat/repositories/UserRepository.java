package com.idontchop.portfolioChat.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.idontchop.portfolioChat.model.User;

@RestResource (exported = false)
public interface UserRepository extends CrudRepository<User, Long> {

	@Override
	<S extends User> S save(S entity) ;

	@Override
	Optional<User> findById(Long id);

	@Override
	Iterable<User> findAll() ;

	@Override
	Iterable<User> findAllById(Iterable<Long> ids) ;

	@Override
	void deleteById(Long id);

	@Override
	void delete(User entity) ;

	@Override
	void deleteAll(Iterable<? extends User> entities) ;

	@Override
	void deleteAll();

}
