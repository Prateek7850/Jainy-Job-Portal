package com.jainyJobPortal.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jainyJobPortal.entity.Users;
import com.jainyJobPortal.repository.UsersRepository;
import com.jainyJobPortal.util.CustomUserDetails;

@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UsersRepository usersRepository;
	
	
	@Autowired
	public CustomUserDetailService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users =usersRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("Could not found user"));
		return new CustomUserDetails(users);
	}

}
