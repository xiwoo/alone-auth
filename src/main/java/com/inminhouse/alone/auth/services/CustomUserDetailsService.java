package com.inminhouse.alone.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inminhouse.alone.auth.config.security.UserPrincipal;
import com.inminhouse.alone.auth.config.security.oauth2.user.AuthProvider;
import com.inminhouse.alone.auth.config.security.oauth2.user.UserLoginStatus;
import com.inminhouse.alone.auth.exception.ResourceNotFoundException;
import com.inminhouse.alone.auth.model.Auth;
import com.inminhouse.alone.auth.model.User;
import com.inminhouse.alone.auth.repository.AuthRepository;
import com.inminhouse.alone.auth.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthRepository authRepository;

	@Override
    @Transactional
	public UserDetails loadUserByUsername(String authId) throws UsernameNotFoundException {
		
        Auth auth = authRepository.findById(authId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with authId : " + authId));
        
        User user = userRepository.findById(auth.getUserId())
        	.orElseThrow(() -> new UsernameNotFoundException("User not found with userId : " + auth.getUserId()));

        return UserPrincipal.create(user, auth, UserLoginStatus.EXIST);
	}

    @Transactional
    public UserDetails loadUserById(Long id, String provider) {
    	
    	Auth auth = authRepository.findByUserIdAndProvider(id, AuthProvider.valueOf(provider))
    		.orElseThrow(() -> new RuntimeException(""));
    	
    	
        User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return UserPrincipal.create(user, auth, UserLoginStatus.EXIST);
    }
}
