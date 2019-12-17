package com.caseyfriday.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.caseyfriday.auth.UserAuthRepository;
import com.caseyfriday.adlister.data.entity.User;
import com.caseyfriday.adlister.data.repository.UserRepository;

@Service
public class AdListerUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	public AdListerUserDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
		if(null==user) {
			throw new UsernameNotFoundException("cannot find username: " + username);
		}
		return new UserPrincipal(user);
	}

}
