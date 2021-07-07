package com.inminhouse.alone.auth.model.enumeration;

import lombok.Getter;

@Getter
public enum Gender {
	
	NONE(null),
	GENDERLESS(0),
	MAN(1),
	WOMAN(2)
	;
	
	private Integer value;
	
	Gender(Integer value) {
		this.value = value;
	}
}
