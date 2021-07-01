package com.inminhouse.alone.auth.config.security.oauth2.user;

import java.util.Map;

import lombok.ToString;

@ToString
public class NaverOAuth2UserInfo extends OAuth2UserInfo {
	
	public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

	@Override
	public String getId() {
        return (String) attributes.get("id");
	}

	@Override
	public String getName() {
        return (String) attributes.get("name");
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