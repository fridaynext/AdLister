package com.caseyfriday.auth;

import com.caseyfriday.adlister.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
}
