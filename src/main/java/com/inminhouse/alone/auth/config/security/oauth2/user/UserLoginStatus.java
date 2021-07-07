package com.inminhouse.alone.auth.config.security.oauth2.user;

import lombok.Getter;

@Getter
public enum UserLoginStatus {
	
	NEW("N"), 
	EXIST("E")
	;
	
	private final String status;
	 
	UserLoginStatus(final String status) {
		this.status = status;
	}
	
}
