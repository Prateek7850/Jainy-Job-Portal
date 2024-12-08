package com.jainyJobPortal.Config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		UserDetails userDetails=(UserDetails) authentication.getPrincipal();
		String userName = userDetails.getUsername();
	    System.out.println("The username"+userName+"islogged in. ");
	    boolean hasJobSeekerRole=authentication.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("Job Seeker"));
	    boolean hasRecruterRole=authentication.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("Recruiter"));
	    if(hasRecruterRole||hasJobSeekerRole) {
	    	response.sendRedirect("/dashboard/");
	    }
	}
}
