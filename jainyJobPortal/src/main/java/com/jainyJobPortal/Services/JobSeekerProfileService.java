package com.jainyJobPortal.Services;

import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jainyJobPortal.entity.JobSeekerProfile;
import com.jainyJobPortal.entity.Users;
import com.jainyJobPortal.repository.JobSeekerProfileRepository;
import com.jainyJobPortal.repository.UsersRepository;

@Service
public class JobSeekerProfileService {
       
	private final JobSeekerProfileRepository jobSeekerProfileRepository;
	private final UsersRepository usersRepository;
	
	
	public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UsersRepository usersRepository) {
		this.jobSeekerProfileRepository = jobSeekerProfileRepository;
		this.usersRepository = usersRepository;
	}



	public Optional<JobSeekerProfile> getOne(Integer id){
		
		return jobSeekerProfileRepository.findById(id);
	}



	public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
		
		return jobSeekerProfileRepository.save(jobSeekerProfile);
	}



	public JobSeekerProfile getCurrentSeekerProfile() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication(); 
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			Users users = usersRepository.findByEmail(currentUserName).orElseThrow(()->new UsernameNotFoundException("Could not found user"));
		   Optional<JobSeekerProfile> seekerProfile = getOne(users.getUserId());
		   return seekerProfile.orElse(null);
		}else return null;
	}
}
