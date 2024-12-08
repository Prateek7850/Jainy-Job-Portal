package com.jainyJobPortal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jainyJobPortal.entity.Users;
import com.jainyJobPortal.entity.UsersType;

public class CustomUserDetails implements UserDetails {
    
	private final Users user;
	
	
	
	public CustomUserDetails(Users user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		UsersType userType = user.getUserTypeId();
		 List<SimpleGrantedAuthority> authorities = new ArrayList<>();	
		authorities.add(new SimpleGrantedAuthority(userType.getUserTypeName()));
		 return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
    
	public boolean isAccountNonExpired() {
		return true;
	}
	public boolean isAccountNonLocked() {
		return true;
	}
	public boolean isCredentailNonExpired() {
		return true;
	}
	public boolean isEnabled() {
		return true;
	}
}
