package com.jainyJobPortal.Services;

import java.util.Optional;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jainyJobPortal.entity.RecruiterProfile;
import com.jainyJobPortal.entity.Users;
import com.jainyJobPortal.repository.RecruiterProfileRepository;
import com.jainyJobPortal.repository.UsersRepository;

@Service
public class RecruiterProfileService {

	private final RecruiterProfileRepository recruiterProfileRepository;
    private final UsersRepository userRepository;
	@Autowired
	public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository,UsersRepository userRepository) {
		this.recruiterProfileRepository = recruiterProfileRepository;
		this.userRepository=userRepository;
	}
    
	public Optional<RecruiterProfile> getOne(Integer id){
		return recruiterProfileRepository.findById(id);
	}

	public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
		// TODO Auto-generated method stub
		return recruiterProfileRepository.save(recruiterProfile);
	}

	public RecruiterProfile getCurrentRecruiterProfile() {
       org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken) {
			String currentUserName = authentication.getName();
			 Users users = userRepository.findByEmail(currentUserName).orElseThrow(()->new UsernameNotFoundException("Could not found user"));
	         Optional<RecruiterProfile> recruiterProfile = getOne(users.getUserId());
	         return recruiterProfile.orElse(null);
		} else return null;
       
       
	}
    
}
