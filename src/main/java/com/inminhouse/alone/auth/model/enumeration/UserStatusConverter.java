package com.inminhouse.alone.auth.model.enumeration;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserStatus userstatus) {
        return userstatus.getValue();
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer integer) {
        return Stream.of(UserStatus.values())
                .filter(v -> v.getValue() == integer)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}