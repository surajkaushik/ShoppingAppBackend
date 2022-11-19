package com.cts.shoppingapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cts.shoppingapp.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByLoginId(String loginId);

	Boolean existsByLoginId(String loginId);

}
