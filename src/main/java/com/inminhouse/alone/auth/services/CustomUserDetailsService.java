package com.inminhouse.alone.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inminhouse.alone.auth.config.security.UserPrincipal;
import com.inminhouse.alone.auth.config.security.oauth2.user.UserStatus;
import com.inminhouse.alone.auth.exception.ResourceNotFoundException;
import com.inminhouse.alone.auth.model.User;
import com.inminhouse.alone.auth.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

	@Override
    @Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return UserPrincipal.create(user, UserStatus.EXIST);
	}

    @Transactional
    public UserDetails loadUserById(Long id) {
    	
        User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return UserPrincipal.create(user, UserStatus.EXIST);
    }
}
