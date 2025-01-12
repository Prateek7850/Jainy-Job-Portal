package com.jainyJobPortal.Services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jainyJobPortal.entity.JobSeekerProfile;
import com.jainyJobPortal.entity.RecruiterProfile;
import com.jainyJobPortal.entity.Users;
import com.jainyJobPortal.repository.JobSeekerProfileRepository;
import com.jainyJobPortal.repository.RecruiterProfileRepository;
import com.jainyJobPortal.repository.UsersRepository;

@Service
public class UsersService {
   
	private final UsersRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
	public UsersService(UsersRepository userRepository,JobSeekerProfileRepository 
			jobSeekerProfileRepository,RecruiterProfileRepository recruiterProfileRepository,PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jobSeekerProfileRepository=jobSeekerProfileRepository;
		this.recruiterProfileRepository=recruiterProfileRepository;
		this.passwordEncoder=passwordEncoder;
	}
	
	public Users addNew(Users user) {
		user.setActive(true);
		user.setRegistrationDate(new Date(System.currentTimeMillis()));
		Users savedUsers = userRepository.save(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		int userTypeId = user.getUserTypeId().getUserTypeId();
		if(userTypeId==1) {
			recruiterProfileRepository.save(new RecruiterProfile(savedUsers));
		}else {
			jobSeekerProfileRepository.save(new JobSeekerProfile(savedUsers));
		}
		return savedUsers;
	}
	
	public Optional getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Object getCurrentUserProfile() {
	   
		Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)){
			String username = authentication.getName();
			Users user=userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Could not found user"));
			int userId=user.getUserId();
			if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
				RecruiterProfile recruiterProfile =recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
				return recruiterProfile;
			}else {
				JobSeekerProfile jobSeekerProfile=jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
				return jobSeekerProfile;
			}
		}
		
		
		return null;
	}

	public Users getCurrentUser() {
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			authentication.getName();
		 String username = authentication.getName();
		 Users user=userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Could not found user"));
			return user;
		}
		
		return null;
	}

	public Users findByEmail(String currentUserName) {

		return userRepository.findByEmail(currentUserName).orElseThrow(()->new UsernameNotFoundException("Could not found user"));
	}
}
