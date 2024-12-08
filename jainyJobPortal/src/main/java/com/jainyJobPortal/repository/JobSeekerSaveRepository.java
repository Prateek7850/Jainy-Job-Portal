package com.jainyJobPortal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jainyJobPortal.entity.JobPostActivity;
import com.jainyJobPortal.entity.JobSeekerProfile;
import com.jainyJobPortal.entity.JobSeekerSave;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave, Integer> {
   
	List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);
    	
	List<JobSeekerSave> findByJob(JobPostActivity job);
	
	
}
