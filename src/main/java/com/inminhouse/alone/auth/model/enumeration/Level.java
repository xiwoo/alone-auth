package com.inminhouse.alone.auth.model.enumeration;

import lombok.Getter;

@Getter
public enum Level {
	
	ADMIN(0),
	EDITOR(1),
	USER(2)
	;
	
	private int value;
	
	Level(final int value) {
		this.value = value;
	}
	
}
