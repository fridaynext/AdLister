package com.caseyfriday.adlister.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.caseyfriday.adlister.data.entity.User;

@Repository// auto implemented into a Bean (userRepository), used to CRUD users
public interface UserRepository extends CrudRepository<User, Long>{
	// Define query to get user by username
	User findByUsername(String username);
}
