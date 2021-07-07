package com.inminhouse.alone.auth.model.enumeration;

import lombok.Getter;

@Getter
public enum Gender {
	
	GENDERLESS(0),
	MAN(1),
	WOMAN(2)
	;
	
	private int value;
	
	Gender(int value) {
		this.value = value;
	}
}
