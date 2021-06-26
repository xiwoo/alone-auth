package com.inminhouse.alone.auth.config.security.oauth2.user;

public enum UserStatus {
	
	NEW("N"), EXIST("E");
	
	private final String status;
	 
	UserStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	
}
