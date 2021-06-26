package com.inminhouse.alone.auth.config.security.oauth2.user;

import java.util.Map;

import com.inminhouse.alone.auth.exception.OAuth2AuthenticationProcessingException;
import com.inminhouse.alone.auth.model.AuthProvider;

public class OAuth2UserInfoFactory {

	private OAuth2UserInfoFactory() {
		throw new IllegalStateException("Utility class");
	}
	
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		
		if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {//equalsIgnoreCase:: 대소문자 구분없이 비교
			return new GoogleOAuth2UserInfo(attributes);
		} else {
			throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}
