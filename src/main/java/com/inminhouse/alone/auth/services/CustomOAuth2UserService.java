package com.inminhouse.alone.auth.services;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.inminhouse.alone.auth.config.security.UserPrincipal;
import com.inminhouse.alone.auth.config.security.oauth2.user.OAuth2UserInfo;
import com.inminhouse.alone.auth.config.security.oauth2.user.OAuth2UserInfoFactory;
import com.inminhouse.alone.auth.config.security.oauth2.user.UserStatus;
import com.inminhouse.alone.auth.exception.OAuth2AuthenticationProcessingException;
import com.inminhouse.alone.auth.model.AuthProvider;
import com.inminhouse.alone.auth.model.User;
import com.inminhouse.alone.auth.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	private UserRepository userRepository;

	private static final Marker MESSAGE_MARKER = MarkerFactory.getMarker("MESSAGE");
	private static final Marker DEBUG_MARKER = MarkerFactory.getMarker("DEBUG");
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOAuth2UserService.class);

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		
		try {
			return processOAuth2User(oAuth2UserRequest, oAuth2User);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}
	
	//현재 OAuth2UserRequest로 들어온 user정보를 가져와 이메일로 기존 등록된 user정보 조회 후 
	//기존 등록된 user이면 user update
	//없으면 user register
	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

		String key = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		OAuth2UserInfo oAuth2UserInfo = 
			OAuth2UserInfoFactory.getOAuth2UserInfo(
				oAuth2UserRequest.getClientRegistration().getRegistrationId(), 
				oAuth2User.getAttributes(),
				key
			);
		
		Optional<User> userOptional;
		
		LOGGER.debug(DEBUG_MARKER, "id:: {}", oAuth2UserInfo.getId());
		LOGGER.debug(DEBUG_MARKER, "email:: {}", oAuth2UserInfo.getEmail());
		
		if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			userOptional = userRepository.findById(Long.parseLong(oAuth2UserInfo.getId()));
		} else {
			if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
				throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
			}
			userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
		}
		
		//해당 email 조회 결과로 기존 유저인지 신규유저인지 판단
//        Optional<User> 
        
        User user;
        UserStatus status;
        
        if(userOptional.isPresent()) {//값이 있으면 > user값 userInfo에서 값 꺼내서 user update
        	
        	user = userOptional.get();
        	if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))) {
                throw new OAuth2AuthenticationProcessingException(
            		"Looks like you're signed up with " +
                    user.getProvider() + " account. Please use your " + user.getProvider() +
                    " account to login."
                );
        	}
        	status = UserStatus.EXIST;
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            LOGGER.debug(MESSAGE_MARKER, "SUCCESS register USER:: {}", user);
            status = UserStatus.NEW;
        }

        LOGGER.debug(MESSAGE_MARKER, "Currrent user:: {}", user);
        LOGGER.debug(MESSAGE_MARKER, "UserStatus:: {}", user);
        
        return UserPrincipal.create(user, status);
	}
	
	private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		User user = new User();
		user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
		user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
	}
	
}
