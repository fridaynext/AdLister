package com.caseyfriday.adlister.data.webservice;

import com.caseyfriday.adlister.data.entity.User;
import com.caseyfriday.adlister.data.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

@RestController
public class UserController {
	@Autowired
	private UserRepository repository;
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	List<User> findAll(@RequestParam(required=false) String username) {
		List<User> users = new ArrayList<>();
		if(null==username) {
			Iterable<User> results = this.repository.findAll();
			results.forEach(user-> {users.add(user);});
		} else {
			User user = this.repository.findByUsername(username);
			if(null!=user) {
				users.add(user);
			}
		}
		return users;
	}
}
