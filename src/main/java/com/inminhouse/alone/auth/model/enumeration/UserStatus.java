package com.inminhouse.alone.auth.model.enumeration;

import lombok.Getter;

@Getter
public enum UserStatus {
	
	ACTIVE(1),
	DEACTIVE(0),
	REMOVE(2)
	;

	private int value;
	
	UserStatus(final int value) {
		this.value = value;
	}
	

}
