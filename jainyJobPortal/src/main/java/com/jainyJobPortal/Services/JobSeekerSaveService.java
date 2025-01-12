package com.jainyJobPortal.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jainyJobPortal.entity.JobPostActivity;
import com.jainyJobPortal.entity.JobSeekerProfile;
import com.jainyJobPortal.entity.JobSeekerSave;
import com.jainyJobPortal.repository.JobSeekerSaveRepository;

@Service
public class JobSeekerSaveService {
   
	private final JobSeekerSaveRepository jobSeekerSaveRepository;

	public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
		this.jobSeekerSaveRepository = jobSeekerSaveRepository;
	}
    
	public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId){
		return jobSeekerSaveRepository.findByUserId(userAccountId);
	}
	
	public List<JobSeekerSave> getJobCandidates(JobPostActivity job){
		return jobSeekerSaveRepository.findByJob(job);
	}

	public void addNew(JobSeekerSave jobSeekerSave) {
          jobSeekerSaveRepository.save(jobSeekerSave)	;	
	}
}
