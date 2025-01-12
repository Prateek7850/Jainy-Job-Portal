package com.jainyJobPortal.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.jainyJobPortal.Services.JobPostActivityService;
import com.jainyJobPortal.Services.JobSeekerApplyService;
import com.jainyJobPortal.Services.JobSeekerProfileService;
import com.jainyJobPortal.Services.JobSeekerSaveService;
import com.jainyJobPortal.Services.RecruiterProfileService;
import com.jainyJobPortal.Services.UsersService;
import com.jainyJobPortal.entity.JobPostActivity;
import com.jainyJobPortal.entity.JobSeekerApply;
import com.jainyJobPortal.entity.JobSeekerProfile;
import com.jainyJobPortal.entity.JobSeekerSave;
import com.jainyJobPortal.entity.RecruiterProfile;
import com.jainyJobPortal.entity.Users;

@Controller
public class JobSeekerApplyController {
        
	private final JobPostActivityService jobPostActivityService;
	private final UsersService usersService;
	private final JobSeekerProfileService jobSeekerProfileService;
	private final JobSeekerApplyService jobSeekerApplyService;
	private final JobSeekerSaveService jobSeekerSaveService;
	private final RecruiterProfileService recruiterProfileService;
	
	
	
	
	public JobSeekerApplyController(JobPostActivityService jobPostActivityService, UsersService usersService,
			JobSeekerProfileService jobSeekerProfileService, JobSeekerApplyService jobSeekerApplyService,
			JobSeekerSaveService jobSeekerSaveService, RecruiterProfileService recruiterProfileService) {
		super();
		this.jobPostActivityService = jobPostActivityService;
		this.usersService = usersService;
		this.jobSeekerProfileService = jobSeekerProfileService;
		this.jobSeekerApplyService = jobSeekerApplyService;
		this.jobSeekerSaveService = jobSeekerSaveService;
		this.recruiterProfileService = recruiterProfileService;
	}

	@GetMapping("/job-details-apply/{id}")
	public String display(@PathVariable("id") int id, Model model) {
		JobPostActivity jobDetails = jobPostActivityService.getOne(id);
		List<JobSeekerApply> seekerApply = jobSeekerApplyService.getJobCandidates(jobDetails);
		List<JobSeekerSave> seekerSave = jobSeekerSaveService.getJobCandidates(jobDetails);
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if(!(authentication instanceof AnonymousAuthenticationToken)){
			 if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
				 RecruiterProfile user =recruiterProfileService.getCurrentRecruiterProfile();
			      if(user !=null) {
			    	  model.addAttribute("applyList",seekerApply);
			      }
			 }else {
				 JobSeekerProfile user =jobSeekerProfileService.getCurrentSeekerProfile();
				 if(user!=null) {
					 boolean exists = false;
					 boolean saved = false;
					 for(JobSeekerApply jobSeekerApply : seekerApply) {
						   if(jobSeekerApply.getUserId().getUserAccountId()==user.getUserAccountId()) {
							   exists=true;
							   break;
						   }
					 }
					 for(JobSeekerSave jobSeekerSave : seekerSave) {
						 if(jobSeekerSave.getUserId().getUserAccountId()==user.getUserAccountId()) {
							 saved = true;
							 break;
						 }
					 }
					 model.addAttribute("alreadyApplied",exists);
					 model.addAttribute("alreadySaved",saved);
				 }
			 }
		 }
		JobSeekerApply jobSeekerApply = new JobSeekerApply();
		
		model.addAttribute("applyJob",jobSeekerApply);
		model.addAttribute("jobDetails",jobDetails);
		model.addAttribute("user",usersService.getCurrentUserProfile());
		
		return "job-details";
	}
	
	@PostMapping("job-details/apply/{id}")
	public String apply(@PathVariable("id") int id, JobSeekerApply jobSeekerApply) {
		
	     org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
	     if(!(authentication instanceof AnonymousAuthenticationToken)){
               String currentUserName = authentication.getName();	    	 
	          Users user= usersService.findByEmail(currentUserName);
	          Optional<JobSeekerProfile> seekerProfile = jobSeekerProfileService.getOne(user.getUserId());
	          JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
	          if(seekerProfile.isPresent()&&jobPostActivity!=null) {
	        	  jobSeekerApply = new JobSeekerApply();
	        	  jobSeekerApply.setUserId(seekerProfile.get());
	        	  jobSeekerApply.setJob(jobPostActivity);
	        	  jobSeekerApply.setApplyDate(new Date());
	          }else {
	        	  throw new RuntimeException("user not found");
	          }
	          jobSeekerApplyService.addNew(jobSeekerApply);
	     }
	  return "redirect:/dashboard/";     
	}
	     
	}
