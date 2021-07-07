package com.inminhouse.alone.auth.config.security.oauth2.user;

import java.util.HashMap;
import java.util.Map;

import com.inminhouse.alone.auth.exception.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {

	private OAuth2UserInfoFactory() {
		throw new IllegalStateException("Utility class");
	}
	
	@SuppressWarnings("unchecked")
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes, String key) {
		
		//equalsIgnoreCase:: 대소문자 구분없이 비교
		if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
			return new GoogleOAuth2UserInfo(attributes);
		} 
		else if(registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
			return new FacebookOAuth2UserInfo(attributes);
		} 
		else if(registrationId.equalsIgnoreCase(AuthProvider.NAVER.toString()) && attributes.containsKey(key)) {
			Map<String, Object> att = (Map<String, Object>) attributes.get(key);
			return new NaverOAuth2UserInfo(att);
		} 
		else if(registrationId.equalsIgnoreCase(AuthProvider.KAKAO.toString()) && attributes.containsKey(key)) {
			
			Map<String, Object> acc = (HashMap<String, Object>) attributes.get("kakao_account");
			acc.put(key, Long.valueOf((Integer)attributes.get(key)));
			return new KakaoOAuth2UserInfo(acc);
		} 
		else {
			throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}
