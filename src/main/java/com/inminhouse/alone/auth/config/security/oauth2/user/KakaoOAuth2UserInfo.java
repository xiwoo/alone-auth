package com.inminhouse.alone.auth.config.security.oauth2.user;

import java.util.Map;

import lombok.ToString;

@ToString
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
	
	private Map<String, String> profile;
	
	@SuppressWarnings("unchecked")
	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        profile = (Map<String, String>) attributes.get("profile");
    }

	@Override
	public String getId() {
        return attributes.get("id") + "";
	}

	@Override
	public String getName() {
        return profile.get("nickname");
	}

	@Override
	public String getEmail() {
        return (String) attributes.get("email");
	}

	@Override
	public String getImageUrl() {
        return (String) attributes.get("picture");
	}
	
}
