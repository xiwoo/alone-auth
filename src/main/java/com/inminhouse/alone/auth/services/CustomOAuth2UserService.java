package com.inminhouse.alone.auth.services;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.inminhouse.alone.auth.config.security.UserPrincipal;
import com.inminhouse.alone.auth.config.security.oauth2.user.AuthProvider;
import com.inminhouse.alone.auth.config.security.oauth2.user.OAuth2UserInfo;
import com.inminhouse.alone.auth.config.security.oauth2.user.OAuth2UserInfoFactory;
import com.inminhouse.alone.auth.config.security.oauth2.user.UserLoginStatus;
import com.inminhouse.alone.auth.exception.OAuth2AuthenticationProcessingException;
import com.inminhouse.alone.auth.model.Auth;
import com.inminhouse.alone.auth.model.User;
import com.inminhouse.alone.auth.model.enumeration.Level;
import com.inminhouse.alone.auth.model.enumeration.UserStatus;
import com.inminhouse.alone.auth.repository.AuthRepository;
import com.inminhouse.alone.auth.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthRepository authRepository;

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
	
	//?????? OAuth2UserRequest??? ????????? user????????? ????????? ???????????? ?????? ????????? user?????? ?????? ??? 
	//?????? ????????? user?????? user update
	//????????? user register
	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

		String key = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		
		OAuth2UserInfo oAuth2UserInfo = 
			OAuth2UserInfoFactory.getOAuth2UserInfo(
				oAuth2UserRequest.getClientRegistration().getRegistrationId(), 
				oAuth2User.getAttributes(),
				key
			);
		
		Optional<Auth> authOptional;
		
		log.debug("id:: {}", oAuth2UserInfo.getId());
		log.debug("email:: {}", oAuth2UserInfo.getEmail());

		authOptional = authRepository.findById(oAuth2UserInfo.getId());
		
        User user;
        Auth auth;
        UserLoginStatus status;
        
        //?????? email ?????? ????????? ?????? ???????????? ?????????????????? ??????
        //?????? ????????? > user??? userInfo?????? ??? ????????? user update
        if(authOptional.isPresent()) {
        	
        	auth = authOptional.get();
        	if(!auth.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))) {
                throw new OAuth2AuthenticationProcessingException(
            		"Looks like you're signed up with " +
        				auth.getProvider() + " account. Please use your " + auth.getProvider() +
                    " account to login."
                );
        	}
        	
        	user = userRepository.findById(auth.getUserId())
        		.orElseThrow(() -> new RuntimeException("not found user"));
        	
        	status = UserLoginStatus.EXIST;
        } 
    	//?????? ????????? > user??? ??????
        else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            auth = registerNewAuth(oAuth2UserRequest, oAuth2UserInfo, user.getId());
            log.debug("SUCCESS register USER:: {}", user);
            log.debug("SUCCESS register AUTH:: {}", auth);
            status = UserLoginStatus.NEW;
        }

        log.debug("Currrent user:: {}", user);
        log.debug("UserStatus:: {}", user);
        
        return UserPrincipal.create(user, auth, status);
	}
	
	private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		User user = new User();
		BeanUtils.copyProperties(oAuth2UserInfo, user);
		user.setLevel(Level.USER);
		user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
	}
	
	private Auth registerNewAuth(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo, long userId) {
		Auth auth = new Auth();
		BeanUtils.copyProperties(oAuth2UserInfo, auth);
		auth.setUserId(userId);
		auth.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
		return authRepository.save(auth);
	}
	
}
